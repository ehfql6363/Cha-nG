package com.ssafy.chaing.contract.service.command;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DraftContractCommand {
    private Long userId;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private RentCommand rent;
    private UtilityCommand utility;

    // RentInfoCommand (렌트 정보)
    @Getter
    @AllArgsConstructor
    public static class RentCommand {
        private Integer totalAmount;
        private Integer dueDate;
        private String rentAccountNo;
        private String ownerAccountNo;
        private Integer totalRatio;
        private List<UserPaymentCommand> userPaymentInfo;

        // UserPaymentInfoCommand (사용자 납부 정보)
        @Getter
        @AllArgsConstructor
        public static class UserPaymentCommand {
            private Long userId;
            private Integer amount;
            private Integer ratio;
        }
    }

    // UtilityInfoCommand (공과금 정보)
    @Getter
    @AllArgsConstructor
    public static class UtilityCommand {
        private Long cardId;
    }
}
