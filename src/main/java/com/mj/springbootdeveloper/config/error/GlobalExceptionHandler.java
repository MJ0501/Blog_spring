package com.mj.springbootdeveloper.config.error;

import com.mj.springbootdeveloper.config.error.exception.BusinessBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e){
        log.error("HttpRequestMethodNotSupportedException",e);
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(BusinessBaseException.class)
    protected ResponseEntity<ErrorResponse> handle(BusinessBaseException e){
        log.error("BusinessException",e);
        return createErrorResponseEntity(e.getErrorCode());
    }
    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(
          ErrorResponse.of(errorCode),errorCode.getStatus()
        );
    }
}
