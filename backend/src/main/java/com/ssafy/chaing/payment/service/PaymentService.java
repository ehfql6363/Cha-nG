package com.ssafy.chaing.payment.service;

import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.payment.controller.response.AccountInfoResponse;
import com.ssafy.chaing.payment.controller.response.PaymentStatusInfoResponse;
import com.ssafy.chaing.payment.domain.PaymentEntity;
import com.ssafy.chaing.payment.service.command.RetrieveRentCommand;
import com.ssafy.chaing.payment.service.command.RetrieveUtilityCommand;
import com.ssafy.chaing.payment.service.command.TransferRentCommand;
import com.ssafy.chaing.payment.service.dto.PaymentOverviewDTO;
import com.ssafy.chaing.payment.service.dto.RetrieveRentDTO;
import com.ssafy.chaing.payment.service.dto.RetrieveUtilityDTO;
import java.time.ZonedDateTime;

public interface PaymentService {

    RetrieveRentDTO retrieveRent(RetrieveRentCommand command);

    AccountInfoResponse getRentAccountNo(Long userId);

    void transferToOwner(TransferRentCommand transferInfo);

    void depositToRentAccount(TransferRentCommand transferCommand);

    RetrieveUtilityDTO retrieveUtility(RetrieveUtilityCommand command);

    PaymentEntity createPayment(ContractEntity contract, ZonedDateTime ownerExecution);

    PaymentOverviewDTO getPaymentOverview(Long userId);

    PaymentStatusInfoResponse getCurrentPaymentStatus(Long id, int month);
}
