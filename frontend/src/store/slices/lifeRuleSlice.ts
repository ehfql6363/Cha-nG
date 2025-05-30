import { act } from 'react'

import { PayloadAction, createSlice } from '@reduxjs/toolkit'

import { LifeRule, UpdateLifeRule } from '@/types/lifeRule'

interface LifeRuleState {
  updateLifeRules: UpdateLifeRule[]
  lifeRules: LifeRule[]
  notApprovedIds: number[]
}

const initialState: LifeRuleState = {
  updateLifeRules: [],
  lifeRules: [],
  notApprovedIds: [],
}

const lifeRuleSlice = createSlice({
  name: 'lifeRule',
  initialState,
  reducers: {
    setUpdateLifeRules: (state, action: PayloadAction<UpdateLifeRule[]>) => {
      state.updateLifeRules = action.payload
    },
    setLifeRules: (state, action: PayloadAction<LifeRule[]>) => {
      state.lifeRules = action.payload
    },
    setNotApprovedIds: (state, action: PayloadAction<number[]>) => {
      state.notApprovedIds = action.payload
    },
    resetNotApprovedIds: (state) => {
      state.notApprovedIds = []
    },
  },
})

export const {
  setUpdateLifeRules,
  setLifeRules,
  setNotApprovedIds,
  resetNotApprovedIds,
} = lifeRuleSlice.actions
export default lifeRuleSlice.reducer
