import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
  gap: 50px;
  background-color: ${({ theme }) => theme.color.background.white};
`

export const FullMain = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 38px;
  width: 100%;
  background-color: ${({ theme }) => theme.color.background.white};
`
export const BottomContainer = styled.div`
  padding: 0px 20px;
  display: flex;
  width: 100%;
  flex-direction: column;
`
