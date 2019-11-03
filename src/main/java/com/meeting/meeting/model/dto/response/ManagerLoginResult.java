package com.meeting.meeting.model.dto.response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class ManagerLoginResult implements Serializable {
    @ApiModelProperty(value = "消息")
    private String msg;
    @ApiModelProperty(value = "状态码")
    private String code;
    @ApiModelProperty(value = "登陆鉴权")
    private String auth;
}
