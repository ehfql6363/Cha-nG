package com.ssafy.chaing.fintech.service;

import com.ssafy.chaing.contract.service.command.CreateCardCommand;
import com.ssafy.chaing.fintech.controller.request.AccountHistoryCommand;
import com.ssafy.chaing.fintech.controller.request.InquireBillingCommand;
import com.ssafy.chaing.fintech.controller.request.ManualTransferCommand;
import com.ssafy.chaing.fintech.controller.request.SimpleTransferCommand;
import com.ssafy.chaing.fintech.controller.request.TransferCommand;
import com.ssafy.chaing.fintech.controller.response.FintechResponse;
import com.ssafy.chaing.fintech.dto.CreateFintechCardRec;
import com.ssafy.chaing.fintech.dto.InquireBillingStatementsRec;
import com.ssafy.chaing.fintech.service.dto.TransferDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface FintechService {
    CreateFintechCardRec createFintechCard(CreateCardCommand createCardCommand);

    TransferDTO manualTransfer(ManualTransferCommand command, Long userId);
    TransferDTO rentTransfer(TransferCommand command);

    TransferDTO utilityTransfer(TransferCommand command);

    List<InquireBillingStatementsRec> inquireBillingStatements(InquireBillingCommand command);

    FintechResponse<?> inquireDemandDepositAccount(String accountNo);

    FintechResponse<?> createAccount();

    FintechResponse<?> getAccountHistory(AccountHistoryCommand command);

    FintechResponse<?> transferWithSimple(SimpleTransferCommand command);
}
