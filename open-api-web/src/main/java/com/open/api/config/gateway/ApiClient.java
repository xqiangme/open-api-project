package com.open.api.config.gateway;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.open.api.config.context.ApplicationContextHelper;
import com.open.api.config.property.ApplicationProperty;
import com.open.api.enums.ApiExceptionEnum;
import com.open.api.exception.BusinessException;
import com.open.api.model.ApiModel;
import com.open.api.model.ResultModel;
import com.open.api.util.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Api请求客户端
 *
 * @author 程序员小强
 */
@Service
public class ApiClient {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClient.class);

    /**
     * jackson 序列化工具类
     */
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    /**
     * Api本地容器
     */
    private final ApiContainer apiContainer;

    public ApiClient(ApiContainer apiContainer) {
        this.apiContainer = apiContainer;
    }


    @Resource
    private ApplicationProperty applicationProperty;

    /**
     * 验签
     *
     * @param params          请求参数
     * @param requestRandomId 请求随机标识（用于日志中分辨是否是同一次请求）
     * @param charset         请求编码
     * @param signType        签名格式
     * @author 程序员小强
     */
    public void checkSign(Map<String, Object> params, String requestRandomId, String charset, String signType) {

        try {
            //校验签名开关
            if (!applicationProperty.getIsCheckSign()) {
                LOGGER.warn("【{}】>> 验签开关关闭", requestRandomId);
                return;
            }

            //map类型转换
            Map<String, String> map = new HashMap<>(params.size());
            for (String s : params.keySet()) {
                map.put(s, params.get(s).toString());
            }

            LOGGER.warn("【{}】 >> 验签参数 {}", requestRandomId, map);
            boolean checkSign = AlipaySignature.rsaCheckV1(map, applicationProperty.getPublicKey(), charset, signType);
            if (!checkSign) {
                LOGGER.info("【{}】 >> 验签失败 >> params = {}", requestRandomId, JSON.toJSONString(params));
                throw new BusinessException(ApiExceptionEnum.INVALID_SIGN);
            }
            LOGGER.warn("【{}】 >> 验签成功", requestRandomId);

        } catch (Exception e) {
            LOGGER.error("【{}】 >> 验签异常 >> params = {}, error = {}",
                    requestRandomId, JSON.toJSONString(params), ExceptionUtils.getStackTrace(e));
            throw new BusinessException(ApiExceptionEnum.INVALID_SIGN);

        }

    }



    /**
     * Api调用方法
     *
     * @param method          请求方法
     * @param requestRandomId 请求随机标识
     * @param content         请求体
     * @author 程序员小强
     */
    public ResultModel invoke(String method, String requestRandomId, String content) throws Throwable {
        //获取api方法
        ApiModel apiModel = apiContainer.get(method);

        if (null == apiModel) {
            LOGGER.info("【{}】 >> API方法不存在 >> method = {}", requestRandomId, method);
            throw new BusinessException(ApiExceptionEnum.API_NOT_EXIST);
        }

        //获得spring bean
        Object bean = ApplicationContextHelper.getBean(apiModel.getBeanName());
        if (null == bean) {
            LOGGER.warn("【{}】 >> API方法不存在 >> method = {}, beanName = {}", requestRandomId, method, apiModel.getBeanName());
            throw new BusinessException(ApiExceptionEnum.API_NOT_EXIST);
        }

        //处理业务参数
        // 忽略JSON字符串中存在，而在Java中不存在的属性
        JSON_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 设置下划线序列化方式
        JSON_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        Object result = JSON_MAPPER.readValue(StringUtils.isBlank(content) ? "{}" : content, Class.forName(apiModel.getParamName()));

        //校验参数
        ValidateUtils.validate(result);

        //执行对应方法
        try {
            Object obj = apiModel.getMethod().invoke(bean, requestRandomId, result);
            return ResultModel.success(obj);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                throw ((InvocationTargetException) e).getTargetException();
            }
            throw new BusinessException(ApiExceptionEnum.SYSTEM_ERROR);
        }

    }


}
