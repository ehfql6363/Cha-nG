'use client'

import React from 'react'
import { useTranslation } from 'react-i18next'
import Picker from 'react-mobile-picker'

import { Colon, PickerItem, PickerWrapper } from './style'

export const CustomPicker = <T extends Record<string, string>>({
  handleChange,
  pickerValue,
  selections,
}: {
  handleChange: (value: T, key: string) => void
  pickerValue: T
  selections: Record<keyof T, string[]>
}) => {
  const { t } = useTranslation()
  return (
    <PickerWrapper
      role="group"
      aria-label="picker"
      tabIndex={0}>
      <Picker
        value={pickerValue}
        onChange={handleChange}
        wheelMode="natural">
        {(Object.keys(selections) as Array<keyof T>).map((name, index) => (
          <React.Fragment key={String(name)}>
            {index !== 0 && String(name) !== 'ampm' && (
              <Colon key={`${String(name)}-colon`}>{t('picker.colon')}</Colon>
            )}
            <Picker.Column
              key={String(name)}
              name={String(name)}>
              {selections[name].map((option: string, index: number) => {
                const isSelected = pickerValue[name] === option
                return (
                  <Picker.Item
                    key={`${option}-${index}`}
                    value={option}>
                    <PickerItem selected={isSelected}>{t(option)}</PickerItem>
                  </Picker.Item>
                )
              })}
            </Picker.Column>
          </React.Fragment>
        ))}
      </Picker>
    </PickerWrapper>
  )
}
