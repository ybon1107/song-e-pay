import axios from 'axios';

const api = axios.create({
    baseURL: '/api/histories', // 기본 경로 설정
    headers: { 'Content-Type': 'application/json' }, // 기본 헤더
});

export default {
    // 거래 내역 필터링
    async applyFilters(filters) {
        const { data } = await api.post('/filter', filters);
        console.log('HISTORIES FILTERED:', data);
        return data;
    },

    // 기본 거래 내역 리스트 가져오기
    async getTransactionList(params) {
        const { data } = await api.get('/getList', { params });
        console.log('HISTORIES GET LIST:', data);
        return data;
    },

    // 거래 메모 업데이트
    async updateMemo(historyNo, memo) {
        const formData = new FormData();
        formData.append('historyNo', historyNo);
        formData.append('memo', memo);

        const { data } = await api.put(`/updateMemo/${historyNo}`, formData);
        console.log('MEMO UPDATE:', data);
        return data;
    },
};
