<script setup>
import { ref, computed, onMounted } from 'vue';
import Modal from './Modal.vue'; // 모달 창
import axios from 'axios';
import moment from 'moment'; // moment.js를 통해 unix timeStamp를 변환

const selectedCurrency = ref('');
const transactionType = ref('');
const transactionStatus = ref('');
const startDate = ref('');
const endDate = ref('');
const searchQuery = ref('');
//history filter 로직
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
const applyFilters = () => {
    let typeCodesToSend = [];
    // 거래 유형이 선택되었는지 확인
    if (transactionType.value) {
        // 선택된 거래 유형을 코드로 변환하여 배열로 생성
        typeCodesToSend = getTransactionTypeCodes([transactionType.value]);
    } else {
        // 거래 유형이 선택되지 않았으면 계좌 종류에 따라 기본 typeCode 배열 설정
        if (selectedCurrency.value === 'KoreaAccount') {
            typeCodesToSend = [1, 2, 5, 6]; // 원화 머니 계좌일 때의 거래 유형 코드
        } else if (selectedCurrency.value === 'ForeignAccount') {
            typeCodesToSend = [3, 4, 5, 6]; // 외화 머니 계좌일 때의 거래 유형 코드
        }
    }
    axios
        .post('/api/histories/filter', {
            userNo: 1, // 사용자 번호 예시
            krwNo: null,
            songNo: null,
            typeCode: typeCodesToSend.length > 0 ? typeCodesToSend : null, // typeCode 배열 전송
            stateCode: getTransactionStateCode(transactionStatus.value) || null,
            beginDate: startDate.value
                ? new Date(startDate.value).toISOString()
                : null, // ISO 형식으로 변환
            endDate: endDate.value
                ? new Date(endDate.value).toISOString()
                : null, // ISO 형식으로 변환
            historyContent: searchQuery.value || null,
        })
        .then((response) => {
            transactions.value = response.data.map((transaction) => ({
                ...transaction,
                historyDate: formatUnixTimestamp(transaction.historyDate), // moment.js로 날짜 포맷팅
                typeCode: convertTransactionType(transaction.typeCode),
                stateCode: convertTransactionStatus(transaction.stateCode),
            }));
        })
        .catch((error) => {
            console.error('필터링된 거래 내역을 가져오는 중 오류 발생:', error);
        });
};

const getTransactionStateCode = (status) => {
    const stateCodes = {
        완료: 1,
        실패: 2,
        취소: 3,
        처리중: 4,
    };
    return stateCodes[status] || null;
};

// 페이지 로드 시 기본 거래 내역 가져오기
const transactions = ref([]);
onMounted(() => {
    getTransactionList();
});

// 기본 거래 내역을 백엔드에서 가져오는 함수
const getTransactionList = () => {
    axios
        .get('/api/histories/getList') // 기본 리스트를 GET 요청으로 가져옴
        .then((response) => {
            // 거래 코드와 상태 코드 변환 후 데이터 저장
            transactions.value = response.data.map((transaction) => ({
                ...transaction,
                historyDate: formatUnixTimestamp(transaction.historyDate), // moment.js로 날짜 포맷팅
                typeCode: convertTransactionType(transaction.typeCode),
                stateCode: convertTransactionStatus(transaction.stateCode),
            }));
        })
        .catch((error) => {
            console.error('API 호출 중 오류 발생:', error);
        });
};

// Unix Timestamp를 포맷팅하는 함수
const formatUnixTimestamp = (unixTimestamp) => {
    if (unixTimestamp) {
        return moment(unixTimestamp).format('YYYY-MM-DD hh:mm');
    }
    return '';
};

// 상태 코드 변환
function convertTransactionStatus(code) {
    const transactionStatuses = {
        1: '성공',
        2: '실패',
        3: '취소',
        4: '처리중',
    };
    return transactionStatuses[code] || '알 수 없는 상태';
}

