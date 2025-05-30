import styled from '@emotion/styled'

import { ContractStatus } from '@/types/contract'

export const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`

export const SwitchContainer = styled.div`
  position: relative;
  border-radius: 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
`

export const SwitchText = styled.span<{ checked: boolean }>`
  color: ${({ checked, theme }) =>
    checked ? theme.color.background.white : theme.color.text.low};
  z-index: 1;
  ${({ theme }) => theme.typography.styles.name};
  user-select: none;
`

export const StatusLabelContainer = styled.div<{ variant: ContractStatus }>`
  position: absolute;
  font-size: 10px;
  padding: 2px 3px;
  color: ${({ theme }) => theme.color.background.white};
  font-family: ${({ theme }) => theme.typography.fonts.paperlogySemiBold};
  ${({ variant, theme }) => {
    switch (variant) {
      case ContractStatus.pending:
        return `
          background-color: #9E6AFF;
          
        `
      case ContractStatus.confirmed:
        return `
          background-color: #00D80B;
        `
      case ContractStatus.reviewRequired:
        return `
          background-color: ${theme.color.text.distructive};
        `
      case ContractStatus.shouldInvite:
        return `
          background-color: ${theme.color.text.low};
        `
      default:
        return ''
    }
  }}
  border-radius: 14px;
  transition: left 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
  white-space: nowrap;
`
