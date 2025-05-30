import { css } from '@emotion/react'
import styled from '@emotion/styled'

import theme from '@/styles/themes'

export const overlayStyle = css`
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 1000;
`

export const contentStyle = css`
  background: white;
  border-radius: 16px;
  width: 90vw;
  position: fixed;
  top: 50%;
  left: 50%;
  min-height: 30vh;
  transform: translate(-50%, -50%);
  z-index: 1001;
  word-break: keep-all;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  @media (min-width: 768px) {
    width: 45vw;
    margin: 0 auto;
  }
`

export const titleStyle = css`
  font-size: 16px;
  font-weight: bold;
  margin: 24px 0 4px 0;
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
  ${theme.typography.styles.description}
  padding: 16px 0 0px 0;
`

export const ButtonWrapper = styled.div`
  display: flex;
  gap: 8px;
  flex: 0;
  padding: 8px 16px 0 16px;
  width: 100%;
  justify-content: space-between;
  button {
    flex: 1;
  }
`
export const ImageContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  > img {
    width: 80px;
    height: 80px;
    margin: 20px 30px 10px 30px;
  }
`
