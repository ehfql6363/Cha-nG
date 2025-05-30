'use client'

import { useTranslation } from 'react-i18next'

import { CustomTimePicker, IconButton } from '@/components'

import { Container, TimePickerContainer, TopContainer } from './styles'

interface TimeInputContainerProps {
  time: string
  setTime: (time: string) => void
}

export function TimeInputContainer({ time, setTime }: TimeInputContainerProps) {
  const { t } = useTranslation()
  return (
    <Container>
      <TopContainer>
        <IconButton
          src="/icons/time-clock.svg"
          alt="time-icon"
        />
        <div> {t(`duty.edit.time.description`)} </div>
      </TopContainer>
      <hr />
      <TimePickerContainer>
        <CustomTimePicker
          time={time}
          setTime={setTime}
        />
      </TimePickerContainer>
    </Container>
  )
}
