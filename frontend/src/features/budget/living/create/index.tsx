'use client'

import { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import styled from '@emotion/styled'
import { useRouter } from 'next/navigation'

import { createAccount } from '@/apis/fintech'
import { saveAccountAndNotify } from '@/apis/livingBudget'
import { Modal, TitleHeaderLayout } from '@/components'
import { Image } from '@/components'
import { useAppSelector, useIsLeader } from '@/hooks'
import { setLivingAccountNo } from '@/store/slices/livingBudgetSlice'
import {
  ShowCenterBox,
  ValidationContainer,
  ValidationMessage,
} from '@/styles/styles'
import { ButtonVariant } from '@/types/ui'

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px 0;
`

export function BudgetLivingCreatePage() {
  const { t } = useTranslation()
  const dispatch = useDispatch()
  const livingAccountNo = useAppSelector(
    (state) => state.livingBudget.livingAccountNo,
  )
  const router = useRouter()
  const group = useAppSelector((state) => state.group.group)
  const isLeader = useIsLeader()
  const [next, setNext] = useState(false)
  const disabled = !!livingAccountNo || !isLeader
  const [open, setOpen] = useState(false)
  useEffect(() => {
    if (next && !disabled) {
      handleConfirm()
    }
    if (livingAccountNo) {
      router.push('/budget/living')
    }
  }, [next])

  const handleConfirm = async () => {
    const response = await createAccount()
    if (response.success) {
      const accountNo = response.data.data.accountNo
      const success = await saveAccountAndNotify(accountNo)
      if (success) {
        dispatch(setLivingAccountNo(accountNo))
        setOpen(true)
      }
    }
  }

  const handleOpenChange = () => {
    router.push('/budget/living')
  }

  const [buttonText, setButtonText] = useState(
    t('contract.rentAccountNo.button'),
  )

  const leaderName = group.members.find(
    (info) => info.id == group.leaderId,
  )?.name

  useEffect(() => {
    const content = livingAccountNo
      ? t('fintech.bankName') + ' ' + livingAccountNo + ' ' + leaderName
      : '생활비 계좌 개설'
    setButtonText(content)
  }, [livingAccountNo, leaderName, t])

  const handleNext = () => {
    setNext(true)
  }

  return (
    <TitleHeaderLayout
      title="생활비"
      label={livingAccountNo ? '완료' : '개설'}
      header="생활비 계좌를 개설해요"
      onClick={handleNext}
      buttonVariant={ButtonVariant.next}>
      <Container>
        <ShowCenterBox
          onClick={() => setNext(true)}
          isDisabled={disabled}>
          {buttonText}
        </ShowCenterBox>

        {!isLeader && !livingAccountNo && (
          <ValidationContainer>
            <Image
              src={`/icons/validation-false.svg`}
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
          onConfirm={handleOpenChange}
          title="생활비 계좌 개설"
          description="생활비 계좌 개설이 완료되었어요"
          confirmText="확인"
          image="/images/etc/account-create-after.svg"
        />
      </Container>
    </TitleHeaderLayout>
  )
}
