'use client'

import { forwardRef } from 'react'
import { useTranslation } from 'react-i18next'

import { ButtonVariant } from '@/types/ui'

import { StyledButton } from './styles'

interface ConfirmButtonProps {
  label?: string
  onClick?: () => void
  variant?: ButtonVariant
}

export const ModalConfirmButton = forwardRef<
  HTMLButtonElement,
  ConfirmButtonProps
>(({ label = 'next', onClick, variant = ButtonVariant.next }, ref) => {
  const { t } = useTranslation()

  const handleKeyDown = (e: React.KeyboardEvent<HTMLButtonElement>) => {
    if (e.key === 'Enter' && label !== 'disabled') {
      e.preventDefault()
      onClick?.()
    }
  }

  return (
    <StyledButton
      ref={ref}
      type="button"
      variant={variant}
      onClick={onClick}
      disabled={label === 'disabled'}
      tabIndex={0}
      onKeyDown={handleKeyDown}
      autoFocus>
      {t(label)}
    </StyledButton>
  )
})

ModalConfirmButton.displayName = 'ModalConfirmButton'
