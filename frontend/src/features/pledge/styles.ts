import styled from '@emotion/styled'

import { PledgeMenu } from '@/types/ui'

export const FullMain = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
  height: 100%;
  overflow-y: auto;
  gap: 10px;
  padding: 20px;
  overflow-y: auto;
`

export const EmptyMain = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  gap: 1rem;
  overflow-y: auto;
  position: relative;
`
export const Container = styled.div<{ variant: PledgeMenu }>`
  display: flex;
  flex-direction: column;
  flex: 1;
  justify-content: space-between;
  height: 100dvh;
  width: 100%;

  ${({ variant, theme }) => {
    switch (variant) {
      case 'account':
        return `background-color: ${theme.color.secondary};`
      case 'rent':
        return `background-color: ${theme.color.secondary};`
      case 'utility':
        return `background-color: ${theme.color.secondary};`
      case 'contract':
        return `
          background: linear-gradient(
            39deg,
            #f1f3f6 35.86%,
            #d9deeb 66.78%,
            #c6def2 99.55%
          );
        `
    }
  }}

  @media (min-width: 768px) {
    width: 50%;
    justify-content: center;
    margin: 0 auto;
  }
`
export const BoxContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  border-radius: 16px;
  padding: 20px 20px 20px;
  background-color: ${({ theme }) => theme.color.background.white};
`

export const BottomContainer = styled.div`
  display: flex;
  flex: 1;
  min-height: 40px;
  width: 100%;
`
