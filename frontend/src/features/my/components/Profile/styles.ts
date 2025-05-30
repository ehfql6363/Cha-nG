import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  gap: 16px;
  padding: 20px 20px 30px; /* top right/left bottom */
  background-color: ${({ theme }) => theme.color.background.white};
`
export const TopContainer = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;
  padding: 10px;
  justify-content: space-between;
  align-items: center;
`

export const LeftContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 20px;
`

export const ImageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  height: 100%;
`

export const IntroduceContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: start;
  gap: 8px;

  >div: first-child {
    ${({ theme }) => theme.typography.styles.description};
    color: ${({ theme }) => theme.color.text.disabled};
  }
`

export const IntroduceBottomContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: start;
  align-items: end;
  gap: 8px;

  >div: first-child {
    ${({ theme }) => theme.typography.styles.title};
    color: ${({ theme }) => theme.color.text.low};
  }
  >div: last-child {
    ${({ theme }) => theme.typography.styles.description};
    color: ${({ theme }) => theme.color.text.disabled};
  }
`

export const BottomContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  border-radius: 16px;
  border: 1px solid ${({ theme }) => theme.color.border};
  gap: 16px;
  padding: 20px 24px;
  width: 100%;
`

export const TextContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;

  >div: first-child {
    ${({ theme }) => theme.typography.styles.topHeader};
    color: ${({ theme }) => theme.color.text.low};
  }

  >div: last-child {
    ${({ theme }) => theme.typography.styles.default};
    color: ${({ theme }) => theme.color.text.disabled};
  }
`
