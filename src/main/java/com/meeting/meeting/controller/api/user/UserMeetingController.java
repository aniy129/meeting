package com.meeting.meeting.controller.api.user;

import com.meeting.meeting.model.dbo.Meeting;
import com.meeting.meeting.model.dto.request.TakePartInRequest;
import com.meeting.meeting.model.dto.request.UserQueryMeetingRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import com.meeting.meeting.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(tags = "会议控制器(普通用户端)")
@RequestMapping("/user/user/meeting")
public class UserMeetingController {

    @Resource
    private MeetingService meetingService;

    @GetMapping("/list")
    @ApiOperation(value = "查看可参与的会议")
    public BaseResponse<Page<Meeting>> list(@Valid UserQueryMeetingRequest request) {
        return meetingService.listForUser(request);
    }

    @PostMapping("/takePartIn")
    @ApiOperation(value = "参与会议")
    public BaseResponse takePartIn(@Valid @RequestBody TakePartInRequest request){
        return meetingService.takePartIn(request);
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "取消参与会议")
    public BaseResponse cancel(@Valid @RequestBody TakePartInRequest request){
        return meetingService.cancel(request);
    }

}
