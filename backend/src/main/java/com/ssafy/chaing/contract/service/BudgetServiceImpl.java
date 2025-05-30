package com.ssafy.chaing.contract.service;

import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import com.ssafy.chaing.contract.domain.ContractUserEntity;
import com.ssafy.chaing.contract.repository.ContractUserRepository;
import com.ssafy.chaing.contract.service.dto.CreateLivingBudgetDto;
import com.ssafy.chaing.contract.service.dto.LivingBudgetAccountDTO;
import com.ssafy.chaing.group.repository.GroupUserRepository;
import com.ssafy.chaing.notification.domain.NotificationCategory;
import com.ssafy.chaing.notification.service.NotificationService;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final UserRepository userRepository;
    private final ContractUserRepository contractUserRepository;
    private final GroupUserRepository groupUserRepository;
    private final NotificationService notificationService;

    @Override
    public void notifyLeaderToRegisterLivingAccount(Long userId) {
        ContractUserEntity contractUser = contractUserRepository.findByUser_Id(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.USER_NOT_FOUND));
        // 생활비 계좌 조회
        if (contractUser.getContract().getLiveAccountNo() == null) {
            Long leaderId = groupUserRepository.findGroupOwnerIdByUserId(userId)
                    .orElseThrow(() -> new BadRequestException(ExceptionCode.GROUP_NOT_FOUND));

            notificationService.sendNotification(
                    leaderId,
                    "생활비 계좌 등록 요청",
                    "생활비 계좌가 아직 등록되지 않았습니다. 등록을 진행해주세요.",
                    NotificationCategory.LIVING_BUDGET
            );
        } else {
            throw new BadRequestException(ExceptionCode.LIVING_ACCOUNT_ALREADY_EXIST);
        }
    }

    @Override
    public LivingBudgetAccountDTO getLivingAccount(Long userId) {
        ContractUserEntity contractUser = contractUserRepository.findByUser_Id(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.USER_NOT_FOUND));

        return new LivingBudgetAccountDTO(
                contractUser.getContract().getLiveAccountNo(),
                contractUser.getAccountNo()
        );
    }

    @Override
    @Transactional
    public void saveAccountAndNotify(CreateLivingBudgetDto accountInfo) {
        ContractUserEntity contractUser = contractUserRepository.findByUser_Id(accountInfo.getId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.USER_NOT_FOUND));
        contractUser.getContract().setLiveAccountNo(accountInfo.getAccountNo());
        List<UserEntity> otherUsers = userRepository.findOtherUsersInSameContract(accountInfo.getId());

        for (UserEntity user : otherUsers) {
            notificationService.sendNotification(
                    user.getId(),
                    "생활비 계좌 등록 완료",
                    "공동 생활비 계좌가 등록되었습니다. 확인해주세요.",
                    NotificationCategory.LIVING_BUDGET
            );
        }
    }


    @Override
    public void notifyLivingDeposit(Long userId) {
        List<UserEntity> otherUsers = userRepository.findOtherUsersInSameContract(userId);

        for (UserEntity user : otherUsers) {
            notificationService.sendNotification(
                    user.getId(),
                    "생활비 입금 알림",
                    "생활비가 입금되었습니다.",
                    NotificationCategory.LIVING_BUDGET
            );
        }
    }

    @Override
    public void notifyLivingWithdraw(Long userId) {
        List<UserEntity> otherUsers = userRepository.findOtherUsersInSameContract(userId);

        for (UserEntity user : otherUsers) {
            notificationService.sendNotification(
                    user.getId(),
                    "생활비 출금 알림",
                    "생활비가 출금되었습니다.",
                    NotificationCategory.LIVING_BUDGET
            );
        }
    }
}
