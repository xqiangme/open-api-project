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
import org.springframework.util.Assert;

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

            //TODO 将app_id与公钥映射关系存储在配置文件或数据库中去
            Object app_id = params.get("app_id");
            Assert.notNull(app_id, "app_id can't be null");

            Map<String, String> publicKeyPair = new HashMap<>();
            publicKeyPair.put("1001", "" +
                    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCX55VkmghF6t2gzuOb2On7wPoD\n" +
                    "w6K3/s/6dAxd9NivnNF/dzv0fjIlrMY2kyL3MXLzHzailNrdfgeTUr8/L8FnI+kM\n" +
                    "VX9nYqPMFArLuRsO6RUVP0Uz3JVBqhd9xGdRHHZJJNXsc6CHHmGGhDVgskSD3Eo7\n" +
                    "EdCT5IeGfZ6htSJU5QIDAQAB\n" +
                    "");
            publicKeyPair.put("1002", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwdK0le7UypaYWEWBoQkGTpu2nlYnM+iX8pa7Gz9neSnANfcuxrMgmmXrG+Dw6f3OQpiHl4mbKelyVjJTBLh4cvo1am2OSZvBjefZNshphx4ctBtx6BpGIRwlTvJRsjajMCY3RyF6px+Ehz0zeDBf7w2M6GZnSv2YhPp2YIZZo/01GYVJ4RgzzfkEEKyC+96+shqANHVOaiiG4byMJL8zv9q3kshSNCA1NT8r7toq8wPYhUKwCas/i5GauyRCIX+KhCpD9+/HTkFmr0PUWoNIZ61lRpTMbiTfDWU/5tJ3UPwdk6oVM3ZwkLoBAO8HXHvPk6avCupXq63u4wGrn30eiwIDAQAB");

            if (!publicKeyPair.containsKey(app_id.toString())) {
                LOGGER.error("【{}】 >> 验签失败 >> params = {}", requestRandomId, JSON.toJSONString(params));
                throw new BusinessException(ApiExceptionEnum.INVALID_PARAM, "app_id 未注册公钥");
            }

            String publicKey = publicKeyPair.get(app_id.toString());

            LOGGER.warn("【{}】 >> 验签参数 {}", requestRandomId, map);
            boolean checkSign = AlipaySignature.rsaCheckV1(map, publicKey, charset, signType);
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
        Object result = null;
        if (!StringUtils.isBlank(content) && !StringUtils.isBlank(apiModel.getParamName())) {
            result = JSON_MAPPER.readValue(StringUtils.isBlank(content) ? "{}" : content, Class.forName(apiModel.getParamName()));
        }

        if (result != null) {
            //校验参数
            ValidateUtils.validate(result);
        }

        //执行对应方法
        try {
            Object obj = null;
            if (result != null) {
                obj = apiModel.getMethod().invoke(bean, requestRandomId, result);
            } else {
                obj = apiModel.getMethod().invoke(bean, requestRandomId);
            }
            return ResultModel.success(obj);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                throw ((InvocationTargetException) e).getTargetException();
            }
            throw new BusinessException(ApiExceptionEnum.SYSTEM_ERROR);
        }

    }


}
