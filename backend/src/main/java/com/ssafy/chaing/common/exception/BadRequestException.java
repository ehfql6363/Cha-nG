package com.ssafy.chaing.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends BaseException {

    public BadRequestException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public BadRequestException(String message) {
        super("BAD_REQUEST", message);
    }
}
