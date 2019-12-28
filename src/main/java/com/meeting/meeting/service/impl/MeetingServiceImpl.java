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
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

    @Override
    @Transactional
    public BaseResponse addMeeting(AddMeetingRequest request, Integer meetingId) {
        for (Integer resourceId : request.getResId()) {
            if (isExistMeeting(request.getStartTime(), request.getEndTime(), resourceId, null)) {
                return BaseResponse.failure("当前会议时间与系统已存在的会议时间冲突");
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
            if (isExistMeeting(request.getStartTime(), request.getEndTime(), resourceId, request.getId())) {
                return BaseResponse.failure("当前会议时间与系统已存在的会议时间冲突");
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

    private boolean isExistMeeting(Timestamp start, Timestamp end, Integer resourceId, Integer id) {
        List<Meeting> list = meetingRepository.getMeetingsByResourceInfosId(resourceId);
        if (list.isEmpty()) {
            return false;
        } else {
            List<Meeting> collect = list.stream().filter(meeting ->
                    (start.getTime() >= meeting.getStartTime().getTime() && start.getTime() <= meeting.getEndTime().getTime())
                            || (end.getTime() >= meeting.getStartTime().getTime() && end.getTime() <= meeting.getEndTime().getTime())
                            || (start.getTime() <= meeting.getStartTime().getTime() && end.getTime() >= meeting.getEndTime().getTime())
            ).collect(Collectors.toList());
            if (id == null) {
                return collect.size() > 0;
            } else {
                return collect.stream().anyMatch(meeting -> !meeting.getId().equals(id));
            }
        }
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
        String sql = "select m.* from meeting m join corporation c on m.cor_id = c.id and m.audit=1 left join user_meeting_ship ship on ship.id=m.id where 1=1 ";
        List<Object> params = new ArrayList<>();
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
        if (request.getIsTakePartIn() != null && request.getIsTakePartIn()) {
            sql += " and ship.use_id=?";
            params.add(user.getId());
        }
        Query listQuery = createQuery(sql, request.getPageIndex(), request.getPageSize(), params);
        PageRequest page = PageRequest.of(request.getPageIndex() - 1, request.getPageSize());
        int total = listQuery.getMaxResults();
        List<Meeting> meetingList = getMeetings(listQuery);
        PageImpl resultPage = new PageImpl(meetingList, page, total);
        return BaseResponse.success(resultPage);
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
        String sql = "select u.* from meeting m  join user_meeting_ship ship on ship.id=m.id join user u on ship.use_id = u.id where u.identity=1 and m.id=" + request.getMeetingId();
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
}
