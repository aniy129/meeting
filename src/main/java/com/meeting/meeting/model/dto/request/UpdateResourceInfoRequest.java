package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateResourceInfoRequest extends AddResourceInfoRequest {
    @NotNull(message = "主键不能为空")
    @ApiModelProperty(name = "id" ,value = "主键")
    private Integer id;

    @NotNull(message = "状态不能为空")
    @Range(min = 0,max = 1,message = "状态只能填0或1")
    @ApiModelProperty(name = "state" ,value = "状态  0-占用中,不可用，1-空闲中,可用")
    private Integer state;
}
