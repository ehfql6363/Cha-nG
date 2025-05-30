'use client'

import { useEffect, useMemo, useRef, useState } from 'react'
import { useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { createGroup, joinGroup } from '@/apis/group'
import { InputBox, TitleHeaderLayout } from '@/components'
import { profileList } from '@/constants/profileList'
import { useAppSelector } from '@/hooks/useAppSelector'
import {
  clearCreate,
  clearJoin,
  setGroup,
  setJoinNickname,
  setJoinProfileImage,
  setOwnerNickname,
  setOwnerProfileImage,
} from '@/store/slices/groupSlice'
import {
  setGroupId,
  setUserNickname,
  setUserProfileImage,
} from '@/store/slices/userSlice'
import { SimpleMain } from '@/styles/styles'
import { ButtonVariant } from '@/types/ui'

import { ProfileSelector } from '../components/ProfileSelector'

export function CreateProfilePage({ leader }: { leader: boolean }) {
  const { t } = useTranslation()
  const dispatch = useDispatch()
  const router = useRouter()
  const create = useAppSelector((state) => state.group.create)
  const join = useAppSelector((state) => state.group.join)
  const group = useAppSelector((state) => state.group.group)

  const defaultProfileImage = useMemo(() => {
    const profileImage = leader ? create.ownerProfileImage : join.profileImage
    if (profileImage === '') {
      const randomIndex = Math.floor(Math.random() * profileList.length)
      return profileList[randomIndex].id
    }
    return profileImage
  }, [leader, create.ownerProfileImage, join.profileImage])

  const {
    register,
    watch,
    setValue,
    handleSubmit,
    formState: { errors },
  } = useForm<{ nickname: string; profileImage: string }>({
    defaultValues: {
      nickname: leader ? create.ownerNickname : join.nickname,
      profileImage: defaultProfileImage,
    },
  })
  const nickname = watch('nickname')
  const profileImage = watch('profileImage')

  if (leader) {
  }
  const existingNickname = useAppSelector(
    (state) => state.group.create.ownerNickname,
  )

  useEffect(() => {
    if (existingNickname) {
      setValue('nickname', existingNickname)
    }
  }, [existingNickname, setValue])
  const [isConfirm, setIsConfirm] = useState(false)
  const hadConfirmed = useRef(false)
  const onSubmit = async ({
    nickname,
    profileImage,
  }: {
    nickname: string
    profileImage: string
  }) => {
    if (hadConfirmed.current) {
      return
    }
    hadConfirmed.current = true
    let response = null
    if (leader) {
      dispatch(setOwnerNickname(nickname))
      dispatch(setOwnerProfileImage(profileImage))
      response = await createGroup({
        ownerNickname: nickname,
        ownerProfileImage: profileImage,
        groupName: create.groupName,
        maxParticipants: create.maxParticipants,
      })
      if (response.success) {
        await dispatch(clearCreate())
        setIsConfirm(true)
      }
    } else {
      dispatch(setJoinNickname(nickname))
      dispatch(setJoinProfileImage(profileImage))
      response = await joinGroup({
        nickname: nickname,
        profileImage: profileImage,
        groupId: join.groupId,
      })
      if (response.success) {
        await dispatch(clearJoin())
        setIsConfirm(true)
      }
    }
    if (response.success) {
      await dispatch(setGroup(response.data))
      await dispatch(setGroupId(response.data.id))
      await dispatch(setUserNickname(nickname))
      await dispatch(setUserProfileImage(profileImage))
      setIsConfirm(true)
    }
    hadConfirmed.current = false
  }

  useEffect(() => {
    const handleRouting = async () => {
      if (isConfirm && group?.id) {
        if (leader) {
          await router.push('/group/create/shareInviteCode')
        } else {
          await router.push('/')
        }
      }
    }
    handleRouting()
  }, [group, router, isConfirm, leader])

  const blockList = useAppSelector(
    (state) =>
      state.group.group.members?.map((member) => member.profileImage ?? '') ??
      [],
  )
  return (
    <TitleHeaderLayout
      title={t('createProfile.title')}
      header={t('createProfile.header')}
      description={t('createProfile.description')}
      label={t('createProfile.confirm')}
      onClick={handleSubmit(onSubmit)}
      buttonVariant={nickname ? ButtonVariant.next : ButtonVariant.disabled}>
      <SimpleMain>
        <ProfileSelector
          selectedId={profileImage}
          onSelect={(id) => setValue('profileImage', id)}
          blockList={blockList}
        />
        <InputBox
          id="nickname"
          label={t('createProfile.nickname.label')}
          {...register('nickname', {
            required: t('createProfile.nickname.error.required'),
          })}
          placeholder={t('createProfile.nickname.placeholder')}
          error={errors.nickname}
        />
      </SimpleMain>
    </TitleHeaderLayout>
  )
}
