'use client'

import { useCallback, useEffect, useRef, useState } from 'react'
import { useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import {
  approveContract,
  confirmContract,
  createContractPDF,
  getContract,
} from '@/apis/group'
import { ConfirmButton, InputBox, Modal, TopHeader } from '@/components'
import { Image } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import { setShowContractApprovedModal } from '@/store/slices/appSlice'
import { setContract } from '@/store/slices/contractSlice'
import { setNoContractWhenLogIn } from '@/store/slices/uiSlice'
import { ImageContainer, Label, PaddingContainer, Title } from '@/styles/styles'
import { ContractStatus, RentUser } from '@/types/contract'

import { ContractViewer } from './component/ContractViewer'
import { BottomContainer, Container, FullMain, HeaderContainer } from './styles'

export function ContractDetail() {
  const dispatch = useDispatch()
  const router = useRouter()
  const { register, handleSubmit } = useForm<{ accountNo: string }>({
    defaultValues: { accountNo: '' },
  })
  const user = useAppSelector((state) => state.user.user)
  const group = useAppSelector((state) => state.group.group)

  const contractMembers = useAppSelector(
    (state) => state.contract.contractMembers,
  )
  const [status, setStatus] = useState<ContractStatus>(
    ContractStatus.none as ContractStatus,
  )
  const contract = useAppSelector((state) => state.contract.contract)
  const fetchContract = useCallback(async () => {
    if (user.contractId) {
      const response = await getContract(user.contractId)
      if (response.success) {
        dispatch(setContract(response.data))
      }
    }
  }, [user.contractId, dispatch])

  useEffect(() => {
    fetchContract()
  }, [user.contractId, fetchContract])

  const rentUserList: RentUser[] = group.members.map((member) => {
    return {
      ...member,
      amount:
        contract.rent.userPaymentInfo.find((info) => info.userId === member.id)
          ?.amount ?? 0,
      ratio:
        contract.rent.userPaymentInfo.find((info) => info.userId === member.id)
          ?.ratio ?? 0,
    }
  })

  const [title, setTitle] = useState('contract.detail.none.title')
  const [label, setLabel] = useState('contract.detail.none.label')
  const [button, setButton] = useState('contract.detail.none.button')
  const [description, setDescription] = useState(
    'contract.detail.none.description',
  )
  const { t } = useTranslation()
  const savePdf = async () => {
    const response = await createContractPDF(contract.id)
    if (response.success) {
      const url = response.data.presignedUrl
      window.open(url, '_blank')
    }
  }
  useEffect(() => {
    setTitle(`contract.detail.${status.toLowerCase()}.title`)
    setLabel(`contract.detail.${status.toLowerCase()}.label`)
    setButton(`contract.detail.${status.toLowerCase()}.button`)
    setDescription(`contract.detail.${status.toLowerCase()}.description`)
  }, [status])

  const approveContractWithAccountNo = async (data: { accountNo: string }) => {
    const success = await approveContract(contract.id, {
      accountNo: data.accountNo,
    })
    if (success) {
      await fetchContract()
      dispatch(setShowContractApprovedModal(true))
    }
    setOpenModal(false)
    router.push('/')
  }

  const confirmModal = async () => {
    if (useModifyModal || status === ContractStatus.isContractApproved) {
      router.push('/contract/create')
    } else if (status === ContractStatus.pending) {
      await handleSubmit(approveContractWithAccountNo)()
    }
    setShouldConfirm(false)
  }
  const [shouldConfirm, setShouldConfirm] = useState(false)
  const [openModal, setOpenModal] = useState(false)
  const [modalTitle, setModalTitle] = useState('')
  const [modalDescription, setModalDescription] = useState('')
  const [modalConfirmText, setModalConfirmText] = useState('')

  useEffect(() => {
    const handleConfirm = async () => {
      if (shouldConfirm) {
        await updateStatus()
      }
    }
    handleConfirm()
  }, [contract.status, shouldConfirm])

  useEffect(() => {
    const member = contractMembers.find((member) => member.id === user.id)
    if (
      member &&
      member?.status === ContractStatus.confirmed &&
      contract.status === ContractStatus.pending
    ) {
      setStatus(ContractStatus.isContractApproved)
    } else {
      setStatus(contract.status)
    }
  }, [user.id, contractMembers, contract.status])
  const [openConfirmModal, setOpenConfirmModal] = useState(false)

  const hadConfirmed = useRef(false)
  const updateContractRequest = async () => {
    if (hadConfirmed.current || !user.contractId) {
      return
    }
    hadConfirmed.current = true
    const response = await confirmContract({
      contractId: user.contractId,
      contract: contract,
    })
    hadConfirmed.current = false
    if (response.success) {
      dispatch(setContract(response.data))
      dispatch(setNoContractWhenLogIn(false))
      setOpenConfirmModal(true)
      // router.push('/contract/detail')
    }
  }
  const [useModifyModal, setUseModifyModal] = useState(false)
  const showModifyModal = () => {
    setModalTitle(t('contract.modify.title'))
    setModalDescription(t('contract.modify.description'))
    setModalConfirmText(t('contract.modify.confirmText'))
    setOpenModal(true)
    setUseModifyModal(true)
  }
  const updateStatus = async () => {
    switch (status) {
      case ContractStatus.none:
        router.push('/contract/create')
        break
      case ContractStatus.draft:
        await updateContractRequest()
        setShouldConfirm(false)
        break
      case ContractStatus.isContractApproved:
        showModifyModal()
        // router.push('/contract/create')
        break
      case ContractStatus.pending:
        setModalTitle(t('contract.approve.title'))
        setModalDescription(t('contract.approve.description'))
        setModalConfirmText(t('contract.approve.confirmText'))
        setOpenModal(true)
        break
      case ContractStatus.reviewRequired:
        setModalTitle(t('contract.detail.review_required.title'))
        setModalDescription(t('contract.detail.review_required.description'))
        setModalConfirmText(t('contract.detail.review_required.confirmText'))
        setOpenModal(true)
        break
      case ContractStatus.confirmed:
        await savePdf()
        break
    }
  }

  return (
    <Container>
      <TopHeader title={t(title)} />
      <FullMain>
        <HeaderContainer>
          <ImageContainer>
            <Image
              src="/images/etc/confirmed.svg"
              alt="contract"
              width={68}
              height={87}
            />
          </ImageContainer>
          <Title>{t(label)}</Title>
          <Label>{t(description, { value: group.name })}</Label>
        </HeaderContainer>
        {user.contractId && rentUserList && (
          <ContractViewer
            contract={contract}
            rentUserList={rentUserList}
          />
        )}
      </FullMain>
      <Modal
        open={openModal}
        onOpenChange={setOpenModal}
        onConfirm={confirmModal}
        title={modalTitle}
        description={modalDescription}
        image={
          useModifyModal
            ? '/images/etc/contract-create.svg'
            : '/images/etc/approved-default.svg'
        }
        confirmText={modalConfirmText}
        onCancel={() => {
          setOpenModal(false)
          setShouldConfirm(false)
        }}>
        {!useModifyModal && (
          <PaddingContainer>
            <InputBox
              label="자동이체용 계좌번호"
              id="accountNo"
              {...register('accountNo')}
              placeholder="계좌번호를 입력해주세요"
            />
          </PaddingContainer>
        )}
      </Modal>
      <Modal
        open={openConfirmModal}
        onOpenChange={setOpenConfirmModal}
        onConfirm={() => {
          setOpenConfirmModal(false)
          router.replace('/contract/detail')
        }}
        title={'초안 서약서 완성을 축하드려요!'}
        description={'모두의 승인을 받아보세요'}
        image={'/images/etc/congratulations.svg'}
      />
      <BottomContainer>
        {status === ContractStatus.isContractApproved && (
          <ConfirmButton
            label={'goToHome'}
            variant="prev"
            onClick={() => router.replace('/')}
          />
        )}
        {status === ContractStatus.pending && (
          <ConfirmButton
            label={'contract.detail.is_contract_approved.button'}
            variant="prev"
            onClick={() => showModifyModal()}
          />
        )}
        <ConfirmButton
          label={button}
          onClick={() => setShouldConfirm(true)}
        />
      </BottomContainer>
    </Container>
  )
}
