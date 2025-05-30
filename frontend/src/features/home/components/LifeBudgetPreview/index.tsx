import { useRouter } from 'next/navigation'

import { IconButton } from '@/components'
import { useAppSelector } from '@/hooks'
import {
  DefaultContainer,
  DefaultRegularTitle,
  SlimContainer,
  Title,
  TitleContainer,
} from '@/styles/styles'
import { formatMoney } from '@/utils/format'

import { CardDescription, Container, DefaultLabel } from './styles'

export function LifeBudgetPreview() {
  const router = useRouter()

  const livingAccountPaymentHistory = useAppSelector(
    (state) => state.livingBudget.livingAccountPaymentHistory,
  )
  return (
    <Container onClick={() => router.push('/budget/living')}>
      <DefaultContainer>
        <TitleContainer>
          <DefaultRegularTitle>생활비</DefaultRegularTitle>
          <IconButton
            src={'/icons/arrow-right.svg'}
            alt="생활비"
            onClick={() => router.push('/budget/living')}
          />
        </TitleContainer>
      </DefaultContainer>
      {livingAccountPaymentHistory.length > 2 && (
        <DefaultContainer>
          <SlimContainer>
            <TitleContainer>
              <DefaultLabel>
                {livingAccountPaymentHistory[0].transactionSummary}
              </DefaultLabel>
              <CardDescription>
                {formatMoney(livingAccountPaymentHistory[0].transactionBalance)}
              </CardDescription>
            </TitleContainer>
          </SlimContainer>
          <hr />
          <SlimContainer>
            <TitleContainer>
              <DefaultLabel>
                {livingAccountPaymentHistory[1].transactionSummary}
              </DefaultLabel>
              <CardDescription>
                {formatMoney(livingAccountPaymentHistory[1].transactionBalance)}
              </CardDescription>
            </TitleContainer>
          </SlimContainer>
        </DefaultContainer>
      )}
    </Container>
  )
}
