import { useEffect, useMemo, useRef, useState } from 'react'

import Image from 'next/image'

import { BottomNavigation } from '@/components'
import {
  ContentWrapper,
  HeaderContainer,
  HomeMain,
  MainImageContainer,
} from '@/styles/styles'
import { useMorning } from '@/utils/formatTime'

import { Container } from './styles'

export function HomeLayout({
  header,
  children,
  headerRightButton,
  isHeaderTransparent,
  setIsHeaderTransparent,
}: {
  title?: string
  header: string
  label?: string
  children: React.ReactNode
  headerRightButton: React.ReactNode
  isHeaderTransparent: boolean
  setIsHeaderTransparent: (isHeaderTransparent: boolean) => void
}) {
  const morning = useMorning()
  const mainRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const mainElement = mainRef.current
    if (!mainElement) return

    const handleScroll = () => {
      const position = mainElement.scrollTop
      setIsHeaderTransparent(position < 50)
    }

    // 초기 스크롤 위치 설정
    handleScroll()

    mainElement.addEventListener('scroll', handleScroll, { passive: true })
    return () => {
      mainElement.removeEventListener('scroll', handleScroll)
    }
  }, [])

  return (
    <>
      <Container>
        <MainImageContainer isMorning={morning}>
          {morning ? (
            <Image
              src={'/images/home/home-morning.svg'}
              alt="home-main"
              fill
              style={{
                objectFit: 'contain',
                objectPosition: 'top',
              }}
            />
          ) : (
            <Image
              src={'/images/home/home-night.svg'}
              alt="home-main"
              fill
              style={{ objectFit: 'contain', objectPosition: 'top' }}
            />
          )}
        </MainImageContainer>
        <ContentWrapper>
          <HeaderContainer>
            <Image
              src="/icons/logo-no-padding.svg"
              alt="logo"
              width={24}
              height={24}
            />
            {headerRightButton}
          </HeaderContainer>
          <HomeMain ref={mainRef}>{children}</HomeMain>
          <BottomNavigation />
        </ContentWrapper>
      </Container>
    </>
  )
}
