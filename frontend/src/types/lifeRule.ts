export interface LifeRule {
  id: number
  content: string
  category: string
}

export interface UpdateLifeRule {
  id?: number | null
  content: string
  category: string
  actionType: string
}

export type GetLifeRuleResponse = { lifeRules: LifeRule[] }
export type PostLifeRuleResponse = { lifeRules: LifeRule[] }

export type CreateLifeRuleRequest = {
  rules: Omit<LifeRule, 'id'>[]
}

export interface LifeRuleCategory {
  id: string
  src: string
}

export type LifeRuleUpdateVariant = 'DEFAULT' | 'UPDATE' | 'DELETE' | 'CREATE'
