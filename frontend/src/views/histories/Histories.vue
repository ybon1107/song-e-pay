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
    <div class="container-fluid">
        <h1>이용 내역</h1>
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
                            <option value="완료">완료</option>
                            <option value="취소">취소</option>
                            <option value="처리중">처리중</option>
                            <option value="진행 중">진행 중</option>
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
                            @keyup.enter="applyFilters" />
                    </div>
                    <div class="col-md-2 d-flex align-items-end mb-3">
                        <button @click="applyFilters" class="btn btn-success mb-0">Apply</button>
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
                            <!-- <td :style="{ color: transaction.amountColor }"></td> -->
                            <td>{{ transaction.amount }}</td>
                            <td>{{ transaction.stateCode }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- 모달 컴포넌트  -->
            <Modal :transaction="selectedTransaction" :isVisible="isModalVisible" @close="closeModal"
                @updateMemo="MemoUpdate" />
        </div>
    </div>

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
