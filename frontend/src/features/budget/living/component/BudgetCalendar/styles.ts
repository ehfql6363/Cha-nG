import styled from '@emotion/styled'

export const Container = styled.div`
  ${({ theme }) => theme.typography.styles.description} !important;
  background-color: ${({ theme }) => theme.color.background.white};
`

export const MonthSummary = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid ${({ theme }) => theme.color.border};
  border-radius: 12px;
  padding: 16px 12px;
  margin: 20px;
`

export const MonthNavigation = styled.div`
  display: flex;
  align-items: center;
  gap: 4px;
  border-right: 1px solid ${({ theme }) => theme.color.border};
`

export const NavArrow = styled.span`
  cursor: pointer;
  color: #999;
  font-size: 16px;
`

export const CurrentMonth = styled.span`
  ${({ theme }) => theme.typography.styles.descriptionBold};
  color: ${({ theme }) => theme.color.text.low};
`

export const ExpenseSummary = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4px;
  justify-content: center;
  align-items: center;
  width: 100%;
`

export const ExpenseLabel = styled.div`
  color: ${({ theme }) => theme.color.text.disabled};
  ${({ theme }) => theme.typography.styles.default};
  padding: 0 10px;
  white-space: nowrap;
`

export const ExpenseAmount = styled.div`
  color: ${({ theme }) => theme.color.text.low};
  ${({ theme }) => theme.typography.styles.topHeader};
  white-space: pre-wrap;
`

export const StyledFullCalendar = styled.div`
  .fc {
    font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};
    border: none;
    background: none;
  }

  .fc-theme-standard td,
  .fc-theme-standard th {
    border: none;
  }

  .fc-theme-standard .fc-scrollgrid {
    border: none;
  }

  .fc .fc-scrollgrid-section-header > *,
  .fc .fc-scrollgrid-section-body > * {
    border-right-width: 0;
  }

  .fc .fc-daygrid-day-events {
    max-height: 1.5rem;
    min-height: 0.5rem;
    overflow: hidden;
  }
  .fc .fc-h-event {
    border: none;
    background-color: transparent;
  }
  .fc .fc-scrollgrid-section > * {
    border-bottom-width: 0;
  }

  .fc .fc-col-header-cell {
    padding: 1rem 0;
  }

  .fc .fc-col-header-cell-cushion {
    font-family: ${({ theme }) => theme.typography.fonts.paperlogySemiBold};
    color: #666;
    width: 50px;
    display: inline-block;
    text-decoration: none;
  }

  /* 토요일 색상 */
  .fc-day-sat .fc-col-header-cell-cushion,
  .fc-day-sat .day-number {
    color: #5388cd !important;
  }

  /* 일요일 색상 */
  .fc-day-sun .fc-col-header-cell-cushion,
  .fc-day-sun .day-number {
    color: #e74c3c !important;
  }

  .fc-daygrid-day {
    cursor: pointer;
  }

  .fc .fc-daygrid-day.fc-day-today {
    background-color: transparent;
    color: ${({ theme }) => theme.color.primary} !important;
  }

  /* 이전/다음 달 날짜 스타일 */
  .fc-day-other .day-number {
    opacity: 0.3;
  }

  /* 날짜 셀 크기 조정 */
  .fc-daygrid-day-frame {
    text-align: center;
    > div {
      justify-content: center;
    }
  }

  .fc .fc-daygrid-body-balanced .fc-daygrid-day-events {
    margin: 0;
  }

  /* 날짜 셀 hover 효과 */
  .fc-daygrid-day:hover {
    background-color: #f0f0f0;
  }
`

export const DayCell = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2px;
`

export const DayNumber = styled.div`
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  ${({ theme }) => theme.typography.styles.name}
  color: ${({ theme }) => theme.color.text.regular};
  margin-bottom: 5px;
`

export const EventAmount = styled.div`
  font-size: 0.5625rem;
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};
  color: ${({ theme }) => theme.color.text.regular};
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
`
