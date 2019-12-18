package com.meeting.meeting.controller.api.user;

import com.meeting.meeting.model.dbo.Meeting;
import com.meeting.meeting.model.dto.request.AddMeetingRequest;
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
@Api(tags = "会议控制器(企业端)")
@RequestMapping("/user/enterprise/meeting")
public class EnterpriseMeetingController {

    @Resource
    private MeetingService meetingService;

    @PostMapping("/add")
    @ApiOperation("申请会议")
    public BaseResponse addMeeting(@RequestBody @Valid AddMeetingRequest request) {
        return meetingService.addMeeting(request);
    }

    @GetMapping("/list")
    @ApiOperation("申请列表")
    public BaseResponse<Page<Meeting>> list(QueryMeetingRequest queryMeetingRequest) {
        return meetingService.list(queryMeetingRequest);
    }

    @GetMapping("/get")
    @ApiOperation("获取会议")
    public BaseResponse<Meeting>get(Integer id){
        return meetingService.get(id);
    }

}
