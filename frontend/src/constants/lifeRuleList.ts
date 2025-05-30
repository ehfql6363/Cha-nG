import { LifeRule, LifeRuleCategory } from '../types/lifeRule'

export const lifeRuleCategoryList: LifeRuleCategory[] = [
  { id: 'clean', src: '/images/lifeRule/life-rule-category-clean.svg' },
]

export const lifeRuleList: LifeRule[] = [
  {
    id: 1,
    content: '안보여',
    category: 'clean',
  },
  {
    id: 2,
    content: '일주일에 한번은 배달음식 먹기',
    category: 'clean',
  },
  {
    id: 3,
    content: '10시에는 자는 걸로하자',
    category: 'clean',
  },
  {
    id: 4,
    content: '화장실 샤워하고 머리카락 치우기',
    category: 'clean',
  },
]
