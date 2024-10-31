import api from '@/api';
import { useAuthStore } from '@/stores/auth';

const BASE_URL = '/api/schedule';

export default {
    async getEvents(userId, year, month) {
        try {
            const response = await api.get(`${BASE_URL}/list/${userId}`, {
                params: { year, month },
            });
            return response.data;
        } catch (error) {
            console.error('이벤트 로딩 중 오류 발생:', error);
            throw error;
        }
    },

    async addEvent(scheduleData) {
        try {
            const response = await api.post(`${BASE_URL}/add`, scheduleData);
            return response.data;
        } catch (error) {
            console.error('일정 추가 중 오류 발생:', error);
            throw error;
        }
    },

    async updateEvent(eventId, scheduleData) {
        try {
            const response = await api.post(
                `${BASE_URL}/update/${eventId}`,
                scheduleData
            );
            return response.data;
        } catch (error) {
            console.error('이벤트 업데이트 중 오류 발생:', error);
            throw error;
        }
    },

    async deleteEvent(eventId) {
        try {
            const response = await api.delete(`${BASE_URL}/delete/${eventId}`);
            return response.data;
        } catch (error) {
            console.error('이벤트 삭제 중 오류 발생:', error);
            throw error;
        }
    },
};
