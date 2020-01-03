package com.meeting.meeting.model.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class MeetingUsersRequest extends Page {
    @NotNull(message = "会议主键不能为空")
    private Integer meetingId;
}
