package com.open.api.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 公共开关，key值 属性配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "open.api.common.key")
public class ApplicationProperty {

    /**
     * 是否校验签名 0-不校验
     */
    private Boolean isCheckSign = true;

    /**
     * 验签公钥
     */
    private String publicKey;

}