'use client'

import { useEffect, useMemo, useRef, useState } from 'react'
import { useDispatch } from 'react-redux'

import { format } from 'date-fns'
import { useRouter } from 'next/navigation'

import { getDuties } from '@/apis/duty'
import { getLivingAccount } from '@/apis/livingBudget'
import { getPaymentCurrentStatus } from '@/apis/payment'
import { Modal } from '@/components'
import { useAppSelector } from '@/hooks'
import { setDutyWeekList } from '@/store/slices/dutySlice'
import {
  setLivingAccountNo,
  setMyAccountNo,
} from '@/store/slices/livingBudgetSlice'
import { setPaymentCurrent } from '@/store/slices/pledgeSlice'
import {
  setIsNoticeModalOpen,
  setNoContractWhenLogIn,
} from '@/store/slices/uiSlice'
import { DayKey, Duty } from '@/types/duty'

import { DashBoard, LifeBudgetPreview, Notice } from '..'
import { Container, ContentsContainer } from './styles'

export function ConfirmedHomeContents() {
  const router = useRouter()
  const dispatch = useDispatch()
  const user = useAppSelector((state) => state.user.user)
  const livingBudget = useAppSelector((state) => state.livingBudget)
  const dutyWeekList = useAppSelector((state) => state.duty.dutyWeekList)
  const today = format(new Date(), 'EEEE').toLowerCase()
  const isNoticeModalOpen = useAppSelector(
    (state) => state.ui.isNoticeModalOpen,
  )
  const hasFetchedAccount = useRef(false)
  const hasFetchedDuties = useRef(false)
  const hasFetchedPaymentCurrent = useRef(false)
  const currentMonth = useMemo(() => format(new Date(), 'yyyyMM'), [])

  useEffect(() => {
    const fetchPaymentCurrent = async () => {
      if (hasFetchedPaymentCurrent.current) return
      hasFetchedPaymentCurrent.current = true
      if (!user.contractId) return

      const response = await getPaymentCurrentStatus(currentMonth)
      if (response.success) {
        dispatch(setPaymentCurrent(response.data))
      }
    }
    fetchPaymentCurrent()
  }, [user.contractId, currentMonth])

  useEffect(() => {
    const fetchAccount = async () => {
      if (hasFetchedAccount.current) return
      hasFetchedAccount.current = true
      if (!user.contractId) return
      const response = await getLivingAccount()
      if (response.success) {
        if (response.data.myAccountNo) {
          dispatch(setMyAccountNo(response.data.myAccountNo))
        }
        if (response.data.liveAccountNo) {
          dispatch(setLivingAccountNo(response.data.liveAccountNo))
        }
      }
    }
    if (!livingBudget.livingAccountNo || !livingBudget.myAccountNo) {
      fetchAccount()
    }
  }, [user.contractId, livingBudget.livingAccountNo, livingBudget.myAccountNo])

  useEffect(() => {
    const fetchDuties = async () => {
      if (hasFetchedDuties.current) return
      hasFetchedDuties.current = true
      if (!user.groupId) return
      const response = await getDuties(user.groupId) // dutyList
      if (response.success) {
        dispatch(setDutyWeekList(response.data))
      }
    }
    fetchDuties()
  }, [user.groupId])

  const todayMyDutyList = useMemo(
    () =>
      dutyWeekList[today as DayKey] &&
      dutyWeekList[today as DayKey]?.filter((duty: Duty) =>
        duty.assignees.includes(user.id),
      ),
    [dutyWeekList],
  )
  const noContractWhenLogIn = useAppSelector(
    (state) => state.ui.noContractWhenLogIn,
  )
  const [openConfirmModal, setOpenConfirmModal] = useState(false)

  useEffect(() => {
    if (noContractWhenLogIn) {
      setOpenConfirmModal(true)
      dispatch(setNoContractWhenLogIn(false))
    }
  }, [noContractWhenLogIn])

  return (
    <Container>
      <Modal
        title={'공지'}
        description={''}
        open={isNoticeModalOpen}
        onOpenChange={() => dispatch(setIsNoticeModalOpen(false))}
        onConfirm={() => dispatch(setIsNoticeModalOpen(false))}
        disablePrev={true}>
        <Notice />
      </Modal>
      <ContentsContainer>
        <DashBoard todayMyDutyList={todayMyDutyList} />
        {livingBudget.livingAccountNo && <LifeBudgetPreview />}
      </ContentsContainer>
      <Modal
        open={openConfirmModal}
        onOpenChange={setOpenConfirmModal}
        onConfirm={() => {
          setOpenConfirmModal(false)
          router.replace('/pledge')
        }}
        title={'서약서 완성을 축하드려요'}
        description={'서약서를 확인해보세요'}
        image={'/images/etc/congratulations.svg'}
      />
    </Container>
  )
}
