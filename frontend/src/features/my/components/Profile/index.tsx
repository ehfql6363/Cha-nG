'use client'

import React from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { ConfirmButton, Image } from '@/components'
import { ErrorModalButtonTypes } from '@/constants/errors'
import { useAppSelector } from '@/hooks/useAppSelector'
import { useInstallPrompt } from '@/hooks/useInstallPrompt'
import { setErrorModal } from '@/store/slices/errorModalSlice'
import { ButtonVariant } from '@/types/ui'

import {
  BottomContainer,
  ImageContainer,
  IntroduceBottomContainer,
  IntroduceContainer,
  LeftContainer,
  TopContainer,
} from './styles'
import { Container, TextContainer } from './styles'

export function Profile() {
  const { t } = useTranslation()
  const router = useRouter()
  const dispatch = useDispatch()
  const user = useAppSelector((state) => state.user)

  const { promptInstall, isSupported } = useInstallPrompt()
  const handleEdit = () => {
    router.push('/my/edit')
  }

  const handleClick = async () => {
    if (!isSupported) return
    const result = await promptInstall()
    console.log('result:', result)
    if (result?.outcome === 'accepted') {
      dispatch(
        setErrorModal({
          modalTitle: '앱 설치 성공',
          modalContent: '앱으로 접근해보세요',
          primaryButtonType: ErrorModalButtonTypes.confirm,
          secondaryButtonType: null,
          isVisible: false,
          useI18n: false,
        }),
      )
    } else {
      dispatch(
        setErrorModal({
          modalTitle: '앱 설치 실패',
          modalContent: '정상적이지 못한 접근입니다.',
          primaryButtonType: ErrorModalButtonTypes.confirm,
          secondaryButtonType: null,
          isVisible: false,
          useI18n: false,
        }),
      )
    }
  }

  return (
    <Container>
      <TopContainer>
        <LeftContainer>
          <Image
            src={`/images/profile/${user.user.profileImage}.svg`}
            alt={user.user.name}
            width={60}
            height={60}
            errorSrc={`/images/profile/user${user.user.id % 9}.svg`}
          />
          <IntroduceContainer>
            <div>{t('my.profile.group')}</div>
            <IntroduceBottomContainer>
              <div>{user.user.nickname}</div>
              <div>{t('my.profile.introduce')}</div>
            </IntroduceBottomContainer>
          </IntroduceContainer>
        </LeftContainer>
        <ImageContainer onClick={handleEdit}>
          <Image
            src="/icons/arrow-right.svg"
            alt="edit"
            width={24}
            height={24}
          />
        </ImageContainer>
      </TopContainer>
      <BottomContainer>
        <TextContainer>
          <div>{t('my.profile.name')}</div>
          {user?.user?.name && <div>{user.user.name}</div>}
        </TextContainer>
        <TextContainer>
          <div>{t('my.profile.email')}</div>
          {user?.summary?.emailAddress && (
            <div>{user.summary.emailAddress}</div>
          )}
        </TextContainer>
      </BottomContainer>
      <ConfirmButton
        onClick={() => {
          router.push('/blockChain')
        }}
        label="블록체인 가이드 바로가기"
        variant="slimPrev"
      />
      <ConfirmButton
        onClick={handleClick}
        label="Cha:nG 앱 설치하기"
        variant={
          !isSupported ? ButtonVariant.slimDisabled : ButtonVariant.slimNext
        }
      />
    </Container>
  )
}
