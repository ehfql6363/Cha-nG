package com.ssafy.chaing.contract.service.command;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmContractCommand {
    private Long userId;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private ConfirmRentCommand rent;
    private ConfirmUtilityCommand utility;

    @Getter
    @AllArgsConstructor
    public static class ConfirmRentCommand {
        private Integer totalAmount;
        private Integer dueDate;
        private String rentAccountNo;
        private String ownerAccountNo;
        private Integer totalRatio;
        private List<ConfirmUserPaymentCommand> userPaymentInfo;

        @Getter
        @AllArgsConstructor
        public static class ConfirmUserPaymentCommand {
            private Long userId;
            private Integer amount;
            private Integer ratio;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ConfirmUtilityCommand {
        private Long cardId;
    }
}
