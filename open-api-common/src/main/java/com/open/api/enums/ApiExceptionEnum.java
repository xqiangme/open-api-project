package com.open.api.enums;

/**
 * 异常枚举
 */
public enum ApiExceptionEnum implements EnumInterface {

    /**
     * api异常枚举
     */
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常"),
    API_NOT_EXIST("API_NOT_EXIST", "API方法不存在"),
    INVALID_PUBLIC_PARAM("INVALID_PUBLIC_PARAM", "无效公共参数 >> {0}"),
    INVALID_REQUEST_ERROR("INVALID_REQUEST_ERROR", " 请求方式 {0} 错误 ! 请使用 {1} 方式"),
    INVALID_PARAM("INVALID_PARAM", "无效参数 >> 参数[{0}] >> 原因[{1}]"),
    INVALID_SIGN("INVALID_SIGN", "无效签名"),
    ;

    ApiExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
