import { useCallback, useEffect } from 'react'
import { useDispatch } from 'react-redux'

import { getContract, getContractMembers, getGroup } from '@/apis/group'
import { getUnreadNotificationCount } from '@/apis/notification'
import { getHomeOverview } from '@/apis/user'
import { setContract, setContractMembers } from '@/store/slices/contractSlice'
import { setGroup } from '@/store/slices/groupSlice'
import { setHomeOverview } from '@/store/slices/userSlice'
import { ContractStatus } from '@/types/contract'

export const useHomeData = (
  userId: number,
  groupId: number,
  contractId: number,
) => {
  const dispatch = useDispatch()

  const fetchContract = useCallback(async () => {
    if (contractId) {
      const response = await getContract(contractId)
      if (response.success) {
        dispatch(setContract(response.data))
      }
    }
  }, [contractId, dispatch])

  const fetchHomeOverView = useCallback(async () => {
    if (!groupId) return
    const response = await getHomeOverview()
    if (response.success) {
      dispatch(setHomeOverview(response.data))
    }
  }, [dispatch, groupId])

  const fetchGroup = useCallback(async () => {
    if (groupId == null || groupId === 0) return
    const response = await getGroup(groupId)
    if (response.success) {
      dispatch(setGroup(response.data))
    }
  }, [groupId, dispatch])

  const fetchContractMembers = useCallback(async () => {
    if (!contractId) return
    const response = await getContractMembers(contractId)
    if (response.success) {
      dispatch(setContractMembers(response.data))
    }
  }, [contractId, dispatch])

  const fetchUnreadNotificationCount = useCallback(async () => {
    const response = await getUnreadNotificationCount(userId)
    if (response.success) {
      return response.data.count > 0
    }
    return false
  }, [userId])

  useEffect(() => {
    fetchContract()
    fetchHomeOverView()
    fetchGroup()
  }, [fetchContract, fetchHomeOverView, fetchGroup])

  return {
    fetchContractMembers,
    fetchUnreadNotificationCount,
  }
}
