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
  > * {
    border-radius: 12px;
    overflow: hidden;
  }
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
  align-items: center;
  flex: 1;
  margin: 0 20px;
  background-color: ${({ theme }) => theme.color.background.white};
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
  padding: 16px 20px 0px;
`

export const TopDescription = styled.div`
  width: 100%;
  ${({ theme }) => theme.typography.styles.name};
  color: ${({ theme }) => theme.color.text.disabled};
`

export const TextContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  gap: 8px;
`

export const AmountContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  width: 100%;
  gap: 10px;

  > div {
    ${({ theme }) => theme.typography.styles.title};
    color: ${({ theme }) => theme.color.text.regular};
  }
`

export const Periond = styled.div`
  display: flex;
  border: 1px solid ${({ theme }) => theme.color.border};
  border-radius: 10px;
  padding: 4px 12px;
  ${({ theme }) => theme.typography.styles.navigator};
  color: ${({ theme }) => theme.color.text.disabled};
`
export const BoxContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  border-radius: 16px;
  padding: 20px 20px 40px;
  background-color: ${({ theme }) => theme.color.background.white};
`

export const AccountContainer = styled.div`
  padding: 0 20px;
  border-radius: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  gap: 20px;
  flex: 1;
  align-self: stretch;
  background-color: ${({ theme }) => theme.color.background.white};
`
export const AccountTitle = styled.div`
  ${({ theme }) => theme.typography.styles.default};
  color: ${({ theme }) => theme.color.text.regular};
  width: 100%;
  text-align: left;
  align-items: center;
  display: flex;
  gap: 8px;
`
export const AccountInfo = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 6px;
  width: 100%;
  text-align: center;
  padding-bottom: 20px;
  > span {
    ${({ theme }) => theme.typography.styles.default};
    color: ${({ theme }) => theme.color.text.low};
    width: 100%;
  }
  > div {
    ${({ theme }) => theme.typography.styles.heading};
    color: ${({ theme }) => theme.color.text.regular};
    width: 100%;
  }
`

export const DashBoardContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`
export const CurrentMonth = styled.span`
  ${({ theme }) => theme.typography.styles.descriptionBold};
  color: ${({ theme }) => theme.color.text.low};
  white-space: nowrap;
`
export const MonthSummary = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 12px;
  padding: 16px 20px;
  background-color: ${({ theme }) => theme.color.background.white};
`

export const MonthNavigation = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 10px;
  margin: 16px 0px;
  border-right: 1px solid ${({ theme }) => theme.color.border};
`
export const BottomContainer = styled.div`
  display: flex;
  min-height: 40px;
  height: 40px;
  width: 100%;
`
