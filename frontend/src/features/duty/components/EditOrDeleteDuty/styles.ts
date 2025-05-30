import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  flex: 1;
  width: 100%;
  height: 100%;
  padding: 1rem;

  > div:first-of-type {
    color: ${({ theme }) => theme.color.text.saturday};
  }
  > div:last-of-type {
    color: ${({ theme }) => theme.color.text.sunday};
  }

  > hr {
    width: 100%;
    border: 1px solid ${({ theme }) => theme.color.border};
  }
`
export const TextContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 1.25rem;
  ${({ theme }) => theme.typography.styles.title};
`
