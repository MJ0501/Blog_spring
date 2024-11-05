package com.mj.springbootdeveloper.config.error.exception;

import com.mj.springbootdeveloper.config.error.ErrorCode;

public class BusinessBaseException extends RuntimeException{
    private final ErrorCode errorCode;
    public BusinessBaseException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
