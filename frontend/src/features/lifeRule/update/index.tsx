'use client'

import React, { useState } from 'react'
import { FormProvider, useFieldArray, useForm } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import { useDispatch } from 'react-redux'

import { useRouter } from 'next/navigation'

import { updateLifeRule } from '@/apis/lifeRule'
import { Image } from '@/components'
import { Modal } from '@/components/'
import { ConfirmButton } from '@/components/ConfirmButton'
import { TopHeader } from '@/components/TopHeader'
import { useAppSelector } from '@/hooks/useAppSelector'
import { setHomeOverviewLifeRuleApproved } from '@/store/slices/userSlice'
import { Container } from '@/styles/styles'
import { LifeRuleUpdateVariant } from '@/types/lifeRule'

import { LifeRuleUpdateListItem } from '../components/LifeRuleUpdateListItem'
import { ConfirmContainer, FullMain, LifeRuleUpdateList } from './styles'

type FormValues = {
  items: Array<{
    id: number
    variant: LifeRuleUpdateVariant
    actionType: LifeRuleUpdateVariant
    category: string
    content: string
  }>
}

export function LifeRuleUpdatePage() {
  const { t } = useTranslation()
  const router = useRouter()
  const dispatch = useDispatch()
  const [isModalOpen, setIsModalOpen] = useState(false)

  const lifeRules = useAppSelector((state) => state.lifeRule.lifeRules)

  const methods = useForm<FormValues>({
    defaultValues: {
      items: lifeRules.map((rule) => ({
        id: rule.id,
        variant: 'DEFAULT',
        actionType: 'DEFAULT',
        category: rule.category,
        content: rule.content,
      })),
    },
  })

  const { fields, append, update } = useFieldArray({
    control: methods.control,
    name: 'items',
    keyName: 'fieldId',
  })

  const items = methods.watch('items')
  const [isUpdated, setIsUpdated] = useState(false)
  const [isUpdateMode, setIsUpdateMode] = useState(false)
  const [localContents, setLocalContents] = useState<{ [key: string]: string }>(
    {},
  )

  const handleVariantChange = (
    id: string | number,
    newVariant: LifeRuleUpdateVariant,
    content: string,
  ) => {
    try {
      console.log('handleVariantChange', id, newVariant, content)
      const itemIndex = items.findIndex(
        (item) => String(item.id) === String(id),
      )

      if (itemIndex === -1) {
        return
      }

      const currentItem = items[itemIndex]

      update(itemIndex, {
        ...currentItem,
        variant: newVariant,
        actionType: newVariant,
        content: content,
        category: currentItem.category,
      })

      setIsUpdateMode(true)
      setIsUpdated(true)
    } catch (error) {
      console.error('Error in handleVariantChange:', error)
    }
  }

  const handleCreateNew = () => {
    append({
      id: Date.now(), // 현재시간으로 생성해버리기
      variant: 'CREATE',
      actionType: 'CREATE',
      category: 'OTHER',
      content: '',
    })

    setIsUpdateMode(true)
    setIsUpdated(true)
  }

  const handleContentChange = (id: string | number, content: string) => {
    setLocalContents((prev) => ({
      ...prev,
      [id]: content,
    }))
  }

  const handleAddItem = (index: number) => {
    const currentItem = items[index]
    const content = localContents[currentItem.id] || ''

    if (content.trim()) {
      const itemIndex = items.findIndex((item) => item.id === currentItem.id)
      if (itemIndex !== -1) {
        update(itemIndex, {
          ...currentItem,
          content,
          category: currentItem.category,
          variant: 'DEFAULT',
          actionType:
            currentItem.actionType === 'CREATE' ? 'CREATE' : 'DEFAULT',
        })
        setLocalContents((prev) => {
          const newState = { ...prev }
          delete newState[currentItem.id]
          return newState
        })
        setIsUpdated(true)
      }
    }
  }

  const handleUpdateConfirm = (index: number) => {
    const currentItem = items[index]
    const content = localContents[currentItem.id] || currentItem.content

    if (content.trim()) {
      update(index, {
        ...currentItem,
        content,
        variant: 'DEFAULT',
        actionType: 'UPDATE',
      })
      setLocalContents((prev) => {
        const newState = { ...prev }
        delete newState[currentItem.id]
        return newState
      })
      setIsUpdated(true)
    }
  }

  const handleConfirmClick = () => {
    if (isUpdated && isUpdateMode) {
      setIsModalOpen(true)
    }
  }

  const handleModalConfirm = async () => {
    setIsModalOpen(false)
    console.log('handleModalConfirm', items)

    const filteredUpdates = items
      .filter((update) => !(update.actionType == 'DEFAULT'))
      .filter(
        (update) =>
          !(Number(update.id) > 1000 && update.actionType == 'DELETE'),
      )
      .map((item) => ({
        id: Number(item.id) > 1000 ? null : Number(item.id),
        content: item.content.trim(),
        category: item.category,
        actionType: Number(item.id) > 1000 ? 'CREATE' : item.actionType,
      }))
      .filter(
        (update) => !(update.actionType == 'CREATE' && update.content == ''),
      )
    console.log('filteredUpdates', filteredUpdates)
    // const filteredUpdates = updates.filter((update) => update.id < 1000)
    const response = await updateLifeRule({ updates: filteredUpdates })

    if (response.success) {
      router.replace('/lifeRule')
      dispatch(setHomeOverviewLifeRuleApproved(true))
    }
  }

  return (
    <Container>
      <TopHeader title={t('lifeRule.updateTitle')} />
      <FormProvider {...methods}>
        <FullMain>
          <LifeRuleUpdateList>
            {fields.map((field, index) => {
              const item = items[index]
              const localContent = localContents[item.id]
              return (
                <LifeRuleUpdateListItem
                  key={field.fieldId}
                  lifeRule={item}
                  variant={item.variant}
                  content={
                    localContent !== undefined ? localContent : item.content
                  }
                  setVariant={(variant, content) =>
                    handleVariantChange(item.id, variant, content)
                  }
                  onContentChange={(content) =>
                    handleContentChange(item.id, content)
                  }
                  onAddItem={handleAddItem}
                  onUpdateConfirm={() => handleUpdateConfirm(index)}
                  index={index}
                />
              )
            })}
          </LifeRuleUpdateList>
          <div onClick={handleCreateNew}>
            <Image
              src="/icons/button-create.svg"
              alt="create"
              width={46}
              height={46}
            />
          </div>
        </FullMain>
      </FormProvider>

      <ConfirmContainer>
        <ConfirmButton
          label="완료"
          variant={isUpdated && isUpdateMode ? 'next' : 'disabled'}
          onClick={handleConfirmClick}
        />
      </ConfirmContainer>

      <Modal
        open={isModalOpen}
        onOpenChange={setIsModalOpen}
        onConfirm={handleModalConfirm}
        title={t('lifeRule.updateModal.title')}
        description={t('lifeRule.updateModal.description')}
        confirmText={t('lifeRule.updateModal.confirmText')}
      />
    </Container>
  )
}
