package com.ssafy.chaing.blockchain.handler.utility.input;

import com.ssafy.chaing.fintech.controller.request.TransferCommand;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UtilityInput {
    private BigInteger id;
    private BigInteger contractId;
    private BigInteger month;
    private String from;
    private String to;
    private BigInteger amount;
    private Boolean status;
    private String time;

    public static UtilityInput from(TransferCommand command) {
        return new UtilityInput(
                BigInteger.valueOf(command.getId()),
                BigInteger.valueOf(command.getContractId()),
                BigInteger.valueOf(command.getMonth() % 100),
                command.getFrom(),
                command.getTo(),
                BigInteger.valueOf(command.getAmount()),
                command.getStatus(),
                command.getTime()
        );
    }
}
