import styled from '@emotion/styled'

export const InputContainer = styled.div`
  position: relative;
  margin-bottom: 1rem;
  width: 100%;
`

export const ValidationWrapper = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
`

export const ValidationMessage = styled.div<{ isValid: boolean }>`
  display: flex;
  align-items: center;
  gap: 4px;
  ${({ theme }) => theme.typography.styles.description};
  color: ${({ isValid, theme }) =>
    isValid ? theme.color.text.regular : theme.color.text.distructive};
`

export const ValidationContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 4px;
  margin: 0.5rem 0;
`

export const StyledInput = styled.input`
  width: 100%;
  padding: 1rem;
  border: 1px solid ${({ theme }) => theme.color.border};
  border-radius: 1rem;
  ${({ theme }) => theme.typography.styles.default};
  background: ${({ theme }) => theme.color.secondary};
  transition: all 0.2s ease-in-out;

  &::placeholder {
    ${({ theme }) => theme.typography.styles.default};
    color: ${({ theme }) => theme.color.text.disabled};
  }

  &:focus {
    border-color: ${({ theme }) => theme.color.primary};
    outline: none;
    box-shadow: 0 0 0 2px ${({ theme }) => theme.color.primary}33;

    &::placeholder {
      color: transparent;
    }
  }

  &:-webkit-autofill {
    ${({ theme }) => theme.typography.styles.default} !important;
    color: ${({ theme }) => theme.color.text.regular} !important;
  }
`

export const Label = styled.label`
  display: block;
  margin-bottom: 8px;
  ${({ theme }) => theme.typography.styles.inputBoxTitle};
  color: ${({ theme }) => theme.color.text.regular};
`

export const ErrorMessage = styled.p`
  font-size: 12px;
  color: ${({ theme }) => theme.color.text.distructive};
  display: flex;
  align-items: center;
  gap: 4px;
  ${({ theme }) => theme.typography.styles.description};
`
