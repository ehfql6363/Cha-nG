import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  align-items: center;
  position: absolute;
  bottom: 5.3125rem;
  left: 0;
  right: 0;
  margin: auto;
  z-index: 1000;
  overflow: hidden;
`

export const SwitchContainer = styled.div<{ steps: number }>`
  position: relative;
  width: ${({ steps }) => `${steps * 82 + 8}px`};
  border-radius: 1.5rem;
  background-color: ${({ theme }) => theme.color.secondary};
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: auto;
  box-shadow: 0px 0px 4px 0px rgba(0, 0, 0, 0.2);
  padding: 4px;
`

export const SwitchText = styled.span<{ selected: boolean }>`
  color: ${({ selected, theme }) =>
    selected ? theme.color.background.white : theme.color.text.low};
  z-index: 1;
  ${({ theme }) => theme.typography.styles.descriptionBold};
  user-select: none;
  text-align: center;
  flex: 1;
  vertical-align: end;
  width: 80px;
  margin: 0.625rem 0;
`

export const SwitchButton = styled.div<{ step: number }>`
  position: absolute;
  left: ${({ step }) => `calc(${3 + step * 82}px)`};
  width: 84px;
  height: 2.25rem;
  background-color: ${({ theme }) => theme.color.primary};
  border-radius: 1.5rem;
  transition: left 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
`
