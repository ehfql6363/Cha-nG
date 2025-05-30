'use client'

import React, { useEffect, useRef } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { logout } from '@/apis/auth'
import { getMySummary } from '@/apis/user'
import { BottomNavigation } from '@/components/BottomNavigation'
import { TopHeader } from '@/components/TopHeader'
import { useAppSelector } from '@/hooks/useAppSelector'
import { setSummary } from '@/store/slices/userSlice'
import { resetStore } from '@/store/store'
import { SimpleMain } from '@/styles/styles'

import { Account, Profile } from './components'
import { Container } from './styles'

export function MyPage() {
  const dispatch = useDispatch()
  const { t } = useTranslation()
  const router = useRouter()
  const fetched = useRef(false)
  useEffect(() => {
    if (fetched.current) return
    fetched.current = true
    const fetchMySummary = async () => {
      try {
        const response = await getMySummary()
        if (response && 'data' in response) {
          dispatch(setSummary(response.data))
        }
      } catch (error) {
        console.error('Failed to fetch my summary:', error)
      }
    }
    fetchMySummary()
  }, [])

  const logouted = useRef(false)
  const handleLogout = async () => {
    if (logouted.current) return
    logouted.current = true
    const success = await logout()
    if (success) {
      router.push('/auth/login')
      dispatch(resetStore())
    }
  }
  const accessToken = useAppSelector(
    (state) => state.auth.loginToken.accessToken,
  )
  return (
    <>
      {accessToken ? (
        <Container>
          <TopHeader title={t('my.title')} />
          <SimpleMain>
            <Profile />
            <Account handleLogout={handleLogout} />
          </SimpleMain>
          <BottomNavigation />
        </Container>
      ) : (
        <div
          style={{
            display: 'flex',
            flexDirection: 'column',
            flex: 1,
            height: '100dvh',
            width: '100%',
          }}>
          <div
            style={{
              padding: '1.25rem',
              display: 'flex',
              flexDirection: 'column',
              minHeight: '60%',
              justifyContent: 'center',
              alignItems: 'center',
              margin: 'auto',
              width: '100%',
              overflowY: 'auto',
            }}>
            <img
              src="/icons/loading.svg"
              alt="logo"
              width={100}
              height={100}
            />
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
              }}>
              <img
                src="/icons/logo-chainG-no.svg"
                alt="logo"
                width={100}
                height={20}
              />
              <p
                style={{
                  color: '#586575',
                }}>
                가볍지만 신뢰할 수 있는 동거 서약관리 서비스
              </p>
            </div>
          </div>
        </div>
      )}
    </>
  )
}
