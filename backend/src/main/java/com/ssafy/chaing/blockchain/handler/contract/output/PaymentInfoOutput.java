package com.ssafy.chaing.blockchain.handler.contract.output;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentInfoOutput {
    private BigInteger userId;
    private BigInteger amount;
    private BigInteger ratio;
}