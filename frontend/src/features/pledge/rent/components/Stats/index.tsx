'use client'

import React from 'react'
import { useTranslation } from 'react-i18next'

import { useAppSelector } from '@/hooks/useAppSelector'
import { BankLabel, ShowCenterBox, TextCenterContainer } from '@/styles/styles'
import { BudgetStatus } from '@/types/budget'
import { formatMoney } from '@/utils/format'

import { BoxContainer } from '../../../styles'
import {
  ContentContainer,
  DisabledColorText,
  LowColorText,
  StatusContainer,
  StatusIcon,
  TextBox,
  TextContainer,
  TopDescription,
} from './styles'

export function Stats() {
  const { t } = useTranslation()
  const rentInfo = useAppSelector((state) => state.pledge.rent)
  const contractInfo = useAppSelector((state) => state.contract.contract)
  const groupSize = useAppSelector((state) => state.group.group.members.length)
  const paymentCurrent = useAppSelector((state) => state.pledge.paymentCurrent)

  const month = Number(
    rentInfo?.monthList[0]?.month.slice(5) ?? new Date().getMonth() + 1,
  )
  const userId = useAppSelector((state) => state.user.user.id)
  const date = new Date().getDate()
  const dutDate = rentInfo?.dueDate || 0
  const status = rentInfo?.currentMonth.find(
    (item) => item.userId === userId,
  )?.status

  let finalStatus: BudgetStatus = 'expected'
  if (date < dutDate) {
    finalStatus = 'expected'
  } else if (date === dutDate) {
    finalStatus = status ? 'complete' : 'expected'
  } else if (date > dutDate) {
    finalStatus = status ? 'complete' : 'debt'
  }

  return (
    <>
      <BoxContainer>
        <ContentContainer>
          <TopDescription>월세 - {month}월 정산내역</TopDescription>
          <TextContainer>
            <TextBox>
              <LowColorText>월세</LowColorText>
              <LowColorText>
                {formatMoney(rentInfo?.totalAmount ?? 0)}
              </LowColorText>
            </TextBox>
            <hr />
            <TextBox>
              <LowColorText>참여 인원</LowColorText>
              <LowColorText>{groupSize} 명</LowColorText>
            </TextBox>
            <TextBox>
              <LowColorText>분담 비율</LowColorText>
              <LowColorText>
                {
                  contractInfo?.rent.userPaymentInfo.find(
                    (member) => member.userId === userId,
                  )?.ratio
                }
                / {contractInfo?.rent.totalRatio}
              </LowColorText>
            </TextBox>
            <TextBox>
              <LowColorText>내가 낼 월세</LowColorText>
              <LowColorText>
                {formatMoney(rentInfo?.myAmount ?? 0)}
              </LowColorText>
            </TextBox>
            <hr />
            <TextBox>
              <LowColorText>자동이체 일</LowColorText>
              <LowColorText>매월 {dutDate}일</LowColorText>
            </TextBox>
            <TextBox>
              <LowColorText>월세 납부 여부</LowColorText>
              <StatusContainer>
                <StatusIcon variant={finalStatus} />
                <LowColorText>{t(`pledge.status.${finalStatus}`)}</LowColorText>
              </StatusContainer>
            </TextBox>
            <ShowCenterBox isDisabled={true}>
              <LowColorText>
                <BankLabel style={{ paddingBottom: '0.5rem' }}>
                  <DisabledColorText>
                    [공통 계좌 → 집주인 계좌]
                  </DisabledColorText>
                </BankLabel>
                <TextCenterContainer>
                  {t(`payment.paymentStatus.${paymentCurrent.rent}.title`)}
                </TextCenterContainer>
              </LowColorText>
            </ShowCenterBox>
            <ShowCenterBox isDisabled={true}>
              <LowColorText>
                <BankLabel style={{ paddingBottom: '0.5rem' }}>
                  <DisabledColorText>[개인 계좌 → 공통 계좌]</DisabledColorText>
                </BankLabel>
                <TextCenterContainer>
                  {t(
                    `payment.userPaymentStatus.${paymentCurrent.userRent}.title`,
                  )}
                </TextCenterContainer>
              </LowColorText>
            </ShowCenterBox>
          </TextContainer>
        </ContentContainer>
      </BoxContainer>
    </>
  )
}
