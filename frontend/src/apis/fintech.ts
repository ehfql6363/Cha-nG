import axios from 'axios'

import { ApiErrorResponse, ApiResponse } from '@/types/api'
import {
  Account,
  AccountDetail,
  AccountPaymentHistoryRequest,
  AccountPaymentHistoryResponse,
  FintechResponseError,
  TransferRequest,
  TransferResponse,
} from '@/types/fintech'
import { handleFintechError } from '@/utils/error/handleFintechError'

import { getRequest, postRequest } from './api'

const fintechApi = axios.create({
  baseURL: `https://finopenapi.ssafy.io/ssafy/api/vi/edu`,
  // baseURL: `${process.env.NEXT_PUBLIC_FINTECH_BASEURL}:
  // timeout: 5000,
  // headers: {
  //   'Content-Type': 'application/json',
  // },
})

fintechApi.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

fintechApi.interceptors.response.use(
  (response) => response,
  (error) => {
    return handleFintechError(error)
  },
)

export default fintechApi

export const getFintechRequest = async <T>(
  url: string,
): Promise<ApiResponse<T>> => {
  try {
    const response = await fintechApi.get<ApiResponse<T>>(url)
    return response.data
  } catch (error) {
    return error as ApiErrorResponse
  }
}

export const getAccountPaymentHistory = async <AccountPaymentHistoryResponse>(
  data: AccountPaymentHistoryRequest,
): Promise<AccountPaymentHistoryResponse | FintechResponseError> => {
  try {
    const response = await fintechApi.post<AccountPaymentHistoryResponse>(
      '/demandDeposit/inquireTransactionHistoryList',
      data,
    )
    return response.data
  } catch (error) {
    return error as FintechResponseError
  }
}

export const putFintechRequest = async <T>(
  url: string,
  data: object,
): Promise<ApiResponse<T>> => {
  try {
    const response = await fintechApi.put<ApiResponse<T>>(url, data)
    return response.data
  } catch (error) {
    return error as ApiErrorResponse
  }
}

export const patchFintechRequest = async <T>(
  url: string,
  data: object,
): Promise<ApiResponse<T>> => {
  try {
    const response = await fintechApi.patch<ApiResponse<T>>(url, data)
    return response.data
  } catch (error) {
    return error as ApiErrorResponse
  }
}

export const deleteFintechRequest = async <T>(
  url: string,
): Promise<ApiResponse<T>> => {
  try {
    const response = await fintechApi.delete<ApiResponse<T>>(url)
    return response.data
  } catch (error) {
    return error as ApiErrorResponse
  }
}

export const transfer = async <TransferResponse>(
  data: TransferRequest,
): Promise<TransferResponse | FintechResponseError> => {
  try {
    const response = await fintechApi.post<TransferResponse>(
      '/demandDeposit/updateDemandDepositAccountTransfer',
      data,
    )
    return response.data
  } catch (error) {
    return error as FintechResponseError
  }
}

//createAccount 계좌 생성
export const createAccount = async () =>
  await postRequest<{ data: Account }>('/fintech/account')

//getAccountDetail 계좌 조회(단건)
export const getAccountDetail = async (accountNo: string) =>
  await getRequest<{ data: AccountDetail }>(`/fintech/account/${accountNo}`)

//createCard 카드 생성
export const createCard = async ({ accountNo }: { accountNo: string }) =>
  await postRequest<{ id: string }>('/card', {
    accountNo,
  })

//simple/transfer
export const simpleTransfer = async (transferRequest: TransferRequest) =>
  await postRequest<TransferResponse>(
    '/fintech/simple/transfer',
    transferRequest,
  )
//simple/transfer
export const simpleHistory = async (
  accountPaymentHistoryRequest: AccountPaymentHistoryRequest,
) =>
  await postRequest<AccountPaymentHistoryResponse>(
    '/fintech/account/history',
    accountPaymentHistoryRequest,
  )
