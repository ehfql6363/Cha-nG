package com.ssafy.chaing.common.exception;

public class AuthenticationException extends BaseException {

    public AuthenticationException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public AuthenticationException(String message) {
        super("INVALID_AUTHENTICATION", message);
    }
}

