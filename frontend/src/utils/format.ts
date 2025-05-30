export const formatMoney = (value: string | number): string => {
  const numericValue = String(value).replace(/[^0-9]+-/g, '')
  const formattedValue = new Intl.NumberFormat('ko-KR').format(
    Number(numericValue),
  )
  return `${formattedValue}원`
}

export const parseMoney = (value: string): string => {
  return value.replace(/[^0-9]+-/g, '')
}

export const formatTransactionDate = (date: string) =>
  `${date.slice(0, 4)}-${date.slice(4, 6)}-${date.slice(6, 8)}`

export const formatTransactionTime = (time: string) =>
  `${time.slice(0, 2)} : ${time.slice(2, 4)}`
