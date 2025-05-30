package com.ssafy.chaing.contract.controller.request;

import com.ssafy.chaing.contract.service.command.DraftContractCommand;
import com.ssafy.chaing.contract.service.command.DraftContractCommand.RentCommand;
import com.ssafy.chaing.contract.service.command.DraftContractCommand.RentCommand.UserPaymentCommand;
import com.ssafy.chaing.contract.service.command.DraftContractCommand.UtilityCommand;
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
public class UpdateDraftContractRequest {
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private RentRequest rent;
    private UtilityRequest utility;

    public DraftContractCommand toCommand(Long userId) {
        return new DraftContractCommand(
                userId,
                startDate,
                endDate,
                rent.toCommand(),
                utility.toCommand()
        );
    }

    // 내부 RentRequest
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class RentRequest {
        private Integer totalAmount;
        private Integer dueDate;
        private String rentAccountNo;
        private String ownerAccountNo;
        private Integer totalRatio;
        private List<UserPaymentInfoRequest> userPaymentInfo;

        public RentCommand toCommand() {
            return new RentCommand(
                    totalAmount,
                    dueDate,
                    rentAccountNo,
                    ownerAccountNo,
                    totalRatio,
                    userPaymentInfo.stream().map(UserPaymentInfoRequest::toCommand).toList()
            );
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Setter
        @Getter
        public static class UserPaymentInfoRequest {
            private Long userId;
            private Integer amount;
            private Integer ratio;

            public UserPaymentCommand toCommand() {
                return new UserPaymentCommand(userId, amount, ratio);
            }
        }
    }

    // 내부 UtilityRequest
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class UtilityRequest {
        private Long cardId;

        public UtilityCommand toCommand() {
            return new UtilityCommand(cardId);
        }
    }

}
