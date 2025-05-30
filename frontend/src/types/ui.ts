import { ImageProps as NextImageProps } from 'next/image'

export interface ValidationItem {
  isValid: boolean
  message: string
}

export const ButtonVariant = {
  next: 'next',
  disabled: 'disabled',
  prev: 'prev',
  slimNext: 'slimNext',
  slimPrev: 'slimPrev',
  slimDisabled: 'slimDisabled',
} as const

export type ButtonVariant =
  | 'next'
  | 'disabled'
  | 'prev'
  | 'reject'
  | 'approve'
  | 'slimNext'
  | 'slimPrev'
  | 'slimDisabled'

export interface CardItem {
  url: string
  image: string
  title: string
  description: string
  children?: React.ReactNode
}
export interface SnapPoints {
  MIN: number
  MID: number
  MAX: number
}

export interface Menu {
  id: string
  name: string
}

export type MenuContent = {
  title: string
  onSelect: () => void
  color?: string
  disabled?: boolean
}

export interface ProgressBarProps {
  step: number
  steps: number
}

export const PledgeMenu = {
  contract: 'contract',
  account: 'account',
  rent: 'rent',
  utility: 'utility',
} as const

export type PledgeMenu = 'contract' | 'account' | 'rent' | 'utility'

export const LivingMenu = {
  calendar: 'calendar',
  history: 'history',
} as const

export type LivingMenu = 'calendar' | 'history'

export interface ImageProps extends NextImageProps {
  errorSrc?: string
  styles?: React.CSSProperties
}

export const ImageVariant = {
  pop: 'pop',
  slice: 'slice',
  fade: 'fade',
  slideLeft: 'slide-left',
  slideRight: 'slide-right',
  slideUp: 'slide-up',
  slideDown: 'slide-down',
  zoomIn: 'zoom-in',
  zoomOut: 'zoom-out',
  flip: 'flip',
  bounce: 'bounce',
  rotate: 'rotate',
  blur: 'blur',
  scale: 'scale',
  sliceFade: 'slice-fade',
} as const

export type ImageVariant =
  | 'pop'
  | 'slice'
  | 'fade'
  | 'slide-left'
  | 'slide-right'
  | 'slide-up'
  | 'slide-down'
  | 'zoom-in'
  | 'zoom-out'
  | 'flip'
  | 'bounce'
  | 'rotate'
  | 'blur'
  | 'scale'
  | 'slice-fade'

export interface AnimatedImageProps extends ImageProps {
  variant?: ImageVariant
}
