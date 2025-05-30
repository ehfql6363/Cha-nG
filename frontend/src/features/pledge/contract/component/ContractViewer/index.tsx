'use client'

import { useTranslation } from 'react-i18next'

import { IconButton, UserItem } from '@/components'
import { useAppSelector } from '@/hooks'
import {
  BankLabel,
  DefaultContainer,
  PaddingContainer,
  ShowBox,
  SlimContainer,
  TitleContainer,
} from '@/styles/styles'
import { Contract, ContractRequest, RentUser } from '@/types/contract'
import { formatMoney } from '@/utils/format'

import {
  Container,
  DefaultLabel,
  HeaderTitle,
  UserContainer,
  UtilityDescription,
} from './styles'

export function ContractViewer({
  contract,
  rentUserList,
}: {
  contract: Contract | ContractRequest
  rentUserList: RentUser[]
}) {
  const startDate = new Date(contract.startDate)
  const endDate = new Date(contract.endDate)
  const contractDate = `${startDate.getFullYear()}.${startDate.getMonth() + 1}.${startDate.getDate()} - ${endDate.getFullYear()}.${endDate.getMonth() + 1}.${endDate.getDate()}`
  const userRentRatio = rentUserList.reduce(
    (acc, user, index) =>
      index === 0 ? String(user.ratio) : acc + ':' + String(user.ratio),
    '',
  )

  const contractUserNames = rentUserList.reduce(
    (acc, user, index) =>
      index === 0 ? String(user.name) : acc + ', ' + String(user.name),
    '',
  )
  const group = useAppSelector((state) => state.group.group)
  const leaderName = group.members.find(
    (info) => info.id == group.leaderId,
  )?.name
  const { t } = useTranslation()
  return (
    <Container>
      <TitleContainer>
        <HeaderTitle>{t('contract.detail.contractDate')}</HeaderTitle>
        <DefaultLabel>{contractDate}</DefaultLabel>
      </TitleContainer>
      <TitleContainer>
        <HeaderTitle>{t('contract.detail.userPaymentInfo')}</HeaderTitle>
        <DefaultLabel>{contractUserNames}</DefaultLabel>
      </TitleContainer>
      <TitleContainer>
        <HeaderTitle>{t('contract.detail.totalAmount')}</HeaderTitle>
        <DefaultLabel>{formatMoney(contract.rent.totalAmount)}</DefaultLabel>
      </TitleContainer>
      <TitleContainer>
        <HeaderTitle>{t('contract.detail.dueDate')}</HeaderTitle>
        <DefaultLabel>
          {t('contract.detail.dueDateFormat', { value: contract.rent.dueDate })}
        </DefaultLabel>
      </TitleContainer>
      <TitleContainer>
        <HeaderTitle>{t('contract.detail.totalRatio')}</HeaderTitle>
        <DefaultLabel>{userRentRatio} </DefaultLabel>
      </TitleContainer>
      <UserContainer>
        {rentUserList.map((user) => (
          <TitleContainer key={user.id}>
            <UserItem
              user={{ ...user }}
              variant="bar"
              size="small"
              showName={true}
            />
            <DefaultLabel>{formatMoney(user.amount)}</DefaultLabel>
          </TitleContainer>
        ))}
      </UserContainer>
      <UtilityDescription>
        {t('contract.detail.utilityDescription')}
      </UtilityDescription>
      <hr />
      <SlimContainer>
        <TitleContainer>
          <HeaderTitle>{t('contract.detail.rentAccountNo')}</HeaderTitle>
        </TitleContainer>
        <ShowBox>
          <BankLabel>
            <IconButton
              src="/icons/logo-bank.svg"
              alt="logo-bank"
            />
            {t('fintech.bankName')} {contract.rent.rentAccountNo}
          </BankLabel>
        </ShowBox>
      </SlimContainer>
      <SlimContainer>
        <TitleContainer>
          <HeaderTitle>{t('contract.detail.ownerAccountNo')}</HeaderTitle>
        </TitleContainer>
        <ShowBox>
          <BankLabel>
            <IconButton
              src="/icons/logo-bank.svg"
              alt="logo-bank"
            />
            {t('fintech.bankName')} {contract.rent.ownerAccountNo}
          </BankLabel>
        </ShowBox>
      </SlimContainer>
      {contract.utility.cardId && (
        <SlimContainer>
          <TitleContainer>
            <HeaderTitle>{t('contract.detail.utilityCard')}</HeaderTitle>
          </TitleContainer>
          <ShowBox>
            <BankLabel>
              <IconButton
                src="/icons/logo-card.svg"
                alt="logo-bank"
              />
              {t('fintech.cardName')}
            </BankLabel>
          </ShowBox>
        </SlimContainer>
      )}
    </Container>
  )
}
