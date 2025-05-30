'use client'

/** @jsxImportSource @emotion/react */
import { useEffect, useMemo, useRef } from 'react'

import * as d3 from 'd3'

import { UserItem } from '@/components'
import { useAppSelector } from '@/hooks/useAppSelector'
import { formatMoney } from '@/utils/format'

import { BoxContainer } from '../../../styles'
import { TextBox } from '../Stats/styles'
import {
  ContentContainer,
  DisabledColorText,
  GraphContainer,
  LowColorText,
  TextContainer,
  TopDescription,
  UserContainer,
} from './styles'

type DataItem = {
  userId: number
  amount: number
  status: boolean
}

type DonutChartProps = {
  data: DataItem[]
  width?: number
  height?: number
}

export const Graph = ({ data, width = 250, height = 250 }: DonutChartProps) => {
  const ref = useRef<SVGSVGElement | null>(null)
  const user = useAppSelector((state) => state.user.user)
  const highlightUserId = user?.id
  const rentInfo = useAppSelector((state) => state.pledge.rent)
  const month = Number(
    rentInfo?.monthList[0]?.month.slice(5) ?? new Date().getMonth() + 1,
  )
  const bonus = useMemo(() => {
    return rentInfo?.currentMonth.find((item) => item.userId == 1)?.amount ?? 0
  }, [rentInfo, user])

  const group = useAppSelector((state) => state.group.group)
  const userData = group.members.map((member) => ({
    ...member,
    amount:
      rentInfo?.currentMonth.find((item) => item.userId === member.id)
        ?.amount ?? 0,
    status:
      rentInfo?.currentMonth.find((item) => item.userId === member.id)
        ?.status ?? false,
  }))

  useEffect(() => {
    if (!ref.current) return
    const radius = Math.min(width, height) / 2
    const size = Math.min(width, height)
    const total = d3.sum(data, (d: DataItem) => d.amount)
    const highlight = data.find((d: DataItem) => d.userId === highlightUserId)
    const percent = highlight ? Math.round((highlight.amount / total) * 100) : 0
    const color = d3.scaleOrdinal<string, string>(d3.schemeSet2)
    const arc = d3
      .arc<d3.PieArcDatum<DataItem>>()
      .innerRadius(radius - 70)
      .outerRadius((d: d3.PieArcDatum<DataItem>) =>
        d.data.userId === highlightUserId ? radius : radius - 20,
      )

    const pie = d3
      .pie<DataItem>()
      .value((d: DataItem) => d.amount)
      .sort(null)

    d3.select(ref.current).selectAll('*').remove() // 초기화
    const svg = d3
      .select(ref.current)
      .attr('width', size)
      .attr('height', size)
      .append('g')
      .attr('transform', `translate(${size / 2}, ${size / 2})`)

    const arcs = pie(data)

    svg
      .selectAll('path')
      .data(arcs)
      .enter()
      .append('path')
      .attr('d', (d) => arc({ ...d, startAngle: 0, endAngle: 0 })!) // 초기 각도 0
      .attr('fill', (d: d3.PieArcDatum<DataItem>) =>
        d.data.userId === highlightUserId
          ? '#54a0ff'
          : color(d.index.toString()),
      )
      .attr('opacity', 0) // 초기 투명도 0
      .transition()
      .duration(800)
      .attr('opacity', 1) // 최종 투명도 1
      .attrTween('d', function (d) {
        const interpolate = d3.interpolate(
          { ...d, startAngle: 0, endAngle: 0 },
          d,
        )
        return function (t) {
          return arc(interpolate(t))!
        }
      })

    const [x, y] = arc.centroid(arcs[0])
    // 퍼센트 레이블
    svg
      .selectAll('text.label')
      .data(arcs)
      .enter()
      .append('text')
      .attr('class', 'label')
      .attr('opacity', 0)
      .transition()
      .duration(800)
      .attr('opacity', 1)
      .attr('transform', (d: d3.PieArcDatum<DataItem>) => {
        const [x, y] = arc.centroid(d)
        return `translate(${x - 22}, ${y - 22})`
      })
      .text((d: d3.PieArcDatum<DataItem>) => {
        if (Math.round((d.data.amount / total) * 100) > 0) {
          return `${Math.round((d.data.amount / total) * 100)}%`
        } else {
          return ''
        }
      })
      .attr('fill', '#292f35')
      .style('font-size', '14px')
      .style('font-family', 'var(--font-paperlogy-regular)')

    const highlightArc = arcs.find(
      (d: d3.PieArcDatum<DataItem>) => d.data.userId === highlightUserId,
    )

    if (highlightArc) {
      const [x, y] = arc.centroid(highlightArc)
      const defs = svg.append('defs')
      const filter = defs
        .append('filter')
        .attr('id', 'drop-shadow')
        .attr('x', '-50%')
        .attr('y', '-50%')
        .attr('width', '200%')
        .attr('height', '200%')

      filter
        .append('feDropShadow')
        .attr('dx', 0)
        .attr('dy', 2)
        .attr('stdDeviation', 3)
        .attr('flood-color', '#000')
        .attr('flood-opacity', 0.3)

      svg
        .append('circle')
        .attr('cx', x)
        .attr('cy', y)
        .attr('r', 40) // 원의 크기를 적절히 조정
        .attr('fill', 'white')
        .attr('stroke', 'lightgray')
        .attr('stroke-width', 1)
        .attr('filter', 'url(#drop-shadow)')

      svg
        .append('text')
        .attr('dx', x)
        .attr('dy', y - 8)
        .text(user?.name)
        .attr('text-anchor', 'middle')
        .style('font-size', '12px')
        .style('font-family', 'var(--font-paperlogy-semi-bold)')
      if (percent > 0) {
        svg
          .append('text')
          .attr('dx', x)
          .attr('dy', y + 12)
          .text(`${percent}%`)
          .attr('text-anchor', 'middle')
          .style('font-size', '18px')
          .style('font-family', 'var(--font-paperlogy-semi-bold)')
          .attr('fill', '#54a0ff')
      }
    }
  }, [data, highlightUserId, width, height])

  return (
    <BoxContainer>
      <ContentContainer>
        <TopDescription>월세 - {month}월 정산내역</TopDescription>
        <GraphContainer>
          <svg ref={ref} />
        </GraphContainer>
        <ContentContainer>
          <TextContainer>
            <UserContainer>
              {userData.map((item) => (
                <TextBox key={item.id}>
                  <span>
                    <UserItem
                      user={{
                        id: item.id,
                        name: item.name,
                        nickname: item.nickname,
                        profileImage: item.profileImage,
                      }}
                      variant="bar"
                      size="xs"
                      showName={true}
                    />
                    {rentInfo?.totalAmount != 0 && (
                      <DisabledColorText>
                        {Math.round(
                          (item.amount / Number(rentInfo?.totalAmount)) * 100,
                        )}
                        %
                      </DisabledColorText>
                    )}
                  </span>
                  <LowColorText>{formatMoney(item.amount)}</LowColorText>
                </TextBox>
              ))}
            </UserContainer>
            {bonus > 0 && (
              <LowColorText>
                Cha:nG가 {formatMoney(bonus)}을 지불했어요
              </LowColorText>
            )}
          </TextContainer>
        </ContentContainer>
      </ContentContainer>
    </BoxContainer>
  )
}
