package com.meeting.meeting.service.impl;

import com.meeting.meeting.model.common.UserContext;
import com.meeting.meeting.model.dbo.*;
import com.meeting.meeting.model.dto.request.*;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.model.dto.response.UserInfoResponse;
import com.meeting.meeting.model.dto.response.UserLoginResult;
import com.meeting.meeting.repository.*;
import com.meeting.meeting.service.MeetingService;
import com.meeting.meeting.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingServiceImpl implements MeetingService {
    @Resource
    private MeetingRepository meetingRepository;

    @Resource
    private UserMeetingShipRepository userMeetingShipRepository;

    @Resource
    private ResourceInfoRepository resourceInfoRepository;

    @Resource
    private CorporationRepository corporationRepository;

    @Resource
    private EntityManager entityManager;

    @Resource
    private MeetingResourceShipRepository meetingResourceShipRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Transactional
    public BaseResponse addMeeting(AddMeetingRequest request, Integer meetingId) {
        for (Integer resourceId : request.getResId()) {
            ResourceInfo resourceInfo = isExistMeeting(request.getStartTime(), request.getEndTime(), resourceId, null);
            if (resourceInfo != null) {
                return BaseResponse.failure(resourceInfo.getName() + "已参与其他会议！");
            }
        }
        BaseResponse x = addAndEditMeeting(request, meetingId);
        if (x != null) {
            return x;
        }
        return BaseResponse.success(null);
    }

    private BaseResponse addAndEditMeeting(AddMeetingRequest request, Integer meetingId) {
        if (request.getResId().isEmpty()) {
            return BaseResponse.failure("会议资源主键列表不能为空");
        }
        Meeting meeting = new Meeting();
        BeanUtils.copyProperties(request, meeting);
        Timestamp now = new Timestamp(new Date().getTime());
        meeting.setApplyTime(now);
        meeting.setAudit(0);
        meeting.setState(0);
        double days = (request.getEndTime().getTime() - request.getStartTime().getTime()) / (1000 * 60 * 60 * 24 * 1.0D);
        if (days < 0) {
            return BaseResponse.failure("当前会议开始时间小于结束时间");
        }
        if ((int) days == 0) {
            meeting.setDays(1);
        } else {
            meeting.setDays((int) days);
        }
        UserLoginResult user = UserContext.getUser();
        if (!user.getIdentity().equals(0) || user.getCorporation() == null || user.getCorporation().getId() == null) {
            return BaseResponse.failure("当前企业不存在，请使用企业身份重新登陆");
        }
        BigDecimal totalCost = new BigDecimal(0);
        List<MeetingResourceShip> ships = new ArrayList<>();
        for (Integer resourceId : request.getResId()) {
            ResourceInfo resourceInfo = resourceInfoRepository.findById(resourceId).orElse(null);
            if (resourceInfo == null) {
                return BaseResponse.failure("资源不存在");
            }
            totalCost = totalCost.add(resourceInfo.getCost().multiply(new BigDecimal(days)));
            MeetingResourceShip ship = new MeetingResourceShip();
            ship.setResourceId(resourceId);
            ships.add(ship);
        }
        meeting.setCorId(user.getCorporation().getId());
        meeting.setCost(totalCost);
        if (meetingId != null) {
            meeting.setId(meetingId);
        }
        meeting = meetingRepository.saveAndFlush(meeting);
        Meeting finalMeeting = meeting;
        ships.forEach(x -> x.setMeetingId(finalMeeting.getId()));
        meetingResourceShipRepository.saveAll(ships);
        UserMeetingShip ship = new UserMeetingShip();
        ship.setUseId(user.getId());
        ship.setId(meeting.getId());
        userMeetingShipRepository.save(ship);
        return null;
    }

    @Override
    @Transactional
    public BaseResponse editMeeting(EditMeetingRequest request) {
        for (Integer resourceId : request.getResId()) {
            ResourceInfo resourceInfo = isExistMeeting(request.getStartTime(), request.getEndTime(), resourceId, request.getId());
            if (resourceInfo != null) {
                return BaseResponse.failure(resourceInfo.getName() + "已参与其他会议！");
            }
        }
        MeetingResourceShip query = new MeetingResourceShip();
        query.setMeetingId(request.getId());
        List<MeetingResourceShip> all = meetingResourceShipRepository.findAll(Example.of(query));
        meetingResourceShipRepository.deleteAll(all);
        BaseResponse x = addAndEditMeeting(request, request.getId());
        if (x != null) {
            return x;
        }
        return BaseResponse.success(null);
    }

    private ResourceInfo isExistMeeting(Timestamp start, Timestamp end, Integer resourceId, Integer id) {
        String sql = String.format("select r.*\n" +
                "from resource_info r\n" +
                "         join meeting_resource_ship ship on r.id = ship.resource_id\n" +
                "         join meeting m on ship.meeting_id = m.id\n" +
                "where ((m.start_time <= ? and m.end_time >= ?)\n" +
                "   or (m.end_time >= ? and m.start_time <= ?)\n" +
                "   or (m.start_time <= ? and m.end_time >= ?))\n");
        if (id != null) {
            sql += "and r.id<>?";
        }
        Query countQuery = entityManager.createNativeQuery(sql, ResourceInfo.class);
        countQuery.setParameter(1, start, TemporalType.TIMESTAMP);
        countQuery.setParameter(2, start, TemporalType.TIMESTAMP);
        countQuery.setParameter(3, end, TemporalType.TIMESTAMP);
        countQuery.setParameter(4, end, TemporalType.TIMESTAMP);
        countQuery.setParameter(5, start, TemporalType.TIMESTAMP);
        countQuery.setParameter(6, end, TemporalType.TIMESTAMP);
        if (id != null) {
            countQuery.setParameter(7, id);
        }
        List<ResourceInfo> resultList = countQuery.getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    @Override
    public BaseResponse<Page<Meeting>> list(QueryMeetingRequest queryMeetingRequest) {
        UserLoginResult user = UserContext.getUser();
        if (!user.getIdentity().equals(0) || user.getCorporation() == null || user.getCorporation().getId() == null) {
            return BaseResponse.failure("当前企业不存在，请使用企业身份重新登陆");
        }
        Meeting query = new Meeting();
        query.setCorId(user.getCorporation().getId());
        if (StringUtils.isNotBlank(queryMeetingRequest.getSubtitle())) {
            query.setSubtitle(queryMeetingRequest.getSubtitle());
        }
        if (StringUtils.isNotBlank(queryMeetingRequest.getTitle())) {
            query.setTitle(queryMeetingRequest.getTitle());
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("subtitle", ExampleMatcher.GenericPropertyMatchers.contains());
        PageRequest page = PageRequest.of(queryMeetingRequest.getPageIndex() - 1, queryMeetingRequest.getPageSize());
        Example<Meeting> example = Example.of(query, exampleMatcher);
        Page<Meeting> all = meetingRepository.findAll(example, page);
        all.getContent().forEach(x -> x.setResourceInfos(resourceInfoRepository.getResourcesByMeetingId(x.getId())));
        return BaseResponse.success(all);
    }

    @Override
    public BaseResponse<Meeting> get(Integer id) {
        UserLoginResult user = UserContext.getUser();
        if (!user.getIdentity().equals(0) || user.getCorporation() == null || user.getCorporation().getId() == null) {
            return BaseResponse.failure("当前企业不存在，请使用企业身份重新登陆");
        }
        Meeting meeting = meetingRepository.getMeetingsByCorIdAndId(user.getCorporation().getId(), id);
        if (meeting != null) {
            meeting.setResourceInfos(resourceInfoRepository.getResourcesByMeetingId(meeting.getId()));
            meeting.setEnterpriseName(corporationRepository.getOne(meeting.getCorId()).getName());
        }
        return BaseResponse.success(meeting);
    }


    @Override
    public BaseResponse<Page<Meeting>> listForManager(QueryMeetingRequest queryMeetingRequest) {
        String sql = String.format("select m.*,c.name as enterprise_name from meeting m join corporation c on m.cor_id = c.id  where 1=1 ");
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(queryMeetingRequest.getTitle())) {
            sql += " and m.title like ?";
            params.add("%" + queryMeetingRequest.getTitle() + "%");
        }
        if (StringUtils.isNotBlank(queryMeetingRequest.getSubtitle())) {
            sql += " and m.subtitle like ?";
            params.add("%" + queryMeetingRequest.getSubtitle() + "%");
        }
        if (StringUtils.isNotBlank(queryMeetingRequest.getEnterpriseName())) {
            sql += " and c.name like ?";
            params.add("%" + queryMeetingRequest.getEnterpriseName() + "%");
        }
        Query listQuery = createQuery(sql, queryMeetingRequest.getPageIndex(), queryMeetingRequest.getPageSize(), params);
        int total = listQuery.getMaxResults();
        PageRequest page = PageRequest.of(queryMeetingRequest.getPageIndex() - 1, queryMeetingRequest.getPageSize());
        List<Meeting> meetingList = getMeetings(listQuery);
        meetingList.forEach(x -> x.setResourceInfos(resourceInfoRepository.getResourcesByMeetingId(x.getId())));
        PageImpl resultPage = new PageImpl(meetingList, page, total);
        return BaseResponse.success(resultPage);
    }

    private Query createQuery(String sql, Integer pageIndex, Integer pageSize, List<Object> params) {
        Query listQuery = entityManager.createNativeQuery(sql, Meeting.class);
        listQuery
                .setFirstResult((pageIndex - 1) * pageSize)
                .setMaxResults(pageSize);
        for (int i = 0; i < params.size(); i++) {
            listQuery.setParameter(i + 1, params.get(i));
        }
        return listQuery;
    }

    private List<Meeting> getMeetings(Query listQuery) {
        List<Meeting> resultList = listQuery.getResultList();
        resultList.forEach(x -> {
            Corporation corporation = corporationRepository.getOne(x.getCorId());
            x.setEnterpriseName(corporation.getName());
            x.setResourceInfos(resourceInfoRepository.getResourcesByMeetingId(x.getId()));
        });
        return resultList;
    }

    @Override
    public BaseResponse audit(AuditRequest request) {
        Meeting meeting = meetingRepository.findById(request.getId()).orElse(null);
        if (meeting == null) {
            return BaseResponse.failure("会议不存在");
        }
        if (meeting.getAudit().equals(1)) {
            return BaseResponse.failure("会议已通过审核，不能重复审核");
        }
        meeting.setCost(request.getCost());
        meeting.setAudit(request.getAudit());
        meeting.setRemark(request.getRemark());
        meetingRepository.save(meeting);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse<Page<Meeting>> listForUser(UserQueryMeetingRequest request) {
        UserLoginResult user = UserContext.getUser();
        if (!user.getIdentity().equals(1)) {
            return BaseResponse.failure("请使用用户身份登录");
        }
        String sql = "select m.* from meeting m join corporation c on m.cor_id = c.id and m.audit=1 ";
        List<Object> params = new ArrayList<>();
        if (request.getIsTakePartIn() != null && request.getIsTakePartIn()) {
            sql += " left join user_meeting_ship ship on ship.id=m.id where ship.use_id=? ";
            params.add(user.getId());
        } else {
            sql += " where 1=1 ";
        }
        if (StringUtils.isNotBlank(request.getSubtitle())) {
            sql += " and m.subtitle like ?";
            params.add("%" + request.getSubtitle() + "%");
        }
        if (StringUtils.isNotBlank(request.getTitle())) {
            sql += " and m.title like ?";
            params.add("%" + request.getTitle() + "%");
        }
        if (StringUtils.isNotBlank(request.getEnterpriseName())) {
            sql += " and c.name like ?";
            params.add("%" + request.getEnterpriseName() + "%");
        }
        Query listQuery = createQuery(sql, request.getPageIndex(), request.getPageSize(), params);
        PageRequest page = PageRequest.of(request.getPageIndex() - 1, request.getPageSize());
        int total = listQuery.getMaxResults();
        List<Meeting> meetingList = getMeetings(listQuery);
        meetingList.forEach(x -> {
            x.setIsTakePartIn(isTakePartIn(x.getId(), user.getId()));
        });
        PageImpl resultPage = new PageImpl(meetingList, page, total);
        return BaseResponse.success(resultPage);
    }

    private Boolean isTakePartIn(Integer meetId, Integer userId) {
        String sql = String.format("select count(1) from meeting m join user_meeting_ship ship on m.id = ship.id where ship.use_id=? and ship.id=?");
        Query nativeQuery = entityManager.createNativeQuery(sql, Integer.class);
        nativeQuery.setParameter(1, userId);
        nativeQuery.setParameter(2, meetId);
        return Integer.parseInt(nativeQuery.getSingleResult().toString()) > 0;
    }

    @Override
    public BaseResponse takePartIn(TakePartInRequest request) {
        UserLoginResult user = UserContext.getUser();
        if (!user.getIdentity().equals(1)) {
            return BaseResponse.failure("请使用用户身份登录");
        }
        Meeting meeting = meetingRepository.findById(request.getMeetingId()).orElse(null);
        if (meeting == null || meeting.getAudit() != 1) {
            return BaseResponse.failure("当会议不存在");
        }
        UserMeetingShip ship = new UserMeetingShip();
        ship.setId(request.getMeetingId());
        ship.setUseId(user.getId());
        userMeetingShipRepository.save(ship);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse cancel(TakePartInRequest request) {
        Meeting meeting = meetingRepository.findById(request.getMeetingId()).orElse(null);
        if (meeting == null || meeting.getAudit() != 1) {
            return BaseResponse.failure("当会议不存在");
        }
        UserLoginResult user = UserContext.getUser();
        if (!user.getIdentity().equals(1)) {
            return BaseResponse.failure("请使用用户身份登录");
        }
        UserMeetingShip ship = new UserMeetingShip();
        ship.setId(request.getMeetingId());
        ship.setUseId(user.getId());
        userMeetingShipRepository.delete(ship);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse<Page<UserInfoResponse>> getMeetingUserInfos(MeetingUsersRequest request) {
        UserLoginResult user = UserContext.getUser();
        if (!user.getIdentity().equals(0) || user.getCorporation() == null || user.getCorporation().getId() == null) {
            return BaseResponse.failure("当前企业不存在，请使用企业身份重新登陆");
        }
        String sql = String.format("select u.* from meeting m  join user_meeting_ship ship on ship.id=m.id join user u on ship.use_id = u.id where u.identity=1 and m.id=%s", request.getMeetingId());
        Query listQuery = entityManager.createNativeQuery(sql, User.class);
        listQuery
                .setFirstResult((request.getPageIndex() - 1) * request.getPageSize())
                .setMaxResults(request.getPageSize());
        int total = listQuery.getMaxResults();
        List<User> list = listQuery.getResultList();
        List<UserInfoResponse> result = new ArrayList<>();
        list.forEach(x -> {
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            BeanUtils.copyProperties(x, userInfoResponse);
            result.add(userInfoResponse);
        });
        PageRequest page = PageRequest.of(request.getPageIndex() - 1, request.getPageSize());
        PageImpl resultPage = new PageImpl(result, page, total);
        return BaseResponse.success(resultPage);
    }

    @Override
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        //true 表⽰示需要创建⼀一个 multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    //会议开始前设置资源状态为占用，给参与会议的人发送邮件通知
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void startMeeting() {
        List<Meeting> list = meetingRepository.getMeetingsForStarting();
        list.forEach(meet -> {
            meet.setState(1);
            meetingRepository.save(meet);
            List<ResourceInfo> resourceInfos = resourceInfoRepository.getResourcesByMeetingId(meet.getId());
            for (ResourceInfo resourceInfo : resourceInfos) {
                resourceInfo.setState(0);
                resourceInfoRepository.save(resourceInfo);
            }
            List<User> users = userRepository.getUsersByMeetingId(meet.getId());
            users.forEach(u -> {
                String subject = String.format("邀请您于%s参加%s会议", meet.getStartTime(), meet.getIntro());
                String content = "<html><body>" + subject + "</body></html>";
                try {
                    sendHtmlMail(u.getEmail(), subject, content);
                    System.out.println("成功了");
                } catch (MessagingException e) {
                    System.out.println("失败了");
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     * 会议结束后将资源设置为空闲
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void endMeeting() {
        List<Meeting> list = meetingRepository.getMeetingsForEnd();
        list.forEach(meet -> {
            meet.setState(2);
            meetingRepository.save(meet);
            List<ResourceInfo> resourceInfos = resourceInfoRepository.getResourcesByMeetingId(meet.getId());
            for (ResourceInfo resourceInfo : resourceInfos) {
                resourceInfo.setState(2);
                resourceInfoRepository.save(resourceInfo);
            }
        });
    }

    @Override
    public BaseResponse<Page<ResourceInfo>> resourceInfos(MeetingUsersRequest request) {
        String sql = String.format("select r.* from resource_info r join meeting_resource_ship ship on r.id = ship.resource_id where ship.meeting_id=?=%s", request.getMeetingId());
        Query listQuery = entityManager.createNativeQuery(sql, ResourceInfo.class);
        listQuery
                .setFirstResult((request.getPageIndex() - 1) * request.getPageSize())
                .setMaxResults(request.getPageSize());
        int total = listQuery.getMaxResults();
        List<ResourceInfo> list = listQuery.getResultList();
        PageRequest page = PageRequest.of(request.getPageIndex() - 1, request.getPageSize());
        PageImpl resultPage = new PageImpl(list, page, total);
        return BaseResponse.success(resultPage);
    }
}
