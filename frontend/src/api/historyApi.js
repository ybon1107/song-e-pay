import axios from 'axios';

// 기본 API 설정을 위한 axios 인스턴스 생성
const api = axios.create({
    baseURL: '/api/histories',
    headers: { 'Content-Type': 'application/json' },
});

// 거래 내역 필터링 API 호출
export const filterTransactionHistory = async (filters) => {
    try {
        const response = await api.post('/filter', filters);
        return response.data; // 결과 데이터를 반환
    } catch (error) {
        console.error('Error filtering transaction history:', error);
        throw error;
    }
};

// 기본 거래 내역 리스트 가져오기
export const getTransactionList = async () => {
    try {
        const response = await api.get('/getList');
        return response.data; // 결과 데이터를 반환
    } catch (error) {
        console.error('Error fetching transaction list:', error);
        throw error;
    }
};

// 메모 업데이트 API 호출
export const updateTransactionMemo = async (historyNo, memo) => {
    try {
        const response = await api.post('/updateMemo', { historyNo, memo });
        return response.data; // 결과 데이터를 반환
    } catch (error) {
        console.error('Error updating memo:', error);
        throw error;
    }
};
