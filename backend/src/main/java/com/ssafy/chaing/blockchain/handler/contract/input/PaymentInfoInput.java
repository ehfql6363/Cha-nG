package com.ssafy.chaing.blockchain.handler.contract.input;

import com.ssafy.chaing.contract.domain.ContractUserEntity;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoInput {
    private BigInteger userId;
    private BigInteger amount;
    private BigInteger ratio;

    public static PaymentInfoInput from(ContractUserEntity entity) {
        return new PaymentInfoInput(
                BigInteger.valueOf(entity.getUser().getId()),
                BigInteger.valueOf(entity.getRentAmount()),
                BigInteger.valueOf(entity.getRentRatio() == null ? 0 : entity.getRentRatio())
        );
    }
}