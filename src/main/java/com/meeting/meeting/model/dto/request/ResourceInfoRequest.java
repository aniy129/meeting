package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceInfoRequest extends Page {
    @ApiModelProperty(name = "name", value = "资源的名称")
    private String name;
    @ApiModelProperty(name = "state", value = "状态 0-占用中,不可用，1-空闲中,可用")
    private Integer state;
    @ApiModelProperty(name = "manager", value = "资源的联系人姓名")
    private String manager;
    @ApiModelProperty(name = "type", value = "资源类型")
    private Integer type;

}
