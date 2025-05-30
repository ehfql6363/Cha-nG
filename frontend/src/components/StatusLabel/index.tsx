import { useTranslation } from 'react-i18next'

import { ContractStatus, MemberContractStatus } from '@/types/contract'

import { Container, StatusLabelContainer, SwitchContainer } from './styles'
 interface StatusLabelProps {
  contractStatus?: ContractStatus
}

export function StatusLabel({ contractStatus }: StatusLabelProps) {
  const { t } = useTranslation()

  // MemberContractStatus에 해당하는 값인지 확인
  const isMemberStatus = (
    status: ContractStatus | undefined,
  ): status is MemberContractStatus => {
    if (!status) return false

    return (
      status === ContractStatus.isContractApproved ||
      status === ContractStatus.pending ||
      status === ContractStatus.reviewRequired ||
      status === ContractStatus.confirmed
    )
  }

  // MemberContractStatus에 해당하는 값만 처리하고 나머지는 빈 값으로 처리
  const displayStatus = isMemberStatus(contractStatus)
    ? contractStatus
    : ContractStatus.none

  return (
    isMemberStatus(contractStatus) && (
      <Container>
        <SwitchContainer>
          <StatusLabelContainer variant={displayStatus}>
            {displayStatus
              ? t(`contract.detail.${displayStatus.toLowerCase()}.status`)
              : ''}
          </StatusLabelContainer>
        </SwitchContainer>
      </Container>
    )
  )
}
