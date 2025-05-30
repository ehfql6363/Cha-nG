import styled from '@emotion/styled'

export const AccountHistoryContainer = styled.div`
  display: flex;
  width: 100%;
  flex: 1;
  justify-content: space-between;
  > span {
    width: 2.125rem;
    padding-top:0.0625rem;
    ${({ theme }) => theme.typography.styles.description};
    color: ${({ theme }) => theme.color.text.low};
`
export const AccountHistoryContent = styled.div`
  display: flex;
  flex-direction: column;
  width: calc(100% - 2.125rem);
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
  width: 100%;
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
  border-top: 8px solid ${({ theme }) => theme.color.secondary};
`
export const EmptyContainer = styled.div`
  display: flex;
  flex: 1;
  height: 60px;
  width: 100%;
  border: 10px solid red;
`
