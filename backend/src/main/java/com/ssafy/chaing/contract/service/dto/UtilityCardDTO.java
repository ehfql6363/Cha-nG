package com.ssafy.chaing.contract.service.dto;

import com.ssafy.chaing.contract.domain.UtilityCardEntity;

public record UtilityCardDTO(
        Long id
) {
    public static UtilityCardDTO of(UtilityCardEntity entity) {
        return new UtilityCardDTO(entity.getId());
    }
}
