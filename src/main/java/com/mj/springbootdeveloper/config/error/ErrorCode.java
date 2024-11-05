package com.mj.springbootdeveloper.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Err1", "올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Err2", "잘못된 HTTP 메서드를 호출했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Err3", "서버 에러가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND,"Err4","존재하지 않는 엔터티입니다."),
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND,"Err5","존재하지 않는 글입니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;
    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
