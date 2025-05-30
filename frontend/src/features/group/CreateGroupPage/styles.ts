import styled from '@emotion/styled'
import Link from 'next/link'

export const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
  background-color: ${({ theme }) => theme.color.background.white};
`

export const Form = styled.form`
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;

  h1 {
    text-align: center;
    margin-bottom: 2rem;
    color: ${({ theme }) => theme.color.text.regular};
    font-family: ${({ theme }) => theme.typography.fonts.paperlogyMedium};
  }
`

export const SubmitButton = styled.button`
  width: 100%;
  padding: 0.75rem;
  background-color: ${({ theme }) => theme.color.primary};
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};

  &:hover {
    background-color: ${({ theme }) => theme.color.primary}dd;
  }
`

export const SignupLinkContainer = styled.div`
  text-align: center;
  margin-top: 1.5rem;
  color: ${({ theme }) => theme.color.text.low};
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};
`

export const StyledLink = styled(Link)`
  color: ${({ theme }) => theme.color.primary};
  text-decoration: none;
  margin-left: 0.5rem;
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};

  &:hover {
    text-decoration: underline;
  }
`
export const Label = styled.label`
  display: block;
  margin-bottom: 8px;
  ${({ theme }) => theme.typography.styles.inputBoxTitle};
  color: ${({ theme }) => theme.color.text.regular};
`

export const ImageButton = styled.button`
  all: unset;
  background: none;
  border: none;
  cursor: pointer;
  background-color: ${({ theme }) => theme.color.secondary};
  border-radius: 12px;
  width: 28px;
  height: 28px;
  align-items: center;
  display: inline-flex;
  justify-content: center;
  transition: opacity 0.2s ease-in-out;

  &:hover {
    opacity: 0.6;
  }

  &:active {
    opacity: 1;
  }
  &:focus {
    opacity: 1;
  }
`

export const ParticipantsContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  padding: 0.5rem;
  border: 2px solid transparent;
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.2s ease-in-out;
  cursor: pointer;
  background-color: ${({ theme }) => theme.color.background.white};
  &:focus {
    outline: 1px solid ${({ theme }) => theme.color.primary};
  }

  &:hover {
    border-color: ${({ theme }) => theme.color.primary}40;
    background-color: ${({ theme }) => theme.color.primary}10;
  }
`

export const HomeImageContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding-bottom: 0.5rem;
  position: relative;
`

export const UserContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  gap: 0.25rem;
  width: 300px;
  padding-bottom: 1rem;
`
