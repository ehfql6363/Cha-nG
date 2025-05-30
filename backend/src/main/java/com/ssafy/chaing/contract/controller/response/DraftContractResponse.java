package com.ssafy.chaing.contract.controller.response;

import com.ssafy.chaing.contract.service.dto.ContractDTO;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DraftContractResponse {
    private Long id;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public static class ContractMemberResponse {
        private Long id;
    }

    public static DraftContractResponse from(ContractDTO dto) {
        return new DraftContractResponse(
                dto.getId(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }
}
