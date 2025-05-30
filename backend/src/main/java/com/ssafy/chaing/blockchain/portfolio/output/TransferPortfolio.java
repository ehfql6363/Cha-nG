package com.ssafy.chaing.blockchain.portfolio.output;

import com.ssafy.chaing.blockchain.handler.rent.output.RentOutput;
import com.ssafy.chaing.blockchain.handler.utility.output.UtilityOutput;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferPortfolio {
    private Map<String, List<RentOutput>> monthlyRent;
    private Map<String, List<UtilityOutput>> monthlyUtility;
}
