package com.ssafy.chaing.contract.service.dto;

import com.ssafy.chaing.contract.domain.ContractUserEntity;
import com.ssafy.chaing.contract.domain.ContractUserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContractUserDTO {
    private Long id;
    private String name;
    private String nickname;
    private String profileImage;
    private boolean approved;
    private ContractUserStatus status;

    static public ContractUserDTO from(ContractUserEntity entity) {
        return new ContractUserDTO(
                entity.getUser().getId(),
                entity.getUser().getName(),
                entity.getUser().getNickname(),
                entity.getUser().getProfileImage(),
                entity.getContractStatus() == ContractUserStatus.CONFIRMED,
                entity.getContractStatus()

        );
    }
}
