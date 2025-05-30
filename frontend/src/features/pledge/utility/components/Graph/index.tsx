// HistogramChart.tsx
import React, { useEffect, useRef, useState } from 'react'

import * as d3 from 'd3'

import { WeekList } from '@/types/budget'

import { ContentContainer } from './styles'

interface Props {
  data: WeekList[]
}

export const Graph: React.FC<Props> = ({ data }) => {
  const [weekList, setWeekList] = useState<WeekList[]>([])

  useEffect(() => {
    setWeekList([...data].reverse())
  }, [data])
  const ref = useRef<SVGSVGElement | null>(null)

  useEffect(() => {
    if (!ref.current) return

    const margin = { top: 20, right: 30, bottom: 30, left: 40 }
    const width = 400 - margin.left - margin.right
    const height = 200 - margin.top - margin.bottom

    d3.select(ref.current).selectAll('*').remove() // clear previous content

    const svg = d3
      .select(ref.current)
      .attr('width', width + margin.left + margin.right)
      .attr('height', height + margin.top + margin.bottom)
      .append('g')
      .attr('transform', `translate(${margin.left},${margin.top})`)

    const x = d3
      .scaleBand()
      .domain(
        weekList?.map((d) => `${d.month.split('-')[1]}월 ${d.week}주`) ?? [],
      )
      .range([0, width])
      .padding(0.1)

    svg
      .append('g')
      .attr('transform', `translate(0,${height})`)
      .call(d3.axisBottom(x))
      .selectAll('text')
      .attr('transform', 'rotate(-45)')
      .style('text-anchor', 'end')

    const y = d3
      .scaleLinear()
      .domain([0, weekList ? d3.max(weekList, (d) => d.amount) || 0 : 0])
      .range([height, 0])

    svg.append('g').call(d3.axisLeft(y))

    svg
      .selectAll('.bar')
      .data(weekList ?? [])
      .enter()
      .append('rect')
      .attr('x', (d) => x(`${d.month.split('-')[1]}월 ${d.week}주`)!)
      .attr('y', height)
      .attr('width', x.bandwidth())
      .attr('height', 0)
      .attr('fill', '#54A0FF')
      .attr('opacity', 0.7)
      .style('color', '#586575')
      .style('font-size', '14px')
      .style('font-family', 'var(--font-paperlogy-regular)')
      .transition()
      .duration(800)
      .attr('y', (d) => y(d.amount))
      .attr('height', (d) => height - y(d.amount))
  }, [weekList])

  return (
    <ContentContainer>
      <svg
        ref={ref}
        style={{
          width: '100%',
          height: '100%',
          minHeight: '220px',
          backgroundColor: '#fff',
          borderRadius: '8px',
        }}></svg>
    </ContentContainer>
  )
}
