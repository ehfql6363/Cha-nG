export const NavItemVariant = {
  home: 'homePage',
  pledge: 'pledgePage',
  my: 'myPage',
} as const

export type NavItemKey = keyof typeof NavItemVariant // 'home' | 'pledge' | 'my'
export type NavItemVariant = (typeof NavItemVariant)[NavItemKey] // 'homePage' | ...
