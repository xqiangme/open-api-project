package com.open.api.advice;

import com.open.api.enums.ApiExceptionEnum;
import com.open.api.exception.BusinessException;
import com.open.api.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

/**
 * 统一异常处理
 */
@ControllerAdvice
@EnableAspectJAutoProxy
public class ExceptionAdvice {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @CrossOrigin
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultModel defaultExceptionHandler(Exception exception, HttpServletResponse response) {
        ResultModel result;

        try {
            logger.warn("全局业务处理异常 >> error = {}", exception.getMessage(), exception);
            throw exception;

        } catch (BusinessException e) {
            result = ResultModel.error(e.getCode(), e.getMsg());

        } catch (HttpRequestMethodNotSupportedException e) {
            String errorMsg = MessageFormat.format(ApiExceptionEnum.INVALID_REQUEST_ERROR.getMsg(), e.getMethod(), e.getSupportedHttpMethods());
            result = ResultModel.error(ApiExceptionEnum.INVALID_REQUEST_ERROR.getCode(), errorMsg);

        } catch (MissingServletRequestParameterException e) {
            String errorMsg = MessageFormat.format(ApiExceptionEnum.INVALID_PUBLIC_PARAM.getMsg(), e.getMessage());
            result = ResultModel.error(ApiExceptionEnum.INVALID_PUBLIC_PARAM.getCode(), errorMsg);
        } catch (Exception e) {
            result = ResultModel.error(ApiExceptionEnum.SYSTEM_ERROR.getCode(), ApiExceptionEnum.SYSTEM_ERROR.getMsg());

        }

        return result;

    }

}
