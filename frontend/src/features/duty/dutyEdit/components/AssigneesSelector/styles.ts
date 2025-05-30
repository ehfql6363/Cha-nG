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

export const AssigneesButtonContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: start;
  width: 100%;
  min-height: 50px;
  border-radius: 1rem;
  border: 1px solid ${({ theme }) => theme.color.border};
  background: ${({ theme }) => theme.color.secondary};
  }
`
export const Placeholder = styled.div`
  display: flex;
  justify-content: center;
  align-items: start;
  width: 100%;
  padding: 1rem;
  flex-direction: column;
  color: ${({ theme }) => theme.color.text.disabled};
  ${({ theme }) => theme.typography.styles.default}
`
