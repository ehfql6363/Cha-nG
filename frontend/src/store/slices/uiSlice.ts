import { PayloadAction, createSlice } from '@reduxjs/toolkit'

import { NavItemVariant } from '@/types/nav'

interface UiState {
  selectedNavItem: NavItemVariant
  isNoticeModalOpen: boolean
  noContractWhenLogIn: boolean
}

const initialState: UiState = {
  selectedNavItem: NavItemVariant.home,
  isNoticeModalOpen: false,
  noContractWhenLogIn: false,
}

const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    setSelectedNavItem: (state, action: PayloadAction<NavItemVariant>) => {
      state.selectedNavItem = action.payload
    },
    setIsNoticeModalOpen: (state, action: PayloadAction<boolean>) => {
      state.isNoticeModalOpen = action.payload
    },
    setNoContractWhenLogIn: (state, action: PayloadAction<boolean>) => {
      state.noContractWhenLogIn = action.payload
    },
  },
})

export const {
  setSelectedNavItem,
  setIsNoticeModalOpen,
  setNoContractWhenLogIn,
} = uiSlice.actions
export default uiSlice.reducer
