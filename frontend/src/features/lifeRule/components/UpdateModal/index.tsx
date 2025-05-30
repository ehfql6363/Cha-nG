/** @jsxImportSource @emotion/react */
import { useTranslation } from 'react-i18next'

import * as Dialog from '@radix-ui/react-dialog'

import { ConfirmButton } from '@/components/ConfirmButton'

import {
  ButtonWrapper,
  contentStyle,
  descStyle,
  overlayStyle,
  titleStyle,
} from './styles'

interface ModalProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  onConfirm: () => void
  title?: string
  description?: string
  confirmText?: string
}

export default function UpdateModal({
  open,
  onOpenChange,
  onConfirm,
}: ModalProps) {
  const { t } = useTranslation()
  return (
    <Dialog.Root
      open={open}
      onOpenChange={onOpenChange}>
      <Dialog.Portal>
        <Dialog.Overlay css={overlayStyle} />
        <Dialog.Content css={contentStyle}>
          <Dialog.Title css={titleStyle}>
            {t('lifeRule.modal.title')}
          </Dialog.Title>
          <Dialog.Description css={descStyle}>
            {t('lifeRule.modal.description')
              .split('\n')
              .map((line, idx) => (
                <p key={idx}>{line}</p>
              ))}
          </Dialog.Description>
          <ButtonWrapper>
            <ConfirmButton
              label={t('lifeRule.modal.confirmText')}
              variant={'next'}
              onClick={onConfirm}
            />
          </ButtonWrapper>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  )
}
