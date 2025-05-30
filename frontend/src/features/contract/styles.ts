import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
  background-color: ${({ theme }) => theme.color.background.white};
`

export const FullMain = styled.div`
  padding: 1.25rem;
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: flex-start;
  gap: 60px;
  width: 100%;
`

export const Navigator = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  border: 1px solid;
  height: 75px;
  gap: 60px;
  width: 100%;
`
