import styled from '@emotion/styled'

export const AccountHistoryContainer = styled.div`
  display: flex;
  width: 100%;
  flex: 1;
  flex-direction: column;
  justify-content: space-between;
  > span {
    width: 2.125rem;
    padding-top:0.0625rem;
    padding-bottom:0.5rem;
    ${({ theme }) => theme.typography.styles.description};
    color: ${({ theme }) => theme.color.text.low};
`
export const AccountHistoryContent = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  * {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
`
export const Container = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: 0.5rem;
  background-color: ${({ theme }) => theme.color.background.white};
  width: 100%;
  height: 100%;
`
export const ButtonContainer = styled.div`
  display: flex;
  flex-direction: row;
  gap: 0.5rem;
  background-color: ${({ theme }) => theme.color.background.white};
  padding: 0 20px;
`
export const ContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  background-color: ${({ theme }) => theme.color.background.white};
`

export const SelectContainer = styled.ul`
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  background-color: ${({ theme }) => theme.color.background.white};
  padding: 10px 10px 0px 20px;
`
export const SelectButton = styled.li<{ isSelected: boolean }>`
  border: none;
  white-space: nowrap;
  width: 2.15rem;
  margin-left: 0.5rem;
  ${({ theme }) => theme.typography.styles.name};
  color: ${({ isSelected, theme }) =>
    isSelected ? theme.color.primary : theme.color.text.low};
  background-color: ${({ theme }) => theme.color.background.white};
`

export const DateContainer = styled.div`
  border: none;
  white-space: nowrap;
  width: 100%;
  text-align: end;
  ${({ theme }) => theme.typography.styles.name};
  color: ${({ theme }) => theme.color.text.low};
  background-color: ${({ theme }) => theme.color.background.white};
  padding: 20px 20px 0px;
`
export const PaddingContainer = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
  padding: 20px;
  margin-bottom: 50px;
`
