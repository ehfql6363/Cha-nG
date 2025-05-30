import {
  CreateLifeRuleRequest,
  GetLifeRuleResponse,
  PostLifeRuleResponse,
  UpdateLifeRule,
} from '@/types/lifeRule'

import { getRequest, postBooleanRequest, postRequest } from './api'

//getLifeRule 생활 룰 조회
export const getLifeRule = async () =>
  await getRequest<GetLifeRuleResponse>(`/life-rule`)

//createLifeRule 생활 룰 생성
export const createLifeRule = async (rules: CreateLifeRuleRequest) =>
  await postRequest<PostLifeRuleResponse>(`/life-rule`, rules)

//getUpdateLifeRule 변경 요청된 룰 목록 조회
export const getUpdateLifeRule = async () =>
  await getRequest<UpdateLifeRule[]>(`/life-rule/update`)

//updateLifeRule 생활 룰 변경 요청
export const updateLifeRule = async (params: { updates: UpdateLifeRule[] }) =>
  await postRequest<UpdateLifeRule[]>(`/life-rule/update`, params)

//getNotApprovedIds 변경 요청된 룰 목록 조회
export const postNotApprovedIds = async (groupId: number) =>
  await postRequest<{ notApprovedIds: number[] }>(
    `/life-rule/not-approved/${groupId}`,
  )

//recommendCategory0
export const recommendCategory = async (params: { content: string }) =>
  await postRequest<{ category: string }>(`/life-rule/category`, params)

//approveUpdateForm
export const approveUpdateForm = async (params: { approved: boolean }) =>
  await postBooleanRequest(`/life-rule/approve`, params)
