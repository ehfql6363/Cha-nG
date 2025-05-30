'use client'

import React from 'react'

import { AnimatedImage } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import {
  Description,
  EmptyContainer,
  ShowCenterBox,
  SlimContainer,
  Title,
} from '@/styles/styles'
import { ImageVariant } from '@/types/ui'

import { BottomContainer, EmptyMain } from '../styles'
import { Graph } from './components/Graph'
import { Payment } from './components/Payment'
import { Stats } from './components/Stats'
import { Step } from './components/Step'

export function RentPage() {
  const rentInfo = useAppSelector((state) => state.pledge?.rent)

  return (
    <>
      {rentInfo?.currentMonth && rentInfo?.currentMonth.length > 0 && (
        <>
          <Payment />
          <Graph data={rentInfo?.currentMonth} />
          <Stats />
          <Step />
          <BottomContainer />
        </>
      )}
      {rentInfo?.currentMonth == null && (
        <EmptyMain>
          <EmptyContainer>
            <ShowCenterBox isDisabled>
              <SlimContainer>
                <AnimatedImage
                  src="/images/pledge/account-no.svg"
                  alt="월세 로딩 중이에요"
                  width={80}
                  height={80}
                  variant={ImageVariant.bounce}
                />
                <Title>월세 로딩 중이에요</Title>
                <Description>월세 데이터 호출까지 기다려주세요</Description>
              </SlimContainer>
            </ShowCenterBox>
          </EmptyContainer>
        </EmptyMain>
      )}
    </>
  )
}
