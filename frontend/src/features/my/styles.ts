import styled from '@emotion/styled'

export const FullMain = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
  width: 100%;
  height: 100%;
  background-color: ${({ theme }) => theme.color.secondary};
  overflow-y: auto;
`
export const Container = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  justify-content: space-between;
  height: 100dvh;
  width: 100%;
  @media (min-width: 768px) {
    width: 50%;
    justify-content: center;
    margin: 0 auto;
  }
`