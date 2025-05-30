import { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import styled from '@emotion/styled'

import { createAccount } from '@/apis/fintech'
import { IconButton, Image, Modal } from '@/components'
import { useAppSelector, useIsLeader } from '@/hooks'
import { setRentAccountConfirm, updateRent } from '@/store/slices/contractSlice'
import {
  BankLabel,
  ShowCenterBox,
  ValidationContainer,
  ValidationMessage,
} from '@/styles/styles'

import { AccountText } from './styles'

interface AccountInputProps {
  value?: string
  onChange?: (value: string) => void
}

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px 0;
`

export function AccountInput({ onChange }: AccountInputProps) {
  const { t } = useTranslation()
  const dispatch = useDispatch()
  const rentAccountConfirm = useAppSelector(
    (state) => state.contract.rentAccountConfirm,
  )

  const rent = useAppSelector((state) => state.contract.contractRequest.rent)
  const group = useAppSelector((state) => state.group.group)
  const isLeader = useIsLeader()
  const [next, setNext] = useState(false)
  const [open, setOpen] = useState(false)
  const disabled = rentAccountConfirm || !isLeader

  useEffect(() => {
    if (next && !disabled) {
      handleConfirm()
    }
  }, [next])

  const handleConfirm = async () => {
    const response = await createAccount()
    if (response.success) {
      const accountNo = response.data.data.accountNo
      onChange?.(accountNo)
      dispatch(
        updateRent({
          ...rent,
          rentAccountNo: accountNo,
        }),
      )
      dispatch(setRentAccountConfirm(true))
      setOpen(true)
    }
  }
  const [buttonText, setButtonText] = useState(
    t('contract.rentAccountNo.button'),
  )

  const leaderName = group.members.find(
    (info) => info.id == group.leaderId,
  )?.name

  useEffect(() => {
    const content = rentAccountConfirm
      ? t('fintech.bankName') + ' ' + rent.rentAccountNo + ' ' + leaderName
      : t('contract.rentAccountNo.button')
    setButtonText(content)
  }, [rentAccountConfirm, rent.rentAccountNo, leaderName, t])

  useEffect(() => {
    dispatch(setRentAccountConfirm(!!rent.rentAccountNo))
  }, [rent.rentAccountNo])

  return (
    <Container>
      <ShowCenterBox
        onClick={() => setNext(true)}
        isDisabled={disabled}>
        <BankLabel>
          <IconButton
            src={'/icons/logo-bank.svg'}
            alt={'bank'}
          />
          <AccountText>{buttonText}</AccountText>
        </BankLabel>
      </ShowCenterBox>
      {!isLeader && !rentAccountConfirm && (
        <ValidationContainer>
          <Image
            src={`/icons/button-invalid.svg`}
            alt={'message'}
            width={14}
            height={14}
          />
          <ValidationMessage isValid={false}>
            {t('contract.rentAccountNo.validation.leaderOnly')}
          </ValidationMessage>
        </ValidationContainer>
      )}
      <Modal
        open={open}
        onOpenChange={setOpen}
        onConfirm={() => {
          setOpen(false)
        }}
        title={t('contract.rentAccountNo.button')}
        description={t('contract.rentAccountNo.afterLabel')}
        image={'/images/etc/account-create-after.svg'}
      />
    </Container>
  )
}
