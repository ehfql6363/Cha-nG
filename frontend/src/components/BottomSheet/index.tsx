/** @jsxImportSource @emotion/react */
import { useEffect } from 'react'

import * as Dialog from '@radix-ui/react-dialog'
import { animated, useSpring } from '@react-spring/web'
import { useDrag } from '@use-gesture/react'

import { Image } from '@/components'
import { SnapPoints } from '@/types/ui'

import {
  bottomSheetStyle,
  contentStyle,
  handleStyle,
  headerStyle,
  overlayStyle,
  visuallyHiddenStyle,
} from './styles'

interface ModalProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  children: React.ReactNode
  title?: string
  snapPoints?: Partial<SnapPoints>
}

const DEFAULT_SNAP_POINTS = { MIN: 0.2, MID: 0.5, MAX: 0.9 } as const
const SPRING_CONFIG = { tension: 300, friction: 30 } as const

export const BottomSheet = ({
  open,
  onOpenChange,
  children,
  title = '',
  snapPoints: customSnapPoints,
}: ModalProps) => {
  const snapPoints = { ...DEFAULT_SNAP_POINTS, ...customSnapPoints }
  const [{ y }, api] = useSpring(() => ({ y: window.innerHeight }))
  const AnimatedContent = animated(Dialog.Content)
  const AnimatedOverlay = animated(Dialog.Overlay)

  useEffect(() => {
    api.start({
      y: open ? window.innerHeight * (1 - snapPoints.MID) : window.innerHeight,
      config: SPRING_CONFIG,
    })
  }, [open, snapPoints.MID, api])

  const getSnapPoints = () => [
    window.innerHeight * (1 - snapPoints.MAX),
    window.innerHeight * (1 - snapPoints.MID),
    window.innerHeight * (1 - snapPoints.MIN),
    window.innerHeight,
  ]

  const bind = useDrag(
    ({ active, movement: [, my], velocity: [, vy], cancel }) => {
      const currentY = y.get()
      const projectedY = currentY + my
      const maxY = window.innerHeight * (1 - snapPoints.MAX)

      if (projectedY < maxY) {
        api.start({ y: maxY })
        cancel()
        return
      }

      if (active) {
        api.start({ y: projectedY, immediate: true })
        return
      }

      const points = getSnapPoints()

      if (
        (vy > 0.5 && my > 0) ||
        Math.min(...points.map((point) => Math.abs(projectedY - point))) ===
          Math.abs(projectedY - window.innerHeight)
      ) {
        onOpenChange(false)
        return
      }

      const snapTo = points.reduce((closest: number, point: number) =>
        Math.abs(projectedY - point) < Math.abs(projectedY - closest)
          ? point
          : closest,
      )

      api.start({ y: snapTo, config: SPRING_CONFIG })
    },
    { from: () => [0, y.get()], filterTaps: true, rubberband: true },
  )

  return (
    <Dialog.Root
      open={open}
      onOpenChange={onOpenChange}>
      <Dialog.Portal>
        <AnimatedOverlay
          css={overlayStyle}
          style={{
            opacity: y.to(
              [window.innerHeight, window.innerHeight * 0.8],
              [0, 1],
            ),
          }}
        />
        <AnimatedContent
          css={bottomSheetStyle}
          style={{
            display: y.to((py) => (py < window.innerHeight ? 'flex' : 'none')),
            transform: y.to((value) => `translateY(${value}px)`),
          }}>
          <div
            css={headerStyle}
            {...bind()}>
            <Dialog.Title css={visuallyHiddenStyle}>{title}</Dialog.Title>
            <Dialog.Description css={visuallyHiddenStyle}>
              {title} 내용을 확인할 수 있는 바텀시트입니다.
            </Dialog.Description>
            <div css={handleStyle} />
            <Dialog.Close>
              <Image
                src="/icons/close.svg"
                alt="close"
                width={14}
                height={14}
              />
            </Dialog.Close>
          </div>
          <div css={contentStyle}>{children}</div>
        </AnimatedContent>
      </Dialog.Portal>
    </Dialog.Root>
  )
}
