'use client'

import { Image } from '@/components'
import { AnimatedImageProps, ImageVariant } from '@/types/ui'

import { AnimatedImageWrapper } from './styles'

export const AnimatedImage = ({
  src,
  alt,
  width,
  height,
  errorSrc,
  variant = ImageVariant.pop,
  styles,
  ...props
}: AnimatedImageProps) => {
  return (
    <AnimatedImageWrapper variant={variant}>
      <Image
        src={src}
        alt={alt}
        width={width}
        height={height}
        errorSrc={errorSrc}
        style={styles}
        {...props}
      />
    </AnimatedImageWrapper>
  )
}
