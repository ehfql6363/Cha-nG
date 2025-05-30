import { useCallback } from 'react'

export const useCopyInviteCode = (
  inviteCode: string,
  name: string,
  groupName: string,
) => {
  return useCallback(async () => {
    const title = '[chainG]'
    const url = `https://chaing.site/group/join?inviteCode=${encodeURIComponent(inviteCode)}`
    const message = `${title} ${name}님이 ${groupName}에 초대했어요.
    초대코드 : ${inviteCode}
    링크 : ${url}
    `

    const shareData = {
      title,
      text: message,
      url,
    }

    navigator.clipboard.writeText(message)
    try {
      await navigator.share(shareData)
    } catch (err) {}
  }, [inviteCode, name, groupName])
}
