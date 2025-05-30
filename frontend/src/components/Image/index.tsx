'use client'

import { useEffect, useState } from 'react'

import NextImage from 'next/image'

import { ImageProps } from '@/types/ui'

export const Image = ({
  src,
  alt = '',
  width = 80,
  height = 80,
  errorSrc = '/images/lifeRule/life-rule-OTHER-inactive.svg',
  styles,
  ...props
}: ImageProps) => {
  const [imgSrc, setImgSrc] = useState(src)

  const handleImageError = () => {
    setImgSrc(errorSrc)
  }
  useEffect(() => {
    setImgSrc(src)
  }, [src])

  return (
    <NextImage
      src={imgSrc}
      alt={alt}
      width={width}
      height={height}
      onError={handleImageError}
      unoptimized={true}
      style={styles}
      {...props}
    />
  )
}
