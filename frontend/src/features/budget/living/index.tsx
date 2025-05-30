'use client'

import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { getAccountDetail } from '@/apis/fintech'
import { notifyLeaderLivingAccountCreated } from '@/apis/livingBudget'
import { BudgetCalendar, FullNavLayout, IconButton, Modal } from '@/components'
import { FloatingSwitchMenu } from '@/components'
import { useFintechTime, useIsLeader } from '@/hooks'
import { useGetAccountHistory } from '@/hooks'
import { useAppSelector } from '@/hooks/useAppSelector'
import {
  setLivingAccountDetail,
  setLivingAccountPaymentHistory,
} from '@/store/slices/livingBudgetSlice'
import { setSelectedMenu } from '@/store/slices/livingBudgetSlice'
import { FormattedAccountPaymentHistory } from '@/types/fintech'
import { LivingMenu } from '@/types/ui'
import {
  formatMoney,
  formatTransactionDate,
  formatTransactionTime,
} from '@/utils/format'

import { History } from './component'
import {
  Account,
  AccountInfo,
  AccountTitle,
  CalendarContainer,
  EmptyContainer,
} from './styles'

export function BudgetLivingPage() {
  const { t } = useTranslation()
  const dispatch = useDispatch()
  const router = useRouter()
  const [budgetStartDate, setBudgetStartDate] = useState(new Date())
  const [budgetEndDate, setBudgetEndDate] = useState(
    new Date(new Date().getFullYear(), new Date().getMonth() + 1, 1),
  )
  const isLeader = useIsLeader()
  const [open, setOpen] = useState(false)
  const { startDate, endDate } = useFintechTime(
    new Date(),
    budgetStartDate,
    budgetEndDate,
  )
  const selectedMenu = useAppSelector(
    (state) => state.livingBudget.selectedMenu,
  )
  const livingAccountNo = useAppSelector(
    (state) => state.livingBudget.livingAccountNo,
  )
  const getAccountHistory = useGetAccountHistory({
    accountNo: livingAccountNo,
    budgetStartDate: budgetStartDate,
    budgetEndDate: budgetEndDate,
  })

  const livingAccountDetail = useAppSelector(
    (state) => state.livingBudget.livingAccountDetail,
  )
  const livingAccountPaymentHistory = useAppSelector(
    (state) => state.livingBudget.livingAccountPaymentHistory,
  )

  const handleBudgetChange = useCallback((date: Date) => {
    setBudgetStartDate(date)
    setBudgetEndDate(new Date(date.getFullYear(), date.getMonth() + 1, 1))
  }, [])

  const [sendNotification, setSendNotification] = useState(false)

  const handleSendNotification = useCallback(async () => {
    if (!sendNotification) {
      await notifyLeaderLivingAccountCreated()
      setSendNotification(true)
    } else {
      setOpen(false)
    }
  }, [sendNotification])

  const hasFetchedDetail = useRef(false)
  useEffect(() => {
    if (!livingAccountNo) {
      if (!isLeader) {
        setOpen(true)
      } else {
        router.push('/budget/living/create')
      }
    } else {
      if (!hasFetchedDetail.current) {
        hasFetchedDetail.current = true
        fetchAccountDetail()
      }
    }
  }, [livingAccountNo])

  const hasFetched = useRef(false)
  useEffect(() => {
    if (livingAccountNo && !hasFetched.current) {
      hasFetched.current = true
      fetchAccountPaymentHistory()
    }
  }, [livingAccountNo, startDate])

  const formattedHistory = useMemo<FormattedAccountPaymentHistory[]>(() => {
    if (livingAccountPaymentHistory.length === 0) return []

    let transactionDate = ''
    return livingAccountPaymentHistory.map((item) => {
      let showDate = false
      if (item.transactionDate != transactionDate) {
        showDate = true
        transactionDate = item.transactionDate
      }
      const isWithdrawal = item.transactionType === '1' ? '+' : '-'
      return {
        transactionUniqueNo: item.transactionUniqueNo,
        showDate: showDate,
        date: formatTransactionDate(item.transactionDate),
        time: formatTransactionTime(item.transactionTime),
        title: isWithdrawal + formatMoney(Number(item.transactionBalance)),
        transactionType: item.transactionType,
        transactionAfterBalance: formatMoney(item.transactionAfterBalance),
        transactionSummary: item.transactionSummary,
        transactionMemo: item.transactionMemo,
      }
    })
  }, [livingAccountPaymentHistory])

  const currentMonthDeposit = useMemo(() => {
    return livingAccountPaymentHistory
      .filter((item) => item.transactionDate >= startDate)
      .filter((item) => item.transactionDate < endDate)
      .filter((item) => item.transactionType === '1')
      .reduce((acc, item) => acc + Number(item.transactionBalance), 0)
  }, [livingAccountPaymentHistory, startDate, endDate])

  const currentMonthWithdrawal = useMemo(() => {
    return livingAccountPaymentHistory
      .filter((item) => item.transactionType === '2')
      .reduce((acc, item) => acc + Number(item.transactionBalance), 0)
  }, [livingAccountPaymentHistory])

  const fetchAccountPaymentHistory = useCallback(async () => {
    const response = await getAccountHistory()
    dispatch(setLivingAccountPaymentHistory(response))
    hasFetched.current = false
  }, [getAccountHistory, dispatch])

  const fetchAccountDetail = useCallback(async () => {
    const response = await getAccountDetail(livingAccountNo)
    if (response.success) {
      dispatch(setLivingAccountDetail(response.data.data))
    }
  }, [livingAccountNo, dispatch])

  const menuList = [
    { id: 'calendar', name: '달력' },
    { id: 'history', name: '내역' },
  ]
  return (
    <FullNavLayout title={'생활비'}>
      <Account>
        <AccountTitle>
          <IconButton
            src="/images/pledge/rentMoney.svg"
            alt="생활비 계좌"
          />
          생활비 계좌
        </AccountTitle>
        <AccountInfo>
          <span>{t('fintech.bankName') + ' ' + livingAccountNo}</span>
          <div>{formatMoney(livingAccountDetail.accountBalance)}</div>
        </AccountInfo>
      </Account>

      {selectedMenu === LivingMenu.calendar && (
        <CalendarContainer>
          <BudgetCalendar
            budgetDate={budgetStartDate}
            setBudgetDate={handleBudgetChange}
            currentMonthDeposit={currentMonthDeposit}
            currentMonthWithdrawal={currentMonthWithdrawal}
            paymentHistory={formattedHistory}
          />
        </CalendarContainer>
      )}
      {selectedMenu === LivingMenu.history && (
        <History
          paymentHistory={formattedHistory}
          startDate={startDate}
          endDate={endDate}
        />
      )}
      <FloatingSwitchMenu
        selectedMenu={selectedMenu}
        onSwitch={(menu) => dispatch(setSelectedMenu(menu as LivingMenu))}
        menuList={menuList}
      />

      <Modal
        open={open}
        onOpenChange={setOpen}
        onConfirm={handleSendNotification}
        title="생활비 계좌 생성 요청"
        description={
          sendNotification
            ? '생활비 계좌 요청이 완료되었습니다'
            : '방장에게 생활비 계좌\n생성을 요청하시겠습니까?'
        }
        image="/images/etc/account-create-before.svg"
        confirmText={sendNotification ? '확인' : '요청'}
      />
    </FullNavLayout>
  )
}
