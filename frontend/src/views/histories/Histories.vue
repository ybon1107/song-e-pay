<script setup>
import { ref, computed, onMounted } from 'vue';
import moment from 'moment'; // moment.js로 Unix Timestamp를 변환
import historyApi from '../../api/historiesApi.js'; // API 파일 import
import Modal from './Modal.vue';
import HistoriesDetailModal from './HistoriesDetailModal.vue';

const selectedCurrency = ref('');
const transactionType = ref('');
const transactionStatus = ref('');
const startDate = ref('');
const endDate = ref('');
const searchQuery = ref('');
const transactions = ref([]);

const totalItems = ref(0); // 전체 항목 수
const pageRequest = ref({
    page: 1, // 현재 페이지
    amount: 10, // 페이지당 항목 수
});

// 거래 유형 옵션 (계좌 종류에 따라 다르게 설정)
const transactionTypes = computed(() => {
    if (selectedCurrency.value === 'KoreaAccount') {
        return ['송금', '환전', '환급', '결제']; // 한국 계좌 거래 유형
    } else if (selectedCurrency.value === 'ForeignAccount') {
        return ['충전', '환전', '환급', '환불']; // 외화 계좌 거래 유형
    }
    return ['충전', '환전', '환급', '결제', '환불', '송금']; // 전체 선택 시 빈 배열
});

// 필터 적용 함수
const applyTransactionFilters = async () => {
    let typeCodesToSend = [];
    if (transactionType.value) {
        typeCodesToSend = getTransactionTypeCodes([transactionType.value]);
    } else {
        if (selectedCurrency.value === 'KoreaAccount') {
            typeCodesToSend = [1, 2, 5, 6]; // 원화 계좌
        } else if (selectedCurrency.value === 'ForeignAccount') {
            typeCodesToSend = [3, 4, 5, 6]; // 외화 계좌
        }
    }

    const filters = {
        userNo: 1,
        krwNo: null,
        songNo: null,
        typeCode: typeCodesToSend.length > 0 ? typeCodesToSend : null,
        stateCode: getTransactionStateCode(transactionStatus.value) || null,
        beginDate: startDate.value
            ? new Date(startDate.value).toISOString()
            : null,
        endDate: endDate.value ? new Date(endDate.value).toISOString() : null,
        historyContent: searchQuery.value || null,
        offset: (pageRequest.value.page - 1) * pageRequest.value.amount, // offset 계산
        amount: pageRequest.value.amount, // 한 페이지에 표시할 개수
    };

    try {
        const response = await historyApi.applyFilters(
            filters,
            pageRequest.value
        );

        // 응답 데이터가 배열인지 확인 후 처리
        if (response && Array.isArray(response.list)) {
            totalItems.value = response.totalCount; // 전체 항목 수 업데이트
            transactions.value = response.list.map((transaction) => ({
                ...transaction,
                historyDate: formatUnixTimestamp(transaction.historyDate),
                typeCode: convertTransactionType(transaction.typeCode),
                stateCode: convertTransactionStatus(transaction.stateCode),
            }));
        } else {
            console.error('예상하지 않은 응답 형식:', response);
        }
    } catch (error) {
        console.error('필터링된 거래 내역을 가져오는 중 오류 발생:', error);
    }
};

// 페이지 로드 시 거래 내역 가져오기
onMounted(async () => {
    await getTransactionList();
});

// 기본 거래 내역 가져오기
const getTransactionList = async () => {
    try {
        const response = await historyApi.getTransactionList(pageRequest.value);
        console.log('API 응답:', response); // 응답 데이터를 로그로 출력하여 확인

        // 응답 데이터가 배열인지 확인
        if (response && Array.isArray(response.list)) {
            totalItems.value = response.totalCount; // 전체 항목 수 업데이트
            transactions.value = response.list.map((transaction) => ({
                ...transaction,
                historyDate: formatUnixTimestamp(transaction.historyDate),
                typeCode: convertTransactionType(transaction.typeCode),
                stateCode: convertTransactionStatus(transaction.stateCode),
            }));
        } else {
            console.error('응답이 예상하지 않은 형식입니다:', response);
        }
    } catch (error) {
        console.error('API 호출 중 오류 발생:', error);
    }
};

// 페이지 이동 함수
const goToPage = async (pageNum) => {
    if (pageNum > 0 && pageNum <= totalPages.value) {
        pageRequest.value.page = pageNum;
        await getTransactionList(); // 새로운 페이지로 이동 시 거래 내역 갱신
    }
};

// 총 페이지 수 계산
const totalPages = computed(() =>
    Math.ceil(totalItems.value / pageRequest.value.amount)
);

// Unix Timestamp 포맷팅 함수
const formatUnixTimestamp = (unixTimestamp) => {
    return unixTimestamp
        ? moment(unixTimestamp).format('YYYY-MM-DD hh:mm')
        : '';
};

// 상태 코드 변환
const convertTransactionStatus = (code) => {
    const statuses = {
        1: '성공',
        2: '실패',
        3: '취소',
        4: '처리중',
    };
    return statuses[code] || '알 수 없는 상태';
};

const getTransactionStateCode = (status) => {
    const stateCodes = {
        성공: 1,
        실패: 2,
        취소: 3,
        처리중: 4,
    };
    return stateCodes[status] || null;
};

