package com.open.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.open.api.annotation.OpenApiService;
import com.open.api.bo.Test1BO;
import com.open.api.service.TestOneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 测试开放接口1
 */
@Service
@OpenApiService
public class TestOneServiceImpl implements TestOneService {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TestOneServiceImpl.class);

    /**
     * 方法1
     */
    @Override
    public Test1BO testMethod1(String requestId, Test1BO test1BO) {
        LOGGER.info("【{}】>> 测试开放接口1 >> 方法1 params={}", requestId, JSON.toJSONString(test1BO));
        return test1BO;
    }
}