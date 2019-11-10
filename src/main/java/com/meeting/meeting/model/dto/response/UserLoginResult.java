package com.meeting.meeting.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserLoginResult {
    @ApiModelProperty(value = "消息")
    private String message;
    @ApiModelProperty(value = "状态码")
    private String status;
    @ApiModelProperty(value = "登陆鉴权")
    private String auth;
    /**
     * 主键，自增
     */
    @ApiModelProperty(value = "主键")
    private Integer id;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String name;

    /**
     * 昵称,作为登录账号
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 登录身份：0-企业管理员，1-企业用户
     */
    @ApiModelProperty(value = "登录身份：0-企业管理员，1-企业用户")
    private Integer identity;

    /**
     * 18位数身份证号码
     */
    @ApiModelProperty(value = "18位数身份证号码")
    private String identifyNum;

    /**
     * 11位数电话号码
     */
    @ApiModelProperty(value = "11位数电话号码")
    private String phone;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(value = "用户邮箱")
    private String email;
}
