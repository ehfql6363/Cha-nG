'use client'

import React from 'react'

import { DayKey, DutyWeekList } from '@/types/duty'

import { WeekSelectorItem } from '../WeekSelectorItem'
import { Container } from './styles'

interface WeekSelectorProps {
  dutyList: DutyWeekList
  selectedWeek: DayKey
  setSelectedWeek: (week: DayKey) => void
}

export function WeekSelector({
  dutyList,
  selectedWeek,
  setSelectedWeek,
}: WeekSelectorProps) {
  const week: DayKey[] = [
    'monday',
    'tuesday',
    'wednesday',
    'thursday',
    'friday',
    'saturday',
    'sunday',
  ]

  // TODO: 1일일때 그 전날을 한달의 전으로 구하기
  const today = new Date()
  const dayOfWeek = today.getDay()
  const diff = dayOfWeek === 0 ? 6 : dayOfWeek - 1

  const mondayDate = new Date(today)
  mondayDate.setDate(today.getDate() - diff)

  return (
    <Container>
      {week.map((item, index) => {
        const thisDate = new Date(mondayDate)
        thisDate.setDate(mondayDate.getDate() + index)
        const dateNumber = thisDate.getDate()

        return (
          <WeekSelectorItem
            key={item}
            day={item}
            date={dateNumber}
            duty={dutyList[item]}
            selectedWeek={selectedWeek}
            setSelectedWeek={setSelectedWeek}
          />
        )
      })}
    </Container>
  )
}
