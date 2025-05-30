package com.ssafy.chaing.fintech.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FintechResponse<T> {
    private T data;
}
