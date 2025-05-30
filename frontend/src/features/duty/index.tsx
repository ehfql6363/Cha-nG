'use client'

import React, { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { getDuties } from '@/apis/duty'
import { BottomNavigation, BottomSheet, TopHeader } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import { setDutyWeekList } from '@/store/slices/dutySlice'
import { clearCompleteDayOfWeek } from '@/store/slices/dutySlice'
import { SimpleMain } from '@/styles/styles'
import { Duty } from '@/types/duty'

import { DutyList } from './components/DutyList'
import { EditOrDeleteDuty } from './components/EditOrDeleteDuty'
import { WeekList } from './components/WeekList'
import useSelectWeek from './hooks/useSelectWeek'
import { Container } from './styles'

export function DutyPage() {
  const { t } = useTranslation()
  const group = useAppSelector((state) => state.group.group) // 그룹정보 받아오는 커스텀 훅
  const dutyWeekList = useAppSelector((state) => state.duty.dutyWeekList)
  const dispatch = useDispatch()
  const userList = group.members

  const completeDayOfWeek = useAppSelector(
    (state) => state.duty.completeDayOfWeek,
  )
  const { selectedWeek, setSelectedWeek } = useSelectWeek()

  const [isBottomSheetOpen, setIsBottomSheetOpen] = useState<boolean>(false)
  const [selectedDuty, setSelectedDuty] = useState<Duty | null>(null)

  const calculateMaxSnapPoint = () => {
    const windowHeight = window.innerHeight // 화면 높이
    return Math.min((0.3 * 740) / windowHeight, 0.9) // 최대 90%를 넘지 않도록 제한
  }

  const handleSelectDuty = (duty: Duty) => {
    setSelectedDuty(duty)
    setIsBottomSheetOpen(true)
  }

  useEffect(() => {
    if (completeDayOfWeek) {
      setSelectedWeek(completeDayOfWeek)
      dispatch(clearCompleteDayOfWeek())
    }
  }, [completeDayOfWeek, dispatch, setSelectedWeek])

  useEffect(() => {
    const fetchDuties = async () => {
      const response = await getDuties(group.id) // dutyList
      if (response.success) {
        dispatch(setDutyWeekList(response.data))
      }
    }
    fetchDuties()
  }, [])

  return (
    <Container>
      <TopHeader title={t('duty.title')} />
      <SimpleMain>
        <WeekList
          dutyList={dutyWeekList}
          selectedWeek={selectedWeek}
          setSelectedWeek={setSelectedWeek}
        />
        <DutyList
          dutyList={dutyWeekList}
          selectedWeek={selectedWeek}
          userList={userList}
          onSelectDuty={handleSelectDuty}
        />
        <BottomSheet
          open={isBottomSheetOpen}
          onOpenChange={setIsBottomSheetOpen}
          snapPoints={{
            MIN: 0.1,
            MID: calculateMaxSnapPoint(),
            MAX: calculateMaxSnapPoint(),
          }}>
          <EditOrDeleteDuty
            selectedDuty={selectedDuty}
            setOpen={setIsBottomSheetOpen}
          />
        </BottomSheet>
      </SimpleMain>
      <BottomNavigation />
    </Container>
  )
}
