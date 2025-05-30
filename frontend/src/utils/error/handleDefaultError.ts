import { isAxiosError } from 'axios'

import { ErrorModalButtonTypes } from '@/constants/errors'
import { setErrorModal } from '@/store/slices/errorModalSlice'
import { store } from '@/store/store'
import { ApiErrorResponse } from '@/types/api'

interface FetchErrorModalProps {
  title: string
  content: string
  useSecondaryButton: boolean
  useI18n: boolean
}

const fetchErrorModal = (props: FetchErrorModalProps) => {
  const { title, content, useSecondaryButton, useI18n } = props
  store.dispatch(
    setErrorModal({
      modalTitle: title,
      modalContent: content,
      primaryButtonType: ErrorModalButtonTypes.confirm,
      secondaryButtonType: useSecondaryButton
        ? ErrorModalButtonTypes.goToHome
        : null,
      isVisible: true,
      useI18n: useI18n,
    }),
  )
}

export const handleDefaultError = (error: unknown) => {
  if (!isAxiosError(error)) {
    // fetchErrorModal({
    //   title: `error.999.title`,
    //   content: `error.999.content`,
    //   useSecondaryButton: true,
    //   useI18n: true,
    // })
    return Promise.reject({
      success: false,
      error: error,
    } as ApiErrorResponse)
  }
  let status = error.response?.status ?? error.code
  const responseData = error.response?.data?.data

  if (responseData && !responseData.success) {
    if (responseData.code == 'LIFE_RULE_NOT_FOUND') {
      return Promise.reject(error.response?.data)
    }
    // fetchErrorModal({
    //   title: '',
    //   content: responseData.message,
    //   useSecondaryButton: false,
    //   useI18n: false,
    // })
    return Promise.reject(error.response?.data as ApiErrorResponse)
  }

  switch (status) {
    case 400:
    case 401:
    case 404:
    case 500:
      break
    case 'ERR_NETWORK':
      break

    default:
      status = 999
      break
  }

  // fetchErrorModal({
  //   title: `error.${status}.title`,
  //   content: `error.${status}.content`,
  //   useSecondaryButton: true,
  //   useI18n: true,
  // })

  return Promise.reject({
    success: false,
    error: {
      code: error.code,
      message: error.message,
    },
  } as ApiErrorResponse)
}
