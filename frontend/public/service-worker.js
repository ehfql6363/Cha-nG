const CACHE_NAME = 'chaing-cache-v1'
const urlsToCache = [
  '/', // 홈
  '/offline.html', // 오프라인 fallback 페이지 (선택)
  // 폰트 파일
  '/fonts/Paperlogy-4Regular.ttf',
  '/fonts/Paperlogy-5Medium.ttf',
  '/fonts/Paperlogy-6SemiBold.ttf',
  '/fonts/Paperlogy-7Bold.ttf',
  // 아이콘 파일 (SVG)
  '/icons/logo.svg',
  '/icons/logo-no-padding.svg',
  '/icons/logo-chainG.svg',
  '/icons/logo-chainG-no.svg',
  '/icons/logo-chainG-blue.svg',
  '/icons/loading.svg',
  '/icons/loading-new.svg',
  '/icons/notification-inactive.svg',
  '/icons/notification-active.svg',
  '/icons/modify.svg',
  '/icons/logo-card.svg',
  '/icons/logo-bank.svg',
  '/icons/leader.svg',
  '/icons/leader-rotate.svg',
  '/icons/file.svg',
  '/icons/copy.svg',
  '/icons/calendar.svg',
  '/icons/button-pending.svg',
  '/icons/button-valid.svg',
  '/icons/button-modify.svg',
  '/icons/button-invalid.svg',
  '/icons/button-delete.svg',
  '/icons/button-create.svg',
  '/icons/arrow-small-right.svg',
  '/icons/arrow-right.svg',
  '/icons/arrow-small-left.svg',
  '/icons/arrow-left.svg',
  '/icons/approve.svg',
  '/icons/announce.svg',
  '/icons/save.svg',
  '/icons/plus.svg',
  '/icons/time-clock.svg',
  '/icons/minus.svg',
  '/icons/close.svg',
  '/icons/update.svg',
  '/icons/plus_circle.svg',
  '/icons/menu.svg',
  '/icons/validation-true.svg',
  '/icons/validation-false.svg',
]

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(urlsToCache)
    }),
  )
})

self.addEventListener('fetch', (event) => {
  event.respondWith(
    caches.match(event.request).then((response) => {
      // 캐시에 있으면 응답하고, 없으면 네트워크로
      return (
        response ||
        fetch(event.request).catch(() => caches.match('/offline.html'))
      )
    }),
  )
})

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames
          .filter((name) => name !== CACHE_NAME)
          .map((name) => caches.delete(name)),
      )
    }),
  )
})
