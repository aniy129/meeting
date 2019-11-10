package com.meeting.meeting.model.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@SuppressWarnings("unchecked")
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {
    @ApiModelProperty(value = "消息")
    private String message;
    @ApiModelProperty(value = "状态码")
    private String status;
    @ApiModelProperty(value = "数据")
    private T data;

    public static <T> BaseResponse<T> success(String msg, T data) {
        return new BaseResponse<T>(msg, "200", data);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>("操作成功", "200", data);
    }

    public static <T> BaseResponse<T> failure(String msg, T data) {
        return new BaseResponse<T>(msg, "400", data);
    }

    public static <T> BaseResponse<T> failure(String msg) {
        return new BaseResponse<T>(msg, "400", null);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
