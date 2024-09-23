import axios from 'axios';

const api = axios.create({
    baseURL: '/api/payment',
    headers: { 'Content-Type': 'application/json' },
});

export default {
    // 서버에 비밀번호를 제출하는 가상의 메서드
    async submitPassword(password) {
        try {
            const response = await api.post('/check-password', { password });
            return response;
        } catch (error) {
            console.error('비밀번호 제출 오류:', error);
            throw error;
        }
    },
};
