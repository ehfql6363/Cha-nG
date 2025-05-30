package com.ssafy.chaing.blockchain.portfolio.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferPortfolioResponse {
    private Long contractId;
    private TransferPortfolio transferPortfolio;
}
