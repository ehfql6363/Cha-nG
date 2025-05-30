'use client'

import { useTranslation } from 'react-i18next'

import { useAppSelector } from '@/hooks/useAppSelector'
import { ShowCenterBox, TextCenterContainer } from '@/styles/styles'
import { BankLabel } from '@/styles/styles'
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
  const utilityInfo = useAppSelector((state) => state.pledge.utility)
  const groupSize = useAppSelector((state) => state.group.group.members.length)
  const user = useAppSelector((state) => state.user.user)
  const paymentCurrent = useAppSelector((state) => state.pledge.paymentCurrent)

  const month = Number(
    utilityInfo?.weekList[0]?.month.slice(5) ?? new Date().getMonth() + 1,
  )
  const weekOfMonth = Number(utilityInfo?.weekList[0]?.week ?? 1)
  const date = new Date()
  const status = utilityInfo?.currentWeek.find(
    (item) => item.userId === user.id,
  )?.status
  const dayOfWeek = date.getDay()

  function getStatus(
    dayOfWeek: number,
    status: boolean | undefined,
  ): BudgetStatus {
    if (dayOfWeek >= 1 && dayOfWeek <= 3) {
      return 'expected'
    }
    if (dayOfWeek === 4) {
      return status ? 'complete' : 'expected'
    }

    if (dayOfWeek === 6 || dayOfWeek === 5 || dayOfWeek === 0) {
      return status ? 'complete' : 'debt'
    }
    return 'expected'
  }

  return (
    <>
      <BoxContainer>
        <ContentContainer>
          <TopDescription>
            공과금 - {month}월 {weekOfMonth}주차 정산내역
          </TopDescription>
          <TextContainer>
            <TextBox>
              <LowColorText>공과금</LowColorText>
              <LowColorText>
                {formatMoney(Number(utilityInfo?.totalAmount ?? 0))}
              </LowColorText>
            </TextBox>
            <hr />
            <TextBox>
              <DisabledColorText>참여 인원</DisabledColorText>
              <DisabledColorText>{groupSize} 명</DisabledColorText>
            </TextBox>
            <TextBox>
              <DisabledColorText>분담 비율</DisabledColorText>
              <DisabledColorText> 1 / {groupSize}</DisabledColorText>
            </TextBox>
            <TextBox>
              <LowColorText>내가 낼 공과금</LowColorText>
              <LowColorText>
                {formatMoney(Number(utilityInfo?.myAmount ?? 0))}
              </LowColorText>
            </TextBox>
            <hr />
            <TextBox>
              <DisabledColorText>자동이체 일</DisabledColorText>
              <DisabledColorText>
                {t(`duty.week.${utilityInfo?.dueDayOfWeek}`)}요일
              </DisabledColorText>
            </TextBox>
            <TextBox>
              <LowColorText>공과금 납부 여부</LowColorText>
              <StatusContainer>
                <StatusIcon variant={getStatus(dayOfWeek, status)} />
                <LowColorText>
                  {t(`pledge.status.${getStatus(dayOfWeek, status)}`)}
                </LowColorText>
              </StatusContainer>
            </TextBox>
          </TextContainer>
          <ShowCenterBox isDisabled={true}>
            <LowColorText>
              <BankLabel style={{ paddingBottom: '0.5rem' }}>
                <DisabledColorText>[공통 계좌 → 카드사]</DisabledColorText>
              </BankLabel>
              <TextCenterContainer>
                {t(`payment.paymentStatus.${paymentCurrent.utility}.title`)}
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
                  `payment.userPaymentStatus.${paymentCurrent.userUtility}.title`,
                )}
              </TextCenterContainer>
            </LowColorText>
          </ShowCenterBox>
        </ContentContainer>
      </BoxContainer>
    </>
  )
}
