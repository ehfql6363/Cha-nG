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
  display: flex;
  padding: 0 0.5rem;
  justify-content: end;
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
