export interface Duty {
  id: number
  title: string
  category: string
  dutyTime: string
  dayOfWeek: DayKey
  useTime: boolean
  assignees: number[]
}

export type DutyRequest = Omit<Duty, 'id'>

export interface DutyWeekList {
  monday: Duty[]
  tuesday: Duty[]
  wednesday: Duty[]
  thursday: Duty[]
  friday: Duty[]
  saturday: Duty[]
  sunday: Duty[]
}

export interface DutyCategory {
  id: string
  src: string
}

export type DayKey =
  | 'monday'
  | 'tuesday'
  | 'wednesday'
  | 'thursday'
  | 'friday'
  | 'saturday'
  | 'sunday'
// 요일 타입 정의

export type SelectorVariant = 'select' | 'sunday' | 'saturday' | 'default'


