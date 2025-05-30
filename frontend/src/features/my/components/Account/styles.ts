import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  padding: 30px 20px 0px; /* top right/left bottom */
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: ${({ theme }) => theme.color.background.white};
`

export const TopContainer = styled.div`
  display: flex;
  width: 100%;
  gap: 10px;
  flex-direction: column;
  align-items: center;
  width: 100%;
`

export const TitleTextContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  ${({ theme }) => theme.typography.styles.name};
  color: ${({ theme }) => theme.color.text.disabled};
`

export const ContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  padding: 0px 20px;

  > hr {
    width: 100%;
    height: 1px;
    background-color: ${({ theme }) => theme.color.border};
    border: none;
  }
`

export const TextContainer = styled.div`
  display: flex;
  gap: 4px;
  flex-direction: column;
  align-items: start;
  padding: 20px 0px;
  // border-bottom: 1px solid ${({ theme }) => theme.color.border};
  width: 100%;

  > div:first-child {
    ${({ theme }) => theme.typography.styles.name};
    color: ${({ theme }) => theme.color.text.disabled};
  }

  > div:last-child {
    ${({ theme }) => theme.typography.styles.default};
    color: ${({ theme }) => theme.color.text.low};
  }
`
