import { User } from './user'

export interface Contract {
  id: number
  startDate: string
  endDate: string
  rent: Rent
  utility: Utility
  status: ContractStatus
  createdAt: string
  updatedAt: string
}

export type ContractRequest = Omit<Contract, 'id' | 'createdAt' | 'updatedAt'>

export type CreateContractResponse = Pick<
  Contract,
  'id' | 'createdAt' | 'updatedAt'
>

export interface Rent {
  totalAmount: number
  dueDate: number
  rentAccountNo: string
  ownerAccountNo: string
  totalRatio: number
  userPaymentInfo: UserPaymentInfo[]
}

export interface UserPaymentInfo {
  userId: number
  amount: number
  ratio: number
}

export interface Utility {
  cardId: number | null
}

export const ContractStatus = {
  none: 'NONE',
  draft: 'DRAFT',
  isContractApproved: 'IS_CONTRACT_APPROVED',
  pending: 'PENDING',
  reviewRequired: 'REVIEW_REQUIRED',
  confirmed: 'CONFIRMED',
  shouldInvite: 'SHOULD_INVITE',
} as const

export type MemberContractStatus =
  | (typeof ContractStatus)['isContractApproved']
  | (typeof ContractStatus)['pending']
  | (typeof ContractStatus)['reviewRequired']
  | (typeof ContractStatus)['confirmed']

export type ContractStatus =
  (typeof ContractStatus)[keyof typeof ContractStatus]

export interface ContractUser extends User {
  approved: boolean
  status: ContractStatus
}

export interface Card {
  accountNo: string
  cardId: string
}

export interface RentUser extends User {
  amount: number
  ratio: number
}
