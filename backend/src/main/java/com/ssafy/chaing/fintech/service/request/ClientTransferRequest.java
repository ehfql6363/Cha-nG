package com.ssafy.chaing.fintech.service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.chaing.fintech.controller.request.TransferCommand;
import com.ssafy.chaing.fintech.service.common.HeaderWithUserKeyDTO;
import com.ssafy.chaing.payment.domain.FeeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientTransferRequest {

    @JsonProperty("Header")
    private HeaderWithUserKeyDTO header;

    @JsonProperty("depositAccountNo")
    private String depositAccountNo;

    @JsonProperty("depositTransactionSummary")
    private String depositTransactionSummary;

    @JsonProperty("transactionBalance")
    private String transactionBalance;

    @JsonProperty("withdrawalAccountNo")
    private String withdrawalAccountNo;

    @JsonProperty("withdrawalTransactionSummary")
    private String withdrawalTransactionSummary;

    public ClientTransferRequest(HeaderWithUserKeyDTO header, TransferCommand body) {
        FeeType type = body.getFeeType();

        String depositTransactionSummary = " : 입금";
        String withdrawalTransactionSummary = " : 출금";
        if (body.getUserId() == null) {
            withdrawalTransactionSummary = " : 납부";
        }

        String feeTypeMemo = null;
        if (type.equals(FeeType.UTILITY)) {
            feeTypeMemo = "공과금 ";
        } else if (type.equals(FeeType.RENT)) {
            feeTypeMemo = "월세 ";
        }

        if (feeTypeMemo != null) {
            depositTransactionSummary = feeTypeMemo + depositTransactionSummary;
            withdrawalTransactionSummary = feeTypeMemo + withdrawalTransactionSummary;
        }

        this.header = header;
        this.depositAccountNo = body.getToAccountNo();
        this.depositTransactionSummary = depositTransactionSummary;
        this.transactionBalance = String.valueOf(body.getAmount());
        this.withdrawalAccountNo = body.getFromAccountNo();
        this.withdrawalTransactionSummary = withdrawalTransactionSummary;
    }

    public void setNameIntoSummary(FeeType type, String name) {
        if (type.equals(FeeType.RENT)) {
            this.depositTransactionSummary = "월세 (" + name + ") : 입금";
            this.withdrawalTransactionSummary = "월세 (" + name + ") : 출금";
        } else {
            this.depositTransactionSummary = "공과금 (" + name + ") : 입금";
            this.withdrawalTransactionSummary = "공과금 (" + name + ") : 출금";
        }
    }
}
