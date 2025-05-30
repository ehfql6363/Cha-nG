import { Menu, PledgeMenu } from '@/types/ui'

export const PledgeMenuList: Menu[] = [
  { id: PledgeMenu.contract, name: '서약서' },
  { id: PledgeMenu.account, name: '계좌' },
  { id: PledgeMenu.rent, name: '월세' },
  { id: PledgeMenu.utility, name: '공과금' },
]

export const LivingMenuList: Menu[] = [
  { id: 'calendar', name: '달력' },
  { id: 'history', name: '내역' },
]
