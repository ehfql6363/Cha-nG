'use client'

import { UserItem } from '@/components'
import { User } from '@/types/user'

import { Container, ProfileContainer } from './styles'

interface AssigneesProps {
  assignees: number[]
  userList: User[]
}

export const AssigneesSelected = ({ assignees, userList }: AssigneesProps) => {
  const assigneeIdSet = new Set(assignees)
  const assigneesList = userList.filter((item) => assigneeIdSet.has(item.id))

  return (
    <Container>
      {assigneesList.map((item) => (
        <ProfileContainer key={item.id}>
          <UserItem
            user={item}
            variant="tile"
            size="medium"
          />
        </ProfileContainer>
      ))}
    </Container>
  )
}
