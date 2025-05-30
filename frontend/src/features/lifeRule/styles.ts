import styled from '@emotion/styled'

// import Link from 'next/link'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100vh;
  background-color: ${({ theme }) => theme.color.background.white};
  position: relative;
  border: 1px solid yellow;
`

export const FullMain = styled.main`
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 20px;
  width: 100%;
  height: 100%;
  overflow-y: auto;
`

export const NavigatorBar = styled.div`
  position: fixed;
  text-align: center;
  height: 75px;
  color: ${({ theme }) => theme.color.text.low};
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};
  z-index: 10;
`

export const EmptyContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  width: 100%;
  height: 100%;
  justify-content: center;
  align-items: center;
`

export const TitleContainer = styled.div`
  text-align: center;
`

export const Description = styled.p`
  color: ${({ theme }) => theme.color.text.disabled};
  ${({ theme }) => theme.typography.styles.description};
  text-align: center;
`
