package com.meeting.meeting.model.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TakePartInRequest {
    @NotNull(message = "会议主键不能为空")
    private Integer meetingId;
}
