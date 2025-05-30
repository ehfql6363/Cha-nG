import styled from '@emotion/styled'
import Link from 'next/link'

export const Container = styled.div`
  padding: 1.25rem;
  display: flex;
  gap: 16px;
  width: 100%;
`

export const CardContainer = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  gap: 8px;
`

export const Card = styled(Link)`
  text-decoration: none;
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  width: 100%;
  height: 100%;
  border: none;
  align-items: center;
  border-radius: 16px;
  color: ${({ theme }) => theme.color.text.regular};
  ${({ theme }) => theme.typography.styles.topHeader}
  background-color: ${({ theme }) => theme.color.secondary};
`

export const CardDescription = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: center;
  text-align: center;
  width: 100%;
  color: ${({ theme }) => theme.color.text.low};
  ${({ theme }) => theme.typography.styles.description}
  background-color: ${({ theme }) => theme.color.background.white};
  white-space: pre-line;
  padding: 4px;
`
