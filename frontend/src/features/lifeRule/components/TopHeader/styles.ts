import styled from '@emotion/styled'

export const Container = styled.div`
  padding: 20px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`

export const HeaderButton = styled.button`
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  color: ${({ theme }) => theme.color.text.regular};
  display: flex;
  align-items: center;
  gap: 8px;
`
export const Title = styled.div`
  ${({ theme }) => theme.typography.styles.title};
  color: ${({ theme }) => theme.color.text.regular};
`
