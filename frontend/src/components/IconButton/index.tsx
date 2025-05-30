import { Image } from '@/components'

import { StyledIconButton } from './styles'

type IconButtonProps = {
  src: string
  alt?: string
  onClick?: () => void
}

export const IconButton = ({ src, alt = 'icon', onClick }: IconButtonProps) => (
  <StyledIconButton onClick={onClick}>
    <Image
      src={src}
      alt={alt}
      width={24}
      height={24}
    />
  </StyledIconButton>
)
