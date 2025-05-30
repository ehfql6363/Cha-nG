package com.ssafy.chaing.blockchain.handler.contract.output;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContractOverviewOutput {
    private BigInteger id;
    private String startDate;
    private String endDate;
}