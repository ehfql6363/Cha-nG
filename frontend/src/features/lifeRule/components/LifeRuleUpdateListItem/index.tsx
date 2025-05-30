'use client'

import React from 'react'

import { IconButton, Image } from '@/components'
import { LifeRule, LifeRuleUpdateVariant } from '@/types/lifeRule'

import { InputBox } from '../InputBox'
import {
  ActionButtons,
  CatrgoryIcon,
  Content,
  CreateButton,
  DeleteButton,
  ItemContainer,
  UpdateButton,
} from './styles'

interface LifeRuleUpdateListItemProps {
  lifeRule: LifeRule
  variant: LifeRuleUpdateVariant
  actionType: LifeRuleUpdateVariant
  content: string
  setVariant: (variant: LifeRuleUpdateVariant, content: string) => void
  onContentChange: (content: string) => void
  onAddItem: (index: number) => void
  onUpdateConfirm: () => void
  index: number
}

const ActionButton = ({
  type,
  onClick,
}: {
  type: 'update' | 'delete'
  onClick: () => void
}) => {
  return (
    <IconButton
      onClick={onClick}
      src={`/icons/button-${type == 'delete' ? 'delete' : 'modify'}.svg`}
      alt={type}
    />
  )
}

//ActionButton.displayName = 'ActionButton'

export const LifeRuleUpdateListItem = ({
  lifeRule,
  variant,
  content,
  setVariant,
  onContentChange,
  onAddItem,
  onUpdateConfirm,
  index,
}: Omit<LifeRuleUpdateListItemProps, 'actionType'>) => {
  //const { t } = useTranslation()

  const renderContent = () => {
    switch (variant) {
      case 'DELETE':
        return (
          <>
            <p>{content}</p>
            <DeleteButton onClick={() => setVariant('DEFAULT', content)}>
              취소
            </DeleteButton>
          </>
        )
      case 'UPDATE':
        return (
          <>
            <InputBox
              id={`update-${lifeRule.id}`}
              value={content}
              onChange={(e) => onContentChange(e.target.value)}
              placeholder="수정할 내용을 입력하세요"
            />
            <UpdateButton onClick={onUpdateConfirm}>확인</UpdateButton>
          </>
        )
      case 'CREATE':
        return (
          <>
            <InputBox
              id={`create-${lifeRule.id}`}
              value={content}
              onChange={(e) => onContentChange(e.target.value)}
              placeholder="내용을 입력하세요"
            />
            <CreateButton onClick={() => onAddItem(index)}>추가</CreateButton>
          </>
        )
      default:
        return (
          <>
            <p>{content}</p>
            <ActionButtons>
              <ActionButton
                type="update"
                onClick={() => setVariant('UPDATE', content)}
              />
              <ActionButton
                type="delete"
                onClick={() => setVariant('DELETE', content)}
              />
            </ActionButtons>
          </>
        )
    }
  }

  return (
    <ItemContainer variant={variant}>
      <CatrgoryIcon>
        <Image
          src={`/images/lifeRule/life-rule-${lifeRule.category.trim()}-inactive.svg`}
          alt={lifeRule.category}
          width={46}
          height={46}
        />
      </CatrgoryIcon>
      <Content>{renderContent()}</Content>
    </ItemContainer>
  )
}
