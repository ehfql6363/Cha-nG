package com.ssafy.chaing.contract.controller.response;

import com.ssafy.chaing.contract.service.dto.UtilityCardDTO;

public record CreateCardResponse(
        Long id
) {
    public static CreateCardResponse from(UtilityCardDTO dto) {
        return new CreateCardResponse(dto.id());
    }
}
