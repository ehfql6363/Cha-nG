package com.ssafy.chaing.home.controller.request;


import com.ssafy.chaing.batch.service.RentBatchService;
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.repository.ContractRepository;
import com.ssafy.chaing.contract.service.ContractService;
import com.ssafy.chaing.contract.service.command.ApproveContractCommand;
import com.ssafy.chaing.contract.service.command.ConfirmContractCommand;
import com.ssafy.chaing.contract.service.dto.ContractDTO;
import com.ssafy.chaing.group.service.GroupService;
import com.ssafy.chaing.group.service.command.CreateGroupCommand;
import com.ssafy.chaing.group.service.command.JoinGroupCommand;
import com.ssafy.chaing.group.service.dto.GroupDTO;
import com.ssafy.chaing.user.domain.RoleType;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestIntegrationService {

    private final UserRepository userRepository;
    private final GroupService groupService;
    private final ContractService contractService;
    private final ContractRepository contractRepository;
    private final RentBatchService rentBatchService;
    private final PasswordEncoder passwordEncoder;

    public ContractDTO createTestPayment(TestPaymentRequest body) {
        log.info("🚀 통합 시나리오용 계약 실행 시작");

        // 유저 생성 및 계약 3개 생성
        List<UserEntity> users1 = createUsers();

        ContractEntity contract1 = setUpContract(body.getDuedate(), users1);

        // Contract1 → 즉시 approve
        // Step 2: 유저 승인 (→ 내부적으로 registerNextMonthPayment 호출)

        contractService.approveContract(contract1.getId(),
                new ApproveContractCommand(users1.get(0).getId(), body.getNo1()));
        contractService.approveContract(contract1.getId(),
                new ApproveContractCommand(users1.get(1).getId(), body.getNo2()));
        contractService.approveContract(contract1.getId(),
                new ApproveContractCommand(users1.get(2).getId(), body.getNo3()));

        return ContractDTO.from(contract1);
    }

    private List<UserEntity> createUsers() {
        List<UserEntity> users = new ArrayList<>();
        String timestamp = ZonedDateTime.now().toInstant().toEpochMilli() + ""; // 유닉스 타임스탬프 문자열로 사용

        for (int i = 0; i < 3; i++) {
            String email = String.format("test%d_%s@test.com", i, timestamp);
            String nickname = String.format("test%d", i);

            UserEntity user = UserEntity.builder()
                    .emailAddress(email)
                    .password(passwordEncoder.encode("password1!"))
                    .name(nickname)
                    .nickname(nickname)
                    .roleType(RoleType.USER)
                    .build();
            userRepository.save(user);
            users.add(user);
        }

        return users;
    }

    private ContractEntity setUpContract(int duedate, List<UserEntity> users) {
        UserEntity creator = users.get(0);
        GroupDTO group = groupService.createGroup(new CreateGroupCommand(
                creator.getId(), "nickname", "profile", "testGroup", 3
        ));

        groupService.joinGroup(new JoinGroupCommand(users.get(1).getId(), group.getId(), "message1", "info1"));
        groupService.joinGroup(new JoinGroupCommand(users.get(2).getId(), group.getId(), "message2", "info2"));

        ContractDTO draftContract = contractService.createDraftContract(group.getId(), creator.getId());

        ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDate koreanDate = nowInKorea.toLocalDate();

        ConfirmContractCommand confirmContractCommand = new ConfirmContractCommand(
                creator.getId(),
                ZonedDateTime.now(),
                ZonedDateTime.now().plusMonths(6),
                new ConfirmContractCommand.ConfirmRentCommand(
                        10000,
                        duedate,
                        "0015632899269172",
                        "0012860463440599",
                        3,
                        List.of(
                                new ConfirmContractCommand.ConfirmRentCommand.ConfirmUserPaymentCommand(
                                        users.get(0).getId(), 3333, 1
                                ),
                                new ConfirmContractCommand.ConfirmRentCommand.ConfirmUserPaymentCommand(
                                        users.get(1).getId(), 3333, 1
                                ),
                                new ConfirmContractCommand.ConfirmRentCommand.ConfirmUserPaymentCommand(
                                        users.get(2).getId(), 3333, 1
                                )
                        )
                ),
                new ConfirmContractCommand.ConfirmUtilityCommand(null)
        );

        contractService.confirmContract(draftContract.getId(), confirmContractCommand);

        return contractRepository.findByIdWithMembers(draftContract.getId())
                .orElseThrow(() -> new BadRequestException("계약이 없습니다."));
    }

}
