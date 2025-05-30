'use client'

import { SwitchHandle } from './styles'
import { SwitchBar } from './styles'
import { HiddenCheckbox } from './styles'
import { SwitchWrapper } from './styles'

interface ToggleSwitchProps {
  isOn: boolean
  setIsOn: (isOn: boolean) => void
}

export function ToggleSwitch({ isOn, setIsOn }: ToggleSwitchProps) {
  return (
    <SwitchWrapper>
      <SwitchBar isOn={isOn}>
        <SwitchHandle isOn={isOn} />
      </SwitchBar>
      <HiddenCheckbox
        type="checkbox"
        checked={isOn}
        onChange={() => setIsOn(!isOn)}
      />
    </SwitchWrapper>
  )
}
