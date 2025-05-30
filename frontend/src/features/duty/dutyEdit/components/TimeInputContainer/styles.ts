import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  padding: 16px;
  width: 100%;
  gap: 16px;
  border-radius: 16px;
  background-color: ${({ theme }) => theme.color.secondary};
`
export const TopContainer = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;
  height: fit-content;
  justify-content: start;
  align-items: center;
  gap: 16px;
  > div {
    ${({ theme }) => theme.typography.styles.default}
    color: ${({ theme }) => theme.color.text.low}
  }
`
export const TimePickerContainer = styled.div`
  display: flex;
  width: 100%;
  height: 100%;
`

export const IconContainer = styled.div`
  display: flex;
  padding: 2px;
`
