package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class ResourceRequest {
    /**
     * 0-会场资源，1-住宿资源
     */
    @ApiModelProperty(value = "0-会场资源，1-住宿资源")
    @NotNull(message = "资源类型不能为空")
    @Range(max = 1, min = 0, message = "只能填0或1")
    private String type;

    /**
     * 对资源的简单描述，仅限文本，长度<=20字
     */
    @ApiModelProperty(value = "对资源的简单描述")
    private String intro;
}
