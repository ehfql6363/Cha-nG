import styled from '@emotion/styled'

import { BudgetStatus } from '@/types/budget'

export const ContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  gap: 30px;
`

export const TopDescription = styled.div`
  width: 100%;
  ${({ theme }) => theme.typography.styles.name};
  color: ${({ theme }) => theme.color.text.disabled};
`

export const StepContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  gap: 16px;
`

export const StepItem = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  gap: 16px;
  > div span {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
`
export const BarContainerWrapper = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  position: relative;
`

export const BarContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: space-between;
  min-width: 50%;
  padding: 0 14px;
  height: 10px;
  border-radius: 24px;
  background-color: ${({ theme }) => theme.color.secondary};
`

export const StatusIcon = styled.div<{ variant: BudgetStatus }>`
  width: 14px;
  height: 14px;
  border-radius: 50%;
  ${({ variant, theme }) => {
    switch (variant) {
      case 'complete':
        return `
          background-color: ${theme.color.text.confirm};
        `
      case 'debt':
        return `
          background-color: ${theme.color.text.distructive};
        `
      case 'expected':
        return `
          background-color: ${theme.color.primary};
        `
      case 'none':
        return `
          background-color: ${theme.color.secondary};
        `
    }
  }}
`

export const StatusBarContainer = styled.div`
  display: flex;
  flex-direction: row-reverse;
  justify-content: space-between;
  position: absolute;
  top: 0px;
  left: 14px; /* BarWrapper의 좌우 padding과 동일하게 */
  right: 14px;
`

export const StatusContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 14px;
  gap: 4px;

  > div {
    ${({ theme }) => theme.typography.styles.tiny};
    color: ${({ theme }) => theme.color.text.disabled};
  }
`

// 상단 월 라벨 영역
export const MonthLabelsContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 0;
  position: absolute;
  bottom: 24px;
  padding: 0 12px;
  > div {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: end;
  }
`
export const MonthLabel = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: end;
`

export const MonthText = styled.div`
  font-size: 8px;
  color: ${({ theme }) => theme.color.text.low};
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};
  text-align: center;
  white-space: nowrap;
`

export const MonthContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-end;
  margin-left: auto;
  min-width: 50%;
  padding: 0 14px;
  width: 70%;
  min-height: 10px;
`

export const BottomContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  width: 100%;
`
