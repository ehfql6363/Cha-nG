import React, { useEffect, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import styled from '@emotion/styled'

import {
  BottomSheet,
  ConfirmButton,
  CustomPicker,
  InputBox,
  UserItem,
} from '@/components'
import { Image } from '@/components'
import { useAppSelector } from '@/hooks'
import { setShowRentRatio, updateRent } from '@/store/slices/contractSlice'
import {
  HeaderTitle,
  Label,
  RegularLabel,
  ShowBox,
  Title,
  ValidationContainer,
  ValidationMessage,
} from '@/styles/styles'

import { Colon } from '../CustomPicker/style'
import { Switch } from '../Switch'
import { Description } from '../TitleHeader/styles'
import { Total } from './styles'

const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 8px;
`

const RentRatioContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  gap: 0px;
  width: 100%;
`
const TileContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: space-around;
  width: 100%;
`
const ModalContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  justify-content: space-between;
  gap: 8px;
  width: 100%;
  height: calc(60vh - 2rem);
`

const UserContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 10px 10px;
`
const TitleContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
`
const MoneyContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  flex: 1;
  gap: 16px;
  & div:nth-of-type(2) {
    padding-top: 8px;
  }
  & div:nth-of-type(2) {
    > input {
      text-align: right !important;
    }
  }
`
export function RentRatio() {
  const [open, setOpen] = useState(false)
  const dispatch = useDispatch()
  const group = useAppSelector((state) => state.group.group)
  const showRentRatio = useAppSelector((state) => state.contract.showRentRatio)
  const rent = useAppSelector((state) => state.contract.contractRequest.rent)
  const { t } = useTranslation()

  const [pickerValue, setPickerValue] = useState<Record<string, string>>(
    group.members.reduce(
      (acc, member) => {
        acc[member.id] =
          rent.userPaymentInfo
            .find((info) => info.userId === member.id)
            ?.ratio.toString() ?? '1'
        return acc
      },
      {} as Record<string, string>,
    ),
  )

  const [totalUserAmount, setTotalUserAmount] = useState(
    rent.userPaymentInfo.reduce((sum, info) => sum + info.amount, 0),
  )

  useEffect(() => {
    if (showRentRatio) {
      if (!pickerValue) return
      if (rent.totalAmount && Object.keys(pickerValue).length > 0) {
        const totalRatio = Object.values(pickerValue).reduce(
          (sum, val) => sum + Number(val),
          0,
        )

        const newUserPaymentInfo = Object.entries(pickerValue).map(
          ([userId, ratio]) => ({
            userId: Number(userId),
            amount: Math.floor(rent.totalAmount * (Number(ratio) / totalRatio)),
            ratio: Number(ratio),
          }),
        )

        dispatch(
          updateRent({
            ...rent,
            totalRatio,
            userPaymentInfo: newUserPaymentInfo,
          }),
        )
      }
    }
  }, [rent.totalAmount, showRentRatio, pickerValue])

  useEffect(() => {
    setTotalUserAmount(
      rent.userPaymentInfo.reduce((sum, info) => sum + info.amount, 0),
    )
  }, [rent.userPaymentInfo])
  const handleChange = (value: Record<string, string>, key: string) => {
    const newPickerValue = { ...pickerValue, [key]: value[key] }
    setPickerValue(newPickerValue)

    // 모든 값의 합산을 계산
    const totalRatio = Object.values(newPickerValue).reduce(
      (sum, val) => sum + Number(val),
      0,
    )

    // userPaymentInfo 업데이트
    const newUserPaymentInfo = Object.entries(newPickerValue).map(
      ([userId, ratio]) => ({
        id: Number(userId),
        userId: Number(userId),
        amount: Math.floor(rent.totalAmount * (Number(ratio) / totalRatio)),
        ratio: Number(ratio),
      }),
    )

    dispatch(
      updateRent({
        ...rent,
        totalRatio,
        userPaymentInfo: newUserPaymentInfo,
      }),
    )
  }

  const selections = group.members.reduce(
    (acc, member) => {
      acc[member.id] = Array.from({ length: 100 }, (_, i) => (i + 1).toString())
      return acc
    },
    {} as Record<string, string[]>,
  )
  const formatMoney = (value: string) => {
    if (value) {
      const numericValue = value.replace(/[^0-9]/g, '')
      const formattedValue = new Intl.NumberFormat('ko-KR').format(
        Number(numericValue),
      )
      return `${formattedValue} 원`
    }
    return ''
  }
  return (
    <Container>
      <RentRatioContainer>
        <UserContainer>
          <Title>{t('contract.rentTotalRatio.title')}</Title>
          <Switch
            checked={showRentRatio}
            onChange={(value) => dispatch(setShowRentRatio(value))}
            onText={t('contract.rentTotalRatio.switch.ratio')}
            offText={t('contract.rentTotalRatio.switch.money')}
          />
        </UserContainer>
        {showRentRatio ? (
          <RentRatioContainer>
            <ShowBox onClick={() => setOpen(true)}>
              <TileContainer>
                {Object.entries(pickerValue).map(([key, value], index) => {
                  return (
                    <React.Fragment key={key}>
                      {index != 0 && (
                        <Colon key={index + 'colon'}>{t('picker.colon')}</Colon>
                      )}
                      <HeaderTitle key={key}>{value}</HeaderTitle>
                    </React.Fragment>
                  )
                })}
              </TileContainer>
            </ShowBox>
            <RentRatioContainer>
              {group.members.map((user) => (
                <UserContainer key={user.id}>
                  <UserItem
                    key={user.id}
                    user={user}
                    variant="bar"
                    showName={true}
                  />
                  <RegularLabel id={user.id.toString()}>
                    {formatMoney(
                      rent?.userPaymentInfo
                        .find((info) => info.userId === user.id)
                        ?.amount?.toString() ?? '',
                    )}
                  </RegularLabel>
                </UserContainer>
              ))}
              <Total>
                총합
                <HeaderTitle>
                  {formatMoney(rent?.totalAmount?.toString() ?? '')}
                </HeaderTitle>
              </Total>
            </RentRatioContainer>
            {totalUserAmount != rent.totalAmount && (
              <Description>{t('contract.rentTotalRatio.label')}</Description>
            )}
          </RentRatioContainer>
        ) : (
          <RentRatioContainer>
            {group.members.map((user) => (
              <MoneyContainer key={user.id}>
                <UserItem
                  key={user.id}
                  user={user}
                  variant="bar"
                  showName={true}
                />
                <InputBox
                  id={user.id.toString()}
                  type="money"
                  value={
                    rent?.userPaymentInfo
                      .find((info) => info.userId === user.id)
                      ?.amount?.toString() ?? ''
                  }
                  onChange={(e) => {
                    const newAmount = Number(e.target.value)
                    const newUserPaymentInfo = rent.userPaymentInfo.map(
                      (info) =>
                        info.userId === user.id
                          ? { ...info, amount: newAmount }
                          : info,
                    )
                    dispatch(
                      updateRent({
                        ...rent,
                        userPaymentInfo: newUserPaymentInfo,
                      }),
                    )
                  }}
                />
              </MoneyContainer>
            ))}
            <Total>
              총합
              <HeaderTitle>
                {formatMoney(totalUserAmount?.toString() ?? '')}
              </HeaderTitle>
            </Total>

            {totalUserAmount != rent.totalAmount && (
              <ValidationContainer>
                <Image
                  src={`/icons/validation-false.svg`}
                  alt={'message'}
                  width={14}
                  height={14}
                />
                <ValidationMessage isValid={false}>
                  {t('contract.rentTotalRatio.validation.match')}
                </ValidationMessage>
              </ValidationContainer>
            )}
          </RentRatioContainer>
        )}
      </RentRatioContainer>
      <BottomSheet
        open={open}
        onOpenChange={setOpen}
        snapPoints={{
          MIN: 0.3,
          MID: 0.6,
          MAX: 0.7,
        }}>
        <ModalContainer>
          <Title>{t('contract.rentTotalRatio.modalTitle')}</Title>
          <TileContainer>
            {group.members.map((user) => (
              <UserItem
                key={user.id}
                user={user}
                showName={true}
              />
            ))}
          </TileContainer>
          <CustomPicker<Record<string, string>>
            handleChange={handleChange}
            pickerValue={pickerValue}
            selections={selections}
          />
          <ConfirmButton
            onClick={() => setOpen(false)}
            label="confirm"
          />
        </ModalContainer>
      </BottomSheet>
    </Container>
  )
}
