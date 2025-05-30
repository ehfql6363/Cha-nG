import { ProgressBarProps } from '@/types/ui'

import {
  Bar,
  BarWrapper,
  ProgressContainer,
  StepText,
  StepsText,
} from './styles'

export const ProgressBar = ({ step, steps }: ProgressBarProps) => {
  const percent = Math.min(100, ((step + 1) / steps) * 100)

  return (
    <ProgressContainer>
      <BarWrapper>
        <Bar percent={percent} />
      </BarWrapper>
      <StepText>{step + 1}</StepText>
      <StepsText>{`/${steps}`}</StepsText>
    </ProgressContainer>
  )
}
