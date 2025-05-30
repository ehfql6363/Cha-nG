'use client'

import { ButtonContainer, StyledButton } from './styles'

interface ApproveButtonProps {
  onApprove?: () => void
  onReject?: () => void
  disabled?: boolean
}

export const ApproveButton = ({
  onApprove,
  onReject,
  disabled = false,
}: ApproveButtonProps) => {
  return (
    <ButtonContainer>
      <StyledButton
        variant="reject"
        onClick={onReject}
        disabled={disabled}>
        거부
      </StyledButton>
      <StyledButton
        variant="approve"
        onClick={onApprove}
        disabled={disabled}>
        승인
      </StyledButton>
    </ButtonContainer>
  )
}
