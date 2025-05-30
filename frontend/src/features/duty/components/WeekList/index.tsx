'use client'

import React from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { IconButton } from '@/components/IconButton'
import { setCreateDayOfWeek } from '@/store/slices/dutySlice'
import { Title } from '@/styles/styles'
import { DayKey, DutyWeekList } from '@/types/duty'

import { WeekSelector } from '../WeekSelector'
import { Container, TextContainer, TopContainer } from './styles'

interface WeekListProps {
  dutyList: DutyWeekList
  selectedWeek: DayKey
  setSelectedWeek: (week: DayKey) => void
}

export function WeekList({
  dutyList,
  selectedWeek,
  setSelectedWeek,
}: WeekListProps) {
  const { t } = useTranslation()
  const router = useRouter()
  const dispatch = useDispatch()
  const week = 'second'

  const handleClick = () => {
    router.push('/duty/create')
    dispatch(setCreateDayOfWeek(selectedWeek))
  }

  return (
    <Container>
      <TopContainer>
        <div>3월 {t(`duty.schedule.week.${week}`)}</div>
        <TextContainer>
          <Title>이번주 당번</Title>
          <IconButton
            src="/icons/plus_circle.svg"
            alt="plus_circle"
            onClick={handleClick}
          />
        </TextContainer>
      </TopContainer>

      <WeekSelector
        dutyList={dutyList}
        selectedWeek={selectedWeek}
        setSelectedWeek={setSelectedWeek}
      />
    </Container>
  )
}
