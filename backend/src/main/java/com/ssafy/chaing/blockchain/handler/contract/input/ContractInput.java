package com.ssafy.chaing.blockchain.handler.contract.input;

import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.UtilityCardEntity;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractInput {
    private BigInteger id; // 실제 ContractEntity ID
    private String startDate;
    private String endDate;
    private BigInteger rentTotalAmount;
    private BigInteger rentDueDate;
    private String rentAccountNo;
    private String ownerAccountNo;
    private BigInteger rentTotalRatio;
    private List<PaymentInfoInput> paymentInfos;
    private String liveAccountNo;
    private Boolean isUtilityEnabled;
    private BigInteger utilitySplitRatio;
    private BigInteger cardId;

    public static ContractInput from(ContractEntity entity, List<PaymentInfoInput> paymentInfos) {
        UtilityCardEntity card = entity.getUtilityCard();
        return new ContractInput(
                BigInteger.valueOf(entity.getId()),
                entity.getStartDate().toString(),
                entity.getEndDate().toString(),
                BigInteger.valueOf(entity.getRentTotalAmount()),
                BigInteger.valueOf(entity.getDueDate()),
                entity.getRentAccountNo(),
                entity.getOwnerAccountNo(),
                BigInteger.valueOf(entity.getTotalRentRatio()),
                paymentInfos,
                entity.getLiveAccountNo() == null ? 
                                            "N/A" : entity.getLiveAccountNo(),
                card != null,
                entity.getUtilityRatio() != null ?
                        BigInteger.valueOf(entity.getUtilityRatio()) : BigInteger.ZERO,
                card != null ?
                        BigInteger.valueOf(card.getId()) : BigInteger.ZERO
        );
    }
}