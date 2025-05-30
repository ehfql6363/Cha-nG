import styled from '@emotion/styled'
import Link from 'next/link'

export const Card = styled(Link)`
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 0rem;
  height: 100%;
  border: 1px solid transparent;
  flex: 1;
  border-radius: 16px;
  color: ${({ theme }) => theme.color.text.low};
  ${({ theme }) => theme.typography.styles.topHeader}
  background-color: ${({ theme }) => theme.color.background.white};
  box-shadow: 0px 0px 20px 0px rgba(118, 118, 118, 0.25);

  overflow: hidden;
  transition:
    opacity 0.3s ease,
    border-color 0.3s ease,
    box-shadow 0.3s ease;
  & > img {
    margin-left: auto;
  }
  cursor: pointer;
`

export const Description = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: center;
  text-align: center;
  width: 100%;
  ${({ theme }) => theme.typography.styles.cardDescription}
  background-color: ${({ theme }) => theme.color.background.white};
  white-space: pre-line;
`

export const Category = styled.div`
  ${({ theme }) => theme.typography.styles.cardDescription}
  color: ${({ theme }) => theme.color.text.disabled};
`
export const CardContainer = styled.div`
  display: flex;
  gap: 1rem;
  width: 100%;
  padding: 0 0px 0 0px;
`

export const StyledButton = styled.button<{ isSelected: boolean }>`
  padding: 0.5rem 1rem;
  border-radius: 16px;
  border: none;
  text-align: center;
  ${({ theme }) => theme.typography.styles.description};
  cursor: pointer;
  outline: none;

  &:focus {
    border-color: ${({ theme }) => theme.color.primary};
    outline: none;
    transition: opacity 0.3s ease;
    box-shadow: 0 0 0 2px ${({ theme }) => theme.color.primary}33;
  }
  &:hover {
    opacity: 0.8;
    transition: opacity 0.3s ease;
    outline: 1px solid ${({ theme }) => theme.color.primary};
  }

  ${({ isSelected, theme }) => {
    switch (isSelected) {
      case true:
        return `
          background-color: ${theme.color.primary};
          border: 1px solid ${theme.color.primary};
          color: white;
        `
      case false:
        return `
          background-color: ${theme.color.background.white};
          border: 1px solid ${theme.color.primary};
          color: ${theme.color.text.regular};
        `
      default:
        return ''
    }
  }}
`
export const IssueContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  flex: 0;
  justify-content: flex-start;
  position: relative;
  margin: 1rem 0.5rem;
  border: none;
`
export const ImageContainer = styled.div`
  flex: 0;
  width: 144px;
  display: flex;
  justify-content: center;
  > img {
    position: absolute;
    top: -16px;
    justify-content: center;
    z-index: 1;
    height: 40px;
    width: 40px;
  }
`
export const IssueContent = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  border-radius: 16px;
  padding-top: 2rem;
  background-color: ${({ theme }) => theme.color.background.white};
  box-shadow: 0px 0px 20px 0px rgba(118, 118, 118, 0.25);
  width: 144px;
  height: 120px;
  overflow: hidden;
`

export const ScrollContainer = styled.div`
  display: flex;
  width: calc(100vw - 20px);
  overflow-y: hidden;
  height: 100%;
  justify-content: flex-start;

  @media (min-width: 768px) {
    width: calc(50vw - 40px);
  }
`
export const IssueTitle = styled.div`
  ${({ theme }) => theme.typography.styles.topHeader};
  color: ${({ theme }) => theme.color.text.regular};
  width: 100%;
  opacity: 0;
  justify-content: center;
  text-align: center;
  padding: 0 1.5rem;
  transform: translateY(20px);
  animation: fadeInUp 0.2s ease-out forwards;

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
`

export const ButtonContainer = styled.div`
  display: flex;
  gap: 0.5rem;
  width: 100%;
  margin-bottom: 0.5rem;
`
export const Container = styled.div`
  margin: 1rem 0;
  padding: 0 20px;
  > div {
    padding-bottom: 2rem;
  }
`
export const LifeContainer = styled.div`
  gap: 1rem;
  display: flex;
  flex-direction: column;
  width: 100%;
`
