'use client'

import React from 'react'
import { useFormContext } from 'react-hook-form'
import { useTranslation } from 'react-i18next'

import { InputBox } from '@/components'

import { Container, TopContainer } from './styles'

export function TitleSelector() {
  const { register, watch } = useFormContext()
  const { t } = useTranslation()
  const titleValue = watch('title') // 현재 폼의 title 값

  return (
    <Container>
      <TopContainer>
        <div>할 일 입력</div>
      </TopContainer>
      <InputBox
        id="title"
        placeholder="할 일을 입력해주세요"
        type="text"
        value={titleValue}
        maxLength={15}
        {...register('title', {
          required: t('signUp.password.error.required'),
        })}
      />
    </Container>
  )
}
