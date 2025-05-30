import styled from '@emotion/styled'

import { Image } from '@/components'

export const Container = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: start;
  align-items: center;
  min-height: 100vh;
  width: 100%;
  gap: 8px;
  background-color: ${({ theme }) => theme.color.background.white};
`
export const NotificationItem = styled.div`
  display: flex;
  justify-content: start;
  align-items: center;
  width: 100%;
  display: flex;
  align-items: flex-start;
  gap: 8px;
  padding-bottom: 1rem;
  align-self: stretch;

  > div {
    display: flex;
    flex-direction: column;
    gap: 4px;
    width: 100%;
    padding: 0 0.5rem 0.5rem;
  }
`

export const NotificationHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;
  width: 100%;
`

export const NotificationTitle = styled.div`
  color: ${({ theme }) => theme.color.text.disabled};
  ${({ theme }) => theme.typography.styles.inputBoxTitle}
`

export const NotificationDate = styled.div`
  color: ${({ theme }) => theme.color.text.disabled};
  line-height: normal;
  ${({ theme }) => theme.typography.styles.description}
`

export const NotificationContent = styled.div`
  color: ${({ theme }) => theme.color.text.low};
  line-height: normal;
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};
  font-size: 1rem;
`

export const CategoryContainer = styled(Image)`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
`
