package com.ssafy.chaing.contract.service.dto;

import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.ContractStatus;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ContractDetailDTO {
    private Long id;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private RentDTO rent;
    private UtilityDTO utility;
    private ContractStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class RentDTO {
        private Integer totalAmount;
        private Integer dueDate;
        private String rentAccountNo;
        private String ownerAccountNo;
        private Integer totalRatio;
        private List<UserPaymentInfoDTO> userPaymentInfo;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class UserPaymentInfoDTO {
        private Long userId;
        private Integer amount;
        private Integer ratio;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class UtilityDTO {
        private Long cardId;
    }

    public static ContractDetailDTO from(ContractEntity contract) {
        RentDTO rent = new RentDTO(
                contract.getRentTotalAmount(),
                contract.getDueDate(),
                contract.getRentAccountNo(),
                contract.getOwnerAccountNo(),
                contract.getTotalRentRatio(),
                contract.getMembers().stream()
                        .filter(user -> !user.isSurplusUser())
                        .map(cu ->
                                new UserPaymentInfoDTO(
                                        cu.getUser().getId(),
                                        cu.getRentAmount(),
                                        cu.getRentRatio()
                                )
                        )
                        .toList()
        );

        UtilityDTO utility = new UtilityDTO(
                contract.getUtilityCard() != null ? contract.getUtilityCard().getId() : null);

        return new ContractDetailDTO(
                contract.getId(),
                contract.getStartDate(),
                contract.getEndDate(),
                rent,
                utility,
                contract.getStatus(),
                contract.getCreatedAt().atZone(ZoneId.of("UTC")),
                contract.getUpdatedAt().atZone(ZoneId.of("UTC"))
        );
    }
}
