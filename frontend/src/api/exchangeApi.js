import api from '@/api';

const BASE_URL = '/api/exchange';

export default {
    async setExchange(rates) {
        try {
            const response = await api.post(
                `${BASE_URL}/rates`, rates
            ); // 쿼리 파라미터로 전달
            return response.data;
        } catch (error) {
            console.error('KRW 계좌 잔액 조회 오류:', error);
            throw error;
        }
    }
}