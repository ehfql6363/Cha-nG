package com.ssafy.chaing.fintech.service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.chaing.fintech.service.common.HeaderWithUserKeyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InquireDemandDepositAccountRequest {
    @JsonProperty("Header")
    private HeaderWithUserKeyDTO header;
    private String accountNo;
}
