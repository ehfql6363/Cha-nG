'use client'

import { PropsWithChildren, useEffect } from 'react'
import { Provider } from 'react-redux'

import { persistStore } from 'redux-persist'
import { PersistGate } from 'redux-persist/integration/react'

import ErrorModal from '@/components/ErrorModal'
import { store } from '@/store/store'

import { EmotionProvider } from './emotionProvider'
import { I18nProvider } from './i18nProvider'

function LoadingFallback() {
  useEffect(() => {
    if ('serviceWorker' in navigator) {
      navigator.serviceWorker.register('/service-worker.js').then(
        (registration) => {
          console.log('ServiceWorker 등록 성공:', registration)
        },
        (err) => {
          console.error('ServiceWorker 등록 실패:', err)
        },
      )
    }
  }, [])
  return (
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
            gap: '10px',
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
  )
}

export function Providers({ children }: PropsWithChildren) {
  const persistor = persistStore(store)

  return (
    <EmotionProvider>
      <Provider store={store}>
        <PersistGate
          loading={<LoadingFallback />}
          persistor={persistor}>
          <ErrorModal />
          <I18nProvider>{children}</I18nProvider>
        </PersistGate>
      </Provider>
    </EmotionProvider>
  )
}
