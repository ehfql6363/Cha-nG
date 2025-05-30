import { createSlice } from '@reduxjs/toolkit'

import { ErrorModalButtonTypes } from '@/constants/errors'
import { ErrorModalProps } from '@/types/errors'

const initialState: ErrorModalProps = {
  modalTitle: '에러 발생',
  modalContent: '에러가 발생했습니다.',
  primaryButtonType: ErrorModalButtonTypes.confirm,
  secondaryButtonType: ErrorModalButtonTypes.goToHome,
  isVisible: false,
  useI18n: false,
}

const errorModalSlice = createSlice({
  name: 'errorModal',
  initialState,
  reducers: {
    resetErrorModal(state) {
      Object.assign(state, initialState)
    },
    setErrorModal(state, action) {
      state.modalTitle = action.payload.modalTitle
      state.modalContent = action.payload.modalContent
      state.primaryButtonType = action.payload.primaryButtonType
      state.secondaryButtonType = action.payload.secondaryButtonType
      state.isVisible = action.payload.isVisible
      state.useI18n = action.payload.useI18n
    },
    setErrorModalVisible(state, action) {
      state.isVisible = action.payload
    },
  },
})

export const { resetErrorModal, setErrorModal, setErrorModalVisible } =
  errorModalSlice.actions
export default errorModalSlice.reducer
