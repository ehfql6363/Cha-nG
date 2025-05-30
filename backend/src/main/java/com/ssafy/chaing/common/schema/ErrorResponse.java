package com.ssafy.chaing.common.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message);
    }
}
