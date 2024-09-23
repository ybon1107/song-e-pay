import axios from 'axios';

const api = axios.create({
  baseURL: '/api/payment',
  headers: { 'Content-Type': 'application/json' }
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
    // QR 스캔을 처리하는 메서드 추가
    async scanQRCode() {
        try {
            const response = await api.get('/qr-scan');
            return response;
        } catch (error) {
            console.error('QR 코드 스캔 오류:', error);
            throw error;
        }
    }
}