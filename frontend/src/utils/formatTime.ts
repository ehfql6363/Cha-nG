import { formatDistanceToNow, isSameMonth, parse } from 'date-fns'
import { format, utcToZonedTime } from 'date-fns-tz'
import { ko } from 'date-fns/locale'

const timeZone = 'Asia/Seoul'
export const formatTime = (timestamp: number) => {
  const diffInHours = Math.floor((Date.now() - timestamp) / 3600000)
  const kstDate = utcToZonedTime(timestamp, timeZone)
  if (diffInHours < 24) {
    return formatDistanceToNow(kstDate, { addSuffix: true, locale: ko })
  }
  const nowKST = utcToZonedTime(new Date(), timeZone)
  return isSameMonth(kstDate, nowKST)
    ? format(kstDate, 'M월 d일', { locale: ko })
    : format(kstDate, 'yyyy년 M월 d일', { locale: ko })
}

export const formatHourMinuteTime = (time: string) => {
  const hh = Number(time.split(':')[0])
  const kstHour = (hh + 9) % 24
  const a = kstHour < 12 ? '오전' : '오후'
  return `${a} ${String(kstHour % 12)}시`
}

export const formatDuration = (startDate: string, endDate: string) => {
  const newStartDate = parse(startDate, 'yyyyMMdd', new Date())
  const newEndDate = parse(endDate, 'yyyyMMdd', new Date())
  newEndDate.setDate(newEndDate.getDate() - 1)
  return `${format(newStartDate, 'PP', { locale: ko })} ~ ${format(newEndDate, 'PP', { locale: ko })}`
}
export const useMorning = () => {
  const now = new Date()
  const kstOffset = 9 * 60 * 60 * 1000
  const kstDate = new Date(now.getTime() + kstOffset)
  return kstDate.getUTCHours() < 18 && kstDate.getUTCHours() >= 6
}
