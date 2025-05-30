'use client'

import { useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { InputBox, TitleHeaderLayout } from '@/components'
import { useAppSelector } from '@/hooks'
import { setSignUpEmail } from '@/store/slices/authSlice'
import { ButtonVariant } from '@/types/ui'

interface SignupForm {
  emailAddress: string
}

export function SignUpPage() {
  const { t } = useTranslation()
  const router = useRouter()
  const dispatch = useDispatch()
  const signUp = useAppSelector((state) => state.auth.signUpRequest)
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<SignupForm>({
    defaultValues: {
      emailAddress: signUp?.emailAddress || '',
    },
  })
  const currentEmail = watch('emailAddress')
  const onSubmit = async (data: SignupForm) => {
    dispatch(setSignUpEmail(data.emailAddress))
    router.push('/auth/signup/name')
  }

  return (
    <TitleHeaderLayout
      header={t('signUp.email.title')}
      onClick={handleSubmit(onSubmit)}
      buttonVariant={
        currentEmail ? ButtonVariant.next : ButtonVariant.disabled
      }>
      <InputBox
        label={t('signUp.email.label')}
        id="emailAddress"
        type="email"
        placeholder={t('signUp.email.placeholder')}
        error={errors.emailAddress}
        {...register('emailAddress', {
          required: t('signUp.email.error.required'),
          pattern: {
            value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
            message: t('signUp.email.error.invalidEmail'),
          },
        })}
      />
    </TitleHeaderLayout>
  )
}
