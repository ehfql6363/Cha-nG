import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  justify-content: end;
  gap: 4px;
  flex: 1;
  height: 100%;
  flex-direction: row;
  height: 100%;
  > div {
    display: flex;
    flex-direction: row;
    gap: 4px;
    align-items: center;
  }
`
export const ProfileContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 4px;
  width: 46px;
  height: 100%;
`
export const Name = styled.div`
  ${({ theme }) => theme.typography.styles.navigator};
  color: ${({ theme }) => theme.color.text.low};
`
