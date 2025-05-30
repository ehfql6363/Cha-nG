'use client'

import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { format } from 'date-fns'
import ko from 'date-fns/locale/ko'
import { useRouter } from 'next/navigation'

import { getPaymentCurrentStatus } from '@/apis/payment'
import { AccountHistoryViewer, ConfirmButton, IconButton } from '@/components'
import { useAppSelector } from '@/hooks'
import { setPaymentCurrent } from '@/store/slices/pledgeSlice'
import { PaymentCurrent, PaymentStatus } from '@/types/budget'
import { FormattedAccountPaymentHistory } from '@/types/fintech'
import { ButtonVariant } from '@/types/ui'
import { formatMoney } from '@/utils/format'

import {
  AccountContainer,
  AccountInfo,
  AccountTitle,
  BottomContainer,
  ButtonContainer,
  Container,
  ContentContainer,
  CurrentMonth,
  DashBoardContainer,
  MonthNavigation,
  MonthSummary,
  SelectButton,
  SelectContainer,
} from './styles'

export function Account({
  paymentHistory,
  budgetDate,
  setBudgetDate,
  startDate,
  endDate,
}: {
  paymentHistory: FormattedAccountPaymentHistory[]
  budgetDate: Date
  setBudgetDate: (date: Date) => void
  startDate: string
  endDate: string
}) {
  type Filter = 'ALL' | '1' | '2'
  const [selectedFilter, setSelectedFilter] = useState<Filter>('ALL')
  const { t } = useTranslation()
  const accountDetail = useAppSelector(
    (state) => state.pledge.account.accountDetail,
  )
  const dispatch = useDispatch()
  const selectItem: { label: string; value: Filter }[] = [
    {
      label: t('livingBudget.all'),
      value: 'ALL',
    },
    {
      label: t('livingBudget.depositAmount'),
      value: '1',
    },
    {
      label: t('livingBudget.withdrawalAmount'),
      value: '2',
    },
  ]
  const filteredHistory = useMemo(() => {
    if (selectedFilter === 'ALL') return paymentHistory
    return paymentHistory.filter(
      (item) => item.transactionType === selectedFilter,
    )
  }, [paymentHistory, selectedFilter])
  const handleMonth = (direction: number) => () => {
    const newDate = new Date(budgetDate)
    newDate.setMonth(newDate.getMonth() + direction)
    setMonth(format(newDate, 'yyyyMM'))
    // if (newDate.getTime() < new Date().getTime()) {
    setBudgetDate(newDate)
    // }
  }
  const formatMonth = (date: Date) => {
    return format(date, 'yyyy.MM', { locale: ko })
  }
  const router = useRouter()
  const paymentCurrent = useAppSelector((state) => state.pledge.paymentCurrent)
  const currentMonth = useMemo(() => format(new Date(), 'yyyyMM'), [])
  const [month, setMonth] = useState(currentMonth)

  return (
    <Container>
      <MonthSummary>
        <AccountContainer>
          <AccountTitle>
            <IconButton
              src="/icons/logo-bank.svg"
              alt="월세 / 공과금 계좌"
            />
            월세 / 공과금 계좌
          </AccountTitle>
          <AccountInfo>
            <span>{t('fintech.bankName') + ' ' + accountDetail.accountNo}</span>
            <div>{formatMoney(accountDetail.accountBalance)}</div>
          </AccountInfo>
        </AccountContainer>
      </MonthSummary>
      <AccountHistoryViewer filteredHistory={filteredHistory}>
        <ButtonContainer>
          {paymentCurrent?.rent === PaymentStatus.COLLECTED && (
            <ConfirmButton
              onClick={() => {
                router.push(`/pledge/transfer/owner?month=${month}`)
              }}
              label="집주인에게 보내기"
            />
          )}

          {paymentCurrent?.userRent != null &&
            paymentCurrent?.userRent !== PaymentStatus.COLLECTED && (
              <ConfirmButton
                variant={
                  paymentCurrent?.rent === PaymentStatus.PAID
                    ? ButtonVariant.disabled
                    : ButtonVariant.next
                }
                onClick={() => {
                  router.push(`/pledge/transfer/rent?month=${month}`)
                }}
                label={
                  paymentCurrent?.rent === PaymentStatus.PAID
                    ? '공통 월세 납부 완료'
                    : '월세 채우기'
                }
              />
            )}
        </ButtonContainer>
        <DashBoardContainer>
          <MonthNavigation>
            <IconButton
              src={'/icons/arrow-small-left.svg'}
              alt="전월 선택"
              onClick={handleMonth(-1)}
            />
            <CurrentMonth>{formatMonth(budgetDate)}</CurrentMonth>
            <IconButton
              src={'/icons/arrow-small-right.svg'}
              alt="다음월 선택"
              onClick={handleMonth(1)}
            />
          </MonthNavigation>
          <ContentContainer>
            <SelectContainer>
              {selectItem.map((item) => (
                <SelectButton
                  key={item.value}
                  isSelected={selectedFilter === item.value}
                  onClick={() => setSelectedFilter(item.value)}>
                  {item.label}
                </SelectButton>
              ))}
            </SelectContainer>
          </ContentContainer>
        </DashBoardContainer>
      </AccountHistoryViewer>
      <BottomContainer />
    </Container>
  )
}
