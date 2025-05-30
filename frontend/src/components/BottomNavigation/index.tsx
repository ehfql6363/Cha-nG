'use client'

import React from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch, useSelector } from 'react-redux'

import { useRouter } from 'next/navigation'

import { IconButton } from '@/components'
import { ErrorModalButtonTypes } from '@/constants/errors'
import { useAppSelector } from '@/hooks/useAppSelector'
import { setErrorModal } from '@/store/slices/errorModalSlice'
import { setSelectedNavItem } from '@/store/slices/uiSlice'
import { RootState } from '@/store/store'
import { ContractStatus } from '@/types/contract'
import { NavItemKey, NavItemVariant } from '@/types/nav'

import { Container, IconName, NavItem } from './styles'

export const BottomNavigation = () => {
  const dispatch = useDispatch()
  const selectedNavItem = useSelector(
    (state: RootState) => state.ui.selectedNavItem,
  )
  const contract = useAppSelector((state) => state.contract.contract)
  const { t } = useTranslation()
  const router = useRouter()
  const handleClick = (
    variant: (typeof NavItemVariant)[NavItemKey],
    key: NavItemKey,
  ) => {
    if (contract.status === ContractStatus.confirmed || key != 'pledge') {
      dispatch(setSelectedNavItem(variant))
      router.push(`/${key === 'home' ? '' : key}`)
    } else {
      dispatch(
        setErrorModal({
          modalTitle: '서약 관리 접근 불가',
          modalContent: '먼저 서약서를 생성해주세요',
          primaryButtonType: ErrorModalButtonTypes.confirm,
          secondaryButtonType: ErrorModalButtonTypes.goToHome,
          isVisible: true,
          useI18n: false,
        }),
      )
    }
  }

  const navKeys = Object.keys(NavItemVariant) as NavItemKey[]

  const isActive = (variant: (typeof NavItemVariant)[NavItemKey]) =>
    variant === selectedNavItem

  const getIconSrc = (variant: NavItemKey) =>
    `/icons/nav/nav-${variant}-${isActive(NavItemVariant[variant]) ? '' : 'in'}active.svg`

  return (
    <Container>
      {navKeys.map((key) => {
        const variant = NavItemVariant[key]
        return (
          <React.Fragment key={key}>
            <NavItem
              onClick={() => handleClick(variant, key)}
              isActive={isActive(variant)}>
              <IconButton
                src={getIconSrc(key)}
                alt={t(variant)}
              />
              <IconName>{t(variant)}</IconName>
            </NavItem>
          </React.Fragment>
        )
      })}
    </Container>
  )
}
