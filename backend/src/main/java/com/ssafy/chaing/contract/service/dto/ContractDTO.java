package com.ssafy.chaing.contract.service.dto;

import com.ssafy.chaing.contract.domain.ContractEntity;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContractDTO {
    private Long id;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;


    public static ContractDTO from(ContractEntity contract) {
        return new ContractDTO(
                contract.getId(),
                contract.getCreatedAt().atZone(ZoneId.of("UTC")),
                contract.getUpdatedAt().atZone(ZoneId.of("UTC"))
        );
    }
}
