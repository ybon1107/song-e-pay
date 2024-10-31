import api from '@/api';

const BASE_URL = '/api/my-accounts';

export default {
    // krw 계좌 잔액 조회
    async fetchkrwAccountBalance(krwNo) {
        try {
            const response = await api.post(
                `${BASE_URL}/krwbalance?krwNo=${krwNo}`
            ); // 쿼리 파라미터로 전달
            return response.data;
        } catch (error) {
            console.error('KRW 계좌 잔액 조회 오류:', error);
            throw error;
        }
    },
    // songe 계좌 잔액 조회
    async fetchsongeAccountBalance(songNo) {
        try {
            const response = await api.post(
                `${BASE_URL}/songebalance?songNo=${songNo}`
            ); // 쿼리 파라미터로 전달
            return response.data;
        } catch (error) {
            console.error('Songe 계좌 잔액 조회 오류:', error);
            throw error;
        }
    },
    // 충전
    async deposit(data) {
        try {
            const response = await api.post(`${BASE_URL}/deposit`, data); // JSON으로 전송
            return response;
        } catch (error) {
            if (error.response.status === 500) {
                throw new Error(
                    'There was a problem on the server.<br/>Please try again later.'
                );
            }
            console.error('충전 오류', error);
            throw error;
        }
    },
    // 환불
    async refund(data) {
        try {
            const response = await api.post(`${BASE_URL}/refund`, data); // JSON으로 전송
            return response;
        } catch (error) {
            console.error('환불 오류', error);
            throw error;
        }
    },
    // 환전
    async exchange(data) {
        console.log("----------data : ", data)
        try {
            const response = await api.post(`${BASE_URL}/exchange`, data);
            return response;
        } catch (error) {
            console.error('환전 오류', error);
            throw error;
        }
    },
    //환급
    async reExchange(data) {
        try {
            const response = await api.post(`${BASE_URL}/re-exchange`, data);
            return response;
        } catch (error) {
            console.error('환급 오류', error);
            throw error;
        }
    },
    //송금
    async transfer(data) {
        try {
            const response = await api.post(`${BASE_URL}/transfer`, data);
            return response;
        } catch (error) {
            console.error('송금 오류', error);
            throw error;
        }
    },
    // 이메일 확인
    async confirmEmail(userId) {
        try {
            // API 요청을 POST 방식으로 하고 usedId를 JSON 데이터로 전달
            const response = await api.post(
                `${BASE_URL}/confirm?userId=${userId}`
            );
            return response.data; // 필요한 데이터만 반환
        } catch (error) {
            console.error('이메일 확인 중 오류 발생:', error);
            throw error; // 에러 발생 시 예외 처리
        }
    },
    // 회원 계좌 번호 조회
    async getKrwNo(userId) {
        try {
            const response = await api.post(
                `${BASE_URL}/krwno?userId=${userId}`
            ); // 쿼리 파라미터로 전달
            return response.data;
        } catch (error) {
            console.error('회원 계좌 번호 조회 오류:', error);
            throw error;
        }
    },
    // 송금받기
    async receiveTransfer(transferData) {
        try {
            const response = await api.post(
                `${BASE_URL}/receive-transfer`,
                transferData
            );
            return response.data;
        } catch (error) {
            console.error('송금받기 오류:', error);
            throw error;
        }
    },
};
