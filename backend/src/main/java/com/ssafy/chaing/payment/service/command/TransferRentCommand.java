package com.ssafy.chaing.payment.service.command;

import com.ssafy.chaing.payment.controller.request.DepositTransferRequest;
import com.ssafy.chaing.payment.controller.request.WithdrawTransferRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransferRentCommand {
    private Long userId;
    private int month;
    private String accountNo;
    private int balance;

    public static TransferRentCommand fromDepositRequest(DepositTransferRequest request, Long userId) {
        return new TransferRentCommand(
                userId,
                request.getMonth(),
                request.getDepositAccountNo(),
                request.getTransactionBalance()
        );
    }

    public static TransferRentCommand fromWithdrawRequest(WithdrawTransferRequest request, Long userId) {
        return new TransferRentCommand(
                userId,
                request.getMonth(),
                request.getWithdrawalAccountNo(),
                request.getTransactionBalance()
        );
    }
}
