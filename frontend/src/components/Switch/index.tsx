import styled from '@emotion/styled'

interface SwitchProps {
  checked?: boolean
  onChange?: (checked: boolean) => void
  onText?: string
  offText?: string
}

const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`

const SwitchContainer = styled.div<{ checked: boolean }>`
  position: relative;
  width: 120px;
  height: 38px;
  border-radius: 16px;
  background-color: ${({ theme }) => theme.color.border};
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  margin: auto;
`

const SwitchText = styled.span<{ checked: boolean }>`
  color: ${({ checked, theme }) =>
    checked ? theme.color.background.white : theme.color.text.low};
  z-index: 1;
  ${({ theme }) => theme.typography.styles.name};
  user-select: none;
`

const SwitchButton = styled.div<{ checked: boolean }>`
  position: absolute;
  top: 3px;
  left: ${({ checked }) => (!checked ? 'calc(100% - 60px)' : '4px')};
  width: 57px;
  height: 32px;
  background-color: ${({ theme }) => theme.color.primary};
  border-radius: 14px;
  transition: left 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
`

export function Switch({
  checked = false,
  onChange,
  onText = '비율',
  offText = '금액',
}: SwitchProps) {
  return (
    <Container>
      <SwitchContainer checked={checked}>
        <SwitchText
          checked={checked}
          onClick={() => onChange?.(true)}>
          {onText}
        </SwitchText>
        <SwitchText
          checked={!checked}
          onClick={() => onChange?.(false)}>
          {offText}
        </SwitchText>
        <SwitchButton checked={checked} />
      </SwitchContainer>
    </Container>
  )
}
