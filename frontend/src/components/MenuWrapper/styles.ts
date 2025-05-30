import { css } from '@emotion/react'
import styled from '@emotion/styled'
import { Menu } from '@headlessui/react'

import { CustomTheme } from '@/styles/themes'

type ColorPath = keyof CustomTheme['color']['text']

const getColorValue = (
  color: ColorPath | string,
  theme: CustomTheme,
): string => {
  return theme.color.text[color as keyof CustomTheme['color']['text']] ?? color
}

export const TriggerWrapper = styled.div`
  display: inline-block;
  position: relative;
`

export const MenuItems = styled(Menu.Items)`
  position: absolute;
  right: 0;
  margin-top: 8px;
  min-width: 160px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  z-index: 100;
  right: 0;
  transform: translateX(-10%);
  max-width: calc(100vw - 16px);
  word-break: keep-all;
  z-index: 100;
`

export const StyledItem = styled.button<{ color?: ColorPath | string }>`
  width: 100%;
  text-align: left;
  padding: 10px 16px;
  background: none;
  border: none;
  border-bottom: 1px solid ${({ theme }) => theme.color.border};
  cursor: pointer;

  &:last-child {
    border-bottom: none;
  }

  ${({ theme }) => theme.typography.styles.name};
  ${({ color, theme }) =>
    color &&
    css`
      color: ${getColorValue(color, theme)};
    `}

  &:hover:enabled {
    background-color: #f5f5f5;
  }

  &:disabled {
    opacity: 0.4;
    cursor: not-allowed;
  }
`
