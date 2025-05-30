import styled from '@emotion/styled'

export const Total = styled.div`
  border-top: 1px solid ${({ theme }) => theme.color.border};
  display: flex;
  width: 100%;
  justify-content: space-between;
  padding: 16px 10px 8px 56px;
  ${({ theme }) => theme.typography.styles.topHeader};
  color: ${({ theme }) => theme.color.text.regular};
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
