package com.meeting.meeting.service.impl;

import com.meeting.meeting.model.common.UserContext;
import com.meeting.meeting.model.dbo.Corporation;
import com.meeting.meeting.model.dbo.Meeting;
import com.meeting.meeting.model.dbo.ResourceInfo;
import com.meeting.meeting.model.dbo.UserMeetingShip;
import com.meeting.meeting.model.dto.request.AddMeetingRequest;
import com.meeting.meeting.model.dto.request.AuditRequest;
import com.meeting.meeting.model.dto.request.QueryMeetingRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.model.dto.response.UserLoginResult;
import com.meeting.meeting.repository.CorporationRepository;
import com.meeting.meeting.repository.MeetingRepository;
import com.meeting.meeting.repository.ResourceInfoRepository;
import com.meeting.meeting.repository.UserMeetingShipRepository;
import com.meeting.meeting.service.MeetingService;
import com.meeting.meeting.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Override
    public BaseResponse addMeeting(AddMeetingRequest request) {
        if (isExistMeeting(request.getStartTime(), request.getEndTime(), request.getResId(), null)) {
            return BaseResponse.failure("当前会议时间与系统已存在的会议时间冲突");
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
        ResourceInfo resourceInfo = resourceInfoRepository.findById(request.getResId()).orElse(null);
        if (resourceInfo == null) {
            return BaseResponse.failure("资源不存在");
        }
        meeting.setCorId(user.getCorporation().getId());
        meeting.setCost(resourceInfo.getCost().multiply(new BigDecimal(days)));
        meeting = meetingRepository.saveAndFlush(meeting);
        UserMeetingShip ship = new UserMeetingShip();
        ship.setUseId(user.getId());
        ship.setId(meeting.getId());
        userMeetingShipRepository.save(ship);
        return BaseResponse.success(null);
    }

    private boolean isExistMeeting(Timestamp start, Timestamp end, Integer resourceId, Integer id) {
        List<Meeting> list = meetingRepository.findAll().stream().filter(meeting -> meeting.getResId().equals(resourceId)).collect(Collectors.toList());
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
        if (queryMeetingRequest.getResId() != null) {
            query.setResId(queryMeetingRequest.getResId());
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("subtitle", ExampleMatcher.GenericPropertyMatchers.contains());
        PageRequest page = PageRequest.of(queryMeetingRequest.getPageIndex() - 1, queryMeetingRequest.getPageSize());
        Example<Meeting> example = Example.of(query, exampleMatcher);
        Page<Meeting> all = meetingRepository.findAll(example, page);
        return BaseResponse.success(all);
    }

    @Override
    public BaseResponse<Meeting> get(Integer id) {
        UserLoginResult user = UserContext.getUser();
        if (!user.getIdentity().equals(0) || user.getCorporation() == null || user.getCorporation().getId() == null) {
            return BaseResponse.failure("当前企业不存在，请使用企业身份重新登陆");
        }
        Meeting meeting = meetingRepository.getMeetingsByCorIdAndId(user.getCorporation().getId(), id);
        return BaseResponse.success(meeting);
    }

    @Override
    public BaseResponse<Page<Meeting>> listForManager(QueryMeetingRequest queryMeetingRequest) {
        String sql = "select m.*,c.name as enterprise_name from meeting m join corporation c on m.cor_id = c.id  where 1=1 ";
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(queryMeetingRequest.getTitle())) {
            sql += " and m.title like ?";
            params.add("%" + queryMeetingRequest.getTitle() + "%");
        }
        if (queryMeetingRequest.getResId() != null) {
            sql += " and m.resId = ?";
            params.add(queryMeetingRequest.getResId());
        }
        if (StringUtils.isNotBlank(queryMeetingRequest.getSubtitle())) {
            sql += " and m.subtitle like ?";
            params.add("%" + queryMeetingRequest.getSubtitle() + "%");
        }
        if (StringUtils.isNotBlank(queryMeetingRequest.getEnterpriseName())) {
            sql += " and c.name like ?";
            params.add("%" + queryMeetingRequest.getEnterpriseName() + "%");
        }
        Query listQuery = entityManager.createNativeQuery(sql, Meeting.class);
        listQuery
                .setFirstResult((queryMeetingRequest.getPageIndex() - 1) * queryMeetingRequest.getPageSize())
                .setMaxResults(queryMeetingRequest.getPageSize());
        for (int i = 0; i < params.size(); i++) {
            listQuery.setParameter(i + 1, params.get(i));
        }
        List<Meeting> resultList = listQuery.getResultList();
        resultList.forEach(x -> {
            Corporation corporation = corporationRepository.getOne(x.getCorId());
            x.setEnterpriseName(corporation.getName());
        });
        int total = listQuery.getMaxResults();
        PageRequest page = PageRequest.of(queryMeetingRequest.getPageIndex() - 1, queryMeetingRequest.getPageSize());
        PageImpl resultPage = new PageImpl(resultList, page, total);
        return BaseResponse.success(resultPage);
    }

    @Override
    public BaseResponse audit(AuditRequest request) {
        Meeting meeting = meetingRepository.findById(request.getId()).orElse(null);
        if (meeting == null) {
            return BaseResponse.failure("会议不存在");
        }
        meeting.setCost(request.getCost());
        meeting.setAudit(request.getAudit());
        meeting.setRemark(request.getRemark());
        meetingRepository.save(meeting);
        return BaseResponse.success(null);
    }
}