// 거래 유형 코드 변환
const convertTransactionType = (code) => {
    const types = {
        1: '결제',
        2: '송금',
        3: '충전',
        4: '환불',
        5: '환전',
        6: '환급',
    };
    return types[code] || '알 수 없는 거래 유형';
};

const getTransactionTypeCodes = (types) => {
    const typeCodes = {
        결제: 1,
        송금: 2,
        충전: 3,
        환불: 4,
        환전: 5,
        환급: 6,
    };
    return types
        .map((type) => typeCodes[type])
        .filter((code) => code !== undefined);
};


// 모달 관련 상태 관리
const isModalVisible = ref(false);
const selectedTransaction = ref(null); // selectedTransaction 정의

const openModal = (transaction) => {
    selectedTransaction.value = transaction;
    isModalVisible.value = true;
};

// 모달을 닫는 함수
const closeModal = () => {
    isModalVisible.value = false;
};


// // HistoriesDetailModal 관련 상태
// const isDetailModalVisible = ref(false);
// const selectedDetailTransaction = ref(null);

// // HistoriesDetailModal을 여는 함수
// const openDetailModal = (transaction) => {
//     selectedDetailTransaction.value = transaction;
//     isDetailModalVisible.value = true;
// };

// // HistoriesDetailModal을 닫는 함수
// const closeDetailModal = () => {
//     isDetailModalVisible.value = false;
// };

</script>

<template>
    <div class="container-fluid">
        <h3>이용 내역</h3>
        <div class="card p-5 mt-3">
            <div class="mb-3">
                <!-- 첫 번째 줄: 계좌 종류와 상세 내용 검색 -->

                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="account-kind">계좌 종류</label>
                        <select id="account-kind" v-model="selectedCurrency" class="form-select">
                            <option value="">전체 내역</option>
                            <option value="KoreaAccount">원화 머니 계좌</option>
                            <option value="ForeignAccount">
                                외화 머니 계좌
                            </option>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="transType">거래 유형</label>
                        <select id="transType" v-model="transactionType" class="form-select">
                            <option value="">모두</option>
                            <option v-for="type in transactionTypes" :key="type" :value="type">
                                {{ type }}
                            </option>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="transactionStatus">거래 상태</label>
                        <select id="transactionStatus" v-model="transactionStatus" class="form-select">
                            <option value="">모두</option>
                            <option value="성공">성공</option>
                            <option value="취소">취소</option>
                            <option value="처리중">처리중</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="startDate">시작 날짜</label>
                        <input id="startDate" type="date" v-model="startDate" @click="openDatePicker"
                            class="form-control" />
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="endDate">끝 날짜</label>
                        <input id="endDate" type="date" v-model="endDate" :min="startDate" @click="openDatePicker"
                            class="form-control" />
                    </div>
                    <div class="col-md-4 d-flex align-items-end mb-3">
                        <input type="text" class="form-control" placeholder="상세 내용 검색" v-model="searchQuery"
                            @keyup.enter="applyTransactionFilters" />
                    </div>
                    <div class="col-md-2 d-flex align-items-end mb-3">
                        <button @click="applyTransactionFilters" class="btn btn-success mb-0">
                            Apply
                        </button>
                    </div>
                </div>
            </div>

            <div class="table-responsive">
                <table class="table table-bordered align-middle">
                    <thead>
                        <tr>
                            <th style="width: 20%">날짜/시간</th>
                            <th style="width: 10%">거래 유형</th>
                            <th style="width: 40%">거래 상세</th>
                            <th style="width: 20%">거래 금액</th>
                            <th style="width: 10%">거래 상태</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="transaction in transactions" :key="transaction.historyNo"
                            @click="openModal(transaction)" style="cursor: pointer">
                            <td>{{ transaction.historyDate }}</td>
                            <td>{{ transaction.typeCode }}</td>
                            <td>{{ transaction.historyContent }}</td>
                            <td>{{ transaction.amount }}</td>
                            <td>{{ transaction.stateCode }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <!-- 페이지네이션 컨트롤 -->
            <div class="d-flex justify-content-center my-4">
                <button class="btn btn-secondary mx-1" @click="goToPage(pageRequest.page - 1)"
                    :disabled="pageRequest.page === 1">
                    Previous
                </button>
                <button class="btn btn-secondary mx-1" @click="goToPage(pageRequest.page + 1)"
                    :disabled="pageRequest.page === totalPages">
                    Next
                </button>
                <span class="mx-2">Page {{ pageRequest.page }} of {{ totalPages }}</span>
            </div>
        </div>
    </div>
    <!-- 이용내역 상세 모달 -->
    <HistoriesDetailModal :transaction="selectedTransaction" :isVisible="isModalVisible" @close="closeModal" />
</template>

<style scoped>
button:hover {
    background-color: #21af71;
}

thead {
    background-color: #f4f4f4;
    text-align: center;
}

/* 반응형: 작은 화면에서는 필터를 세로로 배치 */
@media (max-width: 767px) {
    button {
        width: 100%;
    }

    th:nth-child(3),
    td:nth-child(3) {
        display: none;
        /* 거래 상세를 숨김 */
    }
}
</style>
