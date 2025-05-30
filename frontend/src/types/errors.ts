import { ErrorModalButtonTypes } from '@/constants/errors'

export type ErrorModalButtonType =
  (typeof ErrorModalButtonTypes)[keyof typeof ErrorModalButtonTypes]

export interface ErrorModalProps {
  modalTitle: string
  modalContent: string
  primaryButtonType: ErrorModalButtonType | null
  secondaryButtonType: ErrorModalButtonType | null
  isVisible: boolean
  useI18n: boolean
}
