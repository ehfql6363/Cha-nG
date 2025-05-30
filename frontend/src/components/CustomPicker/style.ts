/** ProgressBar.tsx */
import styled from '@emotion/styled'

export const PickerWrapper = styled.div`
  width: 100%;
  > div {
    display: flex;
    justify-content: center;
    overflow: hidden;
    mask-image: linear-gradient(
      to top,
      transparent,
      transparent 5%,
      white 20%,
      white 80%,
      transparent 95%,
      transparent
    );
  }
`

export const PickerItem = styled.div<{ selected: boolean }>`
  height: 36px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: ${({ theme, selected }) =>
    selected ? theme.color.text.regular : theme.color.text.low};
  ${({ theme, selected }) =>
    selected
      ? theme.typography.styles.topHeader
      : theme.typography.styles.description};
`
export const Colon = styled.div`
  display: flex;
  align-items: center;
  padding: 0 4px;
  ${({ theme }) => theme.typography.styles.topHeader}
`
