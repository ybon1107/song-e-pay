import axios from 'axios';

import { useAuthStore } from '@/stores/auth';
import router from '@/router';
import Swal from 'sweetalert2';

const mulApi = axios.create({
    timeout: 10000,
    withCredentials: true,
    headers: {
        'Content-Type': 'multipart/form-data',
    },
});

// 요청 인터셉터
mulApi.interceptors.request.use(
    (config) => {
        console.log('Request Interceptor Called');
        const authStore = useAuthStore();
        const token = authStore.getToken();

        if (token) {
            // 토큰이 존재하면 헤더에 추가
            config.headers.Authorization = `Bearer ${token}`;
            // config.headers['Authorization'] = `Bearer ${token}`;
            console.log('Header Info: ' + config.headers.Authorization);
        } else {
            console.log('No Token found, Authorization header not added');
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터
mulApi.interceptors.response.use(
    (response) => {
        if (response.status === 200) {
            return response;
        }
        if (response.status === 404) {
            router.push('/error');
            return Promise.reject('404 Not Found: ' + response.request);
        }
        return response;
    },
    (error) => {
        const authStore = useAuthStore();

        // 응답 에러 처리
        if (error.response?.status === 401) {
            const contentType = error.response.headers['content-type'];

            if (contentType && contentType.includes('text/html')) {
              Swal.fire({
                title: "Sessioin Expired",
                text: "Please login again.",
                icon: "info",
              }).then(() => {
                authStore.logout();
                router.push('/login');
              });
            } else if (error.response?.status === 403) {
              // 권한 오류인 경우 메인 페이지로 이동
              router.push('/');
            }
            return Promise.reject(error);
        }
    }
);

export default mulApi;
