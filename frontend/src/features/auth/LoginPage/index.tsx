'use client'

import React from 'react'
import { useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { login, registerFCMToken } from '@/apis/auth'
import { getFCMToken, onForegroundMessage } from '@/app/firebase'
import { AnimatedImage, ConfirmButton, InputBox } from '@/components'
import { useAppSelector } from '@/hooks'
import { setAccessToken, setFCMToken } from '@/store/slices/authSlice'
import { setUser } from '@/store/slices/userSlice'
import {
  CenterContainer,
  Container,
  Form,
  Main,
  SlimContainer,
} from '@/styles/styles'
import { ImageVariant } from '@/types/ui'

import { SignupLinkContainer, StyledLink } from './styles'

interface LoginForm {
  emailAddress: string
  password: string
}

export function LoginPage() {
  const { t } = useTranslation()
  const router = useRouter()
  const dispatch = useDispatch()
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginForm>()

  const FCMToken = useAppSelector((state) => state.auth.FCMToken)

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

  const onSubmit = async (data: LoginForm) => {
    const response = await login(data)
    if (response) {
      const token = response.headers['authorization']
      await Promise.all([
        dispatch(setAccessToken(token)),
        dispatch(setUser(response.data.data)),
        initFCM(),
      ])
      router.replace('/')
    }
  }

  return (
    <Container>
      <Main>
        <Form onSubmit={handleSubmit(onSubmit)}>
          <SlimContainer>
            <CenterContainer>
              <AnimatedImage
                src="/icons/logo-no-padding.svg"
                alt="logo"
                width={100}
                height={100}
                variant={ImageVariant.bounce}
              />
            </CenterContainer>
            <div
              style={{
                display: 'flex',
                alignItems: 'center',
                flexDirection: 'column',
                justifyContent: 'center',
                width: '100%',
                textAlign: 'center',
                fontFamily: 'var(--font-paperlogy-medium)',
                color: 'var(--color-text-regular)',
                gap: '10px',
              }}>
              <AnimatedImage
                src="/icons/logo-chainG-no.svg"
                alt="logo"
                width={120}
                height={20}
                variant={ImageVariant.bounce}
              />
              <div>
                <p
                  style={{
                    color: '#586575',
                  }}>
                  가볍지만 신뢰할 수 있는 동거 서약관리 서비스
                </p>
              </div>
            </div>
          </SlimContainer>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
            <InputBox
              label={t('login.email.label')}
              id="emailAddress"
              type="email"
              placeholder={t('login.email.placeholder')}
              {...register('emailAddress', {
                required: t('login.email.error.required'),
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: t('login.email.error.invalidEmail'),
                },
              })}
              error={errors.emailAddress}
            />
            <InputBox
              id="password"
              label={t('login.password.label')}
              type="password"
              placeholder={t('login.password.placeholder')}
              {...register('password', {
                required: t('login.password.error.required'),
                minLength: {
                  value: 6,
                  message: t('login.password.error.length'),
                },
              })}
              error={errors.password}
            />
            <ConfirmButton
              onClick={handleSubmit(onSubmit)}
              label={'login.title'}
            />
            <SignupLinkContainer>
              <StyledLink href="/auth/signup">{t('signUp.title')}</StyledLink>
            </SignupLinkContainer>
          </div>
        </Form>
      </Main>
    </Container>
  )
}
