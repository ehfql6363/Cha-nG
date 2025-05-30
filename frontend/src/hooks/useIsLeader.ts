import { useAppSelector } from './useAppSelector'

export const useIsLeader = () => {
  const user = useAppSelector((state) => state.user.user)
  const group = useAppSelector((state) => state.group.group)

  return group?.leaderId === user.id
}
