package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel
public class AuditRequest {
    @NotNull(message = "主键不能为空")
    @ApiModelProperty("主键")
    private Integer id;
    @NotNull(message = "费用不能为空")
    @ApiModelProperty("费用")
    private BigDecimal cost;
    @NotNull(message = "审核状态不能为空")
    @Range(min = 1, max = 2, message = "审核驻状态不正确")
    @ApiModelProperty("1-审核通过，2-审核未通过")
    private Integer audit;
}
