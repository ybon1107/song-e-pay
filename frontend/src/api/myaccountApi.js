import axios from 'axios';

const api = axios.create({
  baseURL: '/api/my-accounts',
  headers: { 'Content-Type': 'application/json' },
});

export default {
  // krw 계좌 잔액 조회
  async fetchkrwAccountBalance(krwNo) {
    try {
      const response = await api.post(`/krwbalance?krwNo=${krwNo}`); // 쿼리 파라미터로 전달
      return response.data.balance;
    } catch (error) {
      console.error('KRW 계좌 잔액 조회 오류:', error);
      throw error;
    }
  },
  // songe 계좌 잔액 조회
  async fetchsongeAccountBalance(songNo) {
    try {
      const response = await api.post(`/songebalance?songNo=${songNo}`); // 쿼리 파라미터로 전달
      return response.data.balance;
    } catch (error) {
      console.error('Songe 계좌 잔액 조회 오류:', error);
      throw error;
    }
  },
};
