import { createApp } from 'vue';
// vue map 추가
import { createNaverMap } from 'vue3-naver-maps';

import App from './App.vue';
import store from './stores';
import router from './router';
import './assets/css/nucleo-icons.css';
import './assets/css/nucleo-svg.css';
import ArgonDashboard from './argon-dashboard';

const appInstance = createApp(App);

// 플러그인 등록
appInstance.use(store);
appInstance.use(router);
appInstance.use(ArgonDashboard);

// 네이버 맵 플러그인 등록
appInstance.use(createNaverMap, {
  clientId: 'b8ldima87m', // 네이버 클라우드에서 발급받은 clientId를 입력하세요
  category: 'ncp', // 네이버 플랫폼에 맞게 설정
  subModules: [], // 필요 시 서브 모듈을 추가할 수 있습니다
});

// 애플리케이션 마운트
appInstance.mount('#app');
