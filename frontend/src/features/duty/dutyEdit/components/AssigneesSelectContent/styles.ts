import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;
  padding-top: 20px;
  width: 100%;
  gap: auto;
  overflow-y: auto;
`
export const TextContainer = styled.div`
  margin-bottom: 20px;
  ${({ theme }) => theme.typography.styles.title};
  color: ${({ theme }) => theme.color.text.regular};
`
export const AssigneesSelectItemContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: start;
  gap: 10px;
  width: 100%;
  max-height: 50dvh;
  overflow-y: auto;
`

export const TopContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
`

export const ItemContainer = styled.div<{ selected: boolean }>`
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  padding: 16px 30px;
  width: 100%;
  gap: 30px;

  border: 1px solid
    ${({ theme, selected }) =>
      selected ? theme.color.primary : theme.color.border};
  background-color: ${({ theme, selected }) =>
    selected ? theme.color.secondary : theme.color.background.white};
  border-radius: 16px;
  cursor: pointer;
`

export const ButtonContainer = styled.div`
  display: flex;
  margin-top: auto;
  width: 100%;
  flex-direction: column;
  margin-top: auto;
`
