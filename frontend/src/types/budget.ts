import { DayKey } from './duty'

export interface RetrieveRentResponse {
  totalAmount: number
  myAmount: number
  dueDate: number
  currentMonth: CurrentMonth[]
  monthList: MonthList[]
}

export interface RetrieveUtilityResponse {
  totalAmount: number
  myAmount: number
  dueDayOfWeek: DayKey
  currentWeek: CurrentWeek[]
  weekList: WeekList[]
}

export interface CurrentWeek {
  userId: number
  amount: number
  status: boolean
}

export interface WeekList {
  month: string
  week: number
  amount: number
  paidUserIds: number[]
  debtUserIds: number[]
}

export interface CurrentMonth {
  userId: number
  amount: number
  status: boolean
}

export interface MonthList {
  month: string
  paidUserIds: number[]
  debtUserIds: number[]
}

export interface TransferToOwnerRequest {
  month: number
  depositAccountNo: string
  transactionBalance: number
}

export interface DepositToRentAccountRequest {
  month: number
  withdrawalAccountNo: string
  transactionBalance: number
}

export type BudgetStatus = 'complete' | 'debt' | 'expected' | 'none'

export type PaymentStatus =
  | 'STARTED'
  | 'PARTIALLY_PAID'
  | 'COLLECTED'
  | 'RETRY_PENDING'
  | 'PAID'
  | 'FAILED'
  | 'DEBT'
  | 'PENDING'
// userRent 기준으로 // COLLECTED 면 월세 채우기 오픈 X
// rent 기준으로 COLLECTED 때만 집주인 계좌 송금 오픈 o

export const PaymentStatus = {
  STARTED: 'STARTED', //납부 전
  PARTIALLY_PAID: 'PARTIALLY_PAID', // 모으기 실패
  COLLECTED: 'COLLECTED', // 모으기 완료
  RETRY_PENDING: 'RETRY_PENDING', // 송금 실패
  PAID: 'PAID', // 완료
  FAILED: 'FAILED', // 실패
  DEBT: 'DEBT', // 연체
  PENDING: 'PENDING', // 자동이체 중
} as const
export interface PaymentCurrent {
  rent: PaymentStatus | null
  utility: PaymentStatus | null
  userRent: PaymentStatus | null
  userUtility: PaymentStatus | null
}
