'use client'

import { initReactI18next } from 'react-i18next'

import i18n from 'i18next'

import ko from '@/locales/ko.json'

const resources = {
  ko: {
    translation: ko,
  },
}

if (!i18n.isInitialized) {
  i18n.use(initReactI18next).init({
    resources,
    lng: 'ko',
    fallbackLng: 'ko',
    interpolation: {
      escapeValue: false,
    },
    initImmediate: false,
  })
}

export default i18n
