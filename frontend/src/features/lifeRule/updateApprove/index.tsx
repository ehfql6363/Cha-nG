'use client'

import React, { useEffect, useRef, useState } from 'react'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import {
  approveUpdateForm,
  getLifeRule,
  getUpdateLifeRule,
  postNotApprovedIds,
} from '@/apis/lifeRule'
import { TopHeader } from '@/components/TopHeader'
import ApproveModal from '@/features/lifeRule/components/ApproveModal'
import { ApproveProfile } from '@/features/lifeRule/components/ApproveProfile'
import { LifeRuleUpdateApproveListItem } from '@/features/lifeRule/components/LifeRuleUpdateApproveListItem'
import { useAppSelector } from '@/hooks/useAppSelector'
import {
  resetNotApprovedIds,
  setNotApprovedIds,
  setUpdateLifeRules,
} from '@/store/slices/lifeRuleSlice'
import { setHomeOverviewLifeRuleApproved } from '@/store/slices/userSlice'
import { Container } from '@/styles/styles'
import { LifeRuleUpdateVariant, UpdateLifeRule } from '@/types/lifeRule'

import { ApproveButton } from '../components/ApproveButton'
import { ConfirmContainer } from '../update/styles'
import { ApproveProfileContainer, FullMain, LifeRuleUpdateList } from './styles'

// 수정된 부분 - 업데이트된 규칙에 대한 타입 정의
export function LifeRuleUpdateApprovePage() {
  const { t } = useTranslation()
  const dispatch = useDispatch()
  const router = useRouter()
  const groupId = useAppSelector((state) => state.group.group.id)
  const user = useAppSelector((state) => state.user.user)
  const notApprovedId: number[] = useAppSelector(
    (state) => state.lifeRule.notApprovedIds,
  )

  const [isModalOpen, setIsModalOpen] = useState(false)
  const [approveType, setApproveType] = useState<'approve' | 'reject' | null>(
    null,
  )
  const [mergedLifeRules, setMergedLifeRules] = useState<UpdateLifeRule[]>([])
  const amIApprove: boolean = !notApprovedId.includes(user.id)

  // 승인/거부 처리 함수
  const handleApprovalUpdate = async (approved: boolean) => {
    try {
      if (user?.id) {
        const approveResponse = await approveUpdateForm({ approved }) //api 쏘는거부터 실패?
        if (approveResponse === true) {
          router.replace('/lifeRule')
          if (!approved) {
            // 승인을 누를때
            dispatch(setHomeOverviewLifeRuleApproved(false))
            dispatch(resetNotApprovedIds())
          } else {
            const response = await postNotApprovedIds(groupId) // api 쏘고
            if (response.success) {
              dispatch(setNotApprovedIds(response.data.notApprovedIds)) // 승인된 목록 업데이트
              if (response.data.notApprovedIds?.length === 0) {
                dispatch(setHomeOverviewLifeRuleApproved(false)) // 비어있으면 false 처리
              }
            }
          }
        }
      }
    } catch (error) {
      console.error('Error updating approval:', error)
    }
  }

  const handleApprove = () => {
    setApproveType('approve')
    setIsModalOpen(true)
  }

  const handleReject = () => {
    setApproveType('reject')
    setIsModalOpen(true)
  }

  const handleModalConfirm = async () => {
    await handleApprovalUpdate(approveType === 'approve')
    setIsModalOpen(false)
  }
  const fechtedData = useRef(false)
  useEffect(() => {
    const initializeData = async () => {
      if (fechtedData.current) return
      fechtedData.current = true
      try {
        // 기존 생활규칙 목록 가져오기
        const lifeRuleResponse = await getLifeRule()
        if (lifeRuleResponse.success) {
          // 기본 규칙들에 actionType을 'DEFAULT'로 설정
          const baseRules = lifeRuleResponse.data.lifeRules.map((rule) => ({
            ...rule,
            actionType: 'DEFAULT' as LifeRuleUpdateVariant, // 명시적으로 타입 설정
          }))
          // 변경 요청된 생활규칙 목록 가져오기
          const updateResponse = await getUpdateLifeRule()
          if (updateResponse.success) {
            dispatch(setUpdateLifeRules(updateResponse.data))

            // 기존 규칙과 업데이트된 규칙 병합
            const mergedRules: UpdateLifeRule[] = [...baseRules]
            // 업데이트된 규칙들 처리
            updateResponse.data.forEach((updateRule) => {
              const existingIndex = mergedRules.findIndex(
                (rule) => rule.id === updateRule.id,
              )
              if (existingIndex !== -1) {
                // 기존 규칙 업데이트

                mergedRules[existingIndex] = {
                  ...updateRule,
                  id: updateRule.id || mergedRules[existingIndex].id,
                  actionType: updateRule.actionType || 'DEFAULT', // actionType을 정확히 설정
                }
              } else {
                // 새로운 규칙 추가
                const maxId = Math.max(
                  ...mergedRules.map((rule) => rule.id || 0),
                  0,
                )
                mergedRules.push({
                  ...updateRule,
                  id: updateRule.id || maxId + 1,
                  actionType: updateRule.actionType || 'CREATE',
                })
              }
            })

            setMergedLifeRules(mergedRules)
          } else {
            dispatch(setHomeOverviewLifeRuleApproved(false))
          }
        }
      } catch (error) {
        console.error('Error initializing data:', error)
      }
      fechtedData.current = false
    }

    initializeData()
  }, [dispatch])

  return (
    <Container>
      <TopHeader title={t('lifeRule.updateApproveTitle')} />
      <FullMain>
        <ApproveProfileContainer>
          <ApproveProfile />
        </ApproveProfileContainer>
        <LifeRuleUpdateList>
          {mergedLifeRules.map((item) => (
            <LifeRuleUpdateApproveListItem
              key={item.id}
              lifeRule={item}
            />
          ))}
        </LifeRuleUpdateList>
      </FullMain>
      <ConfirmContainer>
        {/* 여기에 설정해주기 */}
        {!amIApprove && (
          <ApproveButton
            onApprove={handleApprove}
            onReject={handleReject}
          />
        )}
      </ConfirmContainer>

      <ApproveModal
        open={isModalOpen}
        onOpenChange={setIsModalOpen}
        onConfirm={handleModalConfirm}
        title={approveType === 'approve' ? '생활 규칙 승인' : '생활 규칙 거부'}
        description={
          approveType === 'approve'
            ? '생활 규칙 수정 확인하셨나요?\n모두가 승인 버튼을 누르면 적용됩니다!'
            : '바뀐 생활 규칙을 거부하실건가요?\n거부버튼을 누르면 기존의 생활 규칙이 유지됩니다'
        }
        confirmText={approveType === 'approve' ? '확인' : '확인'}
        image={
          approveType === 'approve'
            ? '/images/etc/approved-true.svg'
            : '/images/etc/approved-false.svg'
        }
      />
    </Container>
  )
}
