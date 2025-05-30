'use client'

import React from 'react'

import { useRouter } from 'next/navigation'

import { Image } from '@/components'

import { DivContainer, NoticeBarContainer } from './styles'

interface NoticeBarProps {
  message: string
}

export const NoticeBar: React.FC<NoticeBarProps> = ({ message }) => {
  const router = useRouter()
  const handleClick = () => {
    router.push('/lifeRule/updateApprove')
  }
  return (
    <NoticeBarContainer onClick={handleClick}>
      <DivContainer>
        <Image
          src="/icons/announce.svg"
          alt="notice"
          width={20}
          height={20}
        />

        {message}
      </DivContainer>
      <Image
        src="/icons/arrow-right.svg"
        alt="notice"
        width={20}
        height={20}
      />
    </NoticeBarContainer>
  )
}
