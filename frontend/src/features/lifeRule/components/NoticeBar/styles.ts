import styled from '@emotion/styled'

export const NoticeBarContainer = styled.div`
  background-color: ${({ theme }) => theme.color.background.white};
  color: ${({ theme }) => theme.color.text.low};
  padding: 10px;
  font-size: 12px;
  text-align: center;
  width: 100%;
  border-radius: 8px;
  height: 46px;
  gap: 8px;
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyMedium};
  border: 1px solid ${({ theme }) => theme.color.border};
  display: flex;
  align-items: center;
  justify-content: space-between;
`

export const DivContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
`
