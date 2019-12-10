package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CorporationListRequest extends Page {
    @ApiModelProperty("企业名称")
    private String name;

    @ApiModelProperty("电话")
    private String phone;
}
