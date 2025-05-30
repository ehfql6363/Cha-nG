'use client'

import React, { useState } from 'react'
import { useTranslation } from 'react-i18next'

import { DayKey, Duty, SelectorVariant } from '@/types/duty'

import { Container, DateSelection, DutyContainer, DutyItem } from './styles'

interface WeekSelectorItemProps {
  day: DayKey
  date: number
  duty: Duty[]
  selectedWeek: DayKey
  setSelectedWeek: (week: DayKey) => void
}

export function WeekSelectorItem({
  day,
  date,
  duty,
  selectedWeek,
  setSelectedWeek,
}: WeekSelectorItemProps) {
  const { t } = useTranslation()

  function checkSelect(selectedWeek: DayKey, day: string): SelectorVariant {
    if (selectedWeek === day) {
      return 'select'
    }
    return day === 'sunday' || day === 'saturday' ? day : 'default'
  }
  const title = (item: Duty) => {
    return t(`duty.category.${item.category}`)
  }
  return (
    <Container>
      <div>{`${t(`duty.week.${day}`)}`}</div>
      <DateSelection
        variant={checkSelect(selectedWeek, day)}
        onClick={() => setSelectedWeek(day)}>
        {date}
      </DateSelection>
      <DutyContainer>
        {duty.map((item) => (
          <DutyItem key={item.id}>{title(item)}</DutyItem>
        ))}
      </DutyContainer>
    </Container>
  )
}
