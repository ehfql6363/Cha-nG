/** ProgressBar.tsx */
import styled from '@emotion/styled'

export const ProgressContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
`

export const BarWrapper = styled.div`
  flex: 1;
  height: 8px;
  background-color: ${({ theme }) => theme.color.border};
  border-radius: 4px;
  overflow: hidden;
  margin-right: 8px;
`

export const Bar = styled.div<{ percent: number }>`
  height: 100%;
  background-color: ${({ theme }) => theme.color.primary};
  border-radius: 4px;
  width: ${({ percent }) => percent}%;
  transition: width 0.3s ease;
`

export const StepText = styled.span`
  font-size: 12px;
  color: ${({ theme }) => theme.color.primary};
`

export const StepsText = styled.span`
  font-size: 12px;
  color: ${({ theme }) => theme.color.text.disabled};
`
