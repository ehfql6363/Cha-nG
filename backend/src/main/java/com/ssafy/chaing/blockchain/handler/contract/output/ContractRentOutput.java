package com.ssafy.chaing.blockchain.handler.contract.output;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractRentOutput {
    private BigInteger totalAmount;
    private BigInteger dueDate;
    private String rentAccountNo;
    private String ownerAccountNo;
    private BigInteger totalRatio;
    // 컨트랙트에서 반환하는 여섯 번째 값의 의미에 맞게 이름을 수정하세요.
    private BigInteger paymentInfoSize;
}