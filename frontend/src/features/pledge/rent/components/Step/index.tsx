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
  MonthLabel,
  MonthLabelsContainer,
  MonthText,
  StatusContainer,
  StatusIcon,
  StepContainer,
  StepItem,
  TopDescription,
} from './styles'

interface UserList {
  user: User
  month: monthPaid[]
}

interface monthPaid {
  month: number
  finalStatus: BudgetStatus
}

export function Step() {
  const { t } = useTranslation()
  const rentInfo = useAppSelector((state) => state.pledge.rent)
  const group = useAppSelector((state) => state.group.group.members)

  const [userList, setUserList] = useState<UserList[]>([])
  const currentYear = String(new Date().getFullYear())

  const filteredMonthList = useMemo(
    () =>
      rentInfo?.monthList
        .map((item) => ({
          yyyy: item.month.split('-')[0],
          mm: item.month.split('-')[1].padStart(2, '0'),
          ...item,
        }))
        .sort((a, b) => {
          if (a.yyyy === b.yyyy) {
            return Number(a.mm) - Number(b.mm)
          }
          return Number(a.yyyy) - Number(b.yyyy)
        })
        .map((item) => {
          return {
            ...item,
            yyyy: item.yyyy == currentYear ? '' : `${item.yyyy.slice(2)}년`,
            date:
              item.yyyy == currentYear
                ? `${item.yyyy.slice(2)}${item.mm}월`
                : `${item.yyyy.slice(2)}년${item.mm}월`,
          }
        }),
    [rentInfo],
  )

  useEffect(() => {
    if (!rentInfo || !group || !filteredMonthList) return

    const date = new Date().getDate()
    const dutDate = rentInfo.dueDate || 0
    const exceedDueDate: boolean = dutDate < date

    const newUserList: UserList[] = group.map((item) => ({
      user: item,
      month: [],
    }))

    filteredMonthList.slice(0, 6).forEach((item) => {
      item.paidUserIds?.forEach((paidUser) => {
        newUserList
          .find((user) => user.user.id === paidUser)
          ?.month.push({
            month: Number(item.month.slice(5)),
            finalStatus: 'complete',
          })
      })
      item.debtUserIds.forEach((debtUser) => {
        newUserList
          .find((user) => user.user.id === debtUser)
          ?.month.push({
            month: Number(item.month.slice(5)),
            finalStatus: exceedDueDate ? 'debt' : 'expected',
          })
      })
    })

    setUserList(newUserList)
  }, [rentInfo, group, filteredMonthList])
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
                        {filteredMonthList?.map((filteredItem) => (
                          <MonthLabel key={filteredItem.month}>
                            <MonthText>{filteredItem.yyyy}</MonthText>
                            <MonthText>{Number(filteredItem.mm)}월</MonthText>
                          </MonthLabel>
                        ))}
                      </MonthLabelsContainer>
                    )}
                    <BarContainer>
                      {item.month.map((month) => (
                        <StatusContainer key={month.month}>
                          <StatusIcon variant={month.finalStatus} />
                          <div>{t(`pledge.status.${month.finalStatus}`)}</div>
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
