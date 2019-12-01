package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@ApiModel
public class UserRegisterRequest {
    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    private String name;

    /**
     * 昵称,作为登录账号
     */
    @ApiModelProperty("登录账号")
    @NotBlank(message = "登录账号不能为空")
    private String nickname;

    /**
     * 登录密码，限制30个字符长度
     */
    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 18位数身份证号码
     */
    @ApiModelProperty("身份证号码")
    @NotBlank(message = "身份证号码不能为空")
    private String identifyNum;

    /**
     * 11位数电话号码
     */
    @ApiModelProperty("电话号码")
    @NotBlank(message = "电话号码不能为空")
    private String phone;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;
}
