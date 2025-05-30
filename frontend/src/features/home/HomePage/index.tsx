'use client'

import { useCallback, useEffect, useMemo, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { getContract, getContractMembers, getGroup } from '@/apis/group'
import { getUnreadNotificationCount } from '@/apis/notification'
import { getHomeOverview, getUserInfo } from '@/apis/user'
import { AnimatedImage, CardButton, IconButton, UserItem } from '@/components'
import { Image } from '@/components'
import { useAppSelector, useCopyInviteCode } from '@/hooks'
import { setContract, setContractMembers } from '@/store/slices/contractSlice'
import { setGroup } from '@/store/slices/groupSlice'
import {
  setIsNoticeModalOpen,
  setNoContractWhenLogIn,
} from '@/store/slices/uiSlice'
import { setHomeOverview, setUser } from '@/store/slices/userSlice'
import {
  CenterContainer,
  Container,
  Container2,
  DefaultContainer,
  EmptyContainer,
  HeaderTitle,
  PaddingContainer,
  ShowCenterBox,
  SlimContainer,
  TextCenterContainer,
  Title,
} from '@/styles/styles'
import { ContractStatus } from '@/types/contract'
import { CardItem, ImageVariant } from '@/types/ui'

import {
  ConfirmedHomeContents,
  DefaultHomeContents,
  HomeLayout,
} from '../components'
import {
  CopyBox,
  Dday,
  Description,
  GroupName,
  HomeUserTileContainer,
  ImageContainer,
  Main,
  MainWrapper,
  NoticeContainer,
} from './styles'

export function HomePage() {
  const [isMounted, setIsMounted] = useState(false)
  const router = useRouter()
  const accessToken = useAppSelector(
    (state) => state.auth.loginToken.accessToken,
  )
  useEffect(() => {
    if (!isMounted) return

    if (!accessToken) {
      router.push('/auth/login')
      return
    }
  }, [accessToken, router, isMounted])

  const user = useAppSelector((state) => state.user.user)
  useEffect(() => {
    if (!user.id) return
    if (!user.groupId) {
      router.push('/onboarding')
    }
  }, [user, router])

  const { t } = useTranslation()
  const dispatch = useDispatch()

  const [isHeaderTransparent, setIsHeaderTransparent] = useState(true)
  const contract = useAppSelector((state) => state.contract.contract)
  const [status, setStatus] = useState<ContractStatus>(contract.status)
  const homeOverview = useAppSelector((state) => state.user.homeOverview)

  const group = useAppSelector((state) => state.group.group)
  const shouldInvite = useMemo(() => {
    return group ? group?.members?.length < group?.maxParticipants : false
  }, [group])
  const contractMembers = useAppSelector(
    (state) => state.contract.contractMembers,
  )
  const confirmedCount = useMemo(() => {
    return contractMembers?.filter(
      (member) => member.status === ContractStatus.confirmed,
    ).length
  }, [contractMembers])
  const homeDescription = useMemo(() => {
    switch (status) {
      case ContractStatus.confirmed:
        return (
          <Dday>
            <span>함께한지</span>
            <div>
              {Math.floor(
                (new Date().getTime() -
                  new Date(contract.createdAt).getTime()) /
                  (1000 * 60 * 60 * 24),
              ) + 1}
            </div>
            <p>일째</p>
          </Dday>
        )
      case ContractStatus.pending:
      case ContractStatus.isContractApproved:
        return (
          <Description>
            서약서 승인이 진행 중입니다. {confirmedCount} /
            {group.maxParticipants}
          </Description>
        )
      default:
    }
  }, [status, t, group])

  const [hasUnreadNotification, setHasUnreadNotification] = useState(false)
  useEffect(() => {
    setIsMounted(true)
  }, [])

  const showMemberStatus = useMemo(() => {
    return (
      status !== ContractStatus.confirmed &&
      status !== ContractStatus.shouldInvite &&
      status !== ContractStatus.none
    )
  }, [status])

  const fetchContract = useCallback(async () => {
    if (user.contractId) {
      const response = await getContract(user.contractId)
      if (response.success) {
        dispatch(setContract(response.data))
      }
    } else if (user.id) {
      const response = await getUserInfo()
      if (response.success) {
        dispatch(setUser(response.data))
      }
    }
  }, [user.contractId, dispatch])

  useEffect(() => {
    fetchContract()
  }, [user.contractId, fetchContract])

  const fetchHomeOverView = useCallback(async () => {
    if (!user.groupId) return
    const response = await getHomeOverview()
    if (response.success) {
      dispatch(setHomeOverview(response.data))
    }
  }, [dispatch, user.id])

  const fetchUnreadNotificationCount = useCallback(async () => {
    const response = await getUnreadNotificationCount(user.id)
    if (response.success) {
      setHasUnreadNotification(response.data.count > 0)
    }
  }, [user.id])

  useEffect(() => {
    if (!user.id) return
    fetchUnreadNotificationCount()
    const interval = setInterval(
      () => {
        fetchUnreadNotificationCount()
      },
      30 * 60 * 1000,
    )
    return () => clearInterval(interval)
  }, [fetchUnreadNotificationCount])

  const copyInviteCode = useCopyInviteCode(
    group.inviteCode,
    user.name,
    group.name,
  )

  const handleCopy = () => {
    copyInviteCode()
  }

  useEffect(() => {
    if (!user.id) return
    if (shouldInvite) {
      setStatus(ContractStatus.shouldInvite)
    } else if (!user.contractId) {
      setStatus(ContractStatus.none)
    } else if (contract.status === ContractStatus.pending) {
      const isApproved =
        contractMembers.find((item) => item.id === user.id)?.status ===
        ContractStatus.confirmed
      setStatus(
        isApproved ? ContractStatus.isContractApproved : ContractStatus.pending,
      )
    } else {
      setStatus(contract.status)
    }
    fetchHomeOverView()
  }, [user, shouldInvite, contract.status, contractMembers])

  const fetchGroup = useCallback(async () => {
    if (user.groupId == null || user.groupId === 0) return
    const response = await getGroup(user.groupId)
    if (response.success) {
      dispatch(setGroup(response.data))
    }
  }, [user.groupId, dispatch])

  useEffect(() => {
    fetchGroup()
  }, [user.groupId])

  const fetchContractMembers = useCallback(async () => {
    if (!user.contractId) return
    const response = await getContractMembers(user.contractId)
    if (response.success) {
      dispatch(setContractMembers(response.data))
    }
  }, [user.contractId, dispatch])

  useEffect(() => {
    if (
      status !== ContractStatus.none &&
      status !== ContractStatus.shouldInvite
    ) {
      fetchContractMembers()
    }
  }, [status])

  const cardItems: { key: ContractStatus; item: CardItem[] }[] = [
    {
      key: ContractStatus.shouldInvite,
      item: [
        {
          url: '/group/create/shareInviteCode',
          image: '/images/group/group-join.svg',
          title: t('main.codeCopy.title'),
          description: t('main.codeCopy.description'),
          children: (
            <CopyBox>
              <Description>{t('main.codeCopy.label')}</Description>
              <HeaderTitle>{group.inviteCode}</HeaderTitle>
              <IconButton
                onClick={handleCopy}
                src="/icons/copy.svg"
                alt="copy"
              />
            </CopyBox>
          ),
        },
      ],
    },
    {
      key: ContractStatus.none,
      item: [
        {
          url: '/contract/create',
          image: '/images/etc/contract-create.svg',
          title: t('contract.detail.none.button'),
          description: t('contract.detail.none.description', {
            value: homeOverview?.groupName,
          }),
        },
      ],
    },
    {
      key: ContractStatus.draft,
      item: [
        {
          url: '/contract/create',
          image: '/images/etc/contract-create.svg',
          title: t('contract.detail.draft.button'),
          description: t('contract.detail.draft.description'),
        },
      ],
    },
    {
      key: ContractStatus.isContractApproved,
      item: [
        {
          url: '/contract/detail',
          image: '/images/etc/contract-create.svg',
          title: t('contract.detail.is_contract_approved.cardButton'),
          description: t('contract.detail.is_contract_approved.description'),
        },
      ],
    },
    {
      key: ContractStatus.pending,
      item: [
        {
          url: '/contract/detail',
          image: '/images/etc/contract-create.svg',
          title: t('contract.detail.pending.button'),
          description: t('contract.detail.pending.description'),
        },
      ],
    },
    {
      key: ContractStatus.reviewRequired,
      item: [
        {
          url: '/contract/detail',
          image: '/images/etc/contract-create.svg',
          title: t('contract.detail.review_required.button'),
          description: t('contract.detail.review_required.description'),
        },
      ],
    },
  ]

  if (!isMounted) {
    return null
  }

  if (!accessToken || !user.groupId) {
    return (
      <Container2>
        <SlimContainer>
          <CenterContainer>
            <AnimatedImage
              src="/icons/logo-no-padding.svg"
              alt="logo"
              width={90}
              height={90}
              variant={ImageVariant.bounce}
            />
          </CenterContainer>
          <div
            style={{
              display: 'flex',
              alignItems: 'center',
              flexDirection: 'column',
              justifyContent: 'center',
              width: '100%',
              textAlign: 'center',
              fontFamily: 'var(--font-paperlogy-medium)',
              color: 'var(--color-text-regular)',
              gap: '10px',
            }}>
            <Image
              src="/icons/logo-chainG-no.svg"
              alt="logo"
              width={120}
              height={20}
            />
            <div>
              <p
                style={{
                  color: '#586575',
                }}>
                가볍지만 신뢰할 수 있는 동거 서약관리 서비스
              </p>
            </div>
          </div>
        </SlimContainer>
      </Container2>
    )
  }
  return (
    <HomeLayout
      isHeaderTransparent={isHeaderTransparent}
      setIsHeaderTransparent={setIsHeaderTransparent}
      header="ChainG"
      headerRightButton={
        <NoticeContainer>
          {status === ContractStatus.confirmed && (
            <IconButton
              onClick={() => dispatch(setIsNoticeModalOpen(true))}
              src="/icons/announce.svg"
              alt="notice"
            />
          )}
          <IconButton
            onClick={() => router.push('/notification')}
            src={
              hasUnreadNotification
                ? '/icons/notification-active.svg'
                : '/icons/notification-inactive.svg'
            }
            alt={t('notice.title')}
          />
        </NoticeContainer>
      }>
      <Main>
        <GroupName>{group.name}</GroupName>
        {homeDescription}
      </Main>
      <MainWrapper>
        <ImageContainer
          style={{
            opacity: isHeaderTransparent ? '1' : '0',
            boxShadow: isHeaderTransparent
              ? 'none'
              : '0 2px 4px rgba(0,0,0,0.1)',
            transition: 'all 0.3s ease',
          }}>
          <HomeUserTileContainer>
            {group?.members &&
              group.members.map((user) => (
                <UserItem
                  key={user.id}
                  user={user}
                  variant="tile"
                  size="small"
                  contractStatus={
                    showMemberStatus
                      ? contractMembers?.find((member) => member.id === user.id)
                          ?.status
                      : ContractStatus.none
                  }
                />
              ))}
          </HomeUserTileContainer>
        </ImageContainer>
        {status == ContractStatus.confirmed ? (
          <ConfirmedHomeContents />
        ) : (
          <DefaultHomeContents
            cardItems={
              cardItems.find((item) => item.key === status)?.item ?? []
            }
          />
        )}
      </MainWrapper>
    </HomeLayout>
  )
}
