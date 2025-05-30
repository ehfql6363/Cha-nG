import { LoginUser } from '@/types/user'
import { HomeOverview, UserSummary, UserUpdateRequest } from '@/types/user'

import { getRequest, putRequest } from './api'

//getMySummary
export const getMySummary = async () =>
  await getRequest<UserSummary>('/users/me/summary')

//getHomeOverview
export const getHomeOverview = async () =>
  await getRequest<HomeOverview>('/home')

//putUserUpdate
export const updateProfile = async (params: UserUpdateRequest) =>
  await putRequest<LoginUser>('/users/me/update', params)

export const getUserInfo = async () =>
  await getRequest<LoginUser>(`/users/me/info`)
