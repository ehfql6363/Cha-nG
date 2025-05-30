import { PayloadAction, createSlice } from '@reduxjs/toolkit'

interface AuthState {
  isInitialized: boolean
  showContractApprovedModal: boolean
}

const initialState: AuthState = {
  isInitialized: false,
  showContractApprovedModal: false,
}

const appSlice = createSlice({
  name: 'app',
  initialState,
  reducers: {
    setIsInitialized: (state, action: PayloadAction<boolean>) => {
      state.isInitialized = action.payload
    },
    setShowContractApprovedModal: (state, action: PayloadAction<boolean>) => {
      state.showContractApprovedModal = action.payload
    },
  },
})

export const { setIsInitialized, setShowContractApprovedModal } =
  appSlice.actions
export default appSlice.reducer
