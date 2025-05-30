package com.ssafy.chaing.batch.service;

import com.ssafy.chaing.batch.config.ExecutionTime;
import com.ssafy.chaing.batch.config.PaymentCreatedEvent;
import com.ssafy.chaing.batch.config.PaymentEventPublisher;
import com.ssafy.chaing.blockchain.handler.rent.RentHandler;
import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.ContractUserEntity;
import com.ssafy.chaing.fintech.controller.request.TransferCommand;
import com.ssafy.chaing.fintech.service.FintechService;
import com.ssafy.chaing.fintech.service.dto.TransferDTO;
import com.ssafy.chaing.group.domain.GroupEntity;
import com.ssafy.chaing.group.repository.GroupUserRepository;
import com.ssafy.chaing.notification.service.NotificationService;
import com.ssafy.chaing.payment.domain.PaymentEntity;
import com.ssafy.chaing.payment.domain.PaymentStatus;
import com.ssafy.chaing.payment.domain.UserPaymentEntity;
import com.ssafy.chaing.payment.repository.PaymentRepository;
import com.ssafy.chaing.payment.repository.UserPaymentRepository;
import com.ssafy.chaing.payment.service.PaymentService;
import com.ssafy.chaing.user.repository.UserRepository;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentBatchService {

    private final FintechService fintechService;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final UserPaymentRepository userPaymentRepository;
    private final TaskScheduler taskScheduler;
    private final NotificationService notificationService;
    private final GroupUserRepository groupUserRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final RentHandler rentHandler;
    private final UserRepository userRepository;

    @Setter
    private ExecutionTime collectTime = new ExecutionTime(18, 0, -1);
    @Setter
    private ExecutionTime payTime = new ExecutionTime(18, 0, 0);
    @Setter
    private ExecutionTime retryTime = new ExecutionTime(0, 0, 1);

    public void registerNextMonthPayment(ContractEntity contract) {
        log.info("📅 Payment 생성 시작 → Contract ID = {}", contract.getId());

        ZonedDateTime collectExecution = collectTime.calculate(contract.getDueDate());
        ZonedDateTime ownerExecution = payTime.calculate(contract.getDueDate());

        PaymentEntity payment = paymentService.createPayment(contract, ownerExecution);

        // ✅ 여기서 이벤트만 발행
        paymentEventPublisher.publish(new PaymentCreatedEvent(
                payment.getId(),
                collectExecution,
                ownerExecution
        ));

        log.info("💡 Payment 등록 완료 → Payment ID = {}, Next Execution = {}", payment.getId(), ownerExecution);
    }

    @Transactional
    public void payToOwner(Long paymentId) {
        PaymentEntity payment = paymentRepository.findWithContractAndMembersById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Payment"));
        if (payment.getStatus() == PaymentStatus.PAID || payment.getStatus() == PaymentStatus.FAILED) {
            return;
        }

        payment.setLastAttemptDate(ZonedDateTime.now(ZoneOffset.UTC));
        log.info("💰 *당일 작업!* 공동 계좌로 모으기와 집주인 계좌 송금 둘 다 → Payment ID = {}", payment.getId());

        if (payment.getStatus() == PaymentStatus.PARTIALLY_PAID || payment.getStatus() == PaymentStatus.STARTED) {
            log.warn("⚠️ 공동 계좌 모으기 실패 상태. 재시도 수행 → Payment ID = {}", payment.getId());
            collectToJointAccount(payment);

            // 💡 여기서 상태 재확인 필요!
            if (payment.getStatus() != PaymentStatus.COLLECTED) {
                log.warn("⚠️ 여전히 COLLECTED가 아님 → Retry 등록");
                registerRetryPayment(payment);
                return;
            }
        }

        log.info("💰 집주인에게 송금 시작 → Payment ID = {}", payment.getId());

        ContractEntity contract = payment.getContract();
        GroupEntity group = contract.getGroup();
        TransferDTO result = fintechService.rentTransfer(
                new TransferCommand(
                        paymentId,
                        payment.getContract().getId(),
                        (long) payment.getMonth(),
                        group.getName() + "의 대표 계좌: " + contract.getRentAccountNo().substring(0, 4),
                        contract.getRentAccountNo(),
                        group.getName() + "의 집주인 계좌: " + contract.getOwnerAccountNo().substring(0, 4),
                        contract.getOwnerAccountNo(),
                        payment.getTotalAmount(),
                        false,
                        ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString(),
                        payment.getFeeType(),
                        group.getId(),
                        null
                )
        );

        if (result.isSuccess()) {
            payment.updateStatus(PaymentStatus.PAID);
            log.info("✅ 집주인 송금 성공 → Payment ID = {}", payment.getId());
            registerNextMonthPayment(contract);
        } else {
            payment.updateStatus(PaymentStatus.RETRY_PENDING);
            registerRetryPayment(payment);
        }

        paymentRepository.save(payment);
    }

    @Transactional
    public void collectToJointAccount(Long paymentId) {
        PaymentEntity payment = paymentRepository.findWithContractAndMembersById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Payment"));
        collectToJointAccountInternal(payment);
    }

    @Transactional
    public void collectToJointAccount(PaymentEntity payment) {
        collectToJointAccountInternal(payment);
    }

    private void collectToJointAccountInternal(PaymentEntity payment) {
        if (payment.getStatus() == PaymentStatus.PAID || payment.getStatus() == PaymentStatus.FAILED) {
            return;
        }

        log.info("💰 *전날 작업!* 공동 계좌로 송금 시작 → Payment ID = {}", payment.getId());

        boolean allSuccess = true;

        Map<Long, UserPaymentEntity> userPaymentMap = userPaymentRepository
                .findWithMemberAndUserByPaymentId(payment.getId())
                .stream()
                .collect(Collectors.toMap(
                        up -> up.getContractMember().getId(),
                        up -> up
                ));

        for (ContractUserEntity member : payment.getContract().getMembers()) {
            UserPaymentEntity userPayment = userPaymentMap.get(member.getId());

            if (userPayment == null) {
                log.error("🚨 해당 멤버의 UserPayment가 없음 → memberId={}, paymentId={}", member.getId(), payment.getId());
                allSuccess = false;
                continue;
            }

            if (userPayment.getAmount() == 0) {
                userPayment.updateStatus(PaymentStatus.COLLECTED);
                continue;
            }

            // 이미 처리된 경우 건너뛰기 (재시도 방지용)
            if (userPayment.getStatus() == PaymentStatus.PAID || userPayment.getStatus() == PaymentStatus.COLLECTED) {
                continue;
            }

            ContractEntity contract = payment.getContract();
            TransferDTO result = fintechService.rentTransfer(
                    new TransferCommand(
                            member.getUser().getId(),
                            contract.getId(),
                            (long) payment.getMonth(),
                            member.getUser().getName() + "의 계좌: " + member.getAccountNo().substring(0, 4),
                            member.getAccountNo(),
                            payment.getContract().getGroup().getName() + "의 공동 계좌: " + payment.getContract()
                                    .getRentAccountNo().substring(0, 4),
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
                payment.addPaidAmount(member.getRentAmount());
            } else {
                userPayment.updateStatus(PaymentStatus.FAILED);
                allSuccess = false;
            }

            userPaymentRepository.save(userPayment);
        }

        if (allSuccess) {
            payment.updateStatus(PaymentStatus.COLLECTED);
            log.info("✅ 공동 계좌로 송금 성공 → Payment ID = {}", payment.getId());
        } else {
            payment.updateStatus(PaymentStatus.PARTIALLY_PAID);
            log.warn("❌ 일부 송금 실패 → Payment ID = {}", payment.getId());
        }

        paymentRepository.save(payment);
    }

    private void registerRetryPayment(PaymentEntity payment) {

        if (payment.getRetryCount() >= 5) {
            payment.updateStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            log.warn("❌ Payment ID {} → 최대 재시도 횟수 도달", payment.getId());
            registerNextMonthPayment(payment.getContract());
            return;
        }

        log.info("🔢 retryCount 증가 전 = {}, Payment ID = {}", payment.getRetryCount(), payment.getId());
        payment.increaseRetryCount();
        log.info("🔼 retryCount 증가 후 = {}", payment.getRetryCount());

        ZonedDateTime retryExecution = retryTime.calculateFromNow();

        paymentRepository.save(payment);

        taskScheduler.schedule(() -> payToOwner(payment.getId()),
                retryExecution.toInstant());

        log.info("🔁 {}일 {}시간 뒤에 후 재시도 등록 → Payment ID = {}, Retry Count = {}",
                retryTime.getDayOffset(), retryTime.getHour(), payment.getId(), payment.getRetryCount());

    }

}
