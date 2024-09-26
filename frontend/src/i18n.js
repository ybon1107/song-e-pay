import { createI18n } from 'vue-i18n';
import ko from './locales/ko.json';
import en from './locales/en.json';
import id from './locales/id.json';
import vi from './locales/vi.json';

const messages = {
  ko,
  en,
  id,
  vi
};

const i18n = createI18n({
  locale: 'ko', // 기본 언어 설정 (한국어)
  fallbackLocale: 'en', // 기본 언어가 없을 때 대체할 언어 (영어)
  messages, // 번역 메시지
});

export default i18n;