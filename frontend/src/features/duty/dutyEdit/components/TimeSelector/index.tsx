'use client'

import React from 'react'
import { useTranslation } from 'react-i18next'

import { ToggleSwitch } from '@/components'
import { SwitcherContainer } from '@/styles/styles'
import { useTimeSelector } from '../../../hooks/useTimeSelector'

import { TimeInputContainer } from '../TimeInputContainer'
import { Container, TimeSwitcherContainer } from './styles'

interface TimeSelectorProps {
  time: string
  setTime: (time: string) => void
  useTime?: boolean
}

export function TimeSelector({ time, setTime, useTime = false }: TimeSelectorProps) {
  const { t } = useTranslation()
  const { isVisible, setIsVisible, time: currentTime, setTime: handleTimeChange } = useTimeSelector({
    initialTime: time,
    initialUseTime: useTime,
    onTimeChange: setTime,
  })

  return (
    <Container>
      <TimeSwitcherContainer>
        <SwitcherContainer>
          <div> {t(`duty.edit.time.title`)} </div>
          <ToggleSwitch
            isOn={isVisible}
            setIsOn={setIsVisible}
          />
        </SwitcherContainer>
      </TimeSwitcherContainer>
      {isVisible && (
        <TimeInputContainer
          time={currentTime}
          setTime={handleTimeChange}
        />
      )}
    </Container>
  )
}
