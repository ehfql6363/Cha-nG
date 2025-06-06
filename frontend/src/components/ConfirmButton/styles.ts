import styled from '@emotion/styled'

import { CustomTheme } from '@/styles/themes'
import { ButtonVariant } from '@/types/ui'

interface StyledButtonProps {
  variant: ButtonVariant
}

export const StyledButton = styled.button<StyledButtonProps>`
  padding: 16px 8px;
  border-radius: 16px;
  border: none;
  text-align: center;
  width: 100%;

  ${({ theme }) => theme.typography.styles.button};
  cursor: pointer;
  outline: none;

  &:focus {
    outline-offset: 1px;
    border-color: ${({ theme }) => theme.color.primary};
    outline: none;
    box-shadow: 0 0 0 2px ${({ theme }) => theme.color.primary}33;
  }
  &:hover {
    opacity: 0.8;
    transition: opacity 0.3s ease;
    outline: 1px solid ${({ theme }) => theme.color.primary};
  }

  ${({ variant, theme }: StyledButtonProps & { theme: CustomTheme }) => {
    switch (variant) {
      case 'next':
        return `  
        margin: 20px 0;
        background-color: ${theme.color.primary};
        color: white;
      `
      case 'slimNext':
        return `
        margin:  0;
          background-color: ${theme.color.primary};
          color: white;
        `
      case 'disabled':
        return `
        margin: 20px 0;
          background-color: ${theme.color.border};
          color: ${theme.color.text.disabled};
          cursor: not-allowed;
        `
      case 'slimDisabled':
        return `
        margin: 0;
          background-color: ${theme.color.border};
          color: ${theme.color.text.disabled};
          cursor: not-allowed;
        `
      case 'prev':
        return `
        margin: 20px 0;
          background-color: ${theme.color.secondary};
          color: ${theme.color.text.low};
        `
      case 'slimPrev':
        return `
        margin: 0;
          background-color: ${theme.color.secondary};
          color: ${theme.color.text.low};
        `
      default:
        return ''
    }
  }}
`
