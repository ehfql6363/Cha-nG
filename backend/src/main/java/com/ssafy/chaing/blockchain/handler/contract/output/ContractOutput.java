package com.ssafy.chaing.blockchain.handler.contract.output;

import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ContractOutput {
    private BigInteger id;
    private String startDate;
    private String endDate;
    private BigInteger rentTotalAmount;
    private BigInteger rentDueDate;
    private String rentAccountNo;
    private String ownerAccountNo;
    private BigInteger rentTotalRatio;
    private List<PaymentInfoOutput> paymentInfos;
    private String liveAccountNo;
    private Boolean isUtilityEnabled;
    private BigInteger utilitySplitRatio;
    private BigInteger cardId;
}
