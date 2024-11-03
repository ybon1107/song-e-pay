import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const api = axios.create({
    baseURL: '/api/exchange-reservation',
    headers: { 'Content-Type': 'application/json' }
});

api.interceptors.request.use((config) => {
    const authStore = useAuthStore();
    const token = authStore.getToken();

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
}, (error) => {
    return Promise.reject(error);
});

export default {
    // 알림 설정
    async autoExchange(userId) {
        try {
            const response = await api.get(`/setalert/${userId}`);
            return response;

        } catch (error) {
            console.error('알림 설정 오류:', error.response.data);
            throw error;
        }
    },
    async setReservation(req) {
        try {
            const response = await api.post('', req);
            return response;

        } catch (error) {
            console.error('알림 설정 오류:', error.response.data);
            throw error;
        }
    },
    async setConditions(req) {
        try {
            const response = await api.post(`/setalert`, req);
            return response;

        } catch (error) {
            console.error('알림 설정 오류:', error.response.data);
            throw error;
        }
    },

    async alertConditions(userId) {
        try {
            const response = await api.get(`/${userId}`);
            return response;

        } catch (error) {
            console.error('알림 설정 오류:', error.response.data);
            throw error;
        }
    },

    async deleteConditions(resNo) {
        try {
            const response = await api.delete(`/${resNo}`);
            return response;

        } catch (error) {
            console.error('알림 설정 오류:', error.response.data);
            throw error;
        }
    }
}
