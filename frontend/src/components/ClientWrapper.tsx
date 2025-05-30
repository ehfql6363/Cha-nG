'use client'

import { PropsWithChildren, Suspense } from 'react'

import dynamic from 'next/dynamic'

const ClientWrapper = dynamic(
  () => import('@/providers/clientProvider').then((mod) => mod.ClientProvider),
  {
    ssr: false,
    loading: () => <div>Loading...</div>,
  },
)

export default function ClientWrapperComponent({
  children,
}: PropsWithChildren) {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <ClientWrapper>{children}</ClientWrapper>
    </Suspense>
  )
}
