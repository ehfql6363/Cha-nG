import { BottomNavigation, TopHeader } from '@/components'
import { Container, FullWidthMain } from '@/styles/styles'

export function FullNavLayout({
  title = '',
  children,
  headerRightButton,
}: {
  title?: string
  children: React.ReactNode
  headerRightButton?: React.ReactNode
}) {
  return (
    <>
      <Container>
        <TopHeader
          title={title}
          headerRightButton={headerRightButton}
        />
        <FullWidthMain>{children}</FullWidthMain>
        <BottomNavigation />
      </Container>
    </>
  )
}
