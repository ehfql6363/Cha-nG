'use client'

import React, { useEffect, useState } from 'react'
import { FormProvider, useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useParams, useRouter } from 'next/navigation'

import { createDuty, modifyDuty } from '@/apis/duty'
import { BottomSheet, TitleHeaderLayout } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import {
  clearCreateDayOfWeek,
  setCompleteDayOfWeek,
} from '@/store/slices/dutySlice'
import { DutyRequest } from '@/types/duty'

import useSelectWeek from '../hooks/useSelectWeek'
import { AssigneesSelectContent } from './components/AssigneesSelectContent'
import { AssigneesSelector } from './components/AssigneesSelector'
import { TimeSelector } from './components/TimeSelector'
import { TitleSelector } from './components/TitleSelector'
import { WeekSelector } from './components/WeekSelector'
import { FullMain } from './styles'

export function DutyEdit() {
  const { t } = useTranslation()
  const router = useRouter()
  const dispatch = useDispatch()

  // 수정모드 판단
  const { id } = useParams()
  const isEditMode = Boolean(id)

  const dutyWeekList = useAppSelector((state) => state.duty.dutyWeekList)
  const editDuty = isEditMode
    ? Object.values(dutyWeekList)
        .flat()
        .find((duty) => String(duty.id) === id) || null
    : null

  // 요일 상태관리
  const createDayOfWeek = useAppSelector((state) => state.duty.createDayOfWeek) // 요일 상태 가져오기
  const { selectedWeek, setSelectedWeek } = useSelectWeek(
    createDayOfWeek || undefined,
  )

  // duty 생성 및 수정 폼
  const methods = useForm<DutyRequest>({
    defaultValues: {
      title: editDuty?.title || '',
      category: editDuty?.category || 'CLEAN',
      dutyTime: editDuty?.dutyTime || '',
      dayOfWeek: editDuty?.dayOfWeek || selectedWeek,
      useTime: editDuty?.useTime || false,
      assignees: editDuty?.assignees || [],
    },
  })
  const { watch, handleSubmit, setValue } = methods

  const assignees = watch('assignees')
  const dutyTime = watch('dutyTime')

  useEffect(() => {
    setValue('dayOfWeek', selectedWeek)
  }, [selectedWeek, setValue])

  useEffect(() => {
    return () => {
      dispatch(clearCreateDayOfWeek())
    }
  }, [dispatch]) // 페이지 unMount 시 dayOfWeek 비워주기

  const [isBottomSheetOpen, setIsBottomSheetOpen] = useState(false)
  const group = useAppSelector((state) => state.group.group)
  const handleSaveUsers = (selected: number[]) => {
    setValue('assignees', selected)
    setIsBottomSheetOpen(false)
  }

  const userList = group.members
  const sizeOfUserList = userList.length <= 4 ? userList.length : 4 // BottomSheet 사이즈 제한

  const calculateMaxSnapPoint = () => {
    const windowHeight = window.innerHeight // 화면 높이
    const userListHeight = sizeOfUserList * 90 + 200 // 각 사용자 항목의 높이를 80px로 가정
    return Math.min(userListHeight / windowHeight, 0.9) // 최대 90%를 넘지 않도록 제한
  }

  const onSubmit = async (data: DutyRequest) => {
    data.useTime = dutyTime !== '' // useTime 값 처리

    if (isEditMode) {
      const response = await modifyDuty(editDuty.id, data)
      if (response.success) {
        router.push('/duty')
        dispatch(setCompleteDayOfWeek(data.dayOfWeek))
      }
    } else {
      const response = await createDuty(group.id, data)
      if (response.success) {
        router.push('/duty')
        dispatch(setCompleteDayOfWeek(data.dayOfWeek))
      }
    }
  }

  return (
    <FormProvider {...methods}>
      <TitleHeaderLayout
        title={t('duty.title')}
        description={t('duty.description')}
        label="저장"
        onClick={handleSubmit(onSubmit)}>
        <FullMain>
          <WeekSelector
            selectedWeek={selectedWeek}
            setSelectedWeek={setSelectedWeek}
          />
          <TimeSelector
            time={dutyTime}
            setTime={(time: string) => setValue('dutyTime', time)}
            useTime={editDuty?.useTime || false}
          />

          <TitleSelector />

          <AssigneesSelector
            setIsBottomSheetOpen={setIsBottomSheetOpen}
            assignees={assignees}
            userList={userList}
          />

          <BottomSheet
            open={isBottomSheetOpen}
            onOpenChange={setIsBottomSheetOpen}
            snapPoints={{
              MIN: 0.1,
              MID: calculateMaxSnapPoint(),
              MAX: calculateMaxSnapPoint(),
            }}>
            <AssigneesSelectContent
              userList={userList}
              assignees={assignees}
              onConfirm={handleSaveUsers}
            />
          </BottomSheet>
        </FullMain>
      </TitleHeaderLayout>
    </FormProvider>
  )
}
