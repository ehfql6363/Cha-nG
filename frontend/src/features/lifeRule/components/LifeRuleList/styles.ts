import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  width: 100%;
  gap: 12px;
  background-color: ${({ theme }) => theme.color.background.white};
  padding: 6px;
`
