import api from '@/api';

const BASE_URL = '/api/histories'; // 기본 경로 설정
// const headers = { "Content-Type": "multipart/form-data" }; // 기본 헤더

export default {
    async applyFilters(filters, pageRequest) {
        const requestBody = {
            ...filters, // 필터 데이터만 본문에 포함
            sortOrder: filters.sortOrder,
        };

        console.log(requestBody);

        // pageRequest를 쿼리 파라미터로 전달
        const response = await api.post(`${BASE_URL}/filter`, requestBody, {
            params: {
                page: pageRequest.page, // 페이지 번호를 쿼리 파라미터로 전달
                amount: pageRequest.amount, // 페이지당 항목 수를 쿼리 파라미터로 전달
            },
        });

        const { data } = response;
        // console.log('data 확인:', JSON.stringify(data, null, 2)); // JSON 형식으로 출력
        return data; // 필터링된 거래 내역 및 페이지네이션 정보 반환
    },

    // 기본 거래 내역 리스트 가져오기 (페이지네이션 포함)
    // async getTransactionList(pageRequest) {
    //     const { data } = await api.get('/getList', {
    //         params: {
    //             page: pageRequest.page, // 페이지 번호
    //             amount: pageRequest.amount, // 페이지당 항목 수
    //         },
    //     });
    //     console.log('HISTORIES GET LIST:', data);
    //     return data; // 페이지네이션 정보와 함께 반환
    // },

    // 거래 메모 업데이트
    async updateMemo(historyNo, memo) {
        const formData = new FormData();
        formData.append('historyNo', historyNo);
        formData.append('memo', memo);

        const { data } = await api.put(
            `${BASE_URL}/updateMemo/${historyNo}`,
            formData
        );
        console.log('MEMO UPDATE:', data);
        return data;
    },
};
