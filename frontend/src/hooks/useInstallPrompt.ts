import { useEffect, useState } from 'react'

interface BeforeInstallPromptEvent extends Event {
  prompt: () => Promise<void>
  userChoice: Promise<{ outcome: 'accepted' | 'dismissed' }>
}

export function useInstallPrompt() {
  const [deferredPrompt, setDeferredPrompt] =
    useState<BeforeInstallPromptEvent | null>(null)
  const [isInstalled, setIsInstalled] = useState(false)

  useEffect(() => {
    const checkInstalled = () => {
      try {
        // display-mode: standalone 감지 (Android, PC)
        const isStandalone = window.matchMedia(
          '(display-mode: standalone)',
        ).matches

        // iOS Safari 전용 standalone 감지
        const isIOS = /iphone|ipad|ipod/.test(navigator.userAgent.toLowerCase())
        const isIOSStandalone = (navigator as any).standalone === true

        setIsInstalled(isStandalone || (isIOS && isIOSStandalone))
      } catch (error) {
        console.error('PWA 설치 상태 확인 중 오류 발생:', error)
        setIsInstalled(false)
      }
    }

    checkInstalled()

    const mediaQuery = window.matchMedia('(display-mode: standalone)')
    const handleChange = () => checkInstalled()
    mediaQuery.addEventListener('change', handleChange)

    return () => mediaQuery.removeEventListener('change', handleChange)
  }, [])

  useEffect(() => {
    const handler = (e: BeforeInstallPromptEvent) => {
      try {
        e.preventDefault()
        setDeferredPrompt(e)
      } catch (error) {
        console.error('beforeinstallprompt 이벤트 처리 중 오류 발생:', error)
      }
    }

    try {
      window.addEventListener('beforeinstallprompt', handler as EventListener)
      return () =>
        window.removeEventListener(
          'beforeinstallprompt',
          handler as EventListener,
        )
    } catch (error) {
      console.error('이벤트 리스너 등록 중 오류 발생:', error)
    }
  }, [])

  const promptInstall = async () => {
    if (!deferredPrompt) return null
    try {
      deferredPrompt.prompt()
      const result = await deferredPrompt.userChoice
      setDeferredPrompt(null)
      return result
    } catch (error) {
      console.error('PWA 설치 프롬프트 실행 중 오류 발생:', error)
      return null
    }
  }

  // PWA 설치 가능 여부를 더 정확하게 체크
  const isSupported = () => {
    try {
      const isHTTPS =
        window.location.protocol === 'https:' ||
        window.location.hostname === 'localhost'
      const hasManifest =
        document.querySelector('link[rel="manifest"]') !== null
      const hasServiceWorker = 'serviceWorker' in navigator
      const isMobile =
        /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
          navigator.userAgent,
        )

      // 모바일 환경에서는 beforeinstallprompt 이벤트가 발생하지 않을 수 있으므로,
      // manifest와 service worker만 체크
      if (isMobile) {
        return isHTTPS && hasManifest && hasServiceWorker
      }

      return isHTTPS && hasManifest && hasServiceWorker && !!deferredPrompt
    } catch (error) {
      console.error('PWA 지원 여부 확인 중 오류 발생:', error)
      return false
    }
  }

  return { promptInstall, isSupported: isSupported(), isInstalled }
}
