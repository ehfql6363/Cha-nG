'use client'

import React from 'react'
import { useTranslation } from 'react-i18next'

import { User } from '@/types/user'

import { AssigneesSelected } from '../AssigneesSelected'
import {
  AssigneesButtonContainer,
  Container,
  Placeholder,
  TopContainer,
} from './styles'

interface AssigneesSelectorProps {
  setIsBottomSheetOpen: (isBottomSheetOpen: boolean) => void
  assignees: number[]
  userList: User[]
}

export function AssigneesSelector({
  setIsBottomSheetOpen,
  assignees,
  userList,
}: AssigneesSelectorProps) {
  const { t } = useTranslation()
  const isAssignessExist: boolean = assignees.length > 0

  return (
    <Container>
      <TopContainer>
        <div>{t('duty.edit.assignees.title')}</div>
      </TopContainer>
      <AssigneesButtonContainer onClick={() => setIsBottomSheetOpen(true)}>
        {!isAssignessExist && (
          <Placeholder>{t('duty.edit.assignees.placeholder')}</Placeholder>
        )}
        {isAssignessExist && (
          <AssigneesSelected
            assignees={assignees}
            userList={userList}
          />
        )}
      </AssigneesButtonContainer>
    </Container>
  )
}
