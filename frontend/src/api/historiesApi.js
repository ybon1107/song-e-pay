import axios from 'axios';

const api = axios.create({
  baseURL: '/api/histories', // 기본 경로 설정
  headers: { 'Content-Type': 'application/json' }, // 기본 헤더
});

export default {
  // 거래 내역 필터링 (페이지네이션 포함)
  async applyFilters(filters, pageRequest) {
    const requestBody = {
      ...filters,
      page: pageRequest.page, // 페이지 번호
      amount: pageRequest.amount, // 페이지당 항목 수
    };
    console.log(requestBody);
    const response = await api.post('/filter', requestBody);
    const { data } = response;
    console.log('data 확인' + data);
    return data; // 필터링된 거래 내역 및 페이지네이션 정보 반환
  },

  // 기본 거래 내역 리스트 가져오기 (페이지네이션 포함)
  async getTransactionList(pageRequest) {
    const { data } = await api.get('/getList', {
      params: {
        page: pageRequest.page, // 페이지 번호
        amount: pageRequest.amount, // 페이지당 항목 수
      },
    });
    console.log('HISTORIES GET LIST:', data);
    return data; // 페이지네이션 정보와 함께 반환
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
