package com.ssafy.chaing.contract.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Getter
public class ApproveContractCommand {
    Long userId;
    String accountNo;
}
