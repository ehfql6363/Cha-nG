import styled from '@emotion/styled'

//import Link from 'next/link'

export const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
  background-color: ${({ theme }) => theme.color.background.white};
  padding: 1.25rem;
`
export const FullMain = styled.main`
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 20px;
  width: 100%;
  flex: 1;
  padding-bottom: 100px;
  overflow: auto;
`

export const LifeRuleUpdateList = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 16px;
  width: 100%;
`

export const NavigatorBar = styled.div`
  text-align: center;
  height: 75px;
  color: ${({ theme }) => theme.color.text.low};
  font-family: ${({ theme }) => theme.typography.fonts.paperlogyRegular};
`
export const ApproveProfileContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 16px;
  width: 100%;
  justify-content: center;
  margin-bottom: 0px;
`
