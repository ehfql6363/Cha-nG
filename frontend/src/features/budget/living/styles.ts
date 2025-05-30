import styled from '@emotion/styled'

export const Account = styled.div`
  padding: 0 20px;
  border-radius: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  gap: 20px;
  align-self: stretch;
`
export const AccountTitle = styled.div`
  ${({ theme }) => theme.typography.styles.default};
  color: ${({ theme }) => theme.color.text.regular};
  width: 100%;
  text-align: left;
`
export const AccountInfo = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 6px;
  width: 100%;
  text-align: center;
  padding-bottom: 20px;
  > span {
    ${({ theme }) => theme.typography.styles.default};
    color: ${({ theme }) => theme.color.text.low};
    width: 100%;
  }
  > div {
    ${({ theme }) => theme.typography.styles.heading};
    color: ${({ theme }) => theme.color.text.regular};
    width: 100%;
  }
`
export const CalendarContainer = styled.div`
  padding: 8px 0 50 0;
  margin: 0px 0px 50px 0px;
  background: linear-gradient(
    to bottom,
    ${({ theme }) => theme.color.secondary},
    ${({ theme }) => theme.color.background.white}
  );
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  border-top: 8px solid ${({ theme }) => theme.color.secondary};
`
export const EmptyContainer = styled.div`
  display: flex;
  flex: 1;
  height: 30px;
  width: 100%;
`
