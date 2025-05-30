import { useMemo } from 'react'

export const useCurrentMonth = (date: Date) => {
  return useMemo(() => {
    const now = new Date()
    const kstOffset = 9 * 60 * 60 * 1000
    const kstDate = new Date(now.getTime() + kstOffset)
    const year = kstDate.getUTCFullYear()
    const month = String(kstDate.getUTCMonth() + 1).padStart(2, '0')
    const dateYear = date.getUTCFullYear()
    const dateMonth = String(date.getUTCMonth() + 1).padStart(2, '0')
    return {
      utilityCurrentMonth: `${year}-${month}`,
      paymentCurrentMonth: `${year}${month}`,
      paymentMonth: `${dateYear}${dateMonth}`,
    }
  }, [date])
}
