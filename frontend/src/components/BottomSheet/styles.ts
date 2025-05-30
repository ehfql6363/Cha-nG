import { css } from '@emotion/react'
import styled from '@emotion/styled'

import theme from '@/styles/themes'

export const overlayStyle = css`
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 1000;
`

export const bottomSheetStyle = css`
  background: white;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
  width: 100%;
  height: 100vh;
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 1001;
  display: flex;
  flex-direction: column;
  touch-action: none;
  will-change: transform;
`

export const headerStyle = css`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px 16px;
  position: relative;
  touch-action: none;
  cursor: grab;
  user-select: none;
  background: white;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;

  &:active {
    cursor: grabbing;
  }

  button {
    position: absolute;
    right: 16px;
    top: 16px;
    background: none;
    border: none;
    padding: 0;
    cursor: pointer;
  }
`

export const handleStyle = css`
  width: 32px;
  height: 4px;
  background-color: ${theme.color.border};
  border-radius: 2px;
  margin: 8px 0;
`

export const contentStyle = css`
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  padding: 0 16px 16px;
  overscroll-behavior-y: contain;
  position: relative;
  background: white;
`

export const visuallyHiddenStyle = css`
  clip: rect(0 0 0 0);
  clip-path: inset(50%);
  height: 1px;
  overflow: hidden;
  position: absolute;
  white-space: nowrap;
  width: 1px;
`

export const titleStyle = css`
  font-size: 16px;
  font-weight: bold;
  margin: 16px 0;
  color: ${theme.color.text.regular};
  ${theme.typography.styles.title}
  text-align: center;
`

export const descStyle = css`
  margin-top: 8px;
  font-size: 14px;
  text-align: center;
  color: ${theme.color.text.low};
  line-height: 1.5;
  width: 80%;
  margin: 0 auto;
  ${theme.typography.styles.default}
`

export const ButtonWrapper = styled.div`
  display: flex;
  gap: 8px;
  flex: 0;
  padding: 0 16px;
  width: 100%;
  justify-content: space-between;
  button {
    flex: 1;
  }
`
