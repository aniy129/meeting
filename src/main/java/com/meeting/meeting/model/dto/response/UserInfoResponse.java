package com.meeting.meeting.model.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoResponse {
    /**
     * 主键
     */
    @ApiModelProperty(value = "用户主键")
    private Integer id;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "")
    private String name;

    /**
     * 昵称,作为登录账号
     */
    @ApiModelProperty(value = "")
    private String nickname;

    /**
     * 18位数身份证号码
     */
    @ApiModelProperty(value = "")
    private String identifyNum;

    /**
     * 11位数电话号码
     */
    @ApiModelProperty(value = "")
    private String phone;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(value = "")
    private String email;
}
