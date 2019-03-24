package com.open.api.exception;

import java.text.MessageFormat;

/**
 * 基础异常
 */
public class BaseException extends RuntimeException {
    protected String msg;
    protected String code;

    protected BaseException(String message) {
        super(message);
    }

    protected BaseException(String code, String msgFormat, Object... args) {
        super(MessageFormat.format(msgFormat, args));
        this.code = code;
        this.msg = MessageFormat.format(msgFormat, args);
    }

    public String getMsg() {
        return this.msg;
    }

    public String getCode() {
        return this.code;
    }
}