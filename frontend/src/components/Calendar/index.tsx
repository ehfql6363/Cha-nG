import { useState } from 'react'
import ReactDatePicker from 'react-datepicker'
import { registerLocale } from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'

import styled from '@emotion/styled'
import ko from 'date-fns/locale/ko'

import { ConfirmButton } from '@/components'
import { RegularLabel, ShowBox } from '@/styles/styles'

import { BottomSheet } from '../BottomSheet'

registerLocale('ko', ko)

type Props = {
  value: Date | null
  onChange: (date: Date | null) => void
  onConfirm?: (date: Date | null) => void
}

export const Calendar = ({ value, onChange, onConfirm }: Props) => {
  const [isOpen, setIsOpen] = useState(false)
  const handleConfirm = () => {
    onConfirm?.(value)
    setIsOpen(false)
  }

  return (
    <>
      <ShowBox onClick={() => setIsOpen(!isOpen)}>
        <RegularLabel>{value?.toLocaleDateString()}</RegularLabel>
      </ShowBox>
      <BottomSheet
        open={isOpen}
        onOpenChange={setIsOpen}
        snapPoints={{
          MIN: 0.3,
          MID: 0.6,
          MAX: 0.6,
        }}>
        <CalendarContainer>
          <DatePickerWrapper>
            <ReactDatePicker
              selected={value}
              onChange={(d: Date | null) => onChange(d)}
              dateFormat="yyyy-MM-dd"
              locale="ko"
              placeholderText="날짜 선택"
              inline
              calendarStartDay={1}
            />
          </DatePickerWrapper>
          <ConfirmButton
            onClick={handleConfirm}
            variant="next"
            label="확인"
          />
        </CalendarContainer>
      </BottomSheet>
    </>
  )
}

const CalendarContainer = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 55vh;
`

const DatePickerWrapper = styled.div`
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
