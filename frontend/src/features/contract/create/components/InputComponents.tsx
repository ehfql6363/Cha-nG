import { useDispatch } from 'react-redux'

import { formatDate } from '@fullcalendar/core/index.js'

import {
  AccountInput,
  Card,
  CustomPicker,
  InputBox,
  MoneyInputBox,
  RentRatio,
} from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import {
  updateContractRequestField,
  updateRent,
} from '@/store/slices/contractSlice'
import {
  DefaultContainer,
  RegularLabel,
  ShowCenterBox,
  ValidationContainer,
  ValidationMessage,
} from '@/styles/styles'
import { ShowBox } from '@/styles/styles'

import {
  FormValuesInputProps,
  ValueInputProps,
} from '../../types/contract-input'

export const MoneyInput: React.FC<FormValuesInputProps> = () => {
  const dispatch = useDispatch()
  const rent = useAppSelector((state) => state.contract.contractRequest.rent)

  return (
    <MoneyInputBox
      value={rent?.totalAmount ?? 0}
      onChange={(value: number) => {
        dispatch(
          updateRent({
            ...rent,
            totalAmount: value,
          }),
        )
      }}
    />
  )
}

export const SwitchInput: React.FC<FormValuesInputProps> = () => <RentRatio />

export const TextInput: React.FC<FormValuesInputProps> = () => {
  const dispatch = useDispatch()
  const rent = useAppSelector((state) => state.contract.contractRequest.rent)

  return (
    <InputBox
      id="ownerAccountNo"
      value={rent?.ownerAccountNo ?? ''}
      onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
        dispatch(
          updateRent({
            ...rent,
            ownerAccountNo: e.target.value,
          }),
        )
      }}
    />
  )
}

export const AccountInputWrapper: React.FC<ValueInputProps> = (props) => (
  <AccountInput
    value={props.value ? String(props.value) : undefined}
    onChange={props.onChange}
  />
)

export const CalendarInput: React.FC<FormValuesInputProps> = (props) => {
  const dispatch = useDispatch()
  const item = props.item as 'startDate' | 'endDate'
  const rent = useAppSelector((state) => state.contract.contractRequest.rent)

  const startDate = useAppSelector(
    (state) => state.contract.contractRequest.startDate,
  )

  const handleDateChange = (value: { year: string; month: string }) => {
    // KST로 날짜 생성
    const kstDate =
      new Date(
        Number(value.year),
        Number(value.month) - 1,
        rent?.dueDate ?? 1,
      ) ?? new Date()

    const currentDate = new Date(props.value as string) ?? new Date()
    // startDate인 경우 오늘 날짜보다 작으면 오늘 날짜로 설정
    if (item === 'endDate' && kstDate < new Date()) {
      console.log('kstDate', kstDate)
      return
    }
    // UTC로 변환하여 저장
    const utcDate = new Date(
      Date.UTC(kstDate.getFullYear(), kstDate.getMonth(), kstDate.getDate()),
    )
    const formattedDate = utcDate.toISOString()
    dispatch(
      updateContractRequestField({
        field: 'startDate',
        value: new Date().toISOString(),
      }),
    )
    dispatch(
      updateContractRequestField({
        field: item,
        value: formattedDate,
      }),
    )

    const shouldUpdateOtherDate = item === 'endDate' && kstDate < currentDate

    if (shouldUpdateOtherDate) {
      dispatch(
        updateContractRequestField({
          field: item,
          value: formattedDate,
        }),
      )
    }
  }

  const contractRequest = useAppSelector(
    (state) => state.contract.contractRequest,
  )

  return (
    <div>
      <DefaultContainer>
        <ShowCenterBox isDisabled={true}>
          <RegularLabel>
            {`${new Date(contractRequest.startDate).toLocaleDateString()}${' ~ '}${new Date(props.value as string).toLocaleDateString()}`}
          </RegularLabel>
        </ShowCenterBox>
        <ValidationContainer>
          <ValidationMessage isValid={true}>
            시작일은 언제나 오늘로 고정됩니다.
          </ValidationMessage>
        </ValidationContainer>
      </DefaultContainer>
      <CustomPicker
        handleChange={handleDateChange}
        pickerValue={{
          year: props.value
            ? new Date(props.value as string).getFullYear().toString()
            : '',
          month: props.value
            ? (new Date(props.value as string).getMonth() + 1).toString()
            : '',
        }}
        selections={{
          year: Array.from({ length: 27 }, (_, i) => String(i + 2025)),
          month: Array.from({ length: 12 }, (_, i) => String(i + 1)),
        }}
      />
    </div>
  )
}

export const CardInput: React.FC<ValueInputProps> = (props) => (
  <Card
    value={props.value ? String(props.value) : null}
    onChange={props.onChange}
  />
)

export const CustomPickerInput: React.FC<FormValuesInputProps> = () => {
  const dispatch = useDispatch()
  const rent = useAppSelector((state) => state.contract.contractRequest.rent)
  const contractRequest = useAppSelector(
    (state) => state.contract.contractRequest,
  )

  const handleDueDateChange = (value: { value: string }) => {
    const newDueDate = Number(value.value)

    // rent.dueDate 업데이트
    dispatch(
      updateRent({
        ...rent,
        dueDate: newDueDate,
      }),
    )

    // startDate와 endDate 업데이트
    if (contractRequest.startDate && contractRequest.endDate) {
      const startDate = new Date(contractRequest.startDate)
      const endDate = new Date(contractRequest.endDate)
      if (startDate >= endDate) {
        return
      }
      // KST로 날짜 생성
      const newStartDate = new Date(
        startDate.getFullYear(),
        startDate.getMonth(),
        newDueDate,
      )
      const newEndDate = new Date(
        endDate.getFullYear(),
        endDate.getMonth(),
        newDueDate,
      )

      // UTC로 변환하여 저장
      const utcStartDate = new Date(
        Date.UTC(
          newStartDate.getFullYear(),
          newStartDate.getMonth(),
          newStartDate.getDate(),
        ),
      )
      const utcEndDate = new Date(
        Date.UTC(
          newEndDate.getFullYear(),
          newEndDate.getMonth(),
          newEndDate.getDate(),
        ),
      )

      dispatch(
        updateContractRequestField({
          field: 'startDate',
          value: utcStartDate.toISOString(),
        }),
      )

      dispatch(
        updateContractRequestField({
          field: 'endDate',
          value: utcEndDate.toISOString(),
        }),
      )
    }
  }

  return (
    <CustomPicker
      handleChange={handleDueDateChange}
      pickerValue={{ value: rent?.dueDate ? String(rent.dueDate) : '' }}
      selections={{
        value: Array.from({ length: 27 }, (_, i) => String(i + 2)),
      }}
    />
  )
}
