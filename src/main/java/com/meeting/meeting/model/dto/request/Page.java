package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Page {
    @ApiModelProperty(name = "pageIndex", value = "页码", required = true)
    @NotNull(message = "页码不能为空")
    private Integer pageIndex;

    @ApiModelProperty(name = "pageSize", value = "页容量", required = true)
    @NotNull(message = "页容量不能为空")
    private Integer pageSize;

}
