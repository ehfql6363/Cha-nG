'use client'

import { PropsWithChildren, useState } from 'react'

import createCache from '@emotion/cache'
import { ThemeProvider } from '@emotion/react'
import { CacheProvider } from '@emotion/react'

import theme from '@/styles/themes'

export function EmotionProvider({ children }: PropsWithChildren) {
  const [cache] = useState(() => {
    const cache = createCache({ key: 'css' })
    cache.compat = true
    return cache
  })

  return (
    <CacheProvider value={cache}>
      <ThemeProvider theme={theme}>{children}</ThemeProvider>
    </CacheProvider>
  )
}
