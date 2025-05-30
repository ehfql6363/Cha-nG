'use client'

import { useEffect, useMemo, useState } from 'react'
import { useTranslation } from 'react-i18next'

import { AnimatedImage, Image } from '@/components'
import { useAppSelector } from '@/hooks'
import {
  DisabledLabel,
  PaddingContainer,
  ShowCenterBox,
  SlimContainer,
  Title,
} from '@/styles/styles'
import { Duty } from '@/types/duty'
import { formatHourMinuteTime } from '@/utils/formatTime'

import {
  ButtonContainer,
  Card,
  CardContainer,
  Category,
  Container,
  Description,
  ImageContainer,
  IssueContainer,
  IssueContent,
  IssueTitle,
  LifeContainer,
  ScrollContainer,
  StyledButton,
} from './styles'

interface Issue {
  img: string
  category: string
  title: string
  description: string
}

export function DashBoard({ todayMyDutyList }: { todayMyDutyList: Duty[] }) {
  const [dutyIssues, setDutyIssues] = useState<Issue[]>([])
  // const [rentIssues, setRentIssues] = useState<Issue[]>([])
  const [utilityIssues, setUtilityIssues] = useState<Issue[]>([])
  const contract = useAppSelector((state) => state.contract.contract)
  type Category = 'all' | 'duty' | 'rent' | 'utility'
  const [selectedCategory, setSelectedCategory] = useState<Category>('all')
  const { t } = useTranslation()
  const now = new Date()
  const dueRentDate = contract.rent.dueDate
  const dueCardDay = 5
  const nowDay = now.getDay()
  const nowDate = now.getDate()
  const isDueRentDate = nowDate === dueRentDate
  const isDueCardDay = nowDay === dueCardDay
  const isDueRentDateYesterday = nowDate === dueRentDate - 1
  const isDueCardDayYesterday = nowDay === dueCardDay - 1

  const homeOverview = useAppSelector((state) => state.user.homeOverview)

  useEffect(() => {
    if (todayMyDutyList.length > 0) {
      const dutyIssues = todayMyDutyList.map((duty) => ({
        img: duty.category,
        category: 'duty',
        title: duty.title,
        description: duty.useTime ? formatHourMinuteTime(duty.dutyTime) : '',
      }))
      setDutyIssues(dutyIssues)
    }
  }, [todayMyDutyList])

  useEffect(() => {
    const utilityIssues = []
    if (!homeOverview.isUtilityPaid) {
      utilityIssues.push({
        img: 'SETTLEMENT',
        category: 'utility',
        title: '공과금 카드 납부 실패',
        description: '공과금이 미납되었어요',
      })
    }
    if (isDueCardDay) {
      utilityIssues.push({
        img: 'SETTLEMENT',
        category: 'utility',
        title: '공과금 카드 납부일',
        description: '금요일',
      })
    }
    if (isDueCardDayYesterday) {
      utilityIssues.push({
        img: 'SETTLEMENT',
        category: 'utility',
        title: '공과금 카드\n전날',
        description: '목요일',
      })
    }
    setUtilityIssues(utilityIssues)
  }, [])

  const rentIssues = useMemo(() => {
    const newRentissues = []
    if (!homeOverview.isRentPaid) {
      newRentissues.push({
        img: 'SETTLEMENT',
        category: 'rent',
        title: '월세 미납',
        description: '우리 모두\n월세를 못 냈어요',
      })
    }
    if (isDueRentDate) {
      newRentissues.push({
        img: 'SETTLEMENT',
        category: 'rent',
        title: '월세 납부일',
        description: contract.rent.dueDate + '일',
      })
    }
    if (isDueRentDateYesterday) {
      newRentissues.push({
        img: 'SETTLEMENT',
        category: 'rent',
        title: '월세 납부 전날',
        description: contract.rent.dueDate - 1 + '일',
      })
    }
    return newRentissues
  }, [homeOverview, dueRentDate])

  const issues = useMemo(() => {
    return [...dutyIssues, ...rentIssues, ...utilityIssues]
  }, [dutyIssues, rentIssues, utilityIssues])

  const filteredIssues = useMemo(() => {
    if (selectedCategory === 'all') return issues
    return issues.filter((item) => item.category === selectedCategory)
  }, [issues, selectedCategory])

  return (
    <Container>
      <SlimContainer>
        <Title>오늘 나의 이슈</Title>
        {issues.length != 0 && (
          <>
            <ButtonContainer>
              {issues.length != 0 && (
                <StyledButton
                  isSelected={selectedCategory === 'all'}
                  onClick={() => setSelectedCategory('all')}>
                  {t('main.issues.all')}
                </StyledButton>
              )}
              {dutyIssues.length != 0 && (
                <StyledButton
                  isSelected={selectedCategory === 'duty'}
                  onClick={() => setSelectedCategory('duty')}>
                  {t('main.issues.duty')}
                </StyledButton>
              )}
              {rentIssues.length != 0 && (
                <StyledButton
                  isSelected={selectedCategory === 'rent'}
                  onClick={() => setSelectedCategory('rent')}>
                  {t('main.issues.rent')}
                </StyledButton>
              )}
              {utilityIssues.length != 0 && (
                <StyledButton
                  isSelected={selectedCategory === 'utility'}
                  onClick={() => setSelectedCategory('utility')}>
                  {t('main.issues.utility')}
                </StyledButton>
              )}
            </ButtonContainer>
            <ScrollContainer>
              {filteredIssues.map((issue) => (
                <IssueContainer key={issue.title}>
                  <ImageContainer>
                    <Image
                      src={`/images/duty/duty-${issue.img}.svg`}
                      alt={`${issue.category} 이미지`}
                      width={50}
                      height={50}
                    />
                  </ImageContainer>
                  <IssueContent>
                    <Category>{t(`main.issues.${issue.category}`)}</Category>
                    <IssueTitle>{issue.title}</IssueTitle>
                    <Description>{issue.description}</Description>
                  </IssueContent>
                </IssueContainer>
              ))}
            </ScrollContainer>
          </>
        )}
        {issues.length == 0 && (
          <PaddingContainer>
            <ShowCenterBox isDisabled={true}>
              <AnimatedImage
                src="/images/home/home-my-issue-no.svg"
                alt="오늘 나의 이슈가 없어요"
                width={80}
                height={80}
              />
              <DisabledLabel>오늘 나의 이슈가 없어요</DisabledLabel>
            </ShowCenterBox>
          </PaddingContainer>
        )}
      </SlimContainer>
      <LifeContainer>
        <Title>생활 관리</Title>
        <CardContainer>
          <Card href="/lifeRule">
            생활 규칙
            <Image
              src={'/images/home/home-life-rule.svg'}
              alt="생활 규칙"
              width={50}
              height={50}
            />
          </Card>
          <Card href="/duty">
            당번
            <Image
              src={'/images/duty/duty-CLEANING.svg'}
              alt="당번"
              width={50}
              height={50}
            />
          </Card>
        </CardContainer>
      </LifeContainer>
    </Container>
  )
}
