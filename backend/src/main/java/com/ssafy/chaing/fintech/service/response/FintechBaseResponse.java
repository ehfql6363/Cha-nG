package com.ssafy.chaing.fintech.service.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FintechBaseResponse<T>(
        @JsonProperty("REC")
        T rec
) {
}
