package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AddResourceInfoRequest
{

    @NotNull(message = "资源类型主键不能为空")
    @ApiModelProperty(name = "resId" ,value = "资源类型主键")
    private Integer resId;

    /**
     * 资源的名称，例如xx会场，xx酒店，xx汽车租用公司
     */
    @NotBlank(message = "资源的名称不能为空")
    @ApiModelProperty(name = "name" ,value = "资源的名称")
    private String name;


    /**
     * 资源的具体位置（会场资源-会场详细到层数房间号，住宿资源-住宿地址）
     */
    @NotBlank(message = "资源的具体位置不能为空")
    @ApiModelProperty(name = "address" ,value = "资源的具体位置")
    private String address;

    /**
     * 资源总共的数量（不考虑是否可用），例如，会场资源的会场数量，住宿资源的客房数量
     */
    @NotNull(message = "资源总共的数量不能为空")
    @ApiModelProperty(name = "count" ,value = "资源总共的数量")
    private Integer count;

    /**
     * 资源能够容纳安排的总人数
     */
    @NotNull(message = "资源能够容纳安排的总人数不能为空")
    @ApiModelProperty(name = "toplimit" ,value = "资源能够容纳安排的总人数")
    private Integer toplimit;

    /**
     * 资源的宣传图片，仅限一张
     */
    @ApiModelProperty(name = "image" ,value = "资源的宣传图片")
    private String image;

    /**
     * 对资源的简单描述，仅限文本，长度<=20字
     */
    @ApiModelProperty(name = "intro" ,value = "对资源的简单描述")
    private String intro;

    /**
     * 对资源的详细简述，可文本+图片等，长度不限
     */
    @ApiModelProperty(name = "detail" ,value = "对资源的详细简述")
    private String detail;

    /**
     * 人民币为单位，会场资源的费用以会场平均价格为单位，住宿资源的费用以客房平均每间为单位
     */
    @ApiModelProperty(name = "cost" ,value = "费用")
    private BigDecimal cost;

    /**
     * 该资源的联系人姓名
     */
    @ApiModelProperty(name = "manager" ,value = "该资源的联系人姓名")
    private String manager;

    /**
     * 限定11位数手机号码
     */
    @ApiModelProperty(name = "phone" ,value = "限定11位数手机号码")
    private String phone;

    /**
     * 格式为：xxx@xx.com
     */
    @ApiModelProperty(name = "email" ,value = "邮箱")
    private String email;
}
