package com.open.api.controller;

import com.alibaba.fastjson.JSON;
import com.open.api.client.ApiClient;
import com.open.api.model.ResultModel;
import com.open.api.util.SystemClock;
import jodd.util.StringPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 统一网关
 */
@RestController
@RequestMapping("/open")
public class OpenApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiController.class);

    @Autowired
    private ApiClient apiClient;


    /**
     * 统一网关入口
     *
     * @param method       请求方法
     * @param version      版本
     * @param apiRequestId 请求标识（用于日志中分辨是否是同一次请求）
     * @param charset      请求编码
     * @param signType     签名格式
     * @param sign         签名
     * @param content      业务内容参数
     * @author 码农猿
     */
    @PostMapping("/gateway")
    public ResultModel gateway(@RequestParam(value = "app_id", required = true) String appId,
                               @RequestParam(value = "method", required = true) String method,
                               @RequestParam(value = "version", required = true) String version,
                               @RequestParam(value = "api_request_id", required = true) String apiRequestId,
                               @RequestParam(value = "charset", required = true) String charset,
                               @RequestParam(value = "sign_type", required = true) String signType,
                               @RequestParam(value = "sign", required = true) String sign,
                               @RequestParam(value = "content", required = true) String content,
                               HttpServletRequest request) throws Throwable {

        Map<String, Object> params = WebUtils.getParametersStartingWith(request, StringPool.EMPTY);
        LOGGER.info("【{}】>> 网关执行开始 >> method={} params = {}", apiRequestId, method, JSON.toJSONString(params));
        long start = SystemClock.millisClock().now();

        //验签
        apiClient.checkSign(params, apiRequestId, charset, signType);

        //请求接口
        ResultModel result = apiClient.invoke(method, apiRequestId, content);

        LOGGER.info("【{}】>> 网关执行结束 >> method={},result = {}, times = {} ms",
                apiRequestId, method, JSON.toJSONString(result), (SystemClock.millisClock().now() - start));

        return result;
    }


}
