package com.ssafy.chaing.contract.service.card.dto;

import com.ssafy.chaing.contract.domain.UtilityCardEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
    private Long id;
    private String cardNo;
    private String cvc;

    public CardDTO from(UtilityCardEntity entity) {
        return new CardDTO(
                entity.getId(),
                entity.getCardNo(),
                entity.getCvc()
        );
    }
}