// 거래 코드 변환
function convertTransactionType(code) {
    const transactionTypes = {
        1: '결제',
        2: '송금',
        3: '충전',
        4: '환불',
        5: '환전',
        6: '환급',
    };
    return transactionTypes[code] || '알 수 없는 거래 유형';
}

// 거래 유형 옵션 (계좌 종류에 따라 다르게 설정)
const transactionTypes = computed(() => {
    if (selectedCurrency.value === 'KoreaAccount') {
        return ['송금', '환전', '환급', '결제']; // 한국 계좌 거래 유형
    } else if (selectedCurrency.value === 'ForeignAccount') {
        return ['충전', '환전', '환급', '환불']; // 외화 계좌 거래 유형
    }
    return ['충전', '환전', '환급', '결제', '환불', '송금']; // 전체 선택 시 빈 배열
});

// 선택된 거래 내역 상태 관리
const selectedTransaction = ref(null);
const isModalVisible = ref(false);

// 메모 업데이트 기능
const updateMemo = (newMemo) => {
    if (selectedTransaction.value) {
        // 기존 객체를 복사하고 memo만 수정
        selectedTransaction.value = {
            ...selectedTransaction.value,
            memo: newMemo,
        };
    }
};

// 거래 내역을 클릭하면 모달을 열고 해당 거래를 선택
const openModal = (transaction) => {
    selectedTransaction.value = transaction;
    isModalVisible.value = true;
};

// 모달을 닫는 함수
const closeModal = () => {
    isModalVisible.value = false;
};

// 날짜 선택 창 열기
const openDatePicker = (event) => {
    event.target.showPicker();
};

// 메모 업데이트 처리 함수
const MemoUpdate = ({ historyNo, memo }) => {
    const index = transactions.value.findIndex(
        (t) => t.historyNo === historyNo
    );
    if (index !== -1) {
        transactions.value[index].memo = memo; // 업데이트된 메모 반영
    }
};
</script>

<template>
    <div class="transaction-history-container">
        <div class="transaction-history">
            <h2>이용 내역 (Transaction history)</h2>
            <div class="filters">
                <!-- 첫 번째 줄: 계좌 종류와 상세 내용 검색 -->
                <div class="filter-row">
                    <div class="filter-item filter-account">
                        <label>계좌 종류</label>
                        <select v-model="selectedCurrency">
                            <option value="">전체 내역</option>
                            <option value="KoreaAccount">원화 머니 계좌</option>
                            <option value="ForeignAccount">
                                외화 머니 계좌
                            </option>
                        </select>
                    </div>
                    <div class="filter-item filter-search">
                        <input
                            type="text"
                            class="form-control"
                            placeholder="상세 내용 검색"
                            v-model="searchQuery"
                            @keyup.enter="applyFilters"
                        />
                    </div>
                </div>

                <!-- 두 번째 줄: 시작 날짜, 끝 날짜, 거래 유형, 거래 상태, Apply 버튼 -->
                <div class="filter-row filter-row-responsive">
                    <div class="filter-item">
                        <label>시작 날짜</label>
                        <input
                            type="date"
                            v-model="startDate"
                            @click="openDatePicker"
                        />
                    </div>
                    <div class="filter-item">
                        <label>끝 날짜</label>
                        <input
                            type="date"
                            v-model="endDate"
                            @click="openDatePicker"
                        />
                    </div>

                    <div class="filter-item">
                        <label>거래 유형</label>
                        <select v-model="transactionType">
                            <option value="">모두</option>
                            <option
                                v-for="type in transactionTypes"
                                :key="type"
                                :value="type"
                            >
                                {{ type }}
                            </option>
                        </select>
                    </div>
                    <div class="filter-item">
                        <label>거래 상태</label>
                        <select v-model="transactionStatus">
                            <option value="">모두</option>
                            <option value="완료">완료</option>
                            <option value="취소">취소</option>
                            <option value="처리중">처리중</option>
                            <option value="진행 중">진행 중</option>
                        </select>
                    </div>
                    <div class="filter-item filter-apply">
                        <button @click="applyFilters">Apply</button>
                    </div>
                </div>
            </div>

            <br /><br />
            <!-- 이용 내역 테이블 -->
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th style="width: 120px">날짜/시간</th>
                        <th style="width: 100px">거래 유형</th>
                        <th style="width: 260px">거래 상세</th>
                        <th style="width: 120px">거래 금액</th>
                        <th style="width: 80px">거래 상태</th>
                    </tr>
                </thead>
                <tbody>
                    <tr
                        v-for="transaction in transactions"
                        :key="transaction.historyNo"
                        @click="openModal(transaction)"
                        style="cursor: pointer"
                    >
                        <td>{{ transaction.historyDate }}</td>
                        <td>{{ transaction.typeCode }}</td>
                        <td>{{ transaction.historyContent }}</td>
                        <!-- <td :style="{ color: transaction.amountColor }"></td> -->
                        <td>{{ transaction.amount }}</td>
                        <td>{{ transaction.stateCode }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <!-- 모달 컴포넌트  -->
        <Modal
            :transaction="selectedTransaction"
            :isVisible="isModalVisible"
            @close="closeModal"
            @updateMemo="MemoUpdate"
        />
    </div>
</template>

<style scoped>
/* 부모 컨테이너를 화면의 중앙에 위치하게 설정 */
.transaction-history-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
}

