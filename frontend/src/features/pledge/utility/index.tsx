'use client'

import React from 'react'

import { AnimatedImage } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import {
  Description,
  EmptyContainer,
  HomeMain,
  ShowCenterBox,
  SimpleMain,
  SlimContainer,
  Title,
} from '@/styles/styles'
import { ImageVariant } from '@/types/ui'

import { BottomContainer, EmptyMain } from '../styles'
import { Graph } from './components/Graph'
import { Payment } from './components/Payment'
import { Stats } from './components/Stats'
import { Step } from './components/Step'

export function UtilityPage() {
  const utilityInfo = useAppSelector((state) => state.pledge.utility)
  const cardId = useAppSelector(
    (state) => state.contract.contract.utility.cardId,
  )
  return (
    <>
      {cardId && (
        <>
          <Payment />
          {utilityInfo?.weekList && utilityInfo?.weekList.length != 0 && (
            <Graph data={utilityInfo.weekList} />
          )}
          <Stats />
          <Step />
          <BottomContainer />
        </>
      )}
      {cardId == null && (
        <EmptyMain>
          <EmptyContainer>
            <ShowCenterBox isDisabled>
              <SlimContainer>
                <AnimatedImage
                  src="/images/pledge/account-no.svg"
                  alt="공과금을 이용할 수 없어요"
                  width={80}
                  height={80}
                  variant={ImageVariant.bounce}
                />
                <Title>공과금을 이용할 수 없어요</Title>
                <Description>월세로 한정된 서약서를 사용 중이에요</Description>
              </SlimContainer>
            </ShowCenterBox>
          </EmptyContainer>
        </EmptyMain>
      )}
    </>
  )
}
