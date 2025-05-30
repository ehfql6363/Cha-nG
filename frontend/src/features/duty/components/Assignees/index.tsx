'use client'

import { useState } from 'react'

import { Modal, UserItem } from '@/components'
import { CenterContainer, DefaultLabel, SlimContainer } from '@/styles/styles'
import { User } from '@/types/user'

import { Container } from './styles'

interface AssigneesProps {
  assignees: number[]
  userList: User[]
}

export const Assignees = ({ assignees, userList }: AssigneesProps) => {
  const assigneeIdSet = new Set(assignees)
  const assigneesList = userList.filter((item) => assigneeIdSet.has(item.id))
  const [open, setOpen] = useState(false)
  return (
    <Container>
      <div onClick={() => setOpen(true)}>
        {assigneesList.slice(0, 1).map((item) => (
          <UserItem
            key={item.id}
            user={item}
          />
        ))}
        {assigneesList.length > 1 && (
          <DefaultLabel>{`+${assigneesList.length - 1}`}</DefaultLabel>
        )}
      </div>
      <Modal
        open={open}
        title="당번들"
        description="당번들을 확인할 수 있습니다."
        onOpenChange={setOpen}
        onConfirm={() => setOpen(false)}>
        <SlimContainer>
          {assigneesList.map((item) => (
            <CenterContainer key={item.id}>
              <UserItem
                user={item}
                variant="bar"
              />
            </CenterContainer>
          ))}
        </SlimContainer>
      </Modal>
    </Container>
  )
}
