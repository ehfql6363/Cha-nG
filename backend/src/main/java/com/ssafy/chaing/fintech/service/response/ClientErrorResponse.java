package com.ssafy.chaing.fintech.service.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClientErrorResponse(

        @JsonProperty("responseCode")
        String responseCode,

        @JsonProperty("responseMessage")
        String responseMessage
) {

}

