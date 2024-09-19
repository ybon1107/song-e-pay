<script setup>
import { ref, computed } from 'vue';
import Modal from './Modal.vue'; // 모달 창

// 필터 변수들
const selectedCurrency = ref('');
const endDate = ref('');
const startDate = ref('');
const transactionType = ref('');
const transactionStatus = ref('');
const searchQuery = ref('');

// 거래 내역 데이터 (API로부터 가져올 수 있음)
const transactions = ref([
    {
        id: 1,
        date: '2024-09-03 15:06:22',
        type: '환전',
        amount: '120580KRW (90USD)',
        account: null,
        exchangeRate: null,
        detail: '외화 계좌 → 원화 계좌',
        approvalNumber: null,
        balance: '120,580 USD',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 2,
        date: '2024-09-11 15:30:00',
        type: '결제',
        amount: '3000KRW',
        account: null,
        exchangeRate: null,
        detail: 'GS 편의점',
        approvalNumber: null,
        balance: '117,580 KRW',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 3,
        date: '2024-09-11 17:30:00',
        type: '송금',
        amount: '100000KRW',
        account: null,
        exchangeRate: null,
        detail: 'ABC',
        approvalNumber: null,
        balance: '107,580 KRW',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 4,
        date: '2024-09-12 09:00:00',
        type: '환급',
        amount: '150000KRW (110USD)',
        account: null,
        exchangeRate: null,
        detail: '원화 계좌 → 외화 계좌',
        approvalNumber: null,
        balance: '150,000 USD',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 5,
        date: '2024-09-12 10:00:00',
        type: '결제',
        amount: '5000KRW',
        account: null,
        exchangeRate: null,
        detail: '스타벅스',
        approvalNumber: null,
        balance: '145,000 KRW',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 6,
        date: '2024-09-12 11:30:00',
        type: '송금',
        amount: '250000KRW',
        account: null,
        exchangeRate: null,
        detail: 'DEF',
        approvalNumber: null,
        balance: '120,000 KRW',
        balanceAfterExchange: null,
        status: '처리중',
        memo: null,
    },
    {
        id: 7,
        date: '2024-09-13 13:00:00',
        type: '환전',
        amount: '200000KRW (150USD)',
        account: null,
        exchangeRate: null,
        detail: '외화 계좌 → 원화 계좌',
        approvalNumber: null,
        balance: '150,000 USD',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 8,
        date: '2024-09-13 14:15:00',
        type: '충전',
        amount: '50000KRW',
        account: null,
        exchangeRate: null,
        detail: 'ATM 충전',
        approvalNumber: null,
        balance: '200,000 KRW',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 9,
        date: '2024-09-14 15:30:00',
        type: '결제',
        amount: '15000KRW',
        account: null,
        exchangeRate: null,
        detail: '이마트',
        approvalNumber: null,
        balance: '185,000 KRW',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 10,
        date: '2024-09-14 16:45:00',
        type: '환급',
        amount: '50000KRW (35USD)',
        account: null,
        exchangeRate: null,
        detail: '외화 계좌 → 원화 계좌',
        approvalNumber: null,
        balance: '185,000 USD',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 11,
        date: '2024-09-15 09:00:00',
        type: '환전',
        amount: '30000KRW (22USD)',
        account: null,
        exchangeRate: null,
        detail: '원화 계좌 → 외화 계좌',
        approvalNumber: null,
        balance: '155,000 USD',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 12,
        date: '2024-09-15 10:30:00',
        type: '환불',
        amount: '10000KRW',
        account: null,
        exchangeRate: null,
        detail: '원래 계좌',
        approvalNumber: null,
        balance: '145,000 KRW',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 13,
        date: '2024-09-16 11:00:00',
        type: '결제',
        amount: '12000KRW',
        account: null,
        exchangeRate: null,
        detail: 'CGV 영화관',
        approvalNumber: null,
        balance: '133,000 KRW',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
    {
        id: 14,
        date: '2024-09-17 14:30:00',
        type: '송금',
        amount: '50000KRW',
        account: null,
        exchangeRate: null,
        detail: 'GHI',
        approvalNumber: null,
        balance: '83,000 KRW',
        balanceAfterExchange: null,
        status: '완료',
        memo: null,
    },
]);


// 필터링된 거래 내역 계산
const filteredTransactions = ref([...transactions.value]);

const applyFilters = () => {
    filteredTransactions.value = transactions.value
        .filter((transaction) => {
            // 날짜 필터링
            const transactionDate = new Date(transaction.date);

            // 변환된 날짜 형식을 YYYY-MM-DD로 사용
            const formattedTransactionDate = transactionDate
                .toISOString()
                .slice(0, 10); // YYYY-MM-DD 형식으로 자르기

            const startDateFilter = startDate.value
                ? new Date(startDate.value)
                : null;
            const endDateFilter = endDate.value
                ? new Date(endDate.value)
                : null;

            const dateInRange =
                (!startDateFilter || transactionDate >= startDateFilter) &&
                (!endDateFilter || transactionDate <= endDateFilter);

            // 필터링 로직 (계좌와 거래 유형 매핑 등)
            const KoreaTransaction = ['송금', '환전', '환급', '결제'].includes(
                transaction.type
            );
            const ForeignTransaction = ['충전', '환급', '환전', '환불'].includes(
                transaction.type
            );

            const matchesAccount =
                (selectedCurrency.value === 'Korea' && KoreaTransaction) ||
                (selectedCurrency.value === 'Foreign' && ForeignTransaction) ||
                selectedCurrency.value === '';

            const matchesType =
                transactionType.value === '' ||
                transaction.type === transactionType.value;

            const matchesStatus =
                transactionStatus.value === '' ||
                transaction.status === transactionStatus.value;

            const matchesQuery =
                searchQuery.value === '' ||
                transaction.detail.includes(searchQuery.value);

            return (
                matchesAccount &&
                matchesType &&
                matchesStatus &&
                matchesQuery &&
                dateInRange
            );
        })
        // amountColor 설정
        .map((transaction) => {
            let amountColor = 'black';

            if (selectedCurrency.value === '') {
                amountColor = 'black';
            } else if (selectedCurrency.value === 'Korea') {
                amountColor = transaction.type === '환전' ? 'blue' : 'red';
            } else if (selectedCurrency.value === 'Foreign') {
                amountColor = ['환전', '환불'].includes(transaction.type)
                    ? 'red'
                    : 'blue';
            }

            return {
                ...transaction,
                amountColor,
                date: new Date(transaction.date).toISOString().slice(0, 10), // 날짜 형식 변환
            };
        });

    console.log('필터가 적용되었습니다.');
};

// 거래 유형 옵션 (계좌 종류에 따라 다르게 설정)
const transactionTypes = computed(() => {
    if (selectedCurrency.value === 'Korea') {
        return ['송금', '환전', '환급', '결제']; // 한국 계좌 거래 유형
    } else if (selectedCurrency.value === 'Foreign') {
        return ['충전', '환전', '환급', '환불']; // 외화 계좌 거래 유형
    }
    return ['충전','환전','환급','결제','환불','송금']; // 전체 선택 시 빈 배열
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
      memo: newMemo
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
                            <option value="Korea">원화 머니 계좌</option>
                            <option value="Foreign">외화 머니 계좌</option>
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
											<input type="date" v-model="startDate" @focus="openDatePicker($event)" @click="openDatePicker($event)" />
										</div>
										<div class="filter-item">
											<label>끝 날짜</label>
											<input type="date" v-model="endDate" @focus="openDatePicker($event)" @click="openDatePicker($event)" />
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

            <br><br>
            <!-- 이용 내역 테이블 -->
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th style="width: 120px">날짜/시간</th>
                        <th style="width: 120px">거래 유형</th>
                        <th style="width: 200px">거래 상세</th>
                        <th style="width: 120px">거래 금액</th>
                        <th style="width: 120px">잔액</th>
                        <th style="width: 120px">거래 상태</th>
                    </tr>
                </thead>
                <tbody>
                    <tr
                        v-for="transaction in filteredTransactions"
                        :key="transaction.id"
                        @click="openModal(transaction)"
                        style="cursor: pointer"
                    >
                    <td>{{ transaction.date.slice(0, 10) }}</td>
                        <td>{{ transaction.type }}</td>
                        <td>{{ transaction.detail }}</td>
                        <td :style="{ color: transaction.amountColor }">
                            {{ transaction.amount }}
                        </td>
                        <td>{{ transaction.balance }}</td>
                        <td>{{ transaction.status }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <!-- 모달 컴포넌트  -->
        <Modal 
            :transaction="selectedTransaction" 
            :isVisible="isModalVisible" 
            @close="closeModal" 
            @updateMemo="updateMemo" 
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
    background-color: #2DCE89;
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
    }

    /* 특정 테이블 열 숨기기: 모바일 크기에서는 4개의 탭만 보여줌 */
    th:nth-child(3),
    th:nth-child(6),
    th:nth-child(7),
    td:nth-child(3),
    td:nth-child(6),
    td:nth-child(7) {
        display: none; /* 거래 상세, 승인 번호, 상태를 숨김 */
    }
}
</style>
