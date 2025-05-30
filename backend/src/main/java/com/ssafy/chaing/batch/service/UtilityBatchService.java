package com.ssafy.chaing.batch.service;

import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.ContractStatus;
import com.ssafy.chaing.contract.domain.ContractUserEntity;
import com.ssafy.chaing.contract.domain.UtilityCardEntity;
import com.ssafy.chaing.contract.repository.ContractRepository;
import com.ssafy.chaing.fintech.controller.request.InquireBillingCommand;
import com.ssafy.chaing.fintech.controller.request.TransferCommand;
import com.ssafy.chaing.fintech.dto.InquireBillingStatementsRec;
import com.ssafy.chaing.fintech.service.FintechService;
import com.ssafy.chaing.fintech.service.dto.CurrentBillingStatementDTO;
import com.ssafy.chaing.fintech.service.dto.TransferDTO;
import com.ssafy.chaing.payment.domain.FeeType;
import com.ssafy.chaing.payment.domain.PaymentEntity;
import com.ssafy.chaing.payment.domain.PaymentStatus;
import com.ssafy.chaing.payment.domain.UserPaymentEntity;
import com.ssafy.chaing.payment.repository.PaymentRepository;
import com.ssafy.chaing.payment.repository.UserPaymentRepository;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilityBatchService {

    private final FintechService fintechService;
    private final PaymentRepository paymentRepository;
    private final UserPaymentRepository userPaymentRepository;
    private final ContractRepository contractRepository;

    private final RentBatchService rentBatchService;

    @Transactional
    public void saveCurrentWeekBillingStatement() {
        List<ContractEntity> contracts = contractRepository.findAllWithDetails(ContractStatus.CONFIRMED);

        if (contracts.isEmpty()) {
            log.info("처리할 계약이 없습니다.");
            return;
        }

        List<PaymentEntity> paymentsToSave = new ArrayList<>();
        List<UserPaymentEntity> userPaymentsToSave = new ArrayList<>();

        for (ContractEntity contract : contracts) {
            processContract(contract, paymentsToSave, userPaymentsToSave);
        }

        savePayments(paymentsToSave, userPaymentsToSave);
    }

    @Transactional
    public void collectToUtilityAccount() {
        List<PaymentEntity> payments = paymentRepository.findAllPaymentsForConfirmedContracts(ContractStatus.CONFIRMED);

        for (PaymentEntity payment : payments) {
            if(payment.getFeeType() == FeeType.RENT) continue;
            collectUtility(payment);
        }

    }

    private void processContract(ContractEntity contract,
                                 List<PaymentEntity> paymentsToSave,
                                 List<UserPaymentEntity> userPaymentsToSave
    ) {
        Long contractId = contract.getId();
        UtilityCardEntity card = contract.getUtilityCard();

        if (card == null) {
            log.warn("계약 ID {}에 연결된 UtilityCard 정보가 없습니다. 건너뜁니다.", contractId);
            return;
        }

        List<InquireBillingStatementsRec> statements = fetchBillingStatements(contractId, card);
        if (statements == null || statements.isEmpty()) {
            log.warn("계약 ID {} (카드번호: {})에 대한 청구 내역이 없습니다.", contractId, card.getCardNo());
            return;
        }

        if (Objects.equals(statements.getLast().billingList().getLast().status(), "결제완료")) {
            log.info("저번주에 결제한 내역이 존재하지 않습니다.");
            return;
        }

        CurrentBillingStatementDTO dto = new CurrentBillingStatementDTO(statements.getLast());
        List<ContractUserEntity> contractMembers = contract.getMembers();

        if (contractMembers == null || contractMembers.isEmpty()) {
            log.warn("계약 ID {} 에 멤버가 없어 Payment 생성을 건너뜁니다. (총 청구액: {})", contractId, dto.getTotalBalance());
            return;
        }

        createPaymentsForContract(contract, dto, contractMembers, paymentsToSave, userPaymentsToSave);
    }

    private List<InquireBillingStatementsRec> fetchBillingStatements(Long contractId, UtilityCardEntity card) {
        try {
            InquireBillingCommand command = new InquireBillingCommand(card.getCardNo(), card.getCvc());
            return fintechService.inquireBillingStatements(command);
        } catch (Exception e) {
            log.error("계약 ID {} (카드번호: {})의 청구 내역 조회 중 오류 발생: {}", contractId, card.getCardNo(), e.getMessage(), e);
            return null;
        }
    }

    private void createPaymentsForContract(ContractEntity contract, CurrentBillingStatementDTO dto,
                                           List<ContractUserEntity> contractMembers,
                                           List<PaymentEntity> paymentsToSave,
                                           List<UserPaymentEntity> userPaymentsToSave) {

        PaymentEntity payment = PaymentEntity.builder()
                .totalAmount(dto.getTotalBalance())
                .month(dto.getBillingMonth())
                .week(dto.getBillingWeek())
                .allPaid(false)
                .contract(contract)
                .feeType(FeeType.UTILITY)
                .status(PaymentStatus.STARTED)
                .paidAmount(0)
                .build();
        paymentsToSave.add(payment);

        distributePaymentsAmongMembers(contract, dto, contractMembers, payment, userPaymentsToSave);
    }

    private void distributePaymentsAmongMembers(ContractEntity contract, CurrentBillingStatementDTO dto,
                                                List<ContractUserEntity> contractMembers, PaymentEntity payment,
                                                List<UserPaymentEntity> userPaymentsToSave) {

        int utilityRatio = contract.getUtilityRatio();
        int baseAmount = dto.getTotalBalance() / utilityRatio;
        int remainAmount = dto.getTotalBalance() % utilityRatio;

        boolean remainderAssigned = false;
        for (ContractUserEntity member : contractMembers) {
            int userAmount = baseAmount;
            if (!remainderAssigned && member.isSurplusUser()) {
                userAmount = remainAmount;
                remainderAssigned = true;
            }

            UserPaymentEntity userPayment = UserPaymentEntity.builder()
                    .payment(payment)
                    .contractMember(member)
                    .status(PaymentStatus.STARTED)
                    .amount(userAmount)
                    .build();
            userPaymentsToSave.add(userPayment);
        }
    }

    private void savePayments(List<PaymentEntity> paymentsToSave, List<UserPaymentEntity> userPaymentsToSave) {
        if (!paymentsToSave.isEmpty()) {
            paymentRepository.saveAll(paymentsToSave);
            log.info("{}개의 Payment 정보 저장 완료.", paymentsToSave.size());

            if (!userPaymentsToSave.isEmpty()) {
                userPaymentRepository.saveAll(userPaymentsToSave);
                log.info("{}개의 UserPayment 정보 저장 완료.", userPaymentsToSave.size());
            } else {
                log.warn("Payment는 저장되었으나 UserPayment 저장 목록이 비어있습니다. 로직 확인 필요.");
            }
        } else {
            log.info("저장할 신규 Payment 정보가 없습니다.");
        }

        log.info("이번 주 공과금 청구 내역 저장 배치 작업 완료.");
    }

    private void collectUtility(PaymentEntity payment) {
        if (payment.getStatus() == PaymentStatus.PAID || payment.getStatus() == PaymentStatus.FAILED) {
            return;
        }

        log.info("💰 공동 계좌로 카드 대금 송금 시작 → Payment ID = {}", payment.getId());

        List<UserPaymentEntity> userPayments = userPaymentRepository.findWithMemberAndUserByPaymentId(payment.getId());
        Map<Long, UserPaymentEntity> userPaymentMap = userPayments.stream()
                .collect(Collectors.toMap(
                        up -> up.getContractMember().getId(),
                        up -> up
                ));

        for (ContractUserEntity member : payment.getContract().getMembers()) {
            UserPaymentEntity userPayment = userPaymentMap.get(member.getId());

            if (userPayment == null) {
                log.error("🚨 해당 멤버의 UserPayment가 없음 → memberId={}, paymentId={}", member.getId(), payment.getId());
                continue;
            }

            // 이미 처리된 경우 건너뛰기 (재시도 방지용)
            if (userPayment.getStatus() == PaymentStatus.PAID || userPayment.getStatus() == PaymentStatus.COLLECTED) {
                continue;
            }

            ContractEntity contract = payment.getContract();
            TransferDTO result = fintechService.utilityTransfer(
                    new TransferCommand(
                            userPayment.getId(),
                            contract.getId(),
                            (long) payment.getMonth(),
                            member.getUser().getName() + "의 계좌: " + member.getAccountNo().substring(0, 4),
                            member.getAccountNo(),
                            payment.getContract().getGroup().getName() + "의 공동 계좌: " + payment.getContract().getRentAccountNo().substring(0, 4),
                            payment.getContract().getRentAccountNo(),
                            member.getRentAmount(),
                            false,
                            ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString(),
                            payment.getFeeType(),
                            null,
                            member.getUser().getId()
                    )
            );

            if (result.isSuccess()) {
                userPayment.updateStatus(PaymentStatus.COLLECTED);
                payment.addPaidAmount(userPayment.getAmount());
            } else {
                userPayment.updateStatus(PaymentStatus.FAILED);
            }

            userPaymentRepository.save(userPayment);
        }

        payment.refreshStatusFromUserPayments(userPayments);
        paymentRepository.save(payment);
    }
}