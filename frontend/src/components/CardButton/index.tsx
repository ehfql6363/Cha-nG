'use client'

import { Image } from '@/components'
import { CardItem } from '@/types/ui'

import { Card, CardContainer, CardDescription, Container } from './styles'

export const CardButton = ({ cardItems }: { cardItems: CardItem[] }) => {
  return (
    <Container>
      {cardItems.map((item) => {
        return (
          <CardContainer key={item.url}>
            <Card href={item.url}>
              <Image
                src={item.image}
                alt={item.title}
                width={46}
                height={46}
              />
              {item.title}
              {item.children}
            </Card>
            <CardDescription>{item.description}</CardDescription>
          </CardContainer>
        )
      })}
    </Container>
  )
}
