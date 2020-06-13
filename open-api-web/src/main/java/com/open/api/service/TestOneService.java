package com.open.api.service;


import com.open.api.annotation.OpenApi;
import com.open.api.web.bo.Test1BO;

/**
 * 测试开放接口1
 */
public interface TestOneService {

    /**
     * 方法1
     */
    @OpenApi(method = "open.api.test.one.method1", desc = "测试接口1,方法1")
    void testMethod1(String requestId, Test1BO test1BO);
}