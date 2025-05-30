'use client'

import React from 'react'
import { useTranslation } from 'react-i18next'

import { DayKey, SelectorVariant } from '@/types/duty'

import { Container, DateSelection } from './styles'

interface WeekSelectorItemProps {
  day: DayKey
  selectedWeek: DayKey
  setSelectedWeek: (week: DayKey) => void
}

export function WeekSelectorItem({
  day,
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

  return (
    <Container>
      <DateSelection
        variant={checkSelect(selectedWeek, day)}
        onClick={() => setSelectedWeek(day)}>
        {`${t(`duty.week.${day}`)}`}
      </DateSelection>
    </Container>
  )
}
