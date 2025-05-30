import { simpleTransfer } from '@/apis/fintech'
import { Transfer, TransferRequest } from '@/types/fintech'

import { useFintechTime } from './useFintechTime'

export const useTransfer = ({
  depositAccountNo,
  transactionBalance,
  withdrawalAccountNo,
  depositTransactionSummary,
  withdrawalTransactionSummary,
}: Transfer) => {
  const { formattedDate, formattedTime, uniqueNo } = useFintechTime(new Date())

  return async () => {
    const request: TransferRequest = {
      Header: {
        apiName: 'updateDemandDepositAccountTransfer',
        transmissionDate: formattedDate,
        transmissionTime: formattedTime,
        institutionCode: '00100',
        fintechAppNo: '001',
        apiServiceCode: 'updateDemandDepositAccountTransfer',
        institutionTransactionUniqueNo: uniqueNo,
        apiKey: 'a57e58879de94373856c981706ca1056',
        userKey: 'ed638cf5-675b-4e37-91c5-1ea6f5a92f67',
      },
      depositAccountNo: depositAccountNo,
      depositTransactionSummary:
        depositTransactionSummary ?? '(수시입출금) : 입금(이체)',
      transactionBalance: transactionBalance,
      withdrawalAccountNo: withdrawalAccountNo,
      withdrawalTransactionSummary:
        withdrawalTransactionSummary ?? '(수시입출금) : 출금(이체)',
    }

    const response = await simpleTransfer(request)
    return response
  }
}
