'use client'

import { useEffect } from 'react'
import { useMemo } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { postNotApprovedIds } from '@/apis/lifeRule'
import { Image } from '@/components'
import { useAppSelector } from '@/hooks'
import { useIsLeader } from '@/hooks'
import { setNotApprovedIds } from '@/store/slices/lifeRuleSlice'
import { setSelectedMenu } from '@/store/slices/pledgeSlice'
import { PledgeMenu } from '@/types/ui'

import {
  Container,
  LinkContainer,
  NoticeContent,
  NoticeDescription,
  NoticeItem,
  NoticeTitle,
  StyledLink,
} from './styles'

export function Notice() {
  const homeOverview = useAppSelector((state) => state.user.homeOverview)
  const livingAccountNo = useAppSelector(
    (state) => state.livingBudget.livingAccountNo,
  )
  const isLeader = useIsLeader()
  const paymentCurrent = useAppSelector((state) => state.pledge.paymentCurrent)
  const { t } = useTranslation()
  const user = useAppSelector((state) => state.user.user)
  const rentTitle = paymentCurrent.rent
    ? `payment.paymentStatus.${paymentCurrent.rent}.title`
    : ''
  const rentLabel = paymentCurrent.rent
    ? `payment.paymentStatus.${paymentCurrent.rent}.label`
    : ''

  const utilityTitle = paymentCurrent.utility
    ? `payment.paymentStatus.${paymentCurrent.utility}.title`
    : ''
  const utilityLabel = paymentCurrent.utility
    ? `payment.paymentStatus.${paymentCurrent.utility}.label`
    : ''

  const userRentTitle = paymentCurrent.userRent
    ? `payment.userPaymentStatus.${paymentCurrent.userRent}.title`
    : ''
  const userRentLabel = paymentCurrent.userRent
    ? `payment.userPaymentStatus.${paymentCurrent.userRent}.label`
    : ''

  const userUtilityTitle = paymentCurrent.userUtility
    ? `payment.userPaymentStatus.${paymentCurrent.userUtility}.title`
    : ''
  const userUtilityLabel = paymentCurrent.userUtility
    ? `payment.userPaymentStatus.${paymentCurrent.userUtility}.label`
    : ''
  const router = useRouter()
  const dispatch = useDispatch()
  const group = useAppSelector((state) => state.group.group)
  const notApprovedId: number[] = useAppSelector(
    (state) => state.lifeRule.notApprovedIds,
  )
  const shouldApprove = useMemo(
    () => notApprovedId.includes(user.id),
    [notApprovedId, user.id],
  )

  useEffect(() => {
    const fetchRent = async () => {
      const response = await postNotApprovedIds(group.id)
      if (response.success) {
        dispatch(setNotApprovedIds(response.data.notApprovedIds))
      }
    }
    fetchRent()
  }, [group, dispatch])
  return (
    <Container>
      {paymentCurrent.rent && (
        <NoticeItem>
          <Image
            src="/images/notification/notification-rent.svg"
            alt="rent"
            width={21}
            height={21}
          />
          <NoticeContent>
            <NoticeTitle>
              [{t('main.issues.rent')}]{t(rentTitle)}
            </NoticeTitle>
            <NoticeDescription>
              <span>
                {t('payment.allPrefix')}
                {t(rentLabel)}
              </span>
              <LinkContainer>
                <StyledLink
                  onClick={() => {
                    dispatch(setSelectedMenu(PledgeMenu.rent))
                    router.push('/pledge')
                  }}>
                  확인하기
                </StyledLink>
              </LinkContainer>
            </NoticeDescription>
          </NoticeContent>
        </NoticeItem>
      )}
      {paymentCurrent.userRent && (
        <NoticeItem>
          <Image
            src="/images/notification/notification-rent.svg"
            alt="userRent"
            width={21}
            height={21}
          />
          <NoticeContent>
            <NoticeTitle>
              [{t('main.issues.rent')}]{t(userRentTitle)}
            </NoticeTitle>
            <NoticeDescription>
              <span>
                {t('payment.userPrefix', { value: user.name })}
                {t(userRentLabel)}
              </span>
              <LinkContainer>
                <StyledLink
                  onClick={() => {
                    dispatch(setSelectedMenu(PledgeMenu.rent))
                    router.push('/pledge')
                  }}>
                  확인하기
                </StyledLink>
              </LinkContainer>
            </NoticeDescription>
          </NoticeContent>
        </NoticeItem>
      )}
      {paymentCurrent.utility && (
        <NoticeItem>
          <Image
            src="/images/notification/notification-utility.svg"
            alt="utility"
            width={21}
            height={21}
          />
          <NoticeContent>
            <NoticeTitle>
              [{t('main.issues.utility')}]{t(utilityTitle)}
            </NoticeTitle>
            <NoticeDescription>
              <span>
                {t('payment.allPrefix')}
                {t(utilityLabel)}
              </span>
              <LinkContainer>
                <StyledLink
                  onClick={() => {
                    dispatch(setSelectedMenu(PledgeMenu.utility))
                    router.push('/pledge')
                  }}>
                  확인하기
                </StyledLink>
              </LinkContainer>
            </NoticeDescription>
          </NoticeContent>
        </NoticeItem>
      )}
      {paymentCurrent.userUtility && (
        <NoticeItem>
          <Image
            src="/images/notification/notification-utility.svg"
            alt="userUtility"
            width={21}
            height={21}
          />
          <NoticeContent>
            <NoticeTitle>
              [{t('main.issues.utility')}]{t(userUtilityTitle)}
            </NoticeTitle>
            <NoticeDescription>
              <span>
                {t('payment.userPrefix', { value: user.name })}
                {t(userUtilityLabel)}
              </span>
              <LinkContainer>
                <StyledLink
                  onClick={() => {
                    dispatch(setSelectedMenu(PledgeMenu.utility))
                    router.push('/pledge')
                  }}>
                  확인하기
                </StyledLink>
              </LinkContainer>
            </NoticeDescription>
          </NoticeContent>
        </NoticeItem>
      )}
      {shouldApprove && (
        <NoticeItem>
          <Image
            src="/images/notification/notification-rule.svg"
            alt="생활 규칙 미승인 공지"
            width={21}
            height={21}
          />
          <NoticeContent>
            <NoticeTitle>
              [생활 규칙] 승인해야 하는 생활 규칙이 있어요
            </NoticeTitle>
            <NoticeDescription>
              <span>새로 바뀔 생활 규칙을 확인해 주세요!</span>
              <LinkContainer>
                <StyledLink
                  onClick={() => {
                    router.push('/lifeRule/updateApprove')
                  }}>
                  승인하기
                </StyledLink>
              </LinkContainer>
            </NoticeDescription>
          </NoticeContent>
        </NoticeItem>
      )}
      {!livingAccountNo && (
        <NoticeItem>
          <Image
            src="/images/notification/notification-livingBudget.svg"
            alt="생활비"
            width={21}
            height={21}
          />
          <NoticeContent>
            <NoticeTitle>생활비 계좌 개설</NoticeTitle>
            <NoticeDescription>
              <span>
                {isLeader
                  ? '생활비 계좌를 개설해서 공금을 쉽게 관리해 보세요!'
                  : '방장에게 생활비 계좌 개설을 요청해보세요!'}
              </span>
              <LinkContainer>
                <StyledLink
                  onClick={() => {
                    router.push('/budget/living')
                  }}>
                  {isLeader ? '개설하기' : '요청하기'}
                </StyledLink>
              </LinkContainer>
            </NoticeDescription>
          </NoticeContent>
        </NoticeItem>
      )}
    </Container>
  )
}
