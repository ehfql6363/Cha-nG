import styled from '@emotion/styled'

export const DefaultHomeContent = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  height: 100%;
  border: 1px solid transparent;
  flex: 1;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
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
