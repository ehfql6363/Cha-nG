import { PayloadAction, createSlice } from '@reduxjs/toolkit'

import {
  Contract,
  ContractRequest,
  ContractStatus,
  ContractUser,
  CreateContractResponse,
  Rent,
  Utility,
} from '@/types/contract'
import { User } from '@/types/user'

interface ValidationItem {
  isValid: boolean
  message: string
}

interface ContractState {
  contract: Contract
  contractRequest: ContractRequest
  createContractResponse: CreateContractResponse
  showRentRatio: boolean
  rentAccountConfirm: boolean
  cardConfirm: boolean
  useUtilityCard: boolean
  validations: {
    [key: string]: ValidationItem
  }
  contractMembers: ContractUser[]
}

const initialState: ContractState = {
  contract: {
    id: 0,
    startDate: '',
    endDate: '',
    rent: {
      totalAmount: 0,
      dueDate: 0,
      rentAccountNo: '', // 월세 계좌 번호
      ownerAccountNo: '', // 집주인 계좌 번호
      totalRatio: 0,
      userPaymentInfo: [],
    },
    utility: {
      cardId: null,
    },
    status: ContractStatus.none,
    createdAt: '',
    updatedAt: '',
  },
  contractRequest: {
    startDate: '',
    endDate: '',
    rent: {
      totalAmount: 0,
      dueDate: 0,
      rentAccountNo: '',
      ownerAccountNo: '',
      totalRatio: 0,
      userPaymentInfo: [],
    },
    utility: {
      cardId: null,
    },
    status: ContractStatus.none,
  },
  createContractResponse: {
    id: 0,
    createdAt: '',
    updatedAt: '',
  },
  showRentRatio: true,
  rentAccountConfirm: false,
  cardConfirm: false,
  useUtilityCard: false,
  validations: {
    requiredFields: {
      isValid: false,
      message: '필수 항목을 모두 입력해주세요',
    },
    startDate: {
      isValid: false,
      message: '시작일은 오늘 이후여야 합니다',
    },
    endDate: {
      isValid: false,
      message: '종료일은 시작일 이후여야 합니다',
    },
    rent: {
      isValid: false,
      message: '임대료 정보를 모두 입력해주세요',
    },
    utility: {
      isValid: false,
      message: '유틸리티 정보를 입력해주세요',
    },
  },
  contractMembers: [],
}

