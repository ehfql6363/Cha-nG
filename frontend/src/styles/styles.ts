import styled from '@emotion/styled'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  justify-content: space-between;
  height: 100dvh;
  width: 100dvw;
  position: relative;
  @media (min-width: 768px) {
    width: 50%;
    justify-content: center;
    margin: 0 auto;
  }
`
export const Container2 = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  justify-content: center;
  height: 100dvh;
  width: 100dvw;
  justify-content: center;
  margin: 0 auto;
  align-items: center;
`

export const HeaderContainer = styled.div`
  padding: 20px 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  ${({ theme }) => theme.typography.styles.title};
  color: ${({ theme }) => theme.color.text.regular};
`

export const BottomContainer = styled.div`
  margin: 0 20px;
  display: flex;
  flex-direction: column;
`

export const Main = styled.main`
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  min-height: 70%;
  margin: auto;
  gap: 60px;
  width: 100%;
  overflow-y: auto;
`

export const FullMain = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
  height: 100%;
  overflow-y: auto;
  background-color: ${({ theme }) => theme.color.background.white};
  gap: 8px;
  padding: 0 20px;
`
export const FullWidthMain = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
  height: 100%;
  overflow-y: auto;
`
export const Form = styled.form`
  width: 100%;
  gap: 2rem;
  display: flex;
  flex-direction: column;
`

export const SwitcherContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  align-items: center;
`

export const ShowBox = styled.div<{ isDisabled?: boolean }>`
  display: flex;
  justify-content: center;
  gap: 1rem;
  width: 100%;
  padding: 1rem;
  overflow: hidden;
  border-radius: 16px;
  ${({ theme }) => theme.typography.styles.accountAndCardNum};
  color: ${({ theme }) => theme.color.text.low};
  border: 1px solid ${({ theme }) => theme.color.border};

  transition: all 0.2s ease-in-out;
  cursor: ${({ isDisabled }) => (isDisabled ? 'default' : 'pointer')};

  &:focus {
    outline: ${({ isDisabled, theme }) =>
      isDisabled ? 'none' : `1px solid ${theme.color.primary}`};
  }

  &:hover {
    background-color: ${({ isDisabled, theme }) =>
      isDisabled ? 'white' : `${theme.color.primary}10`};
    outline: ${({ isDisabled, theme }) =>
      isDisabled ? 'none' : `1px solid ${theme.color.primary}`};
  }
`

export const Label = styled.label`
  display: block;
  margin-bottom: 8px;
  ${({ theme }) => theme.typography.styles.inputBoxTitle};
  color: ${({ theme }) => theme.color.text.regular};
  white-space: nowrap;
`

export const ImageContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
`

export const UserTileContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin: auto;
  flex: 0;
  background-color: rgba(256, 256, 256, 0.8) !important;
`
export const TitleCenter = styled.div`
  ${({ theme }) => theme.typography.styles.title};
  color: ${({ theme }) => theme.color.text.regular};
  opacity: 0;
  text-align: center;
  transform: translateY(20px);
  animation: fadeInUp 0.2s ease-out forwards;

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
`
export const Title = styled.div`
  ${({ theme }) => theme.typography.styles.title};
  color: ${({ theme }) => theme.color.text.regular};
  width: 100%;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeInUp 0.2s ease-out forwards;

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
`
export const DefaultRegularTitle = styled.div`
  ${({ theme }) => theme.typography.styles.default};
  color: ${({ theme }) => theme.color.text.regular};
  width: 100%;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeInUp 0.2s ease-out forwards;

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
`

export const DefaultLowTitle = styled.div`
  ${({ theme }) => theme.typography.styles.default};
  color: ${({ theme }) => theme.color.text.low};
  width: 100%;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeInUp 0.2s ease-out forwards;

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
`

export const TitleContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
`

export const CenterContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
`
export const TextCenterContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  text-align: center;
`

export const HeaderTitle = styled.div`
  ${({ theme }) => theme.typography.styles.topHeader};
  color: ${({ theme }) => theme.color.text.regular};
`
export const RegularLabel = styled.div`
  ${({ theme }) => theme.typography.styles.default};
  color: ${({ theme }) => theme.color.text.regular};
`
export const DisabledLabel = styled.div`
  ${({ theme }) => theme.typography.styles.default};
  color: ${({ theme }) => theme.color.text.disabled};
`

