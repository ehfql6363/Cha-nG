import localFont from 'next/font/local'

export const paperlogyRegular = localFont({
  src: '../../public/fonts/Paperlogy-4Regular.ttf',
  variable: '--font-paperlogy-regular',
  display: 'swap',
})

export const paperlogyMedium = localFont({
  src: '../../public/fonts/Paperlogy-5Medium.ttf',
  variable: '--font-paperlogy-medium',
  display: 'swap',
})

export const paperlogySemiBold = localFont({
  src: '../../public/fonts/Paperlogy-6SemiBold.ttf',
  variable: '--font-paperlogy-semi-bold',
  display: 'swap',
})

export const paperlogyBold = localFont({
  src: '../../public/fonts/Paperlogy-7Bold.ttf',
  variable: '--font-paperlogy-bold',
  display: 'swap',
})

export const fontVariables = `${paperlogyRegular.variable} ${paperlogyMedium.variable} ${paperlogySemiBold.variable} ${paperlogyBold.variable}`
