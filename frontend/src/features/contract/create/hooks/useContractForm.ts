import { useState } from 'react'
import { useForm } from 'react-hook-form'

import { FieldValue } from '@/features/contract/types/contract-input'
import { useAppSelector } from '@/hooks/useAppSelector'
import { ContractRequest, Rent, Utility } from '@/types/contract'

export const useContractForm = () => {
  const contractRequest = useAppSelector(
    (state) => state.contract.contractRequest,
  )

  const { handleSubmit, watch, setValue } = useForm<ContractRequest>({
    defaultValues: contractRequest,
  })

  const [rentAccountConfirm, setRentAccountConfirm] = useState(false)
  const [cardConfirm, setCardConfirm] = useState(false)

  const handleChange = (field: string, value: FieldValue) => {
    if (field === 'rent') {
      setValue('rent', value as Rent)
    } else if (field.startsWith('rent.')) {
      const rentField = field.split('.')[1]
      const currentRent = watch('rent')
      const updatedRent = {
        ...currentRent,
        [rentField]: value,
      }

      setValue('rent', updatedRent)

      if (rentField === 'rentAccountNo') {
        setRentAccountConfirm(true)
      }
    } else if (field === 'utility.cardId') {
      setValue('utility', {
        ...watch('utility'),
        cardId: value as number | null,
      })
      setCardConfirm(true)
    } else {
      setValue(field as keyof ContractRequest, value as string | Rent | Utility)
    }
  }

  const isAfter = (type: string) => {
    if (type === 'account') return rentAccountConfirm
    if (type === 'card') return cardConfirm
    return false
  }

  return {
    handleSubmit,
    watch,
    handleChange,
    isAfter,
  } as const
}
