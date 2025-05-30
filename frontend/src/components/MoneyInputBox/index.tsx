import { useState } from 'react'
import { FieldValues } from 'react-hook-form'
import { UseFormRegister } from 'react-hook-form'
import { useTranslation } from 'react-i18next'

import { InputBox } from '@/components'

interface MoneyInputBoxProps {
  value?: number
  onChange?: (value: number) => void
  register?: UseFormRegister<FieldValues>
}

export function MoneyInputBox({ value, onChange }: MoneyInputBoxProps) {
  const { t } = useTranslation()
  const [inputValue, setInputValue] = useState(value?.toString() || '')

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value.replace(/[^0-9]/g, '')
    setInputValue(newValue)
    onChange?.(Number(newValue))
  }

  return (
    <InputBox
      id="rent"
      type="money"
      value={inputValue}
      onChange={handleChange}
      placeholder={t('contract.rent.placeholder')}
    />
  )
}
