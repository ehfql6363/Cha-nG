package com.ssafy.chaing.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class BaseException extends RuntimeException {
    private final String code;
    private final String message;

    public BaseException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
