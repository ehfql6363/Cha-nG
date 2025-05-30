import { useEffect } from 'react'
import { useDispatch } from 'react-redux'

import { CardButton } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import { setNoContractWhenLogIn } from '@/store/slices/uiSlice'
import { ContractStatus } from '@/types/contract'
import { CardItem } from '@/types/ui'

import { DefaultHomeContent } from './styles'

export function DefaultHomeContents({ cardItems }: { cardItems: CardItem[] }) {
  const dispatch = useDispatch()
  const user = useAppSelector((state) => state.user.user)
  const contractStatus = useAppSelector(
    (state) => state.contract.contract.status,
  )

  useEffect(() => {
    if (
      user.contractId &&
      contractStatus != ContractStatus.none &&
      contractStatus != ContractStatus.confirmed
    ) {
      dispatch(setNoContractWhenLogIn(true))
    }
  }, [user.contractId, contractStatus])
  return (
    <DefaultHomeContent>
      <CardButton cardItems={cardItems}></CardButton>
    </DefaultHomeContent>
  )
}
