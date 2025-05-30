// store.ts
import { combineReducers, configureStore } from '@reduxjs/toolkit'
import { createWrapper } from 'next-redux-wrapper'
import { persistReducer } from 'redux-persist'
import createWebStorage from 'redux-persist/lib/storage/createWebStorage'

import appReducer from './slices/appSlice'
import authReducer from './slices/authSlice'
import contractReducer from './slices/contractSlice'
import dutyReducer from './slices/dutySlice'
import errorModalReducer from './slices/errorModalSlice'
import groupReducer from './slices/groupSlice'
import lifeRuleReducer from './slices/lifeRuleSlice'
import livingBudgetReducer from './slices/livingBudgetSlice'
import pledgeReducer from './slices/pledgeSlice'
import uiReducer from './slices/uiSlice'
import userReducer from './slices/userSlice'

export const RESET_STORE = 'RESET_STORE'

const createNoopStorage = () => {
  return {
    getItem(_key: string) {
      void _key
      return Promise.resolve(null)
    },
    setItem(_key: string, value: unknown) {
      void _key
      return Promise.resolve(value)
    },
    removeItem(_key: string) {
      void _key
      return Promise.resolve()
    },
  }
}

const storage =
  typeof window !== 'undefined'
    ? createWebStorage('local')
    : createNoopStorage()

const rootReducer = combineReducers({
  errorModal: errorModalReducer,
  auth: authReducer,
  app: appReducer,
  group: groupReducer,
  ui: uiReducer,
  user: userReducer,
  lifeRule: lifeRuleReducer,
  contract: contractReducer,
  duty: dutyReducer,
  livingBudget: livingBudgetReducer,
  pledge: pledgeReducer,
})

const reducer = (
  state: ReturnType<typeof rootReducer> | undefined,
  action: any,
) => {
  if (action.type === RESET_STORE) {
    storage.removeItem('persist:root')
    state = undefined
  }
  return rootReducer(state, action)
}

const persistConfig = {
  key: 'root',
  storage,
  whitelist: [
    'auth',
    'group',
    'user',
    'contract',
    'app',
    'livingBudget',
    'pledge',
  ],
}

const persistedReducer = persistReducer(persistConfig, reducer)

const makeStore = () => {
  const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) =>
      getDefaultMiddleware({ serializableCheck: false }),
  })

  return store
}

export const wrapper = createWrapper(makeStore, {
  debug: process.env.NODE_ENV === 'development',
})

export type RootState = ReturnType<ReturnType<typeof makeStore>['getState']>
export type AppDispatch = ReturnType<typeof makeStore>['dispatch']

export const resetStore = () => ({ type: RESET_STORE })

export const store = makeStore()
