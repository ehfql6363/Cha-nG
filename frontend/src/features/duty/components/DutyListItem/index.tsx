'use client'

import { useTranslation } from 'react-i18next'

import { Image } from '@/components'
import { dutyCategoryList } from '@/constants/duty'
import { Duty } from '@/types/duty'
import { User } from '@/types/user'
import { formatHourMinuteTime } from '@/utils/formatTime'

import { Assignees } from '../Assignees'
import { CatrgoryIcon, Container, Content, DutyInfo } from './styles'

interface DutyListItemProps {
  duty: Duty
  userList: User[]
  onSelectDuty: (duty: Duty) => void
}

export const DutyListItem = ({
  duty,
  userList,
  onSelectDuty,
}: DutyListItemProps) => {
  const { t } = useTranslation()

  return (
    <Container>
      <CatrgoryIcon>
        <Image
          src={
            dutyCategoryList.find((category) => category.id === duty.category)
              ?.src ?? '/images/duty/duty-OTHER.svg'
          }
          alt={duty.category}
          width={32}
          height={32}
          errorSrc="/images/duty/duty-OTHER.svg"
        />
      </CatrgoryIcon>
      <DutyInfo>
        <Content>
          <div>
            {t(`duty.category.${duty.category}`)} {'|'}
            {duty.useTime ? formatHourMinuteTime(duty.dutyTime) : ''}
          </div>
          <div>{duty.title}</div>
        </Content>
        <Assignees
          key={duty.id}
          assignees={duty.assignees}
          userList={userList}
        />
      </DutyInfo>

      <Image
        src="/icons/menu.svg"
        alt={duty.category}
        width={12}
        height={12}
        onClick={() => onSelectDuty(duty)}
      />
    </Container>
  )
}
