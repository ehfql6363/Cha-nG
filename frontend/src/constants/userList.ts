export interface User {
  id: number
  name: string
  nickname: string | null
  profileImage: string | null
}

export const userList: User[] = [
  {
    id: 101,
    name: 'John Doe',
    nickname: '배고파',
    profileImage: 'user1',
  },
  {
    id: 102,
    name: 'Jane Doe',
    nickname: '배고파',
    profileImage: 'user2',
  },
  {
    id: 103,
    name: 'Jane Doe',
    nickname: '배고파',
    profileImage: 'user2',
  },
  {
    id: 104,
    name: 'Jane Doe',
    nickname: '배고파',
    profileImage: 'user2',
  },
]
