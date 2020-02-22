package com.meeting.meeting.controller.api.manager;

import com.meeting.meeting.model.dbo.Meeting;
import com.meeting.meeting.model.dbo.ResourceInfo;
import com.meeting.meeting.model.dto.request.AuditRequest;
import com.meeting.meeting.model.dto.request.MeetingUsersRequest;
import com.meeting.meeting.model.dto.request.QueryMeetingRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(tags = "会议控制器(管理端)")
@RequestMapping("/manager/meeting")
public class ManagerMeetingController {
    @Resource
    private MeetingService meetingService;

    @GetMapping("/list")
    @ApiOperation("申请列表")
    public BaseResponse<Page<Meeting>> list(@Valid QueryMeetingRequest queryMeetingRequest) {
        return meetingService.listForManager(queryMeetingRequest);
    }

    @PostMapping("/audit")
    @ApiOperation("审核会议")
    public BaseResponse audit(@RequestBody @Valid AuditRequest request) {
        return meetingService.audit(request);
    }

    @GetMapping("/resourceInfos")
    @ApiOperation("获取会议的资源信息")
    public BaseResponse<Page<ResourceInfo>> resourceInfos(@Valid @RequestBody MeetingUsersRequest request) {
        return meetingService.resourceInfos(request);
    }
}
