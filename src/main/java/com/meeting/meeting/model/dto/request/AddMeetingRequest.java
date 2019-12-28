package com.meeting.meeting.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
@ApiModel
public class AddMeetingRequest {

    /**
     * 会议主标题
     */
    @ApiModelProperty("会议主标题")
    @NotBlank(message = "会议主标题不能为空")
    private String title;

    /**
     * 会议副标题
     */
    @ApiModelProperty("会议副标题")
    @NotBlank(message = "会议副标题不能为空")
    private String subtitle;

    /**
     * 对会议的简略介绍，仅限文本，长度<=20
     */
    @ApiModelProperty("会议的简略介绍")
    @NotBlank(message = "会议的简略介绍不能为空")
    private String intro;

    /**
     * 对会议的详细介绍，可文本+图片等，长度无上限
     */
    @ApiModelProperty("详细介绍")
    @NotBlank(message = "详细介绍不能为空")
    private String detail;

    @ApiModelProperty("会议资源主键")
    @NotNull(message = "会议资源主键不能为空")
    private List<Integer> resId;

    /**
     * 会议的参会人数上限（不包括工作人员）
     */
    @ApiModelProperty("会议的参会人数上限")
    @NotNull(message = "会议的参会人数上限不能为空")
    private Integer maxNum;

    /**
     * 会议开始时间
     */
    @ApiModelProperty(value = "会议开始时间", hidden = true)
    private Timestamp startTime;

    /**
     * 会议结束时间，须迟于会议开始时间
     */
    @ApiModelProperty(value = "会议结束时间", hidden = true)
    private Timestamp endTime;

    @NotNull(message = "会议开始时间不能为空")
    @ApiModelProperty(value = "会议开始时间")
    private Long start;

    @NotNull(message = "会议结束时间不能为空")
    @ApiModelProperty(value = "会议结束时间")
    private Long end;

    public Timestamp getEndTime() {
        if (end != null) {
            return new Timestamp(this.end);
        }
        return null;
    }

    public Timestamp getStartTime() {
        if (start != null) {
            return new Timestamp(this.start);
        }
        return null;
    }


}
