import styled from '@emotion/styled'
import Link from 'next/link'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  width: 100%;
  gap: 16px;
`

export const TopContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  gap: 16px;
  ${({ theme }) => theme.typography.styles.title}
  color: ${({ theme }) => theme.color.text.regular}
`
