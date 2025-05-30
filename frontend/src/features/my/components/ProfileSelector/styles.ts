import styled from '@emotion/styled'

export const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`

export const SelectedImage = styled.img`
  border-radius: 50%;
  margin-bottom: 20px;
`

export const ProfileGrid = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 12px;
  margin: 0 0.5rem;
`

export const ProfileImage = styled.img<{
  isSelected: boolean
  primaryColor: string
  disabled: boolean
}>`
  border-radius: 50%;
  border: 3px solid
    ${({ isSelected, primaryColor }) =>
      isSelected ? primaryColor : 'transparent'};
  cursor: pointer;
  box-shadow: 0 0 0 2px
    ${({ isSelected, theme }) =>
      isSelected ? theme.color.primary : 'transparent'}33;
  opacity: ${({ disabled }) => (disabled ? 0.3 : 1)};
`
