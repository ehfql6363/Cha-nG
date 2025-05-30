import { initializeApp } from 'firebase/app'
import { getMessaging, getToken, onMessage } from 'firebase/messaging'

const firebaseConfig = {
  apiKey: 'AIzaSyAshxoFkNNan2sz0i09FXs9uBg7w0gBV_k',
  authDomain: 'chaing-test.firebaseapp.com',
  projectId: 'chaing-test',
  storageBucket: 'chaing-test.firebasestorage.app',
  messagingSenderId: '590460359740',
  appId: '1:590460359740:web:8fa2325b77bc33b0429f39',
  measurementId: 'G-R74J9NZ52L',
}

const vapidKey =
  process.env.NEXT_PUBLIC_FIREBASE_VAPID_KEY ??
  'BGJgredDif8ty9AQYNopTGMzWaox7j2hxz5SXbcU3ylm6dTUJR1nSxv7pIddFkTETZJV_32yoMLJPtu4JlJCW7k'

let app: any = null
let messaging: any = null

export const initFCM = async () => {
  await getFCMToken()
  onForegroundMessage()
}

const initFirebase = () => {
  if (typeof window === 'undefined') return null
  if (!app) {
    app = initializeApp(firebaseConfig)
    messaging = getMessaging(app)
  }
  return { app, messaging }
}

export const getFCMToken = async () => {
  if (typeof window === 'undefined') return null

  try {
    const { messaging } = initFirebase() || {}
    if (!messaging) return null

    const token = await getToken(messaging, {
      vapidKey: vapidKey,
    })
    return token
  } catch (error) {
    return null
  }
}

export const onForegroundMessage = () => {
  if (typeof window === 'undefined') return

  const { messaging } = initFirebase() || {}
  if (!messaging) return

  onMessage(messaging, (payload) => {
    console.log('포그라운드 메시지 수신:', payload?.notification)
  })
}
