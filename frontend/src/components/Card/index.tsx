import { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { createCard } from '@/apis/fintech'
import { IconButton, Image, Modal } from '@/components'
import { useAppSelector, useIsLeader } from '@/hooks'
import {
  setCardConfirm,
  setUseUtilityCard,
  updateUtility,
} from '@/store/slices/contractSlice'
import {
  BankLabel,
  DefaultContainer,
  ShowCenterBox,
  ValidationContainer,
  ValidationMessage,
} from '@/styles/styles'

interface AccountInputProps {
  value?: string | null
  onChange?: (value: string) => void
  label?: string
  isConfirmed?: boolean
  onConfirm?: () => void
}

export function Card({ onChange }: AccountInputProps) {
  const { t } = useTranslation()
  const dispatch = useDispatch()
  const cardConfirm = useAppSelector((state) => state.contract.cardConfirm)
  const utility = useAppSelector(
    (state) => state.contract.contractRequest.utility,
  )
  const useUtilityCard = useAppSelector(
    (state) => state.contract.useUtilityCard,
  )
  const rent = useAppSelector((state) => state.contract.contractRequest.rent)
  const isLeader = useIsLeader()
  const [next, setNext] = useState(false)
  const [open, setOpen] = useState(false)
  const disabled = cardConfirm || !isLeader
  const [buttonText, setButtonText] = useState(t('contract.utility.button'))
  useEffect(() => {
    if (next && !disabled) {
      handleConfirm()
    }
  }, [next])
  useEffect(() => {
    if (utility.cardId) {
      dispatch(setUseUtilityCard(true))
    }
  }, [utility.cardId])

  const handleConfirm = async () => {
    const response = await createCard({ accountNo: rent.rentAccountNo })
    if (response.success) {
      const cardId = response.data.id
      onChange?.(cardId)
      dispatch(
        updateUtility({
          cardId: Number(cardId),
        }),
      )
      setOpen(true)
      dispatch(setCardConfirm(true))
    }
  }
  useEffect(() => {
    if (utility.cardId) {
      dispatch(setCardConfirm(true))
    }
  }, [utility.cardId])
  useEffect(() => {
    const content = cardConfirm
      ? t('fintech.bankName') + ' ' + t('fintech.cardName')
      : t('contract.utility.button')
    setButtonText(content)
  }, [cardConfirm, t])
  return (
    <>
      {useUtilityCard && (
        <DefaultContainer>
          <ShowCenterBox
            onClick={() => setNext(true)}
            isDisabled={disabled}>
            <BankLabel>
              <IconButton
                src={'/icons/logo-card.svg'}
                alt={'card'}
              />
              {buttonText}
            </BankLabel>
          </ShowCenterBox>
          {!isLeader && !cardConfirm && (
            <ValidationContainer>
              <Image
                src={`/icons/validation-false.svg`}
                alt={'message'}
                width={14}
                height={14}
              />
              <ValidationMessage isValid={false}>
                {t('contract.utility.validation.leaderOnly')}
              </ValidationMessage>
            </ValidationContainer>
          )}
          <Modal
            open={open}
            onOpenChange={setOpen}
            onConfirm={() => {
              setOpen(false)
            }}
            title="카드 개설"
            description="카드 개설이 완료되었어요"
            image="/images/etc/logo-card-big.svg"
          />
        </DefaultContainer>
      )}
    </>
  )
}
