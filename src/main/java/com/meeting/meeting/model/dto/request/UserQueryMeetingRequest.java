package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryMeetingRequest extends QueryMeetingRequest{
    @ApiModelProperty(value = "是否参与")
    private Boolean isTakePartIn;
}
