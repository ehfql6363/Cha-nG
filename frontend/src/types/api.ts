export interface ApiSuccessResponse<T> {
  success: true
  data: T
}

export interface ApiErrorDetail {
  code: string
  message: string
}

export interface ApiErrorResponse {
  success: false
  error: ApiErrorDetail
}

export type ApiResponse<T> = ApiSuccessResponse<T> | ApiErrorResponse
