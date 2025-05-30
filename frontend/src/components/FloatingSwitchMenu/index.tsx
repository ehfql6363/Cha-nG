import { useState } from 'react'

import { Container, SwitchButton, SwitchContainer, SwitchText } from './styles'

export function FloatingSwitchMenu({
  selectedMenu,
  onSwitch,
  menuList,
}: {
  selectedMenu: string
  onSwitch: (menu: string) => void
  menuList: {
    id: string
    name: string
  }[]
}) {
  const defaultStep =
    menuList.findIndex((menu) => menu.id === selectedMenu) ?? 0
  const [step, setStep] = useState(defaultStep)
  return (
    <Container>
      <SwitchContainer steps={menuList.length}>
        <SwitchButton step={step}></SwitchButton>
        {menuList.map((item, index) => (
          <SwitchText
            key={item.id}
            onClick={() => {
              setStep(index)
              onSwitch(item.id)
            }}
            selected={selectedMenu === item.id}>
            {item.name}
          </SwitchText>
        ))}
      </SwitchContainer>
    </Container>
  )
}
