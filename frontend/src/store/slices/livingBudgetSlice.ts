import { PayloadAction, createSlice } from '@reduxjs/toolkit'

import { AccountDetail, AccountPaymentHistory } from '@/types/fintech'
import { LivingMenu } from '@/types/ui'

interface LivingBudgetState {
  myAccountNo: string // 내 계좌
  livingAccountNo: string // 생활비 계좌
  livingAccountPaymentHistory: AccountPaymentHistory[]
  livingAccountDetail: AccountDetail
  selectedMenu: LivingMenu
}

const initialState: LivingBudgetState = {
  myAccountNo: '',
  livingAccountNo: '',
  livingAccountPaymentHistory: [],
  livingAccountDetail: {
    bankCode: '',
    bankName: '',
    userName: '',
    accountNo: '',
    accountName: '',
    accountTypeCode: '',
    accountTypeName: '',
    accountCreatedDate: '',
    accountExpiryDate: '',
    dailyTransferLimit: '',
    oneTimeTransferLimit: '',
    accountBalance: '',
    lastTransactionDate: '',
    currency: '',
  },
  selectedMenu: LivingMenu.calendar,
}

const livingBudgetSlice = createSlice({
  name: 'livingBudget',
  initialState,
  reducers: {
    setMyAccountNo: (state, action: PayloadAction<string>) => {
      state.myAccountNo = action.payload
    },
    setLivingAccountNo: (state, action: PayloadAction<string>) => {
      state.livingAccountNo = action.payload
    },
    setLivingAccountPaymentHistory: (
      state,
      action: PayloadAction<AccountPaymentHistory[]>,
    ) => {
      state.livingAccountPaymentHistory = action.payload
    },
    setLivingAccountDetail: (state, action: PayloadAction<AccountDetail>) => {
      state.livingAccountDetail = action.payload
    },
    setSelectedMenu: (state, action: PayloadAction<LivingMenu>) => {
      state.selectedMenu = action.payload
    },
  },
})

export const {
  setMyAccountNo,
  setLivingAccountNo,
  setLivingAccountPaymentHistory,
  setLivingAccountDetail,
  setSelectedMenu,
} = livingBudgetSlice.actions
export default livingBudgetSlice.reducer
