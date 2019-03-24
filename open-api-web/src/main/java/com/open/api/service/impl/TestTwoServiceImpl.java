package com.open.api.service.impl;


import com.open.api.annotation.OpenApiService;
import com.open.api.bo.Test2BO;
import com.open.api.service.TestTwoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 测试开放接口2
 */
@Service
@OpenApiService
public class TestTwoServiceImpl implements TestTwoService {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TestOneServiceImpl.class);

    /**
     * 方法1
     */
    @Override
    public void testMethod1(String requestId, Test2BO test2BO) {
        LOGGER.info("【{}】>> 测试开放接口2 >> 方法1 params={}", requestId, test2BO);
    }
}