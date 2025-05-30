import styled from '@emotion/styled'

export const ContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  gap: 16px;
`

export const TopDescription = styled.div`
  width: 100%;
  ${({ theme }) => theme.typography.styles.name};
  color: ${({ theme }) => theme.color.text.disabled};
`

export const TextContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  gap: 8px;
` 

export const AmountContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  width: 100%;
  gap: 10px;

  >div {
    ${({ theme }) => theme.typography.styles.title};
    color: ${({ theme }) => theme.color.text.regular};
  }
`

export const Periond = styled.div`
  display: flex;
  border: 1px solid ${({ theme }) => theme.color.border};
  border-radius: 10px;
  padding: 4px 12px;
  ${({ theme }) => theme.typography.styles.navigator};
  color: ${({ theme }) => theme.color.text.disabled};
`

