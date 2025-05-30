import isPropValid from '@emotion/is-prop-valid'
import styled from '@emotion/styled'
import Link from 'next/link'

export const Container = styled.div`
  display: flex;
  justify-content: space-between;
  height: 75px;
  padding: 0 20px;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  background-color: ${({ theme }) => theme.color.background.white};
  filter: drop-shadow(0px -1px 20px rgba(0, 0, 0, 0.1));
`

export const NavItem = styled.div<{ isActive: boolean }>`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 4px;
  width: 100%;
  margin: 0 20px;
  color: ${({ isActive, theme }) =>
    isActive ? theme.color.primary : theme.color.text.disabled};
`

export const IconName = styled.span`
  ${({ theme }) => theme.typography.styles.navigator};
`
