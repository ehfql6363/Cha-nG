package com.ssafy.chaing.fintech.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.chaing.fintech.service.response.ClientErrorResponse;

public class ClientErrorParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ClientErrorResponse parseErrorResponse(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, ClientErrorResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("응답 파싱 실패: " + e.getMessage());
        }
    }
}
