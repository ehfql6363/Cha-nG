import styled from '@emotion/styled'

import { BudgetStatus } from '@/types/budget'

export const ContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  gap: 30px;
`

export const TopDescription = styled.div`
  width: 100%;
  ${({ theme }) => theme.typography.styles.name};
  color: ${({ theme }) => theme.color.text.disabled};
`

export const TextContainer = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;

  > hr {
    width: 100%;
    height: 1px;
    border: 0px;
    background-color: ${({ theme }) => theme.color.border};
  }
`

export const TextBox = styled.div`
  display: flex;
  width: 100%;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
`

export const LowColorText = styled.div`
  ${({ theme }) => theme.typography.styles.topHeader};
  color: ${({ theme }) => theme.color.text.low};
`

export const DisabledColorText = styled.div`
  ${({ theme }) => theme.typography.styles.name};
  color: ${({ theme }) => theme.color.text.disabled};
`

export const StatusContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 8px;
`

export const StatusIcon = styled.div<{ variant: BudgetStatus }>`
  width: 10px;
  height: 10px;
  border-radius: 50%;

  ${({ variant, theme }) => {
    switch (variant) {
      case 'complete':
        return `
          background-color: ${theme.color.text.confirm};
        `
      case 'debt':
        return `
          background-color: ${theme.color.text.distructive};
        `
      case 'expected':
        return `
          background-color: ${theme.color.primary};
        `
    }
  }}
`
