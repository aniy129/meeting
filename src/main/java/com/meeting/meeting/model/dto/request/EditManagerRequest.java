package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class EditManagerRequest extends AddManagerRequest{
    @ApiModelProperty("主键")
    @NotNull(message = "主键不能为空")
    private Integer id;
}
