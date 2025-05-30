import { PayloadAction, createSlice } from '@reduxjs/toolkit'

import { SignUpRequest, SocialLogin } from '@/types/auth'

interface AuthState {
  FCMToken: string | null
  loginToken: SocialLogin
  signUpRequest: SignUpRequest
}

const initialState: AuthState = {
  FCMToken: null,
  loginToken: {
    accessToken: null,
    expiresIn: null,
    refreshToken: null,
  },
  signUpRequest: {
    emailAddress: null,
    password: null,
    name: null,
  },
}

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setAccessToken: (state, action: PayloadAction<string>) => {
      state.loginToken.accessToken = action.payload
    },
    setRefreshToken: (state, action: PayloadAction<string>) => {
      state.loginToken.refreshToken = action.payload
    },
    setExpiresIn: (state, action: PayloadAction<number>) => {
      state.loginToken.expiresIn = action.payload
    },
    clearTokens: (state) => {
      Object.assign(state, initialState)
    },
    setFCMToken: (state, action: PayloadAction<string>) => {
      state.FCMToken = action.payload
    },
    setSignUpEmail: (state, action: PayloadAction<string>) => {
      state.signUpRequest.emailAddress = action.payload
    },
    setSignUpPassword: (state, action: PayloadAction<string>) => {
      state.signUpRequest.password = action.payload
    },
    setSignUpName: (state, action: PayloadAction<string>) => {
      state.signUpRequest.name = action.payload
    },
    clearSignUp: (state) => {
      Object.assign(state, initialState)
    },
  },
})

export const {
  setAccessToken,
  setRefreshToken,
  setExpiresIn,
  setFCMToken,
  clearTokens,
  setSignUpEmail,
  setSignUpPassword,
  setSignUpName,
  clearSignUp,
} = authSlice.actions

export default authSlice.reducer
