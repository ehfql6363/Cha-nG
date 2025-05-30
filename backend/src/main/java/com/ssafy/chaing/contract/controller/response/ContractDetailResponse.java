package com.ssafy.chaing.contract.controller.response;

import com.ssafy.chaing.contract.service.dto.ContractDetailDTO;
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
public class ContractDetailResponse {
    private Long id;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private RentResponse rent;
    private UtilityResponse utility;
    private String status; // 계약 상태는 String으로 변환하여 반환
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class RentResponse {
        private Integer totalAmount;
        private Integer dueDate;
        private String rentAccountNo;
        private String ownerAccountNo;
        private Integer totalRatio;
        private List<UserPaymentInfoResponse> userPaymentInfo;

        // RentDTO -> RentResponse 변환 메서드
        public static RentResponse fromDTO(ContractDetailDTO.RentDTO rentDTO) {
            return new RentResponse(
                    rentDTO.getTotalAmount(),
                    rentDTO.getDueDate(),
                    rentDTO.getRentAccountNo(),
                    rentDTO.getOwnerAccountNo(),
                    rentDTO.getTotalRatio(),
                    rentDTO.getUserPaymentInfo().stream()
                            .map(UserPaymentInfoResponse::new)
                            .toList()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class UserPaymentInfoResponse {
        private Long userId;
        private Integer amount;
        private Integer ratio;

        // UserPaymentInfoDTO -> UserPaymentInfoResponse 변환 메서드
        public UserPaymentInfoResponse(ContractDetailDTO.UserPaymentInfoDTO userPaymentInfoDTO) {
            this.userId = userPaymentInfoDTO.getUserId();
            this.amount = userPaymentInfoDTO.getAmount();
            this.ratio = userPaymentInfoDTO.getRatio();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class UtilityResponse {
        private Long cardId;

        // UtilityDTO -> UtilityResponse 변환 메서드
        public static UtilityResponse fromDTO(ContractDetailDTO.UtilityDTO utilityDTO) {
            return new UtilityResponse(utilityDTO.getCardId());
        }
    }

    // ContractDetailDTO -> ContractDetailResponse 변환 메서드
    public static ContractDetailResponse fromDTO(ContractDetailDTO contractDetailDTO) {
        return new ContractDetailResponse(
                contractDetailDTO.getId(),
                contractDetailDTO.getStartDate(),
                contractDetailDTO.getEndDate(),
                RentResponse.fromDTO(contractDetailDTO.getRent()), // RentResponse 변환
                UtilityResponse.fromDTO(contractDetailDTO.getUtility()), // UtilityResponse 변환
                contractDetailDTO.getStatus().name(), // Enum을 String으로 변환
                contractDetailDTO.getCreatedAt(),
                contractDetailDTO.getUpdatedAt()
        );
    }
}
