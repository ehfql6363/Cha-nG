'use client'

import { useState } from 'react'
import { useTranslation } from 'react-i18next'

import Image from 'next/image'
import { useRouter } from 'next/navigation'

import {
  AnimatedImage,
  BottomSheet,
  CardButton,
  ConfirmButton,
  IconButton,
} from '@/components'
import { AnimatedImageWrapper } from '@/components/AnimatedImage/styles'
import { HeaderButton } from '@/components/TopHeader/styles'
import {
  BottomContainer,
  Container,
  DefaultContainer,
  TextCenterContainer,
  Title,
} from '@/styles/styles'
import { CardItem, ImageVariant } from '@/types/ui'

import {
  Dot,
  HeaderContainer,
  ImageContainer,
  LittleTitle,
  Main,
  StepControl,
  TitleContainer,
} from './styles'

export function StartPage() {
  const router = useRouter()
  const { t } = useTranslation()
  const [step, setStep] = useState(0)
  const [isBottomSheetOpen, setIsBottomSheetOpen] = useState(false)

  const stepContent = [
    {
      id: 'blockchain',
      title: t('onboarding.blockChain'),
      image: '/images/onboarding/onboarding-blockchain.png',
    },
    {
      id: 'transfer',
      title: t('onboarding.transfer'),
      image: '/images/onboarding/onboarding-transfer.png',
    },
    {
      id: 'budget',
      title: t('onboarding.budget'),
      image: '/images/onboarding/onboarding-budget.png',
    },
    {
      id: 'awkward',
      title: t('onboarding.awkward'),
      image: '/images/onboarding/onboarding-awkward.png',
    },
    {
      id: 'start',
      title: t('onboarding.start'),
      image: '/images/onboarding/onboarding-start.png',
    },
  ]

  const cardItems: CardItem[] = [
    {
      url: '/group/create',
      image: '/images/group/group-create.svg',
      title: t('onboarding.create.title'),
      description: t('onboarding.create.description'),
    },
    {
      url: '/group/join',
      image: '/images/group/group-join.svg',
      title: t('onboarding.join.title'),
      description: t('onboarding.join.description'),
    },
  ]
  const isConfirmButtonVisible = stepContent[step].id === 'start'

  const handleNext = () => {
    if (isConfirmButtonVisible) {
      setIsBottomSheetOpen(true)
    } else {
      setStep(step + 1)
    }
  }
  const handleBack = () => {
    if (step > 0) {
      setStep(step - 1)
    } else {
      router.back()
    }
  }

  return (
    <Container>
      <HeaderContainer>
        <IconButton
          src="/icons/arrow-left.svg"
          alt={t('icon.back')}
          onClick={handleBack}
        />
        <Image
          src="/icons/logo-chainG.svg"
          alt="logo"
          width={90}
          height={24}
        />
        <HeaderButton />
      </HeaderContainer>
      <Main>
        <ImageContainer>
          <AnimatedImageWrapper
            key={step}
            variant={ImageVariant.flip}>
            <Image
              src={stepContent[step].image}
              alt={stepContent[step].title}
              fill
              style={{
                objectFit: 'contain',
              }}
              unoptimized={true}
            />
          </AnimatedImageWrapper>
          <div onClick={() => setStep(Math.max(step - 1, 0))}></div>
          <div
            onClick={() =>
              setStep(Math.min(step + 1, stepContent.length - 1))
            }></div>
        </ImageContainer>

        <StepControl>
          {stepContent.map((_, i) => (
            <Dot
              key={i}
              active={i === step}
              onClick={() => setStep(i)}
            />
          ))}
        </StepControl>
      </Main>

      <BottomContainer>
        <ConfirmButton
          onClick={handleNext}
          label={isConfirmButtonVisible ? 'onboarding.confirm' : 'next'}
        />
      </BottomContainer>
      <BottomSheet
        open={isBottomSheetOpen}
        onOpenChange={setIsBottomSheetOpen}
        snapPoints={{
          MIN: 0.1,
          MID: 0.45,
          MAX: 0.45,
        }}>
        <DefaultContainer>
          <TextCenterContainer>
            <Title>{t('onboarding.title')}</Title>
          </TextCenterContainer>
          <CardButton cardItems={cardItems} />
        </DefaultContainer>
      </BottomSheet>
    </Container>
  )
}
