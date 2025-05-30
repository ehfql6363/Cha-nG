'use client'

import { forwardRef, memo } from 'react'
import { FieldError } from 'react-hook-form'

import { Image } from '@/components'

import {
  InputContainer,
  Label,
  StyledInput,
  ValidationContainer,
  ValidationMessage,
  ValidationWrapper,
} from './styles'

interface ValidationItem {
  isValid: boolean
  message: string
}

interface InputBoxProps {
  id: string
  type?: string
  error?: FieldError
  value?: string
  placeholder?: string
  label?: string
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void
  className?: string
  disabled?: boolean
  required?: boolean
  validations?: ValidationItem[]
}

const InputBoxBase = forwardRef<HTMLInputElement, InputBoxProps>(
  (
    {
      error,
      placeholder = '',
      label,
      id,
      className,
      disabled,
      required,
      validations,
      ...props
    },
    ref,
  ) => {
    const isError = !!error
    const message = error?.message

    return (
      <InputContainer className={className}>
        {label && (
          <Label htmlFor={id}>
            {label}
            {required && <span className="required">*</span>}
          </Label>
        )}
        <StyledInput
          id={id}
          ref={ref}
          disabled={disabled}
          required={required}
          {...props}
          placeholder={placeholder}
          aria-invalid={isError}
          aria-describedby={message ? `${id}-error` : undefined}
        />
        {!isError && validations && (
          <ValidationWrapper>
            {validations.map((validation, index) => (
              <ValidationContainer key={index}>
                <Image
                  src={`/icons/validation-${validation.isValid ? 'true' : 'false'}.svg`}
                  alt={
                    validation.isValid ? '유효성 검사 통과' : '유효성 검사 실패'
                  }
                  width={14}
                  height={14}
                />
                <ValidationMessage isValid={validation.isValid}>
                  {validation.message}
                </ValidationMessage>
              </ValidationContainer>
            ))}
          </ValidationWrapper>
        )}
        {isError && message && (
          <ValidationContainer id={`${id}-error`}>
            <Image
              src={`/icons/validation-false.svg`}
              alt={message}
              width={14}
              height={14}
            />
            <ValidationMessage isValid={false}>{message}</ValidationMessage>
          </ValidationContainer>
        )}
      </InputContainer>
    )
  },
)

InputBoxBase.displayName = 'InputBoxBase'

export const InputBox = memo(InputBoxBase)
