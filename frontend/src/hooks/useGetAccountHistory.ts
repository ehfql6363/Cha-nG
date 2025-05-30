import { simpleHistory } from '@/apis/fintech'
import { AccountPaymentHistoryRequest } from '@/types/fintech'

import { useFintechTime } from './useFintechTime'

export const useGetAccountHistory = ({
  accountNo,
  budgetStartDate,
  budgetEndDate,
}: {
  accountNo: string
  budgetStartDate: Date
  budgetEndDate: Date
}) => {
  const { startDate, endDate, formattedDate, formattedTime, uniqueNo } =
    useFintechTime(new Date(), budgetStartDate, budgetEndDate)

  return async () => {
    const request: AccountPaymentHistoryRequest = {
      Header: {
        apiName: 'inquireTransactionHistoryList',
        transmissionDate: formattedDate,
        transmissionTime: formattedTime,
        institutionCode: '00100',
        fintechAppNo: '001',
        apiServiceCode: 'inquireTransactionHistoryList',
        institutionTransactionUniqueNo: uniqueNo,
        apiKey: 'a57e58879de94373856c981706ca1056',
        userKey: 'ed638cf5-675b-4e37-91c5-1ea6f5a92f67',
      },
      accountNo: accountNo,
      startDate: startDate,
      endDate: endDate,
      transactionType: 'A',
      orderByType: 'DESC',
    }

    const response = await simpleHistory(request)
    if (response.success) {
      return response.data.data.list
    } else {
      return []
    }
  }
}
