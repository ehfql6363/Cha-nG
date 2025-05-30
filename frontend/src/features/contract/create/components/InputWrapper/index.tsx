'use client'

import { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { ToggleSwitch } from '@/components'
import { useAppSelector } from '@/hooks'
import { setUseUtilityCard } from '@/store/slices/contractSlice'
import { Title, TitleContainer } from '@/styles/styles'

import { InputContainer, Label } from './styles'

interface InputBoxProps {
  id: string
  isAfter: boolean
  children?: React.ReactNode
}

export const InputWrapper = ({ id, isAfter, children }: InputBoxProps) => {
  const [label, setLabel] = useState<string>('')
  const [title, setTitle] = useState<string>('')
  const { t, i18n } = useTranslation()
  const dispatch = useDispatch()
  const useUtilityCard = useAppSelector(
    (state) => state.contract.useUtilityCard,
  )
  useEffect(() => {
    if (!id) return
    const labelText = `contract.${id}.${isAfter ? 'afterLabel' : 'label'}`
    const titleText = `contract.${id}.title`
    setLabel(i18n.exists(labelText) ? t(labelText) : '')
    setTitle(i18n.exists(titleText) ? t(titleText) : '')
  }, [id, isAfter, t, i18n])

  return (
    <InputContainer>
      <TitleContainer>
        {title && <Title>{title}</Title>}
        {id == 'utility' && (
          <ToggleSwitch
            isOn={useUtilityCard}
            setIsOn={() => dispatch(setUseUtilityCard(!useUtilityCard))}
          />
        )}
      </TitleContainer>

      {label && <Label htmlFor={id}>{label}</Label>}
      {children}
    </InputContainer>
  )
}
