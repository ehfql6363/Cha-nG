import styled from '@emotion/styled'

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
  text-align: left;
  gap: 16px;
  justify-content: space-between;
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
          border: 1px solid ${theme.color.text.sunday};
        `
      case 'UPDATE':
        return `
          background-color: ${theme.color.background.white};
          border: 1px solid ${theme.color.border};
          align-items: center;
          flex-direction: row;
        `
      case 'CREATE':
        return `
          background-color: ${theme.color.background.white};
          border: 1px solid ${theme.color.border};
          align-items: center;
          flex-direction: row;
        `
      default:
        return `
        background-color: ${theme.color.background.white};
        border: 1px solid ${theme.color.border};
        align-items: center;
        flex-direction: row;
      `
    }
  }}
`

// 라이프 규칙 액션 버튼 컨테이너
export const ActionButtons = styled.div`
  display: flex;
  gap: 8px;
  align-items: center;
  margin-left: auto;
  justify-content: center;
`

// 라이프 규칙 액션 버튼
export const StyledActionButton = styled.button`
  background: none;
  border: none;
  padding: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
`

// 라이프 규칙 확인 버튼
export const UpdateButton = styled.button`
  width: 46px;
  border-radius: 8px;
  font-size: 14px;
  align-items: center;
  justify-content: center;
  border: 1px solid ${({ theme }) => theme.color.background.white};
  background-color: ${({ theme }) => theme.color.background.white};
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyMedium};
  color: ${({ theme }) => theme.color.text.low};
  display: flex;
`
// 라이프 규칙 취소 버튼
export const DeleteButton = styled.button`
  width: 46px;
  border-radius: 8px;
  font-size: 14px;
  align-items: center;
  justify-content: center;
  border: 1px solid ${({ theme }) => theme.color.background.delete};
  background-color: ${({ theme }) => theme.color.background.delete};
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyMedium};
  color: ${({ theme }) => theme.color.text.low};
`

export const CreateButton = styled.button`
  width: 46px;
  border-radius: 8px;
  font-size: 14px;
  align-items: center;
  justify-content: center;
  border: 1px solid ${({ theme }) => theme.color.background.white};
  background-color: ${({ theme }) => theme.color.background.white};
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyMedium};
  color: ${({ theme }) => theme.color.text.low};
`
