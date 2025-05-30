'use client'

import { useEffect, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import styled from '@emotion/styled'
import { useRouter, useSearchParams } from 'next/navigation'

import { depositToRentAccount } from '@/apis/payment'
import { getMySummary } from '@/apis/user'
import { Modal, TitleHeaderLayout } from '@/components'
import { useAppSelector } from '@/hooks'
import { setSummary } from '@/store/slices/userSlice'
import { DefaultLabel, Label, ShowCenterBox } from '@/styles/styles'
import { formatMoney } from '@/utils/format'

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px 0;
`

export function TransferToRentPage() {
  const searchParams = useSearchParams()
  const month = searchParams.get('month')
  const { t } = useTranslation()
  const router = useRouter()
  const dispatch = useDispatch()
  const summary = useAppSelector((state) => state.user.summary)

  useEffect(() => {
    if (summary.id == 0) {
      fetchSummary()
    }
  }, [summary])

  const fetchSummary = async () => {
    const response = await getMySummary()
    if (response.success) {
      dispatch(setSummary(response.data))
    }
  }
  const myAccountNo = useAppSelector((state) => state.user.summary.myAccountNo)
  const userName = useAppSelector((state) => state.user.user.nickname)
  const rent = useAppSelector((state) => state.contract.contract.rent)
  const user = useAppSelector((state) => state.user.user)
  const userAmount =
    rent.userPaymentInfo.find((item) => item.userId == user.id)?.amount ?? 0
  const [next, setNext] = useState(false)

  useEffect(() => {
    if (next) {
      onSubmit()
    }
  }, [next])

  const handleNext = () => {
    setNext(true)
  }
  const [success, setSuccess] = useState(false)
  const hasTransfered = useRef(false)

  const onSubmit = async () => {
    if (hasTransfered.current) return
    hasTransfered.current = true
    setSuccess(
      await depositToRentAccount({
        month: Number(month),
        withdrawalAccountNo: myAccountNo,
        transactionBalance: Number(userAmount),
      }),
    )
    hasTransfered.current = false
  }

  return (
    <TitleHeaderLayout
      title={t('payment.transfer.rent.title')}
      label={t('payment.transfer.rent.label')}
      header={t('payment.transfer.rent.header')}
      onClick={handleNext}>
      <Container>
        <Label> {t('payment.transfer.rent.unpaid', { month })}</Label>
        <ShowCenterBox>
          <DefaultLabel>{formatMoney(userAmount)}</DefaultLabel>
        </ShowCenterBox>
      </Container>
      <Modal
        open={success}
        onOpenChange={setSuccess}
        title={t('payment.transfer.rent.success.title')}
        description={t('payment.transfer.rent.success.description', {
          userName,
          balance: formatMoney(Number(userAmount)),
        })}
        confirmText={t('confirm')}
        onConfirm={() => {
          setSuccess(false)
          router.push('/pledge')
        }}
      />
    </TitleHeaderLayout>
  )
}
