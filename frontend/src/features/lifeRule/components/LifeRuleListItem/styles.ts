import styled from '@emotion/styled'

export const Container = styled.div`
  padding: 16px;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  width: 100%;
  justify-content: center;
  border-radius: 8px;
  background: ${({ theme }) => theme.color.background.white};
  box-shadow: 0px 0px 15px 0px rgba(0, 0, 0, 0.15);
`

export const CatrgoryIcon = styled.div`
  border-radius: 50%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
`

export const Content = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  margin: auto;
  ${({ theme }) => theme.typography.styles.inputBoxTitle};
  color: ${({ theme }) => theme.color.text.regular};
  font-size: 14px;
`
