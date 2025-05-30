'use client'

import { useRef } from 'react'

import koLocale from '@fullcalendar/core/locales/ko'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'
import FullCalendar from '@fullcalendar/react'
import { format } from 'date-fns'
import { ko } from 'date-fns/locale'

import { TitleContainer } from '@/styles/styles'
import { FormattedAccountPaymentHistory } from '@/types/fintech'
import { formatMoney } from '@/utils/format'

import { IconButton } from '../../../../../components/IconButton'
import {
  Container,
  CurrentMonth,
  DayCell,
  DayNumber,
  EventAmount,
  ExpenseAmount,
  ExpenseLabel,
  ExpenseSummary,
  MonthNavigation,
  MonthSummary,
  StyledFullCalendar,
} from './styles'

export function BudgetCalendar({
  budgetDate,
  setBudgetDate,
  currentMonthDeposit,
  currentMonthWithdrawal,
  paymentHistory,
}: {
  budgetDate: Date
  setBudgetDate: (date: Date) => void
  currentMonthDeposit: number
  currentMonthWithdrawal: number
  paymentHistory: FormattedAccountPaymentHistory[]
}) {
  const calendarRef = useRef<FullCalendar>(null)

  const formatMonth = (date: Date) => {
    return format(date, 'yyyy.MM', { locale: ko })
  }

  const handleMonth = (direction: number) => () => {
    const newDate = new Date(budgetDate)
    newDate.setMonth(newDate.getMonth() + direction)
    if (newDate.getTime() < new Date().getTime()) {
      setBudgetDate(newDate)
      if (calendarRef.current) {
        calendarRef.current.getApi().gotoDate(newDate)
      }
    }
  }

  return (
    <Container>
      <MonthSummary>
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

        <ExpenseSummary>
          <TitleContainer>
            <ExpenseLabel>입금</ExpenseLabel>
            <ExpenseAmount>{formatMoney(currentMonthDeposit)}</ExpenseAmount>
          </TitleContainer>
          <TitleContainer>
            <ExpenseLabel>출금</ExpenseLabel>
            <ExpenseAmount>{formatMoney(currentMonthWithdrawal)}</ExpenseAmount>
          </TitleContainer>
        </ExpenseSummary>
      </MonthSummary>

      <StyledFullCalendar>
        <FullCalendar
          ref={calendarRef}
          plugins={[dayGridPlugin, interactionPlugin]}
          initialView="dayGridMonth"
          locale={koLocale}
          firstDay={1}
          initialDate={budgetDate}
          events={paymentHistory.map((item) => ({
            title: item.title,
            date: item.date,
          }))}
          height="auto"
          contentHeight="auto"
          headerToolbar={false}
          dayCellContent={(info) => {
            const dayNumber = format(info.date, 'd', { locale: ko })
            return (
              <DayCell>
                <DayNumber>{dayNumber}</DayNumber>
              </DayCell>
            )
          }}
          eventContent={(eventInfo) => {
            return <EventAmount>{eventInfo.event.title}</EventAmount>
          }}
          datesSet={(dateInfo) => {
            const newDate = dateInfo.view.currentStart
            if (
              newDate.getMonth() !== budgetDate.getMonth() ||
              newDate.getFullYear() !== budgetDate.getFullYear()
            ) {
              setBudgetDate(newDate)
            }
          }}
        />
      </StyledFullCalendar>
    </Container>
  )
}
