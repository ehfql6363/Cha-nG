'use client'

import React, { useMemo, useState } from 'react'
import { useTranslation } from 'react-i18next'

import { useRouter } from 'next/navigation'

import { AccountHistoryViewer, ConfirmButton } from '@/components'
import { useFormattedDuration } from '@/hooks'
import { FormattedAccountPaymentHistory } from '@/types/fintech'
import { ButtonVariant } from '@/types/ui'

import { EmptyContainer } from '../../styles'
import {
  ButtonContainer,
  ContentContainer,
  DateContainer,
  SelectButton,
  SelectContainer,
} from './styles'

export function History({
  paymentHistory,
  startDate,
  endDate,
}: {
  paymentHistory: FormattedAccountPaymentHistory[]
  startDate: string
  endDate: string
}) {
  type Filter = 'ALL' | '1' | '2'
  const [selectedFilter, setSelectedFilter] = useState<Filter>('ALL')
  const { t } = useTranslation()

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

  const duration = useFormattedDuration(startDate, endDate)
  const router = useRouter()
  return (
    <AccountHistoryViewer filteredHistory={filteredHistory}>
      <ButtonContainer>
        <ConfirmButton
          onClick={() => {
            router.push('/budget/living/deposit')
          }}
          variant={ButtonVariant.prev}
          label="livingBudget.deposit.label"
        />
        <ConfirmButton
          onClick={() => {
            router.push('/budget/living/withdraw')
          }}
          variant={ButtonVariant.next}
          label="livingBudget.withdraw.label"
        />
      </ButtonContainer>
      <DateContainer>{duration}</DateContainer>
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
      <EmptyContainer />
    </AccountHistoryViewer>
  )
}
