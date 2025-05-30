import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  gap: 16px;
  ${({ theme }) => theme.typography.styles.title}
  color: ${({ theme }) => theme.color.text.regular}
`

export const WeekSelectorItemContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: start;
  width: 100%;
`

export const TimeSwitcherContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
`
