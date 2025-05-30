import styled from '@emotion/styled'
import Link from 'next/link'

export const CardContainer = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  gap: 8px;
`

export const Card = styled(Link)`
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
  height: 100%;
  border: none;
  align-items: center;
  border-radius: 16px;
  color: ${({ theme }) => theme.color.text.low};
  ${({ theme }) => theme.typography.styles.topHeader}
  background-color: ${({ theme }) => theme.color.secondary};
`

export const DefaultLabel = styled.div`
  ${({ theme }) => theme.typography.styles.description};
  color: ${({ theme }) => theme.color.text.low};
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
`

export const CardDescription = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: right;
  text-align: right;
  width: 100%;
  ${({ theme }) => theme.typography.styles.default};
  background-color: ${({ theme }) => theme.color.background.white};
  white-space: nowrap;
  color: ${({ theme }) => theme.color.text.regular};
`
export const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  flex: 1;
  margin: 0 20px;
  padding: 16px;
  border-radius: 16px;
  box-shadow: 0px 0px 20px 0px rgba(118, 118, 118, 0.25);

  > div {
    display: flex;
    flex: 1;
    width: 100%;
    justify-content: space-between;
  }
`
