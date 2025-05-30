import { initializeApp } from 'firebase/app'
import { getMessaging, onMessage } from 'firebase/messaging'

const firebaseConfig = {
  apiKey: 'AIzaSyAshxoFkNNan2sz0i09FXs9uBg7w0gBV_k',
  authDomain: 'chaing-test.firebaseapp.com',
  projectId: 'chaing-test',
  storageBucket: 'chaing-test.firebasestorage.app',
  messagingSenderId: '590460359740',
  appId: '1:590460359740:web:8fa2325b77bc33b0429f39',
  measurementId: 'G-R74J9NZ52L',
}

const app = initializeApp(firebaseConfig)
const messaging = getMessaging(app)

export const onForegroundMessage = () => {
  onMessage(messaging, (payload) => {
    console.log('포그라운드 메시지 수신:', payload)
    // 사용자 알림 띄우기 등 처리
  })
}
