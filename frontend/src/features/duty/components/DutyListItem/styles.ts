import styled from '@emotion/styled'

export const Container = styled.div`
  padding: 1rem 0;
  display: flex;
  align-items: start;
  justify-content: space-between;
  gap: 1rem;
  width: 100%;
`

export const CatrgoryIcon = styled.div`
  background-color: ${({ theme }) => theme.color.secondary};
  min-height: 3rem;
  min-width: 3rem;
  border-radius: 50%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
`

export const DutyInfo = styled.div`
  display: flex;
  gap: 1rem;
  width: 100%;
  max-height: 4.75rem;
  flex: 1;
  justify-content: center;
  align-items: center;
`
export const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4px;
  height: 100%;

  > div:first-of-type {
    ${({ theme }) => theme.typography.styles.description};
    color: ${({ theme }) => theme.color.text.disabled};
  }
  > div:last-of-type {
    ${({ theme }) => theme.typography.styles.default};
    color: ${({ theme }) => theme.color.text.low};
    white-space: pre-wrap;
    max-width: 100%;
    word-break: break-all;
    line-height: 1.5;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
`
