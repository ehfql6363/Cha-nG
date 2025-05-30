'use client'

import React, { useEffect, useRef, useState } from 'react'

import { getNotifications, markNotificationAsRead } from '@/apis/notification'
import { NavLayout } from '@/components/layouts/NavLayout'
import { useAppSelector, useFormattedTime } from '@/hooks'
import { Notification } from '@/types/notification'

import {
  CategoryContainer,
  NotificationContent,
  NotificationDate,
  NotificationHeader,
  NotificationItem,
  NotificationTitle,
} from './styles'

const NotificationItemComponent = ({
  notification,
}: {
  notification: Notification
}) => {
  const formattedTime = useFormattedTime(new Date(notification.date).getTime())
  return (
    <NotificationItem>
      <CategoryContainer
        src={
          notification?.category
            ? `/images/notification/notification-${notification.category.toLocaleLowerCase()}.svg`
            : '/images/notification/notification-etc.svg'
        }
        alt="notification"
        width={40}
        height={40}
      />
      <div>
        <NotificationHeader>
          <NotificationTitle>{notification.title}</NotificationTitle>
          <NotificationDate>{formattedTime}</NotificationDate>
        </NotificationHeader>
        <NotificationContent>{notification.content}</NotificationContent>
      </div>
    </NotificationItem>
  )
}

export function NotificationPage() {
  const { user } = useAppSelector((state) => state.user)
  const [notifications, setNotifications] = useState<Notification[]>([])

  const fetchNoti = useRef(false)
  useEffect(() => {
    const fetchNotification = async () => {
      if (!user.id || fetchNoti.current) return
      fetchNoti.current = true
      const response = await getNotifications(user.id)
      if (response.success) {
        setNotifications(response.data)
      }
    }
    fetchNotification()
  }, [user.id])
  const notiRead = useRef(false)

  useEffect(() => {
    const readNotification = async () => {
      if (notifications.length > 0 || !notiRead.current) {
        notiRead.current = true
        const map = notifications.map((notification) => notification.id)
        await markNotificationAsRead(map)
      }
    }
    readNotification()
  }, [notifications])
  return (
    <NavLayout title="알림">
      {notifications.map((notification) => (
        <NotificationItemComponent
          key={notification.id}
          notification={notification}
        />
      ))}
    </NavLayout>
  )
}
