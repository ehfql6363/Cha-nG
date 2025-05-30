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
  > div:first-child {
    ${({ theme }) => theme.typography.styles.description};
    color: ${({ theme }) => theme.color.text.disabled};
  }
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
          color: ${theme.color.text.low};`
    }
  }}

  ${({ theme }) => theme.typography.styles.topHeader};
`
export const DutyContainer = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  justyfiy-content: center;
  gap: 2px;
  padding: 4px 0;
`

export const DutyItem = styled.div`
  display: flex;
  height: 13px;
  width: 100%;
  padding: 0px 4px;
  border-radius: 4px;

  flex-direction: row;
  justify-content: start;
  align-items: center;
  align-self: stretch;
  background-color: ${({ theme }) => theme.color.border};

  ${({ theme }) => theme.typography.styles.tinyBold};
  color: ${({ theme }) => theme.color.text.low};
`
