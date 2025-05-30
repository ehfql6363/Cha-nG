'use client'

import React from 'react'
import { useTranslation } from 'react-i18next'

import { useRouter } from 'next/navigation'

import '@/styles/styles'
import { DayKey } from '@/types/duty'

import { WeekSelectorItem } from '../WeekSelectorItem'
import { Container, WeekSelectorItemContainer } from './styles'

interface WeekSelectorProps {
  selectedWeek: DayKey
  setSelectedWeek: (week: DayKey) => void
}

export function WeekSelector({
  selectedWeek,
  setSelectedWeek,
}: WeekSelectorProps) {
  const { t } = useTranslation()

  const week: DayKey[] = [
    'monday',
    'tuesday',
    'wednesday',
    'thursday',
    'friday',
    'saturday',
    'sunday',
  ]

  return (
    <Container>
      <div> {t(`duty.edit.week.title`)} </div>
      <WeekSelectorItemContainer>
        {week.map((day) => (
          <WeekSelectorItem
            key={day}
            day={day}
            selectedWeek={selectedWeek}
            setSelectedWeek={setSelectedWeek}
          />
        ))}
      </WeekSelectorItemContainer>
    </Container>
  )
}
