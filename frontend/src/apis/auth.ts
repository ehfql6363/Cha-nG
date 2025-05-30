import axios, { AxiosResponse } from 'axios'
import { stringify } from 'qs'

import { ApiResponse, ApiSuccessResponse } from '@/types/api'
import { LoginRequest, SignUpRequest } from '@/types/auth'
import { LoginUser } from '@/types/user'
import { handleDefaultError } from '@/utils/error/handleDefaultError'

import { postBooleanRequest, postRequest } from './api'

const authApi = axios.create({
  baseURL: `https://chaing.site/api/v1`,
  timeout: 5000,
})

authApi.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    console.error('🚨 요청 오류:', error)
    return Promise.reject(error)
  },
)

authApi.interceptors.response.use(
  (response) => response,
  (error) => {
    return handleDefaultError(error)
  },
)

const postForm = async <T>(url: string, data: object) => {
  try {
    const response = await authApi.post<ApiResponse<T>>(url, stringify(data), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    })
    if (response?.data?.success) {
      return response as AxiosResponse<ApiSuccessResponse<T>>
    }
    return null
  } catch {
    return null
  }
}

export const signUp = (params: SignUpRequest) =>
  postForm<LoginUser>('/auth/signup', params)

export const login = (params: LoginRequest) =>
  postForm<LoginUser>('/auth/login', params)

export const getRefreshToken = async () =>
  await postRequest<LoginUser | null>('/auth/refresh-token')

export const registerFCMToken = async (params: { fcmToken: string }) =>
  await postBooleanRequest('/auth/fcm', params)

export const logout = async () => await postBooleanRequest('/auth/logout')
