'use client'

import React, { useEffect, useState } from 'react'
import { useDispatch } from 'react-redux'

import { postNotApprovedIds } from '@/apis/lifeRule'
import { Image } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import { setNotApprovedIds } from '@/store/slices/lifeRuleSlice'
import { UserTileContainer } from '@/styles/styles'
import { User } from '@/types/user'

import { ProfileContainer, ProfileList } from './styles'

export function ApproveProfile() {
  const dispatch = useDispatch()
  const user = useAppSelector((state) => state.user)
  const group = useAppSelector((state) => state.group.group)
  const notApprovedId: number[] = useAppSelector(
    (state) => state.lifeRule.notApprovedIds,
  )

  useEffect(() => {
    const fetchRent = async () => {
      const response = await postNotApprovedIds(group.id)
      if (response.success) {
        dispatch(setNotApprovedIds(response.data.notApprovedIds))
      }
    }
    fetchRent()
  }, [group, dispatch])

  const [approvedUser, setApprovedUser] = useState<User[]>([])
  const [notApprovedUser, setNotApprovedUser] = useState<User[]>([])

  useEffect(() => {
    setApprovedUser(
      group.members.filter((item) => !notApprovedId.includes(item.id)),
    )
    setNotApprovedUser(
      group.members.filter((item) => notApprovedId.includes(item.id)),
    )
  }, [group.members, notApprovedId])

  return (
    <ProfileList>
      <UserTileContainer>
        {approvedUser.map((user) => (
          <ProfileContainer
            key={user.id}
            isApprove={true}>
            <Image
              src={`/images/etc/approved-circle.svg`}
              alt={user.name}
              width={40}
              height={40}
            />
            <span>{user.nickname}</span>
          </ProfileContainer>
        ))}
        {notApprovedUser.map((user) => (
          <ProfileContainer
            key={user.id}
            isApprove={false}>
            <Image
              src={`/images/profile/${user.profileImage}.svg`}
              alt={user.name}
              width={40}
              height={40}
            />
            <span>{user.nickname}</span>
          </ProfileContainer>
        ))}
      </UserTileContainer>
    </ProfileList>
  )
}
