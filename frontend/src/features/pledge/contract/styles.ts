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
  padding: 40px 20px 00px;
  margin: 0rem 0 1rem;
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
  > div:nth-child(1) {
    margin-bottom: 10px;
  }
  > div:nth-child(2) {
    margin-bottom: 10px;
    > div {
      padding: 8px;
    }
  }
`

export const FullMain = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: flex-start;
  gap: 16px;
  width: 100%;
  padding-bottom: 6.625rem;
  margin-bottom: 3rem;
  height: 100%;
  gap: 1rem;
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
  flex-direction: column;
  position: absolute;
  bottom: 0;
  padding: 0 20px;
  width: 100%;
`
export const EmptyContainer = styled.div`
  display: flex;
  flex: 1;
  min-height: 60px;
  width: 100%;
`
