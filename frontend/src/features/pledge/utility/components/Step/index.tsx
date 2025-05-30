'use client'

import { useEffect, useMemo, useState } from 'react'
import { useTranslation } from 'react-i18next'

import { UserItem } from '@/components/UserItem'
import { useAppSelector } from '@/hooks/useAppSelector'
import { BudgetStatus } from '@/types/budget'
import { User } from '@/types/user'

import { BoxContainer } from '../../../styles'
import {
  BarContainer,
  BarContainerWrapper,
  BottomContainer,
  ContentContainer,
  MonthContainer,
  MonthLabel,
  MonthLabelsContainer,
  MonthText,
  StatusBarContainer,
  StatusContainer,
  StatusIcon,
  StepContainer,
  StepItem,
  TopDescription,
} from './styles'

interface UserList {
  user: User
  week: weekPaid[]
}

interface weekPaid {
  month: number
  week: number
  finalStatus: BudgetStatus
}

export function Step() {
  const { t } = useTranslation()
  const utilityInfo = useAppSelector((state) => state.pledge.utility)
  const group = useAppSelector((state) => state.group.group.members)

  const [userList, setUserList] = useState<UserList[]>([])

  const currentMonth = String(new Date().getUTCMonth() + 1).padStart(2, '0')

  const filteredWeekList = useMemo(
    () =>
      utilityInfo?.weekList
        .map((item) => ({
          mm: item.month.split('-')[1].padStart(2, '0'),
          ...item,
        }))
        .sort((a, b) => {
          if (a.mm === b.mm) {
            return Number(a.week) - Number(b.week)
          }
          return Number(a.mm) - Number(b.mm)
        })
        .map((item) => {
          return {
            ...item,
            mm: `${Number(item.mm)}월`,
          }
        }),
    [utilityInfo],
  )
  useEffect(() => {
    if (!utilityInfo || !group || !filteredWeekList) return

    const dayOfWeek = new Date().getDay()

    const exceedDueDate = dayOfWeek === 0 || dayOfWeek >= 5
    //dueDate 는 언제나 금요일인것으로 가정

    const newUserList: UserList[] = group.map((item) => ({
      user: item,
      week: [],
    }))

    filteredWeekList.slice(0, 6).forEach((item) => {
      item.paidUserIds?.forEach((paidUser) => {
        newUserList
          .find((user) => user.user.id === paidUser)
          ?.week.push({
            month: Number(item.month.slice(5)),
            week: item.week,
            finalStatus: 'complete',
          })
      })
      item.debtUserIds.forEach((debtUser) => {
        newUserList
          .find((user) => user.user.id === debtUser)
          ?.week.push({
            month: Number(item.month.slice(5)),
            week: item.week,
            finalStatus: exceedDueDate ? 'debt' : 'expected',
          })
      })
    })

    setUserList(newUserList)
  }, [utilityInfo, filteredWeekList, group])

  return (
    <>
      <BoxContainer>
        <ContentContainer>
          <TopDescription>전체 납부 현황</TopDescription>
          <BottomContainer>
            <StepContainer>
              {userList.map((item, index) => (
                <StepItem key={item.user.id}>
                  <UserItem
                    user={item.user}
                    variant="bar"
                    size="small"
                    showName={true}
                  />
                  <BarContainerWrapper>
                    {index === 0 && (
                      <MonthLabelsContainer>
                        {filteredWeekList?.map((item) => (
                          <MonthLabel key={item.week}>
                            <MonthText>{item.mm}</MonthText>
                            <MonthText>{item.week}주</MonthText>
                          </MonthLabel>
                        ))}
                      </MonthLabelsContainer>
                    )}
                    <BarContainer>
                      {item.week.map((week) => (
                        <StatusContainer key={week.week}>
                          <StatusIcon variant={week.finalStatus} />
                          <div>{t(`pledge.status.${week.finalStatus}`)}</div>
                        </StatusContainer>
                      ))}
                    </BarContainer>
                  </BarContainerWrapper>
                </StepItem>
              ))}
            </StepContainer>
          </BottomContainer>
        </ContentContainer>
      </BoxContainer>
    </>
  )
}
