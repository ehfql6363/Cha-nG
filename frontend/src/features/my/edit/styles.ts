import styled from '@emotion/styled'

export const Description = styled.div`
  width: 100%;

`

export const TopContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 24px;
  > div {
    ${({ theme }) => theme.typography.styles.inputBoxTitle};
    color: ${({ theme }) => theme.color.text.regular};
  }
`
