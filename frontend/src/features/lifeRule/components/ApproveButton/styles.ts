import styled from '@emotion/styled'

import { CustomTheme } from '@/styles/themes'
import { ButtonVariant } from '@/types/ui'

interface StyledButtonProps {
  variant: ButtonVariant
}

export const ButtonContainer = styled.div`
  display: flex;
  gap: 8px;
  width: 100%;
  margin: 20px 0;
`

export const StyledButton = styled.button<StyledButtonProps>`
  flex: 1;
  padding: 20px;
  border-radius: 16px;
  border: none;
  font-size: 16px;
  text-align: center;
  ${({ theme }) => theme.typography.styles.button};

  ${({ variant, theme }: StyledButtonProps & { theme: CustomTheme }) => {
    switch (variant) {
      case 'reject':
        return `
         background-color: ${theme.color.secondary};
        color: ${theme.color.text.low};   

        `
      case 'approve':
        return `
          background-color: ${theme.color.primary};
          color: white;
        `
      case 'disabled':
        return `
          background-color: ${theme.color.border};
          color: ${theme.color.text.disabled};
          cursor: not-allowed;
        `
      default:
        return ''
    }
  }}
`
