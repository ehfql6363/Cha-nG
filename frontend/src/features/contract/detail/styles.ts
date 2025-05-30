import styled from '@emotion/styled'

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
  background: linear-gradient(
    39deg,
    #f1f3f6 35.86%,
    #d9deeb 66.78%,
    #c6def2 99.55%
  );
`

export const HeaderContainer = styled.div`
  padding: 40px 20px;
  margin: 1rem 0;
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  width: 100%;
  text-align: center;
  border-radius: 16px;
  background-color: ${({ theme }) => theme.color.background.white};
  opacity: 0.85;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
`

export const FullMain = styled.div`
  padding: 0 1.25rem;
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
  width: 100%;
  padding-bottom: 6.625rem;
  overflow-y: auto;
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
export const BottomContainer = styled.div`
  display: flex;
  flex-direction: row;
  position: absolute;
  bottom: 0;
  gap: 16px;
  padding: 0 20px;
  width: 100%;
  @media (min-width: 768px) {
    width: 49%;
    margin: 0 auto;
  }
`
export const PaddingContainer = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
  padding: 20px 20px;
`
