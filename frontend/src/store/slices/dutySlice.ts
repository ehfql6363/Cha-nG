import { PayloadAction, createSlice } from '@reduxjs/toolkit'

import { DayKey, Duty, DutyWeekList } from '@/types/duty'

interface DutyState {
  dutyWeekList: DutyWeekList
  createDayOfWeek: DayKey | null
  completeDayOfWeek: DayKey | null
}

const initialState: DutyState = {
  dutyWeekList: {
    monday: [],
    tuesday: [],
    wednesday: [],
    thursday: [],
    friday: [],
    saturday: [],
    sunday: [],
  },
  createDayOfWeek: null,
  completeDayOfWeek: null,
}

const dutySlice = createSlice({
  name: 'duty',
  initialState,
  reducers: {
    setDutyWeekList: (state, action: PayloadAction<DutyWeekList>) => {
      state.dutyWeekList = action.payload
    },

    removeDutyFromList: (state, action: PayloadAction<Duty>) => {
      const { dayOfWeek, id } = action.payload
      state.dutyWeekList[dayOfWeek] = state.dutyWeekList[dayOfWeek].filter(
        (duty) => duty.id !== id,
      )
    },
    setCreateDayOfWeek: (state, action: PayloadAction<DayKey>) => {
      state.createDayOfWeek = action.payload
    },
    clearCreateDayOfWeek: (state) => {
      state.createDayOfWeek = null
    },
    setCompleteDayOfWeek: (state, action: PayloadAction<DayKey>) => {
      state.completeDayOfWeek = action.payload
    },
    clearCompleteDayOfWeek: (state) => {
      state.completeDayOfWeek = null
    },
  },
})

export const {
  setDutyWeekList,
  removeDutyFromList,
  setCreateDayOfWeek,
  clearCreateDayOfWeek,
  setCompleteDayOfWeek,
  clearCompleteDayOfWeek,
} = dutySlice.actions
export default dutySlice.reducer
