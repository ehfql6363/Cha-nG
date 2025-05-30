package com.ssafy.chaing.contract.service;

import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.UtilityCardEntity;
import com.ssafy.chaing.contract.repository.ContractRepository;
import com.ssafy.chaing.contract.repository.UtilityCardRepository;
import com.ssafy.chaing.contract.service.command.CreateCardCommand;
import com.ssafy.chaing.contract.service.dto.UtilityCardDTO;
import com.ssafy.chaing.fintech.dto.CreateFintechCardRec;
import com.ssafy.chaing.fintech.service.FintechService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final UtilityCardRepository utilityCardRepository;
    private final ContractRepository contractRepository;
    private final FintechService fintechService;

    @Override
    public UtilityCardDTO registerUtilityCard(CreateCardCommand command) {
        CreateFintechCardRec rec = fintechService.createFintechCard(command);

        ContractEntity contract = contractRepository.findByContractMemberUserId(command.userId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.CONTRACT_NOT_FOUND));

        log.info(contract.toString());

        if (contract.getUtilityCard() != null) {
            throw new BadRequestException(ExceptionCode.CARD_ALREADY_EXIST);
        }

        UtilityCardEntity card = UtilityCardEntity.builder()
                .cardNo(rec.cardNo())
                .cvc(rec.cvc())
                .build();

        card = utilityCardRepository.save(card);

        contract.setUtilityCard(card);
        contractRepository.save(contract);

        return new UtilityCardDTO(card.getId());
    }
}
