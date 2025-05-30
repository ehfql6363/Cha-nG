'use client'

import React from 'react'

import { Image } from '@/components'
import { LifeRuleUpdateVariant, UpdateLifeRule } from '@/types/lifeRule'

import {
  CatrgoryIcon,
  Content,
  ContentContainer,
  ItemContainer,
  StatusIcon,
} from './styles'

interface LifeRuleUpdateApproveListItemProps {
  lifeRule: UpdateLifeRule
}

export const LifeRuleUpdateApproveListItem = ({
  lifeRule,
}: LifeRuleUpdateApproveListItemProps) => {
  // const { t } = useTranslation()

  const getStatusIcon = () => {
    switch (lifeRule.actionType) {
      case 'UPDATE':
        return '/icons/button-modify.svg'
      case 'CREATE':
        return '/icons/button-create.svg'
      case 'DELETE':
        return '/icons/button-delete.svg'
      default:
        return null
    }
  }

  const getStatusText = () => {
    switch (lifeRule.actionType) {
      case 'UPDATE':
        return '수정'
      case 'CREATE':
        return '추가'
      case 'DELETE':
        return '삭제'
      default:
        return ''
    }
  }

  return (
    <ItemContainer variant={lifeRule.actionType as LifeRuleUpdateVariant}>
      <CatrgoryIcon>
        <Image
          src={`/images/lifeRule/life-rule-${lifeRule.category.trim()}-inactive.svg`}
          alt={lifeRule.category}
          width={46}
          height={46}
        />
      </CatrgoryIcon>
      <Content>
        <ContentContainer>{lifeRule.content}</ContentContainer>
        {getStatusIcon() && (
          <StatusIcon>
            <Image
              src={getStatusIcon() as string}
              alt="status"
              width={24}
              height={24}
            />
            <p>{getStatusText()}</p>
          </StatusIcon>
        )}
      </Content>
    </ItemContainer>
  )
}
