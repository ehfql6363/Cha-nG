import 'react-datepicker/dist/react-datepicker.css'

import styled from '@emotion/styled'

export const CalendarContainer = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 55vh;
`

export const DatePickerWrapper = styled.div`
  ${({ theme }) => theme.typography.styles.description} !important;
  .react-datepicker {
    border: none;
    background: none;
    font-family: inherit;
    width: 100%;
    ${({ theme }) => theme.typography.styles.description} !important;
  }

  .react-datepicker__month-container {
    width: 100%;
  }

  .react-datepicker__header {
    background: none;
    border: none;
    padding: 1rem;
    ${({ theme }) => theme.typography.styles.descriptionBold} !important;
  }

  .react-datepicker__day-name {
    width: 50px;
    line-height: 40px;
    margin: 0;
    color: #666;
    ${({ theme }) => theme.typography.styles.descriptionBold} !important;
    &:nth-child(6) {
      color: ${({ theme }) => theme.color.text.saturday};
    }
    &:nth-child(7) {
      color: ${({ theme }) => theme.color.text.sunday};
    }
  }

  .react-datepicker__current-month {
    font-size: 16px;
    margin-bottom: 10px;
    ${({ theme }) => theme.typography.styles.description} !important;
  }

  .react-datepicker__day {
    width: 50px;
    line-height: 40px;
    margin: 0;
    color: #333;
    ${({ theme }) => theme.typography.styles.description} !important;
    &:nth-child(6) {
      color: ${({ theme }) => theme.color.text.saturday};
    }
    &:nth-child(7) {
      color: ${({ theme }) => theme.color.text.sunday};
    }
    &:hover {
      background-color: #f0f0f0;
    }
  }

  .react-datepicker__day--selected {
    background-color: ${({ theme }) => theme.color.primary};
    color: white;
    font-weight: bold;

    &:hover {
      background-color: ${({ theme }) => theme.color.primary};
    }
  }

  .react-datepicker__day--keyboard-selected {
    background-color: ${({ theme }) => theme.color.primary};
    color: white;
    ${({ theme }) => theme.typography.styles.descriptionBold} !important;
  }

  .react-datepicker__day--outside-month {
    ${({ theme }) => theme.typography.styles.description} !important;
    opacity: 0.3;
  }

  .react-datepicker__navigation {
    top: 8px;
  }
`
