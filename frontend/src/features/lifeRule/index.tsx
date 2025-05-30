'use client'

import React, { useEffect, useMemo, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import {
  createLifeRule,
  getLifeRule,
  postNotApprovedIds,
} from '@/apis/lifeRule'
import { AnimatedImage, ConfirmButton, Modal, NavLayout } from '@/components'
import { TopHeader } from '@/features/lifeRule/components/TopHeader'
import { useAppSelector } from '@/hooks'
import { setLifeRules, setNotApprovedIds } from '@/store/slices/lifeRuleSlice'
import { RootState } from '@/store/store'
import { Title } from '@/styles/styles'
import { ImageVariant } from '@/types/ui'

import { LifeRuleList } from './components/LifeRuleList'
import { NoticeBar } from './components/NoticeBar'
import { Description, EmptyContainer, FullMain, TitleContainer } from './styles'

export function LifeRulePage() {
  const { t } = useTranslation()
  const dispatch = useDispatch()
  const lifeRuleList = useAppSelector(
    (state: RootState) => state.lifeRule.lifeRules,
  )

  const [isModalOpen, setIsModalOpen] = useState<boolean>(false)
  const [isEmpty, setIsEmpty] = useState<boolean>(false)

  const handleCreate = async () => {
    const response = await createLifeRule({ rules: [] })
    if (response.success) {
      // router.push('/lifeRule/update')
      // dispatch(setHomeOverviewLifeRuleApproved(true))
    }
  }
  const homeOverview = useAppSelector(
    (state: RootState) => state.user.homeOverview,
  )

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 생활규칙 목록 가져오기
        const lifeRuleResponse = await getLifeRule()
        if (lifeRuleResponse.success) {
          console.log('lifeRuleResponse', lifeRuleResponse.data)
          await dispatch(setLifeRules(lifeRuleResponse.data.lifeRules))
        } else {
          setIsEmpty(true)
        }
        // 업데이트된 내용 확인
      } catch (error) {
        console.error('Error fetching data:', error)
      }
    }
    fetchData()
  }, [])

  const group = useAppSelector((state) => state.group.group)
  const user = useAppSelector((state) => state.user.user)
  const notApprovedId: number[] = useAppSelector(
    (state) => state.lifeRule.notApprovedIds,
  )
  const shouldApprove = useMemo(
    () => notApprovedId.includes(user.id),
    [notApprovedId, user.id],
  )

  useEffect(() => {
    const fetchRent = async () => {
      const response = await postNotApprovedIds(group.id)
      if (response.success) {
        dispatch(setNotApprovedIds(response.data.notApprovedIds))
      }
    }
    fetchRent()
  }, [group, dispatch])
  return (
    <NavLayout
      title={t('lifeRule.title')}
      headerRightButton={
        <TopHeader
          isUpdated={notApprovedId.length > 0}
          handleOpenModal={() => setIsModalOpen(true)}
        />
      }>
      <FullMain>
        {!isEmpty && homeOverview?.isLifeRuleApproved && (
          <NoticeBar
            message={
              shouldApprove
                ? t('lifeRule.updateMessage')
                : '룸메들의 승인을 기다리는 생활 규칙을 확인해보세요'
            }
          />
        )}
        {isEmpty && (
          <EmptyContainer>
            <AnimatedImage
              src={'/images/lifeRule/life-rule-no.svg'}
              alt={'생활 규칙 수정아이콘'}
              width={80}
              height={80}
              variant={ImageVariant.bounce}
            />
            <TitleContainer>
              <Title>만들어진 생활 규칙이 없습니다</Title>
            </TitleContainer>
            <Description>
              친구들과 대화를 통해 생활 규칙을 만들어보세요!
            </Description>
            <ConfirmButton
              label={'생활 규칙 생성하러 가기'}
              onClick={handleCreate}
            />
          </EmptyContainer>
        )}

        {lifeRuleList?.length > 0 && (
          <LifeRuleList lifeRuleList={lifeRuleList} />
        )}
      </FullMain>
      <Modal
        open={isModalOpen}
        onOpenChange={setIsModalOpen}
        onConfirm={() => setIsModalOpen(false)}
        title={t('lifeRule.modal.title')}
        description={t('lifeRule.modal.description')}
        disablePrev={true}
      />
    </NavLayout>
  )
}
