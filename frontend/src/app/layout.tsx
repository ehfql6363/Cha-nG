// src/app/layout.tsx
import { Metadata } from 'next'

import { ClientProvider } from '@/providers/clientProvider'
import { fontVariables } from '@/styles/fonts'

import './globals.css'

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="ko">
      <head>
        <link
          rel="manifest"
          href="/manifest.webmanifest"
        />
      </head>
      <body className={fontVariables}>
        <ClientProvider>{children}</ClientProvider>
      </body>
    </html>
  )
}

export const metadata: Metadata = {
  title: 'Chain G',
  description: '가볍지만 신뢰할 수 있는 동거 서약관리 서비스',
}
