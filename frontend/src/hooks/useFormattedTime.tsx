import { useMemo } from 'react'

import { formatTime } from '../utils/formatTime'

export const useFormattedTime = (timestamp: number) =>
  useMemo(() => formatTime(timestamp), [timestamp])
