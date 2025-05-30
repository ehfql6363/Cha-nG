'use client'

import { useEffect, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import Image from 'next/image'
import { useRouter } from 'next/navigation'

import {
  confirmContract,
  createEmptyContract,
  updateContract,
} from '@/apis/group'
import {
  ConfirmButton,
  IconButton,
  Modal,
  ProgressBar,
  UserItem,
} from '@/components'
import { HeaderButton } from '@/components/TopHeader/styles'
import { useAppSelector } from '@/hooks/useAppSelector'
import {
  initContractRequest,
  setContractRequest,
  updateContractRequestField,
  updateRent,
  validateContractRequest,
} from '@/store/slices/contractSlice'
import { setContract } from '@/store/slices/contractSlice'
import { setContractId } from '@/store/slices/userSlice'
import {
  Container,
  FullMain,
  HeaderContainer,
  PaddingContainer,
  UserTileContainer,
} from '@/styles/styles'
import { ContractRequest, ContractStatus } from '@/types/contract'

import { useContractSteps } from '../hooks/useContractSteps'
import {
  FieldValue,
  InputComponentMap,
  InputType,
} from '../types/contract-input'
import {
  AccountInputWrapper,
  CalendarInput,
  CardInput,
  CustomPickerInput,
  MoneyInput,
  SwitchInput,
  TextInput,
} from './components/InputComponents'
import { InputWrapper } from './components/InputWrapper'
import {
  BottomContainer,
  DraftLabel,
  HeaderPaddingTitle,
  UserContainer,
} from './styles'

const InputComponents: InputComponentMap = {
  moneyInputBox: MoneyInput,
  switch: SwitchInput,
  inputBox: TextInput,
  account: AccountInputWrapper,
  calendar: CalendarInput,
  customPicker: CustomPickerInput,
  card: CardInput,
}

export function ContractCreatePage() {
  const { t } = useTranslation()
  const dispatch = useDispatch()
  const router = useRouter()
  const group = useAppSelector((state) => state.group.group)
  const user = useAppSelector((state) => state.user.user)
  const contract = useAppSelector((state) => state.contract.contract)
  const [openModal, setOpenModal] = useState(false)
  const contractRequest = useAppSelector(
    (state) => state.contract.contractRequest,
  )
  const rentAccountConfirm = useAppSelector(
    (state) => state.contract.rentAccountConfirm,
  )

  const cardConfirm = useAppSelector((state) => state.contract.cardConfirm)
  const {
    step,
    stepContent,
    currentStepContent,
    handleNext,
    handleBack,
    isFirstStep,
    isLastStep,
  } = useContractSteps()
  const hasCreated = useRef(false)
  const [isLoading, setIsLoading] = useState(false)
  useEffect(() => {
    const createContract = async () => {
      if (hasCreated.current) return
      hasCreated.current = true

      if (!user.contractId || !user.groupId) {
        const response = await createEmptyContract({
          groupId: user.groupId as number,
        })
        if (response.success) {
          setIsLoading(true)
          await dispatch(setContractId(response.data.id))
          await dispatch(initContractRequest(group.members))
        }
      } else {
        dispatch(setContractRequest(contract))
      }
    }

    createContract()
  }, [])
  useEffect(() => {
    if (
      isLoading &&
      user.contractId &&
      contractRequest.status === ContractStatus.draft
    ) {
      updateContract({
        contractId: user.contractId,
        contract: contractRequest,
      })
      setIsLoading(false)
    }
  }, [isLoading, contractRequest, user])

  const isAfter = (item: string) => {
    return item === 'rentAccountNo'
      ? rentAccountConfirm
      : item === 'utility'
        ? cardConfirm
        : false
  }

  const handleChange = (field: string, value: FieldValue) => {
    if (field === 'rent') {
      dispatch(updateRent(value as ContractRequest['rent']))
    } else {
      dispatch(
        updateContractRequestField({
          field: field as keyof ContractRequest,
          value: value as ContractRequest[keyof ContractRequest],
        }),
      )
    }
  }
  const renderInputComponent = (type: InputType, item: string) => {
    const Component = InputComponents[type]

    if (!Component) return null

    let value: FieldValue

    if (item === 'utility') {
      value = contractRequest.utility.cardId?.toString() ?? ''
    } else if (item === 'rent') {
      value = contractRequest.rent.rentAccountNo ?? ''
    } else {
      value = contractRequest[item as keyof ContractRequest] as FieldValue
    }

    const valueProps = {
      onChange: (value: FieldValue) => handleChange(item, value),
      value,
      formValues: {
        ...contractRequest,
        utility: contractRequest.utility.cardId?.toString() ?? '',
      },
      item,
    }

    return <Component {...valueProps} />
  }
  const validations = useAppSelector((state) => state.contract.validations)
  const [isFinalSubmit, setIsFinalSubmit] = useState(false)

  useEffect(() => {
    if (isFinalSubmit) {
      setOpenModal(true)
    }
  }, [isFinalSubmit])

  const handleUpdate = async () => {
    if (isValid) {
      setIsFinalSubmit(true)
      setShouldUpdate(true)
    }
  }
  useEffect(() => {
    dispatch(validateContractRequest())
  }, [contractRequest, dispatch])

  const handleModalConfirm = async () => {
    setOpenModal(false)
    setShouldUpdate(true)
  }
  const isValid = Object.values(validations).every((v) => v.isValid)
  const [shouldUpdate, setShouldUpdate] = useState(false)
  useEffect(() => {
    const handleDraft = async () => {
      if (shouldUpdate) {
        await draftContractRequest()
        setShouldUpdate(false)
      }
    }
    handleDraft()
  }, [shouldUpdate])

  const handleConfirm = () => (isLastStep ? handleUpdate() : handleNext())
  const handleDraft = async () => {
    setIsFinalSubmit(false)
    setOpenModal(true)
  }
  const hadConfirmed = useRef(false)
  const draftContractRequest = async () => {
    setOpenModal(false)
    if (!user.contractId || hadConfirmed.current) {
      return
    }
    hadConfirmed.current = true
    const response =
      contractRequest.status == ContractStatus.draft
        ? await updateContract({
            contractId: user.contractId,
            contract: contractRequest,
          })
        : await confirmContract({
            contractId: user.contractId,
            contract: contractRequest,
          })
    if (response.success) {
      dispatch(setContract(response.data))
    }

    hadConfirmed.current = true
    if (isFinalSubmit) {
      router.push('/contract/detail')
    }
  }

  return (
    <Container>
      <HeaderContainer>
        <HeaderButton onClick={() => router.replace('/')}>
          <IconButton
            src="/icons/arrow-left.svg"
            alt={t('icon.back')}
            onClick={handleBack}
          />
        </HeaderButton>
        <HeaderPaddingTitle>{t('contract.title')}</HeaderPaddingTitle>
        <DraftLabel onClick={handleDraft}>임시저장</DraftLabel>
      </HeaderContainer>
      <HeaderContainer>
        <ProgressBar
          step={step}
          steps={stepContent.length}
        />
      </HeaderContainer>
      <HeaderContainer>
        <PaddingContainer>
          <UserTileContainer>
            {group?.members.map((user) => (
              <UserContainer key={user.id}>
                {group.leaderId === user.id && (
                  <Image
                    src={'/icons/leader-rotate.svg'}
                    alt={user.name}
                    width={25}
                    height={25}
                    style={{
                      position: 'absolute',
                      top: -15,
                      left: -5,
                    }}
                  />
                )}
                <UserItem
                  user={user}
                  showName={true}
                  size="small"
                />
              </UserContainer>
            ))}
          </UserTileContainer>
        </PaddingContainer>
      </HeaderContainer>
      <FullMain>
        {Object.entries(currentStepContent).map(([item, type]) => (
          <InputWrapper
            key={item}
            id={item}
            isAfter={isAfter(item)}>
            {renderInputComponent(type, item)}
          </InputWrapper>
        ))}
      </FullMain>
      <BottomContainer>
        <ConfirmButton
          onClick={() => {
            if (isFirstStep) {
              router.replace('/')
            } else {
              handleBack()
            }
          }}
          label={isFirstStep ? t('goToHome') : t('prev')}
          variant={'prev'}
        />
        <ConfirmButton
          onClick={handleConfirm}
          label={isLastStep ? t('finish') : t('next')}
          variant={isValid || !isLastStep ? 'next' : 'disabled'}
        />
      </BottomContainer>
      <Modal
        open={openModal}
        onOpenChange={setOpenModal}
        onConfirm={handleModalConfirm}
        title={
          isFinalSubmit ? t('contract.update.title') : t('contract.draft.title')
        }
        description={
          isFinalSubmit
            ? t('contract.update.content')
            : t('contract.draft.content')
        }
      />
    </Container>
  )
}
