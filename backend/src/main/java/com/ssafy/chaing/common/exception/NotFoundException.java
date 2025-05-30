package com.ssafy.chaing.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends BaseException{

    public NotFoundException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }

}
