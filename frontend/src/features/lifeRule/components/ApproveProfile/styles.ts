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
`

export const ProfileImage = styled.img<{
  isSelected: boolean
  primaryColor: string
}>`
  border-radius: 50%;
  border: 3px solid
    ${({ isSelected, primaryColor }) =>
      isSelected ? primaryColor : 'transparent'};
  box-sizing: border-box;
  cursor: pointer;
`

export const ProfileList = styled.div`
  display: flex;
  gap: 1rem;
  padding: 1rem;
`

export const ProfileItem = styled.div<{ isSelected: boolean }>`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;

  img {
    width: 42px;
    height: 42px;
    border-radius: 50%;
    border: 2px solid
      ${({ isSelected }) => (isSelected ? '#FF6B00' : 'transparent')};
  }

  span {
    font-family: ${({ theme }) => theme.typography.styles.navigator};
    color: ${({ theme }) => theme.color.text.low};
  }
`
// -----------

export const ProfileContainer = styled.div<{ isApprove: boolean }>`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  color: ${({ isApprove, theme }) =>
    isApprove ? theme.color.text.low : theme.color.text.disabled};
  white-space: nowrap;

  gap: 0.5rem;
  ${({ theme }) => theme.typography.styles.name};

  > span {
    text-align: center;
    min-width: 60px;
  }
`
