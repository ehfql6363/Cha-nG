'use client'

import { useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { TitleHeaderLayout } from '@/components'
import { InputBox } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import { setSignUpName } from '@/store/slices/authSlice'
import { ButtonVariant } from '@/types/ui'

interface SignupForm {
  name: string
}

export function SignUpNamePage() {
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
      name: signUp.name || '',
    },
  })

  const currentName = watch('name')

  const onSubmit = async (data: SignupForm) => {
    dispatch(setSignUpName(data.name))
    router.push('/auth/signup/password')
  }

  return (
    <TitleHeaderLayout
      header={t('signUp.name.title')}
      onClick={handleSubmit(onSubmit)}
      buttonVariant={currentName ? ButtonVariant.next : ButtonVariant.disabled}>
      <InputBox
        label={t('signUp.name.label')}
        id="name"
        type="text"
        error={errors.name}
        placeholder={t('signUp.name.placeholder')}
        {...register('name', {
          required: t('signUp.name.error.required'),
          minLength: {
            value: 2,
            message: t('signUp.name.error.minLength'),
          },
        })}
      />
    </TitleHeaderLayout>
  )
}
