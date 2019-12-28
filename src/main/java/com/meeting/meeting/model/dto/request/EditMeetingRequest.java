package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class EditMeetingRequest extends AddMeetingRequest {
    @NotNull(message = "会议主键不能为空")
    @ApiModelProperty("会议主键")
    private Integer id;
}
