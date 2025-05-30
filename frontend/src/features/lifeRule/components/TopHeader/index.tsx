'use client'

import { memo } from 'react'

import { useRouter } from 'next/navigation'

import { Image } from '@/components'

import { Container, HeaderButton, Title } from './styles'

interface TopHeaderProps {
  isUpdated: boolean
  handleOpenModal: () => void
}

export const TopHeader = memo(function TopHeader({
  isUpdated,
  handleOpenModal,
}: TopHeaderProps) {
  const router = useRouter()

  const handleUpdateClick = () => {
    if (isUpdated) {
      handleOpenModal()
    } else {
      router.push('/lifeRule/update')
    }
  }

  return (
    <div
      onClick={handleUpdateClick}
      style={{ cursor: isUpdated ? 'not-allowed' : 'pointer' }}>
      <Image
        src="/icons/modify.svg"
        alt="수정"
        width={30}
        height={30}
      />
    </div>
  )
})