export const contractSlice = createSlice({
  name: 'contract',
  initialState,
  reducers: {
    setContract: (state, action: PayloadAction<Contract>) => {
      state.contract = action.payload
    },
    setContractRequest: (state, action: PayloadAction<ContractRequest>) => {
      state.contractRequest = action.payload
    },
    setCreateContractResponse: (
      state,
      action: PayloadAction<CreateContractResponse>,
    ) => {
      state.createContractResponse = action.payload
    },
    setShowRentRatio: (state, action: PayloadAction<boolean>) => {
      state.showRentRatio = action.payload
    },
    updateRent: (state, action: PayloadAction<Partial<Rent>>) => {
      if (!state.contractRequest) return
      state.contractRequest.rent = {
        ...state.contractRequest.rent,
        ...action.payload,
      }
      // validation 상태만 업데이트하고 값 저장에는 관여하지 않음
      contractSlice.caseReducers.validateContractRequest(state)
    },
    updateRentField: (
      state,
      action: PayloadAction<{
        field: keyof Rent
        value: unknown
        // value: Rent[keyof Rent]
      }>,
    ) => {
      const { field, value } = action.payload
      if (state.contractRequest.rent[field] != undefined) {
        // @ts-expect-error 나중에해
        state.contractRequest.rent[field] = value
      }
    },
    updateContractRequestField: (
      state,
      action: PayloadAction<{
        field: keyof ContractRequest
        value: unknown
        // value: ContractRequest[keyof ContractRequest]
      }>,
    ) => {
      const { field, value } = action.payload
      if (state.contractRequest[field] == null) {
        // @ts-expect-error 나중에해
        state.contractRequest[field] = value
      }
      if (state.contractRequest[field] != undefined) {
        // @ts-expect-error 나중에해
        state.contractRequest[field] = value
      }
    },
    setRentAccountConfirm: (state, action: PayloadAction<boolean>) => {
      state.rentAccountConfirm = action.payload
    },
    setCardConfirm: (state, action: PayloadAction<boolean>) => {
      state.cardConfirm = action.payload
    },
    updateUtility: (state, action: PayloadAction<Utility>) => {
      state.contractRequest.utility = action.payload
    },
    validateContractRequest: (state) => {
      try {
        // contractRequest가 없으면 모든 validation을 false로 설정
        if (!state.contractRequest) {
          state.validations.requiredFields.isValid = false
          state.validations.startDate.isValid = false
          state.validations.endDate.isValid = false
          state.validations.rent.isValid = false
          state.validations.utility.isValid = false
          return
        }

        const today = new Date()
        today.setHours(0, 0, 0, 0)

        // 필수 필드 검증
        const hasEmptyFields = (() => {
          try {
            // startDate, endDate 검증
            if (
              !state.contractRequest?.startDate ||
              !state.contractRequest?.endDate
            ) {
              return true
            }

            // rent 객체 검증 (userPaymentInfo 제외)
            const rent = state.contractRequest?.rent
            if (
              !rent?.totalAmount ||
              !rent?.dueDate ||
              !rent?.rentAccountNo ||
              !rent?.ownerAccountNo ||
              !rent?.totalRatio
            ) {
              return true
            }

            // utility 객체 검증 (cardId는 null 허용)
            const utility = state.contractRequest?.utility
            if (utility?.cardId === undefined) {
              return true
            }

            return false
          } catch {
            return true
          }
        })()

        // 시작일 검증
        const startDate = new Date(state.contractRequest.startDate)
        const isStartDateValid =
          !isNaN(startDate.getTime()) && startDate > today

        // 종료일 검증
        const endDate = new Date(state.contractRequest.endDate)
        const isEndDateValid =
          !isNaN(endDate.getTime()) && endDate > today && endDate > startDate

        state.validations.requiredFields.isValid = !hasEmptyFields
        state.validations.startDate.isValid = isStartDateValid
        state.validations.endDate.isValid = isEndDateValid
        state.validations.rent.isValid = !hasEmptyFields
        state.validations.utility.isValid = !hasEmptyFields
      } catch {
        // 최상위 에러 처리
        state.validations.requiredFields.isValid = false
        state.validations.startDate.isValid = false
        state.validations.endDate.isValid = false
        state.validations.rent.isValid = false
        state.validations.utility.isValid = false
      }
    },
    setContractMembers: (state, action: PayloadAction<ContractUser[]>) => {
      state.contractMembers = action.payload
    },

    initContractRequest: (state, actions: PayloadAction<User[]>) => {
      const startDate = new Date().toISOString()
      const endDate = new Date()
      endDate.setFullYear(endDate.getFullYear() + 1)

      const newUserPaymentInfo = actions.payload.map((member) => {
        return {
          userId: member.id,
          amount: 100000,
          ratio: 1,
        }
      })

      state.contractRequest = {
        startDate: startDate,
        endDate: endDate.toISOString(),
        rent: {
          totalAmount: 100000 * actions.payload.length,
          dueDate: 15,
          rentAccountNo: '',
          ownerAccountNo: '',
          totalRatio: actions.payload.length,
          userPaymentInfo: newUserPaymentInfo,
        },
        utility: {
          cardId: null,
        },
        status: ContractStatus.draft,
      }
    },
    setUseUtilityCard: (state, action: PayloadAction<boolean>) => {
      state.useUtilityCard = action.payload
    },
  },
})

export const {
  setContract,
  setContractRequest,
  setCreateContractResponse,
  setShowRentRatio,
  updateRent,
  updateRentField,
  updateContractRequestField,
  setRentAccountConfirm,
  setCardConfirm,
  updateUtility,
  validateContractRequest,
  setContractMembers,
  initContractRequest,
  setUseUtilityCard,
} = contractSlice.actions

export default contractSlice.reducer
