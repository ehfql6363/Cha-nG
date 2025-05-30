import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  justify-content: space-between;
  height: 100dvh;
  width: 100dvw;
  position: relative;
  @media (min-width: 768px) {
    width: 50%;
    justify-content: center;
    margin: 0 auto;
  }
`
