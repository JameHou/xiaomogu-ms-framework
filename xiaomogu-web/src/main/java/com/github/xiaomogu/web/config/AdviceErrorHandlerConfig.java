package com.github.xiaomogu.web.config;

import com.github.xiaomogu.commons.exception.MicroServiceException;
import com.github.xiaomogu.commons.jackson.RespResultCode;
import com.github.xiaomogu.commons.jackson.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Configuration
@ConditionalOnWebApplication
@RestControllerAdvice
public class AdviceErrorHandlerConfig {

    @ConditionalOnClass(MicroServiceException.class)
    @RestControllerAdvice
    static class MicroServiceExceptionConfiguration{
        @ExceptionHandler(MicroServiceException.class)
        public ResponseResult handlerMicroServiceException(MicroServiceException m){
            return ResponseResult.fail(m.getCode(),m.getMessage());
        }
    }

    @ExceptionHandler(Throwable.class)
    public ResponseResult handlerMicroServiceException(Throwable e){
        log.error("system error ,please call manager ......{}",e);
        return ResponseResult.fail(RespResultCode.FAIL.getCode(),RespResultCode.FAIL.getMsg());
    }
}
