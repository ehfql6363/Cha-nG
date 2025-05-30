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
export const BottomContainer = styled.div`
  margin: 0 20px;
  display: flex;
  gap: 16px;
`
export const DraftLabel = styled.div`
  ${({ theme }) => theme.typography.styles.default};
  color: ${({ theme }) => theme.color.text.low};
  align-items: right;
  display: flex;
  white-space: nowrap;
  ${({ theme }) => theme.typography.styles.accountAndCardNum};
  padding: 4px 8px;
  border-radius: 8px;
  width: 74px;
  border: 1px solid ${({ theme }) => theme.color.border};
`
export const HeaderPaddingTitle = styled.div`
  padding: 0 0 0 48px;
`
export const UserContainer = styled.div`
  position: relative;
`
