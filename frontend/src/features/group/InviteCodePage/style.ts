import styled from '@emotion/styled'

export const SignupLinkContainer = styled.div`
  text-align: center;
  display: flex;
  padding: 0 0.5rem;
  justify-content: end;
  color: ${({ theme }) => theme.color.text.low};
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};
`

export const StyledLink = styled.div`
  color: ${({ theme }) => theme.color.primary};
  text-decoration: none;
  margin-left: 0.5rem;
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};

  &:hover {
    text-decoration: underline;
  }
`
