import axios from 'axios';

import { useAuthStore } from '@/stores/auth';
import router from '@/router';

const instance = axios.create({
  timeout: 10000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터
instance.interceptors.request.use(
  (config) => {
    console.log("Request Interceptor Called");
    // 토큰이 있는 경우 헤더에 추가
    const authStore = useAuthStore();
    const token = authStore.getToken();
    console.log("Token in interceptor: " + token);
    // const { getToken } = useAuthStore();
    // const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
      // config.headers['Authorization'] = `Bearer ${token}`;
      console.log("Header Info: " + config.headers.Authorization);
    } else {
      console.log("No Token found, Authorization header not added");
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터
instance.interceptors.response.use(
  (response) => {
    if (response.status === 200) {
      return response;
    }
    if (response.status === 404) {
      return Promise.reject('404 Not Found: ' + response.request);
    }
    return response;
  },
  (error) => {
    // 응답 에러 처리
    if (error.response?.status === 401) {
      const { logout } = useAuthStore();
      logout();
      // 인증 오류인 경우 로그인 페이지로 이동
      router.push('/login');
    } else if (error.response?.status === 403) {
      // 권한 오류인 경우 메인 페이지로 이동
      router.push('/');
    }
    return Promise.reject(error);
  }
);

export default instance;