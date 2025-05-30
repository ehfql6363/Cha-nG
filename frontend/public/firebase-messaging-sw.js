importScripts(
  'https://www.gstatic.com/firebasejs/10.8.1/firebase-app-compat.js',
)
importScripts(
  'https://www.gstatic.com/firebasejs/10.8.1/firebase-messaging-compat.js',
)

firebase.initializeApp({
  apiKey: 'AIzaSyAshxoFkNNan2sz0i09FXs9uBg7w0gBV_k',
  authDomain: 'chaing-test.firebaseapp.com',
  projectId: 'chaing-test',
  storageBucket: 'chaing-test.firebasestorage.app',
  messagingSenderId: '590460359740',
  appId: '1:590460359740:web:8fa2325b77bc33b0429f39',
})

const messaging = firebase.messaging()

messaging.onBackgroundMessage(function (payload) {
  console.log('백그라운드 메시지 수신:', payload)
  self.registration.showNotification(payload.notification.title, {
    body: payload.notification.body,
    icon: '/icons/homescreen192.png',
  })
})
