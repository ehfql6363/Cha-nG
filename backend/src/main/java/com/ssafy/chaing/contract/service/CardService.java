package com.ssafy.chaing.contract.service;

import com.ssafy.chaing.contract.service.command.CreateCardCommand;
import com.ssafy.chaing.contract.service.dto.UtilityCardDTO;

public interface CardService {

    /**
     * 등록된 계좌로 신용카드를 발급합니다.
     *
     * @param command
     * @return 발급한 카드에 대한 PK 값
     */
    UtilityCardDTO registerUtilityCard(CreateCardCommand command);
}
