export interface User {
  id: number
  name: string
  nickname: string | null
  profileImage: string | null
}

export type UserUpdateRequest = Pick<User, 'nickname' | 'profileImage'>

export interface UserSummary extends User {
  emailAddress: string | null
  myAccountNo: string
  livingAccountNo: string
}

export interface LoginUser extends User {
  groupId: number | null
  contractId: number | null
}

export interface HomeOverview {
  groupName: string
  isRentPaid: boolean
  isMyRentPaid: boolean
  isUtilityPaid: boolean
  isMyUtilityPaid: boolean
  isLifeRuleApproved: boolean
}
