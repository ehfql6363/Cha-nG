package com.ssafy.chaing.contract.service;

import com.ssafy.chaing.contract.service.command.ApproveContractCommand;
import com.ssafy.chaing.contract.service.command.ConfirmContractCommand;
import com.ssafy.chaing.contract.service.command.DraftContractCommand;
import com.ssafy.chaing.contract.service.dto.ContractDTO;
import com.ssafy.chaing.contract.service.dto.ContractDetailDTO;
import com.ssafy.chaing.contract.service.dto.ContractUserDTO;
import java.util.List;

public interface ContractService {
    ContractDetailDTO getContract(Long contractId);

    ContractDetailDTO updateContract(Long contractId, DraftContractCommand command);

    ContractDTO createDraftContract(Long groupId, Long userId);

    ContractDetailDTO confirmContract(Long contractId, ConfirmContractCommand command);

    List<ContractUserDTO> getContractMembers(Long contractId);

    void approveContract(Long contractId, ApproveContractCommand command);

}
