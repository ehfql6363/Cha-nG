'use client'

import { useEffect, useRef } from 'react'
import { useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import {
  AnimatedImage,
  Image,
  InputBox,
  TitleHeader,
  TitleHeaderLayout,
} from '@/components'
import '@/components/TitleHeader/styles'
import { useAppSelector } from '@/hooks/useAppSelector'
import { setGroupName, setMaxParticipants } from '@/store/slices/groupSlice'
import { Title, TitleCenter } from '@/styles/styles'
import { CreateGroupRequest } from '@/types/group'
import { ImageVariant } from '@/types/ui'

import {
  HomeImageContainer,
  ImageButton,
  Label,
  ParticipantsContainer,
  UserContainer,
} from './styles'

export function CreateGroupPage() {
  const { t } = useTranslation()
  const dispatch = useDispatch()
  const router = useRouter()
  const create = useAppSelector((state) => state.group.create)
  const participantsRef = useRef<HTMLDivElement>(null)

  const {
    register,
    watch,
    getValues,
    setValue,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateGroupRequest>({
    defaultValues: {
      groupName: create.groupName,
      maxParticipants: create.maxParticipants,
    },
  })
  const groupName = watch('groupName')
  const participants = watch('maxParticipants')

  useEffect(() => {
    return () => {
      const values = getValues()
      dispatch(setGroupName(values.groupName))
      dispatch(setMaxParticipants(values.maxParticipants))
    }
  }, [dispatch])

  useEffect(() => {
    if (participantsRef.current) {
      participantsRef.current.focus()
    }
  }, [participantsRef])

  const onSubmit = (data: CreateGroupRequest) => {
    dispatch(setGroupName(data.groupName))
    dispatch(setMaxParticipants(data.maxParticipants))
    router.push('/group/create/createProfile')
  }
  const increaseParticipants = () => {
    setValue('maxParticipants', Math.min(10, participants + 1))
  }
  const decreaseParticipants = () => {
    setValue('maxParticipants', Math.max(1, participants - 1))
  }

  const handleKeyDown = (e: React.KeyboardEvent<HTMLDivElement>) => {
    if (e.key === 'ArrowUp') {
      e.preventDefault()
      increaseParticipants()
    } else if (e.key === 'ArrowDown') {
      e.preventDefault()
      decreaseParticipants()
    }
  }
  return (
    <TitleHeaderLayout
      title={t('createGroup.title')}
      header={t('createGroup.header')}
      label={t('createGroup.confirm')}
      onClick={handleSubmit(onSubmit)}
      buttonVariant={groupName ? 'next' : 'disabled'}
      gap="20px">
      <HomeImageContainer>
        <Image
          src="/images/group/roof.svg"
          alt="plus"
          width={326}
          height={61}
        />
        <UserContainer>
          {Array.from({ length: participants }).map((_, index) => (
            <AnimatedImage
              key={index}
              src="/images/group/user-default.svg"
              alt="plus"
              width={50}
              height={50}
              variant={ImageVariant.pop}
            />
          ))}
        </UserContainer>
        <Image
          src="/images/group/floor.svg"
          alt="plus"
          width={272}
          height={9}
        />
      </HomeImageContainer>

      <div>
        <Label>{t('createGroup.maxParticipants.label')}</Label>
        <ParticipantsContainer
          ref={participantsRef}
          onKeyDown={handleKeyDown}
          tabIndex={0}
          role="button"
          aria-label="참가자 수 조절">
          <ImageButton onClick={decreaseParticipants}>
            <Image
              src="/icons/minus.svg"
              alt="minus"
              width={28}
              height={28}
            />
          </ImageButton>
          <TitleCenter> {participants + ''}</TitleCenter>
          <ImageButton onClick={increaseParticipants}>
            <Image
              src="/icons/plus.svg"
              alt="plus"
              width={28}
              height={28}
            />
          </ImageButton>
        </ParticipantsContainer>
      </div>
      <InputBox
        {...register('groupName')}
        label={t('createGroup.groupName.label')}
        id="groupName"
        {...register('groupName', {
          required: t('createGroup.groupName.error.required'),
          maxLength: {
            value: 20,
            message: t('createGroup.groupName.error.maxLength'),
          },
        })}
        placeholder={t('createGroup.groupName.placeholder')}
        error={errors.groupName}
      />
    </TitleHeaderLayout>
  )
}