.transaction-history {
    background-color: white;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 1000px;
}

.filters {
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
}

/* 첫 번째 줄: 계좌와 검색 필터 */
.filter-row {
    display: flex;
    align-items: flex-end; /* 필터들을 아래쪽 끝에 맞추어 정렬 */
    justify-content: space-between;
    margin-bottom: 10px;
}

/* 두 번째 줄: 필터를 공간을 채워 고르게 배치하고 Apply 버튼을 오른쪽으로 */
.filter-row-responsive {
    display: flex;
    align-items: flex-end; /* 필터들을 아래쪽 끝에 맞추어 정렬 */
    justify-content: space-between;
    gap: 10px; /* 필터 아이템 간의 간격을 추가 */
}

.filter-item {
    flex: 1; /* 각 필터 아이템이 고르게 차지하도록 설정 */
}

.filter-item.filter-apply {
    flex: none; /* Apply 버튼의 너비가 고정되도록 */
    margin-left: auto;
}

.filter-item input,
.filter-item select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 8px;
    outline: none;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    line-height: 1.2; /* 높이를 조정 */
}

/* 상세 내용 검색 필터와 Apply 버튼의 위치를 조정 */
.filter-item.filter-search input {
    padding: 12px 8px; /* 상단에 더 많은 패딩을 주어 텍스트를 살짝 아래로 이동 */
    line-height: 1.4; /* 텍스트의 높이를 약간 조정 */
}

.filter-item.filter-apply button {
    padding: 12px 16px; /* Apply 버튼에 동일한 패딩을 적용 */
    line-height: 1.4;
    background-color: #2dce89;
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-weight: bold;
    transition: background-color 0.3s ease;
}

button:hover {
    background-color: #21af71;
}

table {
    width: 100%;
    border-collapse: collapse;
}

thead {
    background-color: #f4f4f4;
    text-align: center;
}

th,
td {
    padding: 10px;
    border: 1px solid #ddd;
}

td {
    text-align: center;
}

/* 반응형: 작은 화면에서는 필터를 세로로 배치 */
@media (max-width: 767px) {
    .filter-row,
    .filter-row-responsive {
        flex-direction: column;
        align-items: flex-start;
    }

    .filter-item {
        width: 100%;
    }

    button {
        width: 100%;
    }

    th,
    td {
        font-size: 12px;
        text-align: center;
    }

    th:nth-child(3),
    td:nth-child(3) {
        display: none; /* 거래 상세, 승인 번호, 상태를 숨김 */
    }
}
</style>
