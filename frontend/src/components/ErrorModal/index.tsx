/** @jsxImportSource @emotion/react */
import { useTranslation } from 'react-i18next'
import { useDispatch, useSelector } from 'react-redux'

import * as Dialog from '@radix-ui/react-dialog'
import { useRouter } from 'next/navigation'

import { setErrorModalVisible } from '@/store/slices/errorModalSlice'
import { RootState } from '@/store/store'

import { ConfirmButton } from '../ConfirmButton'
import {
  ButtonWrapper,
  contentStyle,
  descStyle,
  overlayStyle,
  titleStyle,
} from '../Modal/styles'
import { ModalConfirmButton } from '../ModalConfirmButton'

export default function ErrorModal() {
  const { t } = useTranslation()
  const router = useRouter()
  const {
    isVisible,
    modalTitle,
    modalContent,
    primaryButtonType,
    secondaryButtonType,
    useI18n,
  } = useSelector((state: RootState) => state.errorModal)
  const dispatch = useDispatch()

  if (!isVisible) return null

  const title = useI18n && modalTitle ? t(modalTitle) : modalTitle
  const content = useI18n && modalContent ? t(modalContent) : modalContent
  return (
    <Dialog.Root
      open={isVisible}
      onOpenChange={() => dispatch(setErrorModalVisible(false))}>
      <Dialog.Portal>
        <Dialog.Overlay css={overlayStyle} />
        <Dialog.Content css={contentStyle}>
          <Dialog.Title css={titleStyle}>{title}</Dialog.Title>
          {modalContent && (
            <Dialog.Description css={descStyle}>{content}</Dialog.Description>
          )}
          <ButtonWrapper>
            {secondaryButtonType && (
              <ModalConfirmButton
                label={secondaryButtonType}
                variant={'prev'}
                onClick={() => {
                  dispatch(setErrorModalVisible(false))
                  router.replace('/')
                }}
              />
            )}
            {primaryButtonType && (
              <ModalConfirmButton
                label={primaryButtonType}
                variant={'next'}
                onClick={() => dispatch(setErrorModalVisible(false))}
              />
            )}
          </ButtonWrapper>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  )
}
