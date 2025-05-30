import styled from '@emotion/styled'
import Link from 'next/link'

import { CustomTheme } from '@/styles/themes'
import { DayKey, SelectorVariant } from '@/types/duty'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  width: 2.5rem; // 피그마상으로 하면 초과돼서 크기 수정
  gap: 4px;
`

export const DateSelection = styled.div<{ variant: SelectorVariant }>`
  display: flex;
  width: 100%;
  aspect-ratio: 1/1;
  justify-content: center;
  align-items: center;
  border-radius: 16px;
  background-color: ${({ theme }) => theme.color.secondary};

  ${({ variant, theme }) => {
    switch (variant) {
      case 'select':
        return `
          background-color: ${theme.color.primary};
          color: ${theme.color.background.white};
        `
      case 'sunday':
        return `
          background-color: ${theme.color.secondary};
          color: ${theme.color.text.sunday};`
      case 'saturday':
        return `
          background-color: ${theme.color.secondary};
          color: ${theme.color.text.saturday};`
      default:
        return `
          background-color: ${theme.color.secondary};
          color: ${theme.color.text.regular};`
    }
  }}

  ${({ theme }) => theme.typography.styles.topHeader};
`
