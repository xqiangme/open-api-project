package com.open.api.annotation;

import java.lang.annotation.*;

/**
 * 开放接口注解
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApi {

    /**
     * api 方法名
     *
     * @return
     */
    String method();


    /**
     * 方法描述
     */
    String desc() default "";

}
