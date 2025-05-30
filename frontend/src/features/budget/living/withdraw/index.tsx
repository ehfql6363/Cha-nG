'use client'

import { useEffect, useMemo, useRef, useState } from 'react'
import { useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import styled from '@emotion/styled'
import { useRouter } from 'next/navigation'

import { notifyLivingWithdraw } from '@/apis/livingBudget'
import { getMySummary } from '@/apis/user'
import { InputBox, Modal, TitleHeaderLayout } from '@/components'
import { useAppSelector, useTransfer } from '@/hooks'
import { setSummary } from '@/store/slices/userSlice'
import { ButtonVariant } from '@/types/ui'

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px 0;
`

interface WithdrawForm {
  myAccountNo: string
  balance: string
}

export function BudgetLivingWithdrawPage() {
  const { t } = useTranslation()
  const router = useRouter()
  const dispatch = useDispatch()

  const summary = useAppSelector((state) => state.user.summary)

  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors },
  } = useForm<WithdrawForm>({
    defaultValues: {
      myAccountNo: summary.myAccountNo,
      balance: '',
    },
  })

  useEffect(() => {
    if (summary.id == 0) {
      fetchSummary()
    } else if (summary.myAccountNo) {
      setValue('myAccountNo', summary.myAccountNo)
    }
  }, [summary])

  const fetchSummary = async () => {
    const response = await getMySummary()
    if (response.success) {
      dispatch(setSummary(response.data))
    }
  }
  const livingAccountNo = useAppSelector(
    (state) => state.livingBudget.livingAccountNo,
  )
  if (!livingAccountNo) {
    router.push('/budget/living/create')
  }

  const userName = useAppSelector((state) => state.user.user.nickname)
  const myAccountNo = watch('myAccountNo')
  const balance = watch('balance')
  const [success, setSuccess] = useState(false)
  const transfer = useTransfer({
    depositAccountNo: myAccountNo,
    transactionBalance: balance,
    withdrawalAccountNo: livingAccountNo,
    depositTransactionSummary: userName + '의 생활비 꺼내기',
    withdrawalTransactionSummary: userName + '의 생활비 꺼내기',
  })
  const [next, setNext] = useState(false)
  const livingAccountDetail = useAppSelector(
    (state) => state.livingBudget.livingAccountDetail,
  )
  const disabled = useMemo(() => {
    return !!(
      !livingAccountNo ||
      !balance ||
      !myAccountNo ||
      Number(livingAccountDetail.accountBalance) < Number(balance)
    )
  }, [
    livingAccountNo,
    balance,
    myAccountNo,
    livingAccountDetail.accountBalance,
  ])

  useEffect(() => {
    if (next && !disabled) {
      handleSubmit(onSubmit)()
    }
  }, [next])

  const handleNext = () => {
    setNext(true)
  }
  const hasTransfered = useRef(false)
  const onSubmit = async (data: WithdrawForm) => {
    if (hasTransfered.current) return
    hasTransfered.current = true

    const response = await transfer()
    if (response.success) {
      setSuccess(await notifyLivingWithdraw())
    }
  }

  const handleMyAccountNoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value.replace(/[^0-9]/g, '')
    setValue('myAccountNo', newValue)
    setNext(false)
  }

  const balanceRegister = register('balance', {
    required: t('livingBudget.deposit.balance.error.required'),
  })

  const myAccountNoRegister = register('myAccountNo', {
    required: t('livingBudget.deposit.myAccountNo.error.required'),
    validate: (value) => {
      if (value === livingAccountNo) {
        return t('livingBudget.deposit.myAccountNo.error.same')
      }
      return true
    },
  })
  return (
    <TitleHeaderLayout
      title={t('livingBudget.withdraw.title')}
      label={t('livingBudget.withdraw.label')}
      header={t('livingBudget.withdraw.header')}
      onClick={handleNext}
      buttonVariant={disabled ? ButtonVariant.disabled : ButtonVariant.next}>
      <Container>
        <InputBox
          label={t('livingBudget.withdraw.myAccountNo.label')}
          id="myAccountNo"
          type="text"
          value={myAccountNo}
          onChange={handleMyAccountNoChange}
          ref={myAccountNoRegister.ref}
          placeholder={t('livingBudget.withdraw.myAccountNo.placeholder')}
          error={errors.myAccountNo}
        />
        <InputBox
          id="balance"
          name="balance"
          label={t('livingBudget.withdraw.balance.label')}
          type="money"
          value={balance}
          onChange={(e) => {
            const numeric = e.target.value.replace(/[^0-9]/g, '')
            setValue('balance', numeric)
            setNext(false)
          }}
          ref={balanceRegister.ref}
          placeholder={t('livingBudget.withdraw.balance.placeholder')}
          error={errors.balance}
        />
      </Container>
      <Modal
        open={success}
        onOpenChange={setSuccess}
        title={t('livingBudget.withdraw.success.title')}
        description={t('livingBudget.withdraw.success.description', {
          userName,
          balance,
        })}
        confirmText={t('confirm')}
        onConfirm={() => {
          setSuccess(false)
          router.push('/budget/living')
        }}
      />
    </TitleHeaderLayout>
  )
}
