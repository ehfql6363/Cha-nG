'use client'

import { memo } from 'react'
import { useTranslation } from 'react-i18next'

import { useRouter } from 'next/navigation'

import { IconButton } from '../IconButton'
import { Container, HeaderButton, HeaderTitle } from './styles'

interface TopHeaderProps {
  title: string
  headerRightButton?: React.ReactNode
}

export const TopHeader = memo(function TopHeader({
  title,
  headerRightButton,
}: TopHeaderProps) {
  const { t } = useTranslation()
  const router = useRouter()
  return (
    <Container>
      <IconButton
        src="/icons/arrow-left.svg"
        alt={t('icon.back')}
        onClick={() => router.back()}
      />
      <HeaderTitle>{title}</HeaderTitle>
      {headerRightButton ?? <HeaderButton />}
    </Container>
  )
})
