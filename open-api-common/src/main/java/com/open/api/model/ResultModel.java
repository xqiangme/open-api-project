package com.open.api.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

/**
 * 开放接口统一返回对象
 */
@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultModel implements Serializable {

    private static final long serialVersionUID = -1L;

    private boolean success;
    private Object data;
    private String errorCode;
    private String errorMsg;

    /**
     * 处理成功构造器
     *
     * @param data
     */
    public ResultModel(Object data) {
        this.success = true;
        this.data = data;
    }

    /**
     * 处理失败构造器
     *
     * @param errorCode
     * @param errorMsg
     */
    public ResultModel(String errorCode, String errorMsg) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 返回成功方法
     *
     * @param data
     * @return
     */
    public static ResultModel success(Object data) {
        return new ResultModel(data);
    }

    /**
     * 返回失败方法
     *
     * @param errorCode
     * @param errorMsg
     * @return
     */
    public static ResultModel error(String errorCode, String errorMsg) {
        return new ResultModel(errorCode, errorMsg);
    }


}
