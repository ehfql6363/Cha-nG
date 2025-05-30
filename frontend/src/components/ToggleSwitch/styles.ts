import styled from '@emotion/styled'

export const SwitchWrapper = styled.label`
  display: flex;
  cursor: pointer;
  align-items: center;
  height: 1.5rem;
  // border: 1px solid purple;
`

export const HiddenCheckbox = styled.input`
  display: none;
`

export const SwitchBar = styled.span<{ isOn: boolean }>`
  display: flex;
  width: 2.5rem;
  height: 100%;
  border-radius: 1.25rem;
  align-items: center;
  background-color: ${({ isOn, theme }) =>
    isOn ? theme.color.primary : theme.color.border};
  position: relative;
  transition: background-color 0.3s;
`

export const SwitchHandle = styled.span<{ isOn: boolean }>`
  position: absolute;
  left: ${({ isOn }) => (isOn ? '1.125rem' : '0.125rem')};
  width: 1.25rem;
  height: 1.25rem;
  border-radius: 50%;
  background-color: white;
  transition: left 0.3s;
`
