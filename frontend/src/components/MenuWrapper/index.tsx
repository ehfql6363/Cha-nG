// components/MenuWrapper.tsx
import { Fragment, ReactNode, useEffect } from 'react'

import {
  autoUpdate,
  flip,
  offset,
  shift,
  useFloating,
} from '@floating-ui/react'
import { Menu, MenuButton, MenuItem, Transition } from '@headlessui/react'

import { MenuContent } from '@/types/ui'

import { MenuItems, StyledItem, TriggerWrapper } from './styles'

interface MenuWrapperProps {
  button: ReactNode
  items: MenuContent[]
}

export function MenuWrapper({ button, items }: MenuWrapperProps) {
  const { refs, floatingStyles, update } = useFloating({
    placement: 'bottom-end',
    middleware: [offset(8), flip(), shift()],
  })

  useEffect(() => {
    if (!refs.reference.current || !refs.floating.current) return

    return autoUpdate(refs.reference.current, refs.floating.current, update)
  }, [refs.reference, refs.floating, update])

  return (
    <TriggerWrapper>
      <Menu as="div">
        <MenuButton
          ref={refs.setReference}
          as={Fragment}>
          {button}
        </MenuButton>

        <Transition
          as={Fragment}
          enter="transition duration-150 ease-out"
          enterFrom="opacity-0 scale-90"
          enterTo="opacity-100 scale-100"
          leave="transition duration-100 ease-in"
          leaveFrom="opacity-100 scale-100"
          leaveTo="opacity-0 scale-90">
          <MenuItems
            ref={refs.setFloating}
            style={floatingStyles}>
            {items.map(({ title, onSelect, color, disabled }, idx) => (
              <MenuItem
                key={idx}
                disabled={disabled}>
                {() => (
                  <StyledItem
                    color={color}
                    onClick={onSelect}
                    disabled={disabled}>
                    {title}
                  </StyledItem>
                )}
              </MenuItem>
            ))}
          </MenuItems>
        </Transition>
      </Menu>
    </TriggerWrapper>
  )
}
