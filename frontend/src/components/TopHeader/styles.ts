import styled from '@emotion/styled'

export const Container = styled.div`
  padding: 20px 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  ${({ theme }) => theme.typography.styles.title};
  color: ${({ theme }) => theme.color.text.regular};
`

export const HeaderButton = styled.button`
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  display: flex;
  align-items: center;
  color: ${({ theme }) => theme.color.text.regular};
  gap: 8px;
  width: 24px;
  height: 24px;
`
export const HeaderTitle = styled.div`
  ${({ theme }) => theme.typography.styles.topHeader};
  color: ${({ theme }) => theme.color.text.low};
`
