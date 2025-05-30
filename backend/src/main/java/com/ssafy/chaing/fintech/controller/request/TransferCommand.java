package com.ssafy.chaing.fintech.controller.request;

import com.ssafy.chaing.payment.domain.FeeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransferCommand {
    private Long id;
    private Long contractId;
    private Long month;
    private String from;
    private String fromAccountNo;
    private String to;
    private String toAccountNo;
    private int amount;
    private Boolean status;
    private String time;
    private FeeType feeType;
    private Long groupId;
    private Long userId;
}
