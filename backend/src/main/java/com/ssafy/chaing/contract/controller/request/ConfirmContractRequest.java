package com.ssafy.chaing.contract.controller.request;

import com.ssafy.chaing.contract.service.command.ConfirmContractCommand;
import com.ssafy.chaing.contract.service.command.ConfirmContractCommand.ConfirmRentCommand;
import com.ssafy.chaing.contract.service.command.ConfirmContractCommand.ConfirmRentCommand.ConfirmUserPaymentCommand;
import com.ssafy.chaing.contract.service.command.ConfirmContractCommand.ConfirmUtilityCommand;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ConfirmContractRequest {
    @NotNull
    private ZonedDateTime startDate;
    @NotNull
    private ZonedDateTime endDate;
    @NotNull
    private ConfirmRentRequest rent;

    private ConfirmUtilityRequest utility;

    public ConfirmContractCommand toCommand(Long userId) {
        return new ConfirmContractCommand(
                userId,
                startDate,
                endDate,
                rent.toCommand(),
                utility.toCommand()
        );
    }

    // 내부 RentComfirmRequest
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class ConfirmRentRequest {
        @NotNull
        private Integer totalAmount;
        @NotNull
        private Integer dueDate;
        @NotNull
        private String rentAccountNo;
        @NotNull
        private String ownerAccountNo;
        @NotNull
        private Integer totalRatio;
        @NotNull
        private List<UserPaymentInfoConfirmRequest> userPaymentInfo;

        public ConfirmRentCommand toCommand() {
            return new ConfirmRentCommand(
                    totalAmount,
                    dueDate,
                    rentAccountNo,
                    ownerAccountNo,
                    totalRatio,
                    userPaymentInfo.stream().map(UserPaymentInfoConfirmRequest::toCommand).toList()
            );
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Setter
        @Getter
        public static class UserPaymentInfoConfirmRequest {
            @NotNull
            private Long userId;
            @NotNull
            private Integer amount;
            @NotNull
            private Integer ratio;

            public ConfirmUserPaymentCommand toCommand() {
                return new ConfirmUserPaymentCommand(userId, amount, ratio);
            }
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class ConfirmUtilityRequest {
        private Long cardId;

        public ConfirmUtilityCommand toCommand() {
            return new ConfirmUtilityCommand(cardId);
        }
    }

}
