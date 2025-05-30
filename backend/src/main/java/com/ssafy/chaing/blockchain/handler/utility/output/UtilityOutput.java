package com.ssafy.chaing.blockchain.handler.utility.output;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UtilityOutput {
    private BigInteger id;
    private BigInteger accountId;
    private BigInteger month;
    private String from;
    private String to;
    private BigInteger amount;
    private Boolean status;
    private String time;
}
