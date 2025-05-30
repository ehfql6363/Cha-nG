import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  width: 100%;
  background-color: ${({ theme }) => theme.color.background.white};
  padding: 0 20px 20px 20px;
`
