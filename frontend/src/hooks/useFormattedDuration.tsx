import { useMemo } from 'react'

import { formatDuration } from '../utils/formatTime'

export const useFormattedDuration = (startDate: string, endDate: string) =>
  useMemo(() => formatDuration(startDate, endDate), [startDate, endDate])
