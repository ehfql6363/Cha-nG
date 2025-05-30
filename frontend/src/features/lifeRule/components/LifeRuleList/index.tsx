'use client'

import React from 'react'

// import { useTranslation } from 'react-i18next'

import { LifeRule } from '@/types/lifeRule'

import { LifeRuleListItem } from '../LifeRuleListItem'
import { Container } from './styles'

interface LifeRuleListProps {
  lifeRuleList: LifeRule[]
}

export function LifeRuleList({ lifeRuleList }: LifeRuleListProps) {
  // const { t } = useTranslation()

  return (
    <Container>
      {lifeRuleList.map((lifeRule) => (
        <LifeRuleListItem
          key={lifeRule.id}
          lifeRule={lifeRule}
        />
      ))}
    </Container>
  )
}
