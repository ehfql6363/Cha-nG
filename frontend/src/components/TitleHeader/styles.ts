import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8px;
`

export const Title = styled.h2`
  color: ${({ theme }) => theme.color.text.regular};
  ${({ theme }) => theme.typography.styles.heading};
  white-space: pre-line;
  opacity: 0;
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

export const Description = styled.p`
  color: ${({ theme }) => theme.color.text.disabled};
  ${({ theme }) => theme.typography.styles.description};
`
