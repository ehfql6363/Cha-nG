/** @jsxImportSource @emotion/react */
import * as Dialog from '@radix-ui/react-dialog'

import { ModalConfirmButton } from '@/components'
import { Image } from '@/components'

import {
  ButtonWrapper,
  ImageContainer,
  contentStyle,
  descStyle,
  overlayStyle,
  titleStyle,
} from './styles'

interface ModalProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  onConfirm: () => void
  onCancel?: () => void
  title?: string
  description?: string
  confirmText?: string
  children?: React.ReactNode
  image?: string
  disablePrev?: boolean
}

export function Modal({
  open,
  onOpenChange,
  onConfirm,
  title = '당번 삭제',
  description = '서약서를 임시저장할까요?\n서약서의 내용을 그룹원들이 서로 확인할 수 있어요.',
  confirmText = '확인',
  children,
  disablePrev = false,
  image,
  onCancel,
}: ModalProps) {
  return (
    <Dialog.Root
      open={open}
      onOpenChange={onOpenChange}>
      <Dialog.Portal>
        <Dialog.Overlay css={overlayStyle} />
        <Dialog.Content css={contentStyle}>
          <Dialog.Title css={titleStyle}>{title}</Dialog.Title>
          {image && (
            <ImageContainer>
              <Image
                src={image}
                alt="modal"
                width={80}
                height={80}
              />
            </ImageContainer>
          )}
          <Dialog.Description css={descStyle}>
            {description.split('\n').map((line, idx) => (
              <span
                key={idx}
                style={{ display: 'block' }}>
                {line}
              </span>
            ))}
          </Dialog.Description>
          {children}
          <ButtonWrapper>
            {!disablePrev && (
              <Dialog.Close asChild>
                <ModalConfirmButton
                  label={'cancel'}
                  variant={'prev'}
                  onClick={onCancel ?? (() => {})}
                />
              </Dialog.Close>
            )}
            <ModalConfirmButton
              label={confirmText}
              variant={'next'}
              onClick={onConfirm}
            />
          </ButtonWrapper>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  )
}
