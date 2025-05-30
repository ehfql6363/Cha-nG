package com.ssafy.chaing.blockchain.handler.contract.output;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentInfoCountOutput {
    private BigInteger count;
}