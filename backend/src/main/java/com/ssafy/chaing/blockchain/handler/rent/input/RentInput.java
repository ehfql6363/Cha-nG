package com.ssafy.chaing.blockchain.handler.rent.input;

import com.ssafy.chaing.fintech.controller.request.TransferCommand;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentInput {
    private BigInteger id;
    private BigInteger contractId; // contractId
    private BigInteger month;
    private String from;
    private String to;
    private BigInteger amount;
    private Boolean status;
    private String time;

    public static RentInput from(TransferCommand command) {
        return new RentInput(
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