export const DisabledColorText = styled.div`
  color: ${({ theme }) => theme.color.text.disabled};
`
export const ValidationMessage = styled.div<{ isValid: boolean }>`
  display: flex;
  align-items: center;
  gap: 4px;
  ${({ theme }) => theme.typography.styles.description};
  color: ${({ isValid, theme }) =>
    isValid ? theme.color.text.regular : theme.color.text.distructive};
`

export const ValidationContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 4px;
  margin: 0.5rem 0;
`

export const ShowCenterBox = styled.div<{ isDisabled?: boolean }>`
  display: flex;
  flex: 1;
  align-items: center;
  justify-content: center;
  margin: auto;
  white-space: nowrap;
  overflow: hidden;
  width: 100%;
  padding: 1rem;
  color: ${({ isDisabled, theme }) =>
    isDisabled ? theme.color.text.disabled : theme.color.text.regular};
  background-color: ${({ isDisabled, theme }) =>
    isDisabled ? theme.color.background.white : theme.color.secondary};
  ${({ theme }) => theme.typography.styles.button};
  border: ${({ isDisabled, theme }) =>
    isDisabled ? `1px solid ${theme.color.border}` : 'none'};
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.2s ease-in-out;
  cursor: ${({ isDisabled }) => (isDisabled ? 'default' : 'pointer')};

  &:focus {
    outline: ${({ isDisabled, theme }) =>
      isDisabled ? 'none' : `1px solid ${theme.color.primary}`};
  }

  &:hover {
    background-color: ${({ isDisabled, theme }) =>
      isDisabled ? 'white' : `${theme.color.primary}10`};
    outline: ${({ isDisabled, theme }) =>
      isDisabled ? 'none' : `1px solid ${theme.color.primary}`};
  }
`
export const DefaultContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
`
export const SlimContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  width: 100%;
`
export const SimpleMain = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
  height: 100%;
  overflow-y: auto;
  background-color: ${({ theme }) => theme.color.background.white};
  gap: 1rem;
`
export const HomeMain = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-between;
  width: 100%;
  gap: 1rem;
  overflow-y: auto;
  position: relative;
`
export const PaddingContainer = styled.div`
  display: flex;
  flex: 1;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
  padding: 20px;
`
export const Description = styled.p`
  color: ${({ theme }) => theme.color.text.disabled};
  ${({ theme }) => theme.typography.styles.description};
  text-align: center;
`
export const DefaultLabel = styled.div`
  ${({ theme }) => theme.typography.styles.default};
  color: ${({ theme }) => theme.color.text.low};
`

export const BankLabel = styled.div`
  ${({ theme }) => theme.typography.styles.default};
  color: ${({ theme }) => theme.color.text.low};
  justify-content: center;
  align-items: center;
  display: flex;
  gap: 0.5rem;
  ${({ theme }) => theme.typography.styles.accountAndCardNum};
`
export const ContentWrapper = styled.div`
  position: relative;
  width: 100%;
  height: 100dvh;
  flex-direction: column;
  display: flex;
  justify-content: space-between;
  flex-1;
  z-index: 1;
`
export const MainImageContainer = styled.div<{ isMorning: boolean }>`
  position: absolute;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    to bottom,

    ${({ isMorning }) => (isMorning ? '#FFFBE8' : '#E6E7E8')} 0%,
    ${({ isMorning }) => (isMorning ? '#FFFBE8' : '#E6E7E8')} 70%,
    ${({ isMorning }) => (isMorning ? '#48C5A6' : '#36CCC1')} 77%,
    ${({ isMorning }) => (isMorning ? '#48C5A6' : '#36CCC1')} 85%,
    white 89%,
    white 100%
  );
`

export const EmptyContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  width: 100%;
  height: 15rem;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 0 20px 40px;
`
export const EmptyContainer2 = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  width: 100%;
  height: 15rem;
  justify-content: center;
  align-items: center;
  padding: 0 20px 40px;
  background: linear-gradient(
    to bottom,
    white,
    ${({ theme }) => theme.color.secondary}
  );
`
