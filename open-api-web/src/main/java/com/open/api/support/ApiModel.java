package com.open.api.support;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * api接口对象
 */
@Data
public class ApiModel {

    /**
     * 类 spring bean
     */
    private String beanName;

    /**
     * 方法对象
     */
    private Method method;

    /**
     * 业务参数
     */
    private String paramName;

    public ApiModel(String beanName, Method method, String paramName) {
        this.beanName = beanName;
        this.method = method;
        this.paramName = paramName;
    }
}
