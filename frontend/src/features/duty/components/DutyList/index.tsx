'use client'

import React from 'react'

import { AnimatedImage } from '@/components'
import {
  Description,
  EmptyContainer,
  PaddingContainer,
  Title,
  TitleContainer,
} from '@/styles/styles'
import { DayKey, Duty, DutyWeekList } from '@/types/duty'
import { ImageVariant } from '@/types/ui'
import { User } from '@/types/user'

import { DutyListItem } from '../DutyListItem'
import { Container } from './styles'

interface DutyListProps {
  dutyList: DutyWeekList
  selectedWeek: DayKey
  userList: User[]
  onSelectDuty: (duty: Duty) => void
}

export function DutyList({
  dutyList,
  selectedWeek,
  userList,
  onSelectDuty,
}: DutyListProps) {
  return (
    <Container>
      {dutyList[selectedWeek].length > 0 &&
        dutyList[selectedWeek].map((duty) => (
          <DutyListItem
            key={duty.id}
            duty={duty}
            userList={userList}
            onSelectDuty={onSelectDuty}
          />
        ))}
      {dutyList[selectedWeek].length == 0 && (
        <PaddingContainer>
          <EmptyContainer>
            <AnimatedImage
              src="/images/duty/duty-no.svg"
              alt="당번이 없습니다"
              width={80}
              height={80}
              variant={ImageVariant.bounce}
            />
            <TitleContainer>
              <Title>당번이 없습니다</Title>
            </TitleContainer>
            <Description>친구들과 대화를 통해 당번을 만들어보세요!</Description>
          </EmptyContainer>
        </PaddingContainer>
      )}
    </Container>
  )
}
