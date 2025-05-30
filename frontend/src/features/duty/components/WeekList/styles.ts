import styled from '@emotion/styled'
import Link from 'next/link'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  width: 100%;
  gap: 16px;
  background-color: ${({ theme }) => theme.color.background.white};
  padding-right: 20px;
  padding-left: 20px;
`

export const TopContainer = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: flex-start;
  gap: 4px;
  ${({ theme }) => theme.typography.styles.default}
  color: ${({ theme }) => theme.color.text.disabled}
`

export const TextContainer = styled.div`
  display: flex;
  width: 100%;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  position: relative;
  > div {
    ${({ theme }) => theme.typography.styles.title}
    color: ${({ theme }) => theme.color.text.regular}
  }
`
