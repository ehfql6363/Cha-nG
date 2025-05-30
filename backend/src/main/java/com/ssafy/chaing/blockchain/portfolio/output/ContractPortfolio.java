package com.ssafy.chaing.blockchain.portfolio.output;

import com.ssafy.chaing.blockchain.handler.contract.output.ContractOutput;
import com.ssafy.chaing.blockchain.handler.contract.output.PaymentInfoOutput;
import com.ssafy.chaing.blockchain.handler.rent.output.RentOutput;
import com.ssafy.chaing.blockchain.handler.utility.output.UtilityOutput;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContractPortfolio {
    private Long id;
    private String startDate;
    private String endDate;
    private Long rentTotalAmount;
    private Long rentDueDate;
    private String rentAccountNo;
    private String ownerAccountNo;
    private Long rentTotalRatio;
    private List<PaymentInfoOutput> paymentInfos;
    private String liveAccountNo;
    private Boolean isUtilityEnabled;
    private Long utilitySplitRatio;
    private Long cardId;

    public static ContractPortfolio from(ContractOutput contractOutput) {
        return new ContractPortfolio(
                contractOutput.getId().longValue(),
                contractOutput.getStartDate(),
                contractOutput.getEndDate(),
                contractOutput.getRentTotalAmount().longValue(),
                contractOutput.getRentDueDate().longValue(),
                contractOutput.getRentAccountNo(),
                contractOutput.getOwnerAccountNo(),
                contractOutput.getRentTotalRatio().longValue(),
                contractOutput.getPaymentInfos(),
                contractOutput.getLiveAccountNo(),
                contractOutput.getIsUtilityEnabled(),
                contractOutput.getUtilitySplitRatio().longValue(),
                contractOutput.getCardId().longValue()
        );
    }
}
