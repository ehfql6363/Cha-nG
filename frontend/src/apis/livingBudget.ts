import { getBooleanRequest, getRequest, postBooleanRequest } from './api'

//notifyLivingWithdraw 출금 알림 트리거
export const notifyLivingWithdraw = async () =>
  await postBooleanRequest('/budget/living/notice/withdraw')

//getLivingAccount 생활비 계좌 조회
export const getLivingAccount = async () =>
  await getRequest<{ liveAccountNo: string; myAccountNo: string }>(
    '/budget/living/account',
  )

// saveAccountAndNotify 생활비 계좌 저장 및 알림 전송
export const saveAccountAndNotify = async (accountNo: string) =>
  await postBooleanRequest('/budget/living/account', { accountNo })

//notifyLivingDeposit 입금 알림 트리거
export const notifyLivingDeposit = async () =>
  await getBooleanRequest('/budget/living/notice/deposit')

//notifyLeaderLivingAccountCreated 그룹장에게 계좌 등록 요청 알림 전송
export const notifyLeaderLivingAccountCreated = async () =>
  await getBooleanRequest('/budget/living/notice/create')
