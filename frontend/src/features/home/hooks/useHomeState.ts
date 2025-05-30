import { useCallback, useEffect, useState } from 'react'

import { useAppSelector } from '@/hooks'
import { ContractStatus } from '@/types/contract'

export const useHomeState = () => {
  const [isMounted, setIsMounted] = useState(false)
  const [hasUnreadNotification, setHasUnreadNotification] = useState(false)
  const [status, setStatus] = useState<ContractStatus>(ContractStatus.none)

  const user = useAppSelector((state) => state.user.user)
  const contract = useAppSelector((state) => state.contract.contract)
  const group = useAppSelector((state) => state.group.group)
  const contractMembers = useAppSelector(
    (state) => state.contract.contractMembers,
  )

  const shouldInvite = useCallback(() => {
    return group ? group?.members?.length < group?.maxParticipants : false
  }, [group])

  const confirmedCount = useCallback(() => {
    return contractMembers?.filter(
      (member) => member.status === ContractStatus.confirmed,
    ).length
  }, [contractMembers])

  useEffect(() => {
    setIsMounted(true)
  }, [])

  useEffect(() => {
    if (!user.id) return
    if (shouldInvite()) {
      setStatus(ContractStatus.shouldInvite)
    } else if (!user.contractId) {
      setStatus(ContractStatus.none)
    } else if (contract.status === ContractStatus.pending) {
      const isApproved =
        contractMembers.find((item) => item.id === user.id)?.status ===
        ContractStatus.confirmed
      setStatus(
        isApproved ? ContractStatus.isContractApproved : ContractStatus.pending,
      )
    } else {
      setStatus(contract.status)
    }
  }, [user, shouldInvite, contract.status, contractMembers])

  return {
    isMounted,
    hasUnreadNotification,
    setHasUnreadNotification,
    status,
    user,
    contract,
    group,
    contractMembers,
    confirmedCount,
  }
}
