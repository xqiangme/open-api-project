package com.open.api.service;


import com.open.api.annotation.OpenApi;
import com.open.api.bo.Test1BO;
import com.open.api.bo.Test2BO;

/**
 * 测试开放接口2
 */
public interface TestTwoService {

    /**
     * 方法1
     */
    @OpenApi(method = "open.api.test.two.method1", desc = "测试接口1，方法1")
    void testMethod1(String requestId, Test2BO test2BO);
}