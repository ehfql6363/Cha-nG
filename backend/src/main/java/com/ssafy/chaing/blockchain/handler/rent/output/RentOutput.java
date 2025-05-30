package com.ssafy.chaing.blockchain.handler.rent.output;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentOutput {
    private BigInteger id;
    private BigInteger accountId;
    private BigInteger month;
    private String from;
    private String to;
    private BigInteger amount;
    private Boolean status;
    private String time;
}
