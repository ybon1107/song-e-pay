import api from '@/api';

const BASE_URL = '/api/payment';

export default {
    // QR 스캔을 처리하는 메서드 추가
    async scanQRCode(req) {
        try {
            const response = await api.post(`${BASE_URL}/qr-scan`, req);

            if (response.data === true) {
                return response;
            } else {
                this.message = 'Payment failed';
                throw error;
            }
        } catch (error) {
            this.message = 'An error occurred while processing the payment';
            // 오류 처리 로직 (예: 오류 로깅, 사용자에게 알림)
            console.error('QR 코드 스캔 오류:', error);
            throw error;
        }
    },

    // 송이 결제
    async accommodationPayment(paymentData) {
        console.log('paymentData: ', paymentData);
        const data = (await api.post(`${BASE_URL}/accommodation`, paymentData))
            .data;
        console.log('data: ', data);
        return data;
    },

    // 카카오페이 결제
    async accommodationKakaoPayment(req) {
        console.log('req: ', req);
        const data = (await api.post(`${BASE_URL}/accommodation/kakao`, req))
            .data;
        console.log('data: ', data);
        return data;
    },
};
