'use client'

import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { registerFCMToken, signUp } from '@/apis/auth'
import { getFCMToken, onForegroundMessage } from '@/app/firebase'
import { InputBox, TitleHeaderLayout } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import {
  clearSignUp,
  setAccessToken,
  setFCMToken,
  setSignUpPassword,
} from '@/store/slices/authSlice'
import { setUser } from '@/store/slices/userSlice'
import { ButtonVariant, ValidationItem } from '@/types/ui'

interface SignupForm {
  password: string
  confirmPassword: string
}

export function SignUpPasswordPage() {
  const { t } = useTranslation()
  const router = useRouter()
  const dispatch = useDispatch()
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<SignupForm>()

  const signUpRequest = useAppSelector((state) => state.auth.signUpRequest)
  const FCMToken = useAppSelector((state) => state.auth.FCMToken)
  const user = useAppSelector((state) => state.user.user)

  const password = watch('password')
  const confirmPassword = watch('confirmPassword')

  useEffect(() => {
    if (user?.id) {
      router.push('/onboarding')
    }
  }, [user, router])

  const [validations, setValidations] = useState<{
    [key: string]: ValidationItem
  }>({
    requiredAlphabet: {
      isValid: false,
      message: t('signUp.password.error.requiredAlphabet'),
    },
    requiredNumber: {
      isValid: false,
      message: t('signUp.password.error.requiredNumber'),
    },
    length: {
      isValid: false,
      message: t('signUp.password.error.length'),
    },
  })

  const [sameValidations, setSameValidations] = useState<{
    [key: string]: ValidationItem
  }>({
    same: {
      isValid: true,
      message: t('signUp.confirmPassword.error.match'),
    },
  })

  const isValidPassword =
    Object.values(validations).every((v) => v.isValid) &&
    password.length >= 8 &&
    password.length <= 20 &&
    password === confirmPassword

  useEffect(() => {
    if (password) {
      const newValidations = { ...validations }
      newValidations.requiredAlphabet.isValid = /[a-zA-Z]/.test(password)
      newValidations.requiredNumber.isValid = /[0-9]/.test(password)
      newValidations.length.isValid =
        password.length >= 8 && password.length <= 20
      setValidations(newValidations)

      const newSameValidations = { ...sameValidations }
      newSameValidations.same.isValid = password === confirmPassword
      setSameValidations(newSameValidations)
    }
  }, [password, confirmPassword])

  const initFCM = async () => {
    if (FCMToken) return
    const token = await getFCMToken()

    if (token) {
      await dispatchFCMToken(token)
    }
  }

  const dispatchFCMToken = async (token: string) => {
    const response = await registerFCMToken({ fcmToken: token })
    if (!response) return

    dispatch(setFCMToken(token))
    onForegroundMessage()
  }

  useEffect(() => {
    if (signUpRequest?.password) {
      const handleSignUp = async () => {
        const response = await signUp(signUpRequest)
        await dispatch(clearSignUp())
        if (response) {
          const token = response.headers['authorization']
          await dispatch(setAccessToken(token))
          await dispatch(setUser(response.data.data))
        }
      }
      handleSignUp()
    }
  }, [signUpRequest?.password, dispatch])

  const onSubmit = async (data: SignupForm) => {
    if (isValidPassword) {
      dispatch(setSignUpPassword(data.password))
    }
  }

  return (
    <TitleHeaderLayout
      header={t('signUp.password.title')}
      onClick={handleSubmit(onSubmit)}
      buttonVariant={
        isValidPassword ? ButtonVariant.next : ButtonVariant.disabled
      }
      gap="20px">
      <InputBox
        label={t('signUp.password.label')}
        id="password"
        type="password"
        placeholder={t('signUp.password.placeholder')}
        error={errors.password}
        validations={validations}
        {...register('password', {
          required: t('signUp.password.error.required'),
        })}
      />
      <InputBox
        label={t('signUp.confirmPassword.label')}
        id="confirmPassword"
        type="password"
        placeholder={t('signUp.confirmPassword.placeholder')}
        validations={sameValidations}
        {...register('confirmPassword', {
          required: t('signUp.confirmPassword.error.required'),
          validate: (value) =>
            value === watch('password') ||
            t('signUp.confirmPassword.error.match'),
        })}
        error={errors.confirmPassword}
      />
    </TitleHeaderLayout>
  )
}
