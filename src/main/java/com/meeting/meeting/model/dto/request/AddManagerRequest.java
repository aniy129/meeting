package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class AddManagerRequest {
    /**
     * 管理员编码
     */
    @NotBlank(message = "管理员编码不能为空")
    @ApiModelProperty("管理员编码")
    private String code;

    /**
     * 登录账号，限制15个字符长度
     */
    @NotBlank(message = "登录账号不能为空")
    @ApiModelProperty("登录账号")
    private String account;

    /**
     * 登录密码，限制30个字符长度
     */
    @ApiModelProperty("登录密码")
    @NotBlank(message = "登录密码不能为空")
    private String password;

    /**
     * 管理员姓名
     */
    @NotBlank(message = "管理员姓名不能为空")
    @ApiModelProperty("管理员姓名")
    private String name;

    /**
     * 0-男，1-女
     */
    @NotNull(message = "性别不能为空")
    @ApiModelProperty("0-男，1-女")
    private Integer sex;

    /**
     * 年龄限制18-100岁
     */
    @ApiModelProperty("年龄")
    @NotNull(message = "年龄不能为空")
    private Integer age;

    /**
     * 仅限11位电话号码
     */
    @ApiModelProperty("电话")
    @NotBlank(message = "电话不能为空")
    private String phone;

    /**
     * 管理员邮箱
     */
    @ApiModelProperty("管理员邮箱")
    @NotBlank(message = "管理员邮箱不能为空")
    private String email;

    /**
     * 管理员照片
     */
    @ApiModelProperty("管理员照片")
    private String photo;
    /**
     * 是否可用
     */
    @ApiModelProperty("是否可用")
    @NotNull(message = "是否可用不能为空")
    private Boolean available;

}
