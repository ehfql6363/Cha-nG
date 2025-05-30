'use client'

import { PropsWithChildren } from 'react'

import { Providers } from '@/providers/providers'

export function ClientProvider({ children }: PropsWithChildren) {
  return <Providers>{children}</Providers>
}
