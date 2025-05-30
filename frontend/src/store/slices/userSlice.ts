import { PayloadAction, createSlice } from '@reduxjs/toolkit'

import { HomeOverview, LoginUser, UserSummary } from '@/types/user'

interface UserState {
  user: LoginUser
  summary: UserSummary
  homeOverview: HomeOverview
}

const initialState: UserState = {
  user: {
    // 내정보들
    id: 0,
    name: '',
    nickname: '',
    profileImage: '',
    groupId: 0,
    contractId: 0,
  },
  summary: {
    // 내정보들
    id: 0,
    emailAddress: '',
    name: '',
    nickname: '',
    profileImage: '',
    myAccountNo: '',
    livingAccountNo: '',
  },
  homeOverview: {
    groupName: '',
    isRentPaid: true,
    isMyRentPaid: true,
    isUtilityPaid: true,
    isMyUtilityPaid: true,
    isLifeRuleApproved: true,
  },
}

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    setUser: (state, action: PayloadAction<LoginUser>) => {
      state.user = action.payload
    },
    setUserNickname: (state, action: PayloadAction<string>) => {
      state.user.nickname = action.payload
    },
    setUserProfileImage: (state, action: PayloadAction<string>) => {
      state.user.profileImage = action.payload
    },
    setGroupId: (state, action: PayloadAction<number>) => {
      state.user.groupId = action.payload
    },
    setContractId: (state, action: PayloadAction<number>) => {
      state.user.contractId = action.payload
    },
    setHomeOverview: (state, action: PayloadAction<HomeOverview>) => {
      state.homeOverview = action.payload
    },
    setHomeOverviewLifeRuleApproved: (
      state,
      action: PayloadAction<boolean>,
    ) => {
      state.homeOverview.isLifeRuleApproved = action.payload
    },
    setSummary: (state, action: PayloadAction<UserSummary>) => {
      state.summary = action.payload
    },
  },
})

export const {
  setUser,
  setUserNickname,
  setUserProfileImage,
  setGroupId,
  setContractId,
  setHomeOverview,
  setSummary,
  setHomeOverviewLifeRuleApproved,
} = userSlice.actions

export default userSlice.reducer
