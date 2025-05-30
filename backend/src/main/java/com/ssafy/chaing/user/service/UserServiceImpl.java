package com.ssafy.chaing.user.service;

import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import com.ssafy.chaing.common.exception.NotFoundException;
import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.ContractUserEntity;
import com.ssafy.chaing.contract.repository.ContractRepository;
import com.ssafy.chaing.contract.repository.ContractUserRepository;
import com.ssafy.chaing.group.domain.GroupEntity;
import com.ssafy.chaing.group.repository.GroupRepository;
import com.ssafy.chaing.group.repository.GroupUserRepository;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import com.ssafy.chaing.user.service.command.UpdateUserProfileCommand;
import com.ssafy.chaing.user.service.dto.UserDTO;
import com.ssafy.chaing.user.service.dto.UserDetailInfoDTO;
import com.ssafy.chaing.user.service.dto.UserProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ContractRepository contractRepository;
    private final ContractUserRepository contractUserRepository;
    private final GroupUserRepository groupUserRepository;

    @Override
    public UserDTO getMe(Long userId) {
        return userRepository.findById(userId).map(UserDTO::fromEntity).orElseThrow(
                () -> new NotFoundException(ExceptionCode.USER_NOT_FOUND)
        );
    }

    @Override
    public UserProfileDTO getMyProfile(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_NOT_FOUND));

        String ownerAccountNo = null;
        String myAccountNo = null;

        if (user.getGroupId() == null) {
            return UserProfileDTO.from(user, myAccountNo, ownerAccountNo);
        }

        GroupEntity group = groupRepository.findById(user.getGroupId())
                .orElse(null);

        if (group == null) {
            return UserProfileDTO.from(user, myAccountNo, ownerAccountNo);
        }

        Long contractId = group.getContractId();
        if (contractId == null) {
            return UserProfileDTO.from(user, myAccountNo, ownerAccountNo);
        }

        ContractEntity contract = contractRepository.findById(contractId).orElse(null);
        if (contract == null) {
            return UserProfileDTO.from(user, myAccountNo, ownerAccountNo);
        }

        ContractUserEntity contractUser = contractUserRepository
                .findByContractIdAndUserId(contract.getId(), userId)
                .orElse(null);

        ownerAccountNo = contract.getOwnerAccountNo();

        if (contractUser == null) {
            return UserProfileDTO.from(user, myAccountNo, ownerAccountNo);
        }

        myAccountNo = contractUser.getAccountNo();

        return UserProfileDTO.from(user, myAccountNo, ownerAccountNo);
    }


    @Override
    @Transactional
    public UserProfileDTO updateMyProfile(UpdateUserProfileCommand command) {

        UserEntity user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_NOT_FOUND));

        Long groupId = user.getGroupId();

        // 그룹이 있다면 닉네임 중복 검사
        if (groupId != null) {
            boolean nicknameExists = groupUserRepository.existsByGroupIdAndUserNickname(groupId, command.getNickname());

            // 자기 자신의 닉네임은 허용 (다른 사람과 겹칠 때만 에러)
            if (nicknameExists && !command.getNickname().equals(user.getNickname())) {
                throw new BadRequestException(ExceptionCode.DUPLICATE_NICKNAME);
            }
        }

        user.setNickname(command.getNickname());
        user.setProfileImage(command.getProfileImage());
        userRepository.save(user);

        return UserProfileDTO.from(user);

    }

    @Override
    public UserDetailInfoDTO getUserInfo(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_NOT_FOUND));

        Long contractId = null;
        if (user.getGroupId() != null) {
            contractId = groupRepository.findById(user.getGroupId())
                    .map(GroupEntity::getContractId)
                    .orElse(null);
        }

        UserDetailInfoDTO dto = new UserDetailInfoDTO(user.getId(), user.getName(), user.getNickname(),
                user.getProfileImage(), user.getGroupId(), contractId);

        return dto;
    }
}
