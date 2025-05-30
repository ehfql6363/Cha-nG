import { Duty, DutyRequest, DutyWeekList } from '@/types/duty'

import { deleteRequest, getRequest, patchRequest, postRequest } from './api'

//getDuties 당번 리스트 조회
export const getDuties = async (groupId: number) =>
  await getRequest<DutyWeekList>(`/duty/${groupId}`)

//createDuty 당번 생성
export const createDuty = async (groupId: number, duty: DutyRequest) =>
  await postRequest<Duty>(`/duty/${groupId}`, duty)

//recommendCategory 카테고리 추천
export const recommendCategory = async (content: string) =>
  await postRequest<{ category: string }>(`/duty/category`, { content })

//deleteDuty 당번 삭제
export const deleteDuty = async (dutyId: number) =>
  await deleteRequest<{ deletedId: number }>(`/duty/${dutyId}`)

//modifyDuty 당번 수정
export const modifyDuty = async (dutyId: number, duty: DutyRequest) =>
  await patchRequest<Duty>(`/duty/${dutyId}`, duty)
