import axios from 'axios'

import { store } from '@/store/store'
import { RootState } from '@/store/store'
import { ApiErrorResponse, ApiResponse } from '@/types/api'
import { handleDefaultError } from '@/utils/error/handleDefaultError'

const api = axios.create({
  baseURL: `/api/v1`,
  // baseURL: `${process.env.NEXT_PUBLIC_FRONTEND_SCHEME}://${process.env.NEXT_PUBLIC_FRONTEND_HOST}${process.env.NEXT_PUBLIC_FRONTEND_PATH}`,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use(
  (config) => {
    const state: RootState = store.getState()
    const accessToken = state.auth.loginToken.accessToken
    if (accessToken) {
      config.headers = config.headers || {}
      config.headers.Authorization = accessToken
    } else {
      delete config.headers.accessToken
    }

    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

api.interceptors.response.use(
  (response) => response,
  (error) => {
    return handleDefaultError(error)
  },
)

export default api

export const getRequest = async <T>(url: string): Promise<ApiResponse<T>> => {
  try {
    const response = await api.get<ApiResponse<T>>(url)
    return response.data
  } catch (error) {
    return error as ApiErrorResponse
  }
}

export const getBooleanRequest = async (url: string): Promise<boolean> => {
  const response = await getRequest<unknown>(url)
  return response.success
}

export const postRequest = async <T>(
  url: string,
  data?: object,
): Promise<ApiResponse<T>> => {
  try {
    const response = await api.post<ApiResponse<T>>(url, data)
    return response.data
  } catch (error) {
    return error as ApiErrorResponse
  }
}

export const postBooleanRequest = async (
  url: string,
  data?: object,
): Promise<boolean> => {
  const response = await postRequest<unknown>(url, data)
  return response.success
}

export const putRequest = async <T>(
  url: string,
  data: object,
): Promise<ApiResponse<T>> => {
  try {
    const response = await api.put<ApiResponse<T>>(url, data)
    return response.data
  } catch (error) {
    return error as ApiErrorResponse
  }
}

export const patchRequest = async <T>(
  url: string,
  data: object,
): Promise<ApiResponse<T>> => {
  try {
    const response = await api.patch<ApiResponse<T>>(url, data)
    return response.data
  } catch (error) {
    return error as ApiErrorResponse
  }
}

export const deleteRequest = async <T>(
  url: string,
): Promise<ApiResponse<T>> => {
  try {
    const response = await api.delete<ApiResponse<T>>(url)
    return response.data
  } catch (error) {
    return error as ApiErrorResponse
  }
}
