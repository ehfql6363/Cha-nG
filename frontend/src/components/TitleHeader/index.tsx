'use client'

import { memo } from 'react'

import { Container, Description, Title } from './styles'

interface TitleHeaderProps {
  title: string
  description?: string
}

export const TitleHeader = memo(function TitleHeader({
  title,
  description,
}: TitleHeaderProps) {
  return (
    <Container>
      <Title>{title}</Title>
      {description && <Description>{description}</Description>}
    </Container>
  )
})
