import { createI18n } from 'vue-i18n';
import ko from './locales/json/ko.json';
import en from './locales/json/en.json';
import id from './locales/json/id.json';
import vi from './locales/json/vi.json';

const messages = {
    ko,
    en,
    id,
    vi,
};

const i18n = createI18n({
    locale: 'en', // 기본 언어 설정 (영어)
    fallbackLocale: 'en', // 기본 언어가 없을 때 대체할 언어 (영어)
    messages, // 번역 메시지
});

export default i18n;
