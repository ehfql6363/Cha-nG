import styled from '@emotion/styled'

import { colors } from '@/constants/colors'
import { CustomTheme } from '@/styles/themes'
import { LifeRuleUpdateVariant } from '@/types/lifeRule'

export const Container = styled.div`
  padding: 16px;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  width: 100%;
  justify-content: center;
  border-radius: 8px;
  background: ${({ theme }) => theme.color.background.white};
`

// 카테고리 아이콘
export const CatrgoryIcon = styled.div`
  border-radius: 50%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
`

// 라이프 룰 내용
export const Content = styled.div`
  display: flex;
  width: 100%;
  align-items: center;
  gap: 16px;
  justify-content: space-between;
  font-size: 16px;
  ${({ theme }) => theme.typography.styles.inputBoxTitle};
  color: ${({ theme }) => theme.color.text.regular};
`

interface StyledButtonProps {
  variant: LifeRuleUpdateVariant
}

// 라이프 규칙 전체 컨테이너
export const ItemContainer = styled(Container)<StyledButtonProps>`
  padding: 16px;
  border-radius: 12px;
  text-align: center;
  align-items: center;
  color: ${({ theme }) => theme.color.text.regular};
  ${({ theme }) => theme.typography.styles.button};

  ${({ variant, theme }: StyledButtonProps & { theme: CustomTheme }) => {
    switch (variant) {
      case 'DELETE':
        return `
          background-color: ${theme.color.background.delete};
          border: 1px solid ${colors.delete};
        `
      case 'UPDATE':
        return `
          background-color: ${theme.color.background.update};
          border: 1px solid ${colors.update};
        `
      case 'CREATE':
        return `
          background-color: ${theme.color.background.create};
          border: 1px solid ${colors.create};
        `
      default:
        return `
          background-color: ${theme.color.background.white};
          border: 1px solid ${theme.color.border};
        `
    }
  }}
`

export const StatusIcon = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  margin-left: auto;

  p {
    color: ${({ theme }) => theme.color.text.disabled};
    ${({ theme }) => theme.typography.styles.navigator};
  }
`
export const ContentContainer = styled.div`
  display: flex;
  width: 100%;
  align-items: center;
  gap: 16px;
  justify-content: space-between;
  ${({ theme }) => theme.typography.styles.inputBoxTitle};
  color: ${({ theme }) => theme.color.text.regular};
`
