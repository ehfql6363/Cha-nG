import { css } from '@emotion/react'

import theme from '@/styles/themes'

export const overlayStyle = css`
  background-color: rgba(0, 0, 0, 0.4);
  position: fixed;
  inset: 0;
  animation: overlayShow 150ms cubic-bezier(0.16, 1, 0.3, 1);
`

export const contentStyle = css`
  background-color: white;
  border-radius: 16px;
  box-shadow: hsl(206 22% 7% / 35%) 0px 10px 38px -10px;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90vw;
  max-width: 450px;
  max-height: 85vh;
  padding-top: 20px;
  padding-right: 20px;
  padding-left: 20px;
  animation: contentShow 150ms cubic-bezier(0.16, 1, 0.3, 1);

  &:focus {
    outline: none;
  }
`

export const ImageWrapper = css`
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
`

export const titleStyle = css`
  text-align: center;
  padding-bottom: 16px;
  ${theme.typography.styles.title}
  color: ${theme.color.text.regular};
`

export const descStyle = css`
  line-height: 1.5;
  text-align: center;
  ${theme.typography.styles.default}
  color: ${theme.color.text.low};
  padding-bottom: 8px;
  padding-top: 8px;
`

export const ButtonWrapper = css`
  display: flex;
  gap: 8px;
  justify-content: center;
`
