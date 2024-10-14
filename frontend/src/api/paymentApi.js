import api from "@/api"

const BASE_URL = '/api/payment';
const headers = { 'Content-Type': 'application/json' };

export default {
  // QR 스캔을 처리하는 메서드 추가
  async scanQRCode(req) {
    try {
      const response = await api.post(`${BASE_URL}/qr-scan`, req, { headers: { ...headers, ...api.defaults.headers.common } });

      if (response.data === true) {
        return response;
      } else {
        this.message = 'Payment failed';
      }
    } catch (error) {
      this.message = 'An error occurred while processing the payment';
      // 오류 처리 로직 (예: 오류 로깅, 사용자에게 알림)
      console.error('QR 코드 스캔 오류:', error);
      throw error;
    }
  }
}
