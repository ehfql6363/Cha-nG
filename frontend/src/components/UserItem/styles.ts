import { Theme, css } from '@emotion/react'
import styled from '@emotion/styled'

const getVariantStyles = (variant: 'bar' | 'tile') => css`
  justify-content: ${variant === 'bar' ? 'flex-start' : 'center'};
  align-items: center;
  flex-direction: ${variant === 'bar' ? 'row' : 'column'};
  gap: ${variant === 'bar' ? '1rem' : '0.5rem'};
`
const getNameStyles = (size: 'xs' | 'small' | 'medium' | 'large') => css`
  ${size === 'xs'
    ? '50px'
    : size === 'small'
      ? '50px'
      : size === 'medium'
        ? '77px'
        : '80px'};
`

const getSizeStyles = (
  size: 'xs' | 'small' | 'medium' | 'large',
  theme: Theme,
) =>
  size === 'xs' || size === 'small'
    ? theme.typography.styles.name
    : theme.typography.styles.default

export const ProfileContainer = styled.div<{
  variant: 'bar' | 'tile'
  size: 'xs' | 'small' | 'medium' | 'large'
}>`
  display: flex;
  color: ${({ theme }) => theme.color.text.low};
  white-space: ${({ variant }) => (variant === 'bar' ? 'nowrap' : 'normal')};
  ${({ variant }) => getVariantStyles(variant)}
  ${({ size, theme }) => getSizeStyles(size, theme)}
  gap: ${({ size }) => (size === 'xs' ? '0.5rem !important' : '0.5rem')};
  > span {
    text-align: ${({ variant }) => (variant === 'bar' ? 'left' : 'center')};
    width: ${({ size }) => getNameStyles(size)};
    word-break: break-all;
  }
`
