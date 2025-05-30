'use client'

import React, { useEffect, useMemo, useRef, useState } from 'react'
import { useDispatch } from 'react-redux'

import { getAccountDetail } from '@/apis/fintech'
import {
  getPaymentCurrentStatus,
  retrieveRent,
  retrieveUtility,
} from '@/apis/payment'
import { BottomNavigation, TopHeader } from '@/components'
import { FloatingSwitchMenu } from '@/components'
import { PledgeMenuList } from '@/constants/FloatingSwitchMenu'
import { useFintechTime } from '@/hooks'
import { useGetAccountHistory } from '@/hooks'
import { useCurrentMonth } from '@/hooks'
import { useAppSelector } from '@/hooks/useAppSelector'
import {
  setAccountDetail,
  setPaymentCurrent,
  setPaymentHistory,
  setRent,
  setSelectedMenu,
  setUtility,
} from '@/store/slices/pledgeSlice'
import { FormattedAccountPaymentHistory } from '@/types/fintech'
import { PledgeMenu } from '@/types/ui'
import {
  formatMoney,
  formatTransactionDate,
  formatTransactionTime,
} from '@/utils/format'

import { Account } from './account'
import { ContractDetail } from './contract'
import { RentPage } from './rent'
import { Container, FullMain } from './styles'
import { UtilityPage } from './utility'

export function PledgePage() {
  const dispatch = useDispatch()
  const utilityInfo = useAppSelector((state) => state.pledge.utility)
  const selectedMenu = useAppSelector((state) => state.pledge.selectedMenu)

  const [budgetStartDate, setBudgetStartDate] = useState(new Date())
  const [budgetEndDate, setBudgetEndDate] = useState(
    new Date(new Date().getFullYear(), new Date().getMonth() + 1, 1),
  )
  const { startDate, endDate } = useFintechTime(
    new Date(),
    budgetStartDate,
    budgetEndDate,
  )
  const { utilityCurrentMonth, paymentCurrentMonth, paymentMonth } =
    useCurrentMonth(budgetStartDate)

  const rentFetched = useRef(false)
  useEffect(() => {
    const fetchRent = async () => {
      if (
        utilityCurrentMonth &&
        !rentFetched.current &&
        selectedMenu === PledgeMenu.rent
      ) {
        rentFetched.current = true
        const response = await retrieveRent(utilityCurrentMonth)
        if (response.success) {
          dispatch(setRent(response.data))
        }
        rentFetched.current = false
      }
    }
    fetchRent()
  }, [utilityCurrentMonth, selectedMenu])

  const rentAccountNo = useAppSelector(
    (state) => state.contract.contract.rent.rentAccountNo,
  )

  const getAccountHistory = useGetAccountHistory({
    accountNo: rentAccountNo,
    budgetStartDate: budgetStartDate,
    budgetEndDate: budgetEndDate,
  })

  const handleBudgetChange = (date: Date) => {
    setBudgetStartDate(date)
    setBudgetEndDate(new Date(date.getFullYear(), date.getMonth() + 1, 1))
  }

  const hasFetchedDetail = useRef(false)
  useEffect(() => {
    const fetchAccountDetail = async () => {
      if (
        rentAccountNo &&
        !hasFetchedDetail.current &&
        selectedMenu === PledgeMenu.account
      ) {
        hasFetchedDetail.current = true
        const response = await getAccountDetail(rentAccountNo)
        if (response.success) {
          dispatch(setAccountDetail(response.data.data))
        }
        hasFetchedDetail.current = false
      }
    }
    fetchAccountDetail()
  }, [rentAccountNo, selectedMenu])

  const historyFetched = useRef(false)
  useEffect(() => {
    const fetchAccountPaymentHistory = async () => {
      if (
        rentAccountNo &&
        !historyFetched.current &&
        selectedMenu == 'account'
      ) {
        historyFetched.current = true
        const response = await getAccountHistory()
        dispatch(setPaymentHistory(response))
        historyFetched.current = false
      }
    }
    fetchAccountPaymentHistory()
  }, [rentAccountNo, startDate, selectedMenu])

  const paymentHistory = useAppSelector(
    (state) => state.pledge.account.paymentHistory,
  )

  const formattedHistory = useMemo<FormattedAccountPaymentHistory[]>(() => {
    if (paymentHistory.length === 0) return []

    let transactionDate = ''
    return paymentHistory.map((item) => {
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
  }, [paymentHistory])

  const utilityFetched = useRef(false)
  useEffect(() => {
    const fetchUtility = async () => {
      if (
        utilityCurrentMonth &&
        selectedMenu === PledgeMenu.utility &&
        !utilityFetched.current
      ) {
        utilityFetched.current = true
        const response = await retrieveUtility(utilityCurrentMonth)
        if (response.success) {
          dispatch(setUtility(response.data))
        }
        utilityFetched.current = false
      }
    }
    fetchUtility()
  }, [selectedMenu, utilityCurrentMonth])
  const user = useAppSelector((state) => state.user.user)

  const hasFetchedPaymentCurrent = useRef(false)

  useEffect(() => {
    const fetchPaymentCurrent = async () => {
      if (
        !hasFetchedPaymentCurrent.current &&
        user.contractId &&
        selectedMenu != 'contract'
      ) {
        hasFetchedPaymentCurrent.current = true
        const month =
          selectedMenu == 'account' ? paymentMonth : paymentCurrentMonth
        const response = await getPaymentCurrentStatus(month)
        if (response.success) {
          dispatch(setPaymentCurrent(response.data))
        }
        hasFetchedPaymentCurrent.current = false
      }
    }
    fetchPaymentCurrent()
  }, [user.contractId, paymentCurrentMonth, selectedMenu])

  const menuList = PledgeMenuList
  return (
    <Container variant={selectedMenu}>
      <TopHeader title={'서약 관리'} />
      <FullMain>
        {selectedMenu === PledgeMenu.contract && <ContractDetail />}
        {selectedMenu === PledgeMenu.account &&
          rentAccountNo &&
          startDate != '' && (
            <Account
              paymentHistory={formattedHistory}
              startDate={startDate}
              endDate={endDate}
              budgetDate={budgetStartDate}
              setBudgetDate={handleBudgetChange}
            />
          )}
        {selectedMenu === PledgeMenu.rent && <RentPage />}
        {selectedMenu === PledgeMenu.utility && utilityInfo && <UtilityPage />}
        <FloatingSwitchMenu
          selectedMenu={selectedMenu}
          onSwitch={(menu) => dispatch(setSelectedMenu(menu as PledgeMenu))}
          menuList={menuList}
        />
      </FullMain>
      <BottomNavigation />
    </Container>
  )
}
