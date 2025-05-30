import { useMemo, useState } from 'react'

import { DayKey } from '@/types/duty'

const getCurrentWeek = (): DayKey => {
  const today = new Date()
  const day = today.getDay()
  const weeks: DayKey[] = [
    'sunday',
    'monday',
    'tuesday',
    'wednesday',
    'thursday',
    'friday',
    'saturday',
  ]
  return weeks[day]
}

const useSelectWeek = (initialWeek?: DayKey) => {
  const [selectedWeek, setSelectedWeek] = useState<DayKey>(initialWeek || getCurrentWeek())

  return useMemo(
    () => ({
      selectedWeek,
      setSelectedWeek,
    }),
    [selectedWeek],
  )
}

export default useSelectWeek
