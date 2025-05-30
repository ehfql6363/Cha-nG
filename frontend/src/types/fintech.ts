export interface AccountDetail {
  bankCode: string
  bankName: string
  userName: string
  accountNo: string
  accountName: string
  accountTypeCode: string
  accountTypeName: string
  accountCreatedDate: string
  accountExpiryDate: string
  dailyTransferLimit: string
  oneTimeTransferLimit: string
  accountBalance: string
  lastTransactionDate: string
  currency: string
}

export interface Account {
  accountNo: string
  bankCode: string
  currency: {
    currency: string
    currencyName: string
  }
}
export interface AccountPaymentHistoryRequest {
  Header: FintechRequestHeader
  accountNo: string
  startDate: string
  endDate: string
  transactionType: string
  orderByType: string
}
export interface AccountPaymentHistoryResponse {
  data: {
    totalCount: string
    list: AccountPaymentHistory[]
  }
}

export interface AccountPaymentHistory {
  transactionUniqueNo: string
  transactionDate: string
  transactionTime: string
  transactionType: string
  transactionTypeName: string
  transactionAccountNo: string
  transactionBalance: string
  transactionAfterBalance: string
  transactionSummary: string
  transactionMemo: string
}
export interface FormattedAccountPaymentHistory {
  transactionUniqueNo: string
  showDate: boolean
  date: string
  time: string
  title: string
  transactionType: string
  transactionAfterBalance: string
  transactionSummary: string
  transactionMemo: string
}
export interface FintechResponseHeader {
  responseCode: string
  responseMessage: string
  apiName: string
  transmissionDate: string
  transmissionTime: string
  institutionCode: string
  apiKey: string
  apiServiceCode: string
  institutionTransactionUniqueNo: string
}

export interface FintechRequestHeader {
  apiName: string
  transmissionDate: string
  transmissionTime: string
  institutionCode: string
  fintechAppNo: string
  apiServiceCode: string
  institutionTransactionUniqueNo: string
  apiKey: string
  userKey: string
}

export interface FintechResponseError {
  responseCode: string
  responseMessage: string
}

export interface TransferRequest {
  Header: FintechRequestHeader
  depositAccountNo: string
  depositTransactionSummary: string
  transactionBalance: string
  withdrawalAccountNo: string
  withdrawalTransactionSummary: string
}

export interface Transfer {
  depositAccountNo: string
  depositTransactionSummary?: string
  transactionBalance: string
  withdrawalAccountNo: string
  withdrawalTransactionSummary?: string
}

export interface TranferItem {
  transactionUniqueNo: string
  accountNo: string
  transactionDate: string
  transactionType: string
  transactionTypeName: string
  transactionAccountNo: string
}
export interface TransferResponse {
  Header: FintechResponseHeader
  REC: TranferItem[]
}
