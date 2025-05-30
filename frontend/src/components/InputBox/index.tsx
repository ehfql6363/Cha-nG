'use client'

import { forwardRef, memo, useEffect, useState } from 'react'
import { FieldError } from 'react-hook-form'

import { Image } from '@/components'
import { ValidationItem } from '@/types/ui'
import { formatMoney, parseMoney } from '@/utils/format'

import {
  InputContainer,
  Label,
  StyledInput,
  ValidationContainer,
  ValidationMessage,
  ValidationWrapper,
} from './styles'

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
  validations?: { [key: string]: ValidationItem }
  name?: string
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
      type,
      value,
      onChange,
      name,
      ...props
    },
    ref,
  ) => {
    const isError = !!error
    const message = error?.message
    const [displayValue, setDisplayValue] = useState(value || '')
    const [isFocused, setIsFocused] = useState(false)

    useEffect(() => {
      if (type === 'money' && value) {
        if (isFocused) {
          setDisplayValue(parseMoney(value))
        } else {
          setDisplayValue(formatMoney(value))
        }
      } else {
        setDisplayValue(value || '')
      }
    }, [value, type, isFocused])

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      if (type === 'money') {
        const numericValue = parseMoney(e.target.value)
        setDisplayValue(numericValue)

        if (onChange) {
          const event = {
            ...e,
            target: {
              ...e.target,
              value: numericValue,
            },
          }
          onChange(event)
        }
      } else {
        setDisplayValue(e.target.value)
        onChange?.(e)
      }
    }

    const handleFocus = () => {
      setIsFocused(true)
      if (type === 'money' && value) {
        setDisplayValue(parseMoney(value))
      }
    }

    const handleBlur = () => {
      setIsFocused(false)
      if (type === 'money' && value) {
        setDisplayValue(formatMoney(value))
      }
    }

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
          value={displayValue}
          onChange={handleChange}
          onFocus={handleFocus}
          onBlur={handleBlur}
          type={type}
          placeholder={placeholder}
          aria-invalid={isError}
          aria-describedby={message ? `${id}-error` : undefined}
          name={name}
          {...props}
        />
        {!isError && validations && (
          <ValidationWrapper>
            {Object.entries(validations || {}).map(([key, validation]) => (
              <ValidationContainer key={key}>
                <Image
                  src={`/icons/button-${validation.isValid ? 'valid' : 'invalid'}.svg`}
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
