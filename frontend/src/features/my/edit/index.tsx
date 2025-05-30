'use client'

import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { updateProfile } from '@/apis/user'
import { InputBox, TitleHeaderLayout } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import { setUserNickname, setUserProfileImage } from '@/store/slices/userSlice'
import { ButtonVariant } from '@/types/ui'

import { ProfileSelector } from '../components/ProfileSelector'
import { TopContainer } from './styles'

export function EditProfilePage() {
  const { t } = useTranslation()
  const dispatch = useDispatch()
  const router = useRouter()

  const user = useAppSelector((state) => state.user)
  const defaultProfileImage = 'user1'

  const {
    register,
    watch,
    setValue,
    handleSubmit,
    formState: { errors },
  } = useForm<{ nickname: string; profileImage: string }>({
    defaultValues: {
      nickname: user.user.nickname ?? '',
      profileImage: user.user.profileImage ?? defaultProfileImage,
    },
  })
  const nickname = watch('nickname')
  const profileImage = watch('profileImage')

  // api 쏘는곳
  const onSubmit = async ({
    nickname,
    profileImage,
  }: {
    nickname: string
    profileImage: string
  }) => {
    let response = null
    response = await updateProfile({
      nickname: nickname,
      profileImage: profileImage,
    })
    if (response.success) {
      // 성공하면 redux에 저장해주기
      await dispatch(setUserNickname(nickname))
      await dispatch(setUserProfileImage(profileImage))
      setIsConfirm(true)
    }
  }

  // 확인 버튼 누르면 원래화면으로 이동
  const [isConfirm, setIsConfirm] = useState(false)
  useEffect(() => {
    const handleRouting = async () => {
      if (isConfirm) {
        await router.push('/my')
      }
    }
    handleRouting()
  }, [router, isConfirm])

  const blockList = useAppSelector(
    (state) =>
      state.group.group.members?.map((member) => member.profileImage ?? '') ??
      [],
  )

  return (
    <TitleHeaderLayout
      title={t('my.edit.title')}
      label={t('my.edit.confirm')}
      header={t('my.edit.header')}
      onClick={handleSubmit(onSubmit)}
      buttonVariant={nickname ? ButtonVariant.next : ButtonVariant.disabled}>
      <TopContainer>
        <ProfileSelector
          selectedId={profileImage}
          onSelect={(id) => setValue('profileImage', id)}
          blockList={blockList}
        />
      </TopContainer>
      <InputBox
        id="nickname"
        label={t('createProfile.nickname.label')}
        {...register('nickname', {
          required: t('createProfile.nickname.error.required'),
        })}
        placeholder={t('createProfile.nickname.placeholder')}
        error={errors.nickname}
        value={nickname}
      />
    </TitleHeaderLayout>
  )
}
