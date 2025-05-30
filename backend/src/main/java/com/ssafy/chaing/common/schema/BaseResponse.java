package com.ssafy.chaing.common.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, data);
    }

    public static <T> BaseResponse<T> error(T error) {
        return new BaseResponse<>(false, error);
    }
}
