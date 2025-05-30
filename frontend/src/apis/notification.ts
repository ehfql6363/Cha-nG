import { Notification } from '@/types/notification'

import { getRequest, putRequest } from './api'

//markNotificationAsRead
export const markNotificationAsRead = async (notificationIds: number[]) =>
  await putRequest<Notification[]>(`/notification/read`, {
    notificationIds: notificationIds,
  })

//getNotifications
export const getNotifications = async (userId: number) =>
  await getRequest<Notification[]>(`/notification?userId=${userId}`)

//getUnreadNotificationCount
export const getUnreadNotificationCount = async (userId: number) =>
  await getRequest<{ count: number }>(`/notification/count?userId=${userId}`)
