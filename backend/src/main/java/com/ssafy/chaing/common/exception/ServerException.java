package com.ssafy.chaing.common.exception;

import lombok.Getter;

@Getter
public class ServerException extends BaseException {

    public ServerException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public ServerException(String message) {
        super("BAD_REQUEST", message);
    }
}
