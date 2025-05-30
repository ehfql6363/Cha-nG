'use client'

import { useTranslation } from 'react-i18next'

import { useRouter } from 'next/navigation'

import { TitleHeaderLayout } from '@/components'
import { Image } from '@/components'
import { IconButton } from '@/components/IconButton'
import { Label } from '@/components/InputBox/styles'
import { HeaderButton } from '@/components/TopHeader/styles'
import { useAppSelector, useCopyInviteCode } from '@/hooks'
import { BankLabel, ImageContainer, ShowCenterBox } from '@/styles/styles'

export function ShareInviteCodePage() {
  const { t } = useTranslation()
  const router = useRouter()
  const group = useAppSelector((state) => state.group.group)
  const user = useAppSelector((state) => state.user.user)

  // 훅을 컴포넌트의 최상위 레벨에서 호출
  const copyInviteCode = useCopyInviteCode(
    group.inviteCode,
    user.name,
    group.name,
  )

  const handleCopy = () => {
    copyInviteCode()
  }
  return (
    <TitleHeaderLayout
      title={t('shareInviteCode.title')}
      header={t('shareInviteCode.header')}
      description={t('shareInviteCode.description')}
      onClick={() => {
        router.push('/')
      }}
      label={'goToHome'}>
      <div>
        <ImageContainer>
          <Image
            src="/images/group/invite-code.svg"
            alt="inviteCode"
            width={106}
            height={105}
          />
        </ImageContainer>
        <Label>{t('shareInviteCode.inviteCode.label')}</Label>
        <ShowCenterBox>
          <HeaderButton />
          <BankLabel>
            {group.inviteCode}
            <IconButton
              onClick={handleCopy}
              src="/icons/copy.svg"
              alt="copy"
            />
          </BankLabel>
        </ShowCenterBox>
      </div>
    </TitleHeaderLayout>
  )
}
