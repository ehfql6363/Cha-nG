package com.ssafy.chaing.contract.controller.request;

import com.ssafy.chaing.contract.service.command.ApproveContractCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ApproveContractRequest {
    private String accountNo;

    public ApproveContractCommand toCommand(Long userId) {
        return new ApproveContractCommand(
                userId,
                accountNo
        );
    }
}
