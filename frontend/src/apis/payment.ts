import {
  DepositToRentAccountRequest,
  PaymentCurrent,
  RetrieveRentResponse,
  RetrieveUtilityResponse,
  TransferToOwnerRequest,
} from '@/types/budget'

import { getRequest, postBooleanRequest, postRequest } from './api'

//transferToOwner
export const transferToOwner = async (params: TransferToOwnerRequest) =>
  await postBooleanRequest('/payment/withdraw', params)

//depositToRentAccount
export const depositToRentAccount = async (
  params: DepositToRentAccountRequest,
) => await postBooleanRequest('/payment/deposit', params)

//retrieveUtility 공과금 월별 통계 조회
export const retrieveUtility = async (month: string) =>
  await getRequest<RetrieveUtilityResponse>(`/payment/utility?month=${month}`)

//retrieveRent 월세 월별 통계 조회
export const retrieveRent = async (month: string) =>
  await getRequest<RetrieveRentResponse>(`/payment/rent?month=${month}`)

//getRentAccountNo 공과금 계좌 정보 조회
export const getRentAccountNo = async () =>
  await getRequest<{ accountNo: string }>(`/payment/account`)

//createTransferPDF
export const createTransferPDF = async (contractId: number) =>
  await postRequest<{ presignedUrl: string }>(
    `/blockchain/payment/${contractId}/pdf`,
  )

export const getPaymentCurrentStatus = async (month: string) =>
  await getRequest<PaymentCurrent>(
    `/payment/rent/current-status?month=${month}`,
  )
