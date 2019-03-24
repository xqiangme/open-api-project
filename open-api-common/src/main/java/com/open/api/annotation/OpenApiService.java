package com.open.api.annotation;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

/**
 * 开放接口实现类注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface OpenApiService {
}
