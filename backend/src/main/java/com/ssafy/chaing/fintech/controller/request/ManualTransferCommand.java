package com.ssafy.chaing.fintech.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ManualTransferCommand {
    private String fromAccountNo;
    private String toAccountNo;
    private Long amount;
}
