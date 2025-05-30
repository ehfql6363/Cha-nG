'use client'

import React, { useState } from 'react'
import { useTranslation } from 'react-i18next'

import { ConfirmButton, UserItem } from '@/components'
import { User } from '@/constants/userList'

import {
  AssigneesSelectItemContainer,
  ButtonContainer,
  Container,
  ItemContainer,
  TextContainer,
  TopContainer,
} from './styles'

interface AssigneesSelectContentProps {
  userList: User[]
  assignees: number[]
  onConfirm: (assignees: number[]) => void
}

export function AssigneesSelectContent({
  userList,
  assignees,
  onConfirm,
}: AssigneesSelectContentProps) {
  const { t } = useTranslation()
  const [selected, setSelected] = useState<number[]>(assignees)
  const toggle = (id: number) => {
    setSelected((prev) =>
      prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id],
    )
  }

  return (
    <Container>
      <TopContainer>
        <TextContainer>{t('duty.edit.assignees.placeholder')}</TextContainer>
        <AssigneesSelectItemContainer>
          {userList.map((user) => (
            <ItemContainer
              key={user.id}
              selected={selected.includes(user.id)}
              onClick={() => toggle(user.id)}>
              <UserItem
                user={user}
                variant="bar"
                size="large"
              />
            </ItemContainer>
          ))}
        </AssigneesSelectItemContainer>
      </TopContainer>
      <ButtonContainer>
        <ConfirmButton
          label="선택 완료"
          onClick={() => onConfirm(selected)}
        />
      </ButtonContainer>
    </Container>
  )
}
