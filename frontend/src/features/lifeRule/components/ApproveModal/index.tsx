/** @jsxImportSource @emotion/react */
import * as Dialog from '@radix-ui/react-dialog'

import { Image } from '@/components'
import { ConfirmButton } from '@/components/ConfirmButton'

import {
  ButtonWrapper,
  ImageWrapper,
  contentStyle,
  descStyle,
  overlayStyle,
  titleStyle,
} from './styles'

interface ApproveModalProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  onConfirm: () => void
  title: string
  description: string
  confirmText: string
  image: string
}

export default function ApproveModal({
  open,
  onOpenChange,
  onConfirm,
  title,
  description,
  confirmText,
  image,
}: ApproveModalProps) {
  return (
    <Dialog.Root
      open={open}
      onOpenChange={onOpenChange}>
      <Dialog.Portal>
        <Dialog.Overlay css={overlayStyle} />
        <Dialog.Content css={contentStyle}>
          <Dialog.Title css={titleStyle}>{title}</Dialog.Title>
          <div css={ImageWrapper}>
            <Image
              src={image}
              alt={title}
              width={80}
              height={80}
              priority
            />
          </div>
          <Dialog.Description css={descStyle}>
            {description.split('\n').map((line, idx) => (
              <span
                key={idx}
                style={{ display: 'block' }}>
                {line}
              </span>
            ))}
          </Dialog.Description>
          <div css={ButtonWrapper}>
            <Dialog.Close asChild>
              <ConfirmButton
                label={'취소'}
                variant={'prev'}
              />
            </Dialog.Close>
            <ConfirmButton
              label={confirmText}
              variant={'next'}
              onClick={onConfirm}
            />
          </div>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  )
}
