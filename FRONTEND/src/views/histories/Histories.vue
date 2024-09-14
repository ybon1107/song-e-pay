<script setup>
import { ref, computed } from 'vue';
import Modal from './Modal.vue'; // 새로 만든 모달 컴포넌트 가져오기

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
            const startDateFilter = startDate.value
                ? new Date(startDate.value)
                : null;
            const endDateFilter = endDate.value
                ? new Date(endDate.value)
                : null;

                const dateInRange =
                (!startDateFilter || transactionDate >= startDateFilter) &&
                (!endDateFilter || transactionDate <= endDateFilter);

            // 계좌와 거래 유형 매핑 로직
            const KoreaTransaction = ['송금', '환전', '환급', '결제'].includes(
                transaction.type
            );
            const ForeignTransaction = ['충전','환급','환전','환불',].includes(
              transaction.type
            );

            // 선택된 계좌에 따른 필터링 로직
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
                matchesAccount && matchesType && matchesStatus && matchesQuery
            );
        })

        // amountColor
        .map((transaction) => {
            // 거래 유형에 따른 amountColor 결정
            let amountColor = 'black'; // 기본 색상

            if (selectedCurrency.value === '') {
                // 계좌 종류가 선택되지 않은 경우
                amountColor = 'black'; // 기본 색상
            } else if (selectedCurrency.value === 'Korea') {
                // 한국 계좌인 경우
                amountColor = transaction.type === '환전' ? 'blue' : 'red'; // 환전은 파란색, 나머지는 빨간색
            } else if (selectedCurrency.value === 'Foreign') {
                // 외화 계좌인 경우
                amountColor = ['환전', '환불'].includes(transaction.type)
                    ? 'red'
                    : 'blue'; // 환전, 환급은 빨간색, 나머지는 파란색
            }

            // 필터링된 거래 반환 (amountColor 추가)
            return {
                ...transaction,
                amountColor,
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
    return []; // 전체 선택 시 빈 배열
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



</script>

<template>
    <div class="transaction-history">
        <h2>이용 내역 (Transaction history)</h2>
<br><br><br><br>
        <div class="filters">
            <!--  계좌 종류와 상세 내용 검색 -->
            <div class="filter-row">
                <div class="filter-item">
                    <label>계좌 종류</label>
                    <select v-model="selectedCurrency">
                        <option value="">전체 내역</option>
                        <option value="Korea">원화 머니 계좌</option>
                        <option value="Foreign">외화 머니 계좌</option>
                    </select>
                </div>
                <div class="input-group filter-item">
                    <span class="input-group-text text-body">
                        <i class="fas fa-search" aria-hidden="true"></i>
                    </span>
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
            <div class="filter-row">
                <div class="filter-item">
                    <label>시작 날짜</label>
                    <input type="date" v-model="startDate" />
                    <label>끝 날짜</label>
                    <input type="date" v-model="endDate" />
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
                <div class="filter-item">
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
                    <td>{{ transaction.date }}</td>
                    <td>{{ transaction.type }}</td>
                    <td>{{ transaction.detail }}</td>
                    <!-- 금액을 보여주면서 색상을 적용 -->
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
</template>

<style scoped>
.filters {
    display: flex;
    flex-direction: column; /* 필터들을 세로로 배치 */
    margin-bottom: 20px;
}

.filter-row {
    display: flex; /* 각 행을 가로로 배치 */
    margin-bottom: 10px; /* 두 줄 사이의 간격 */
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

button {
    padding: 5px 10px;
    background-color: #f9d71c;
    border: none;
    cursor: pointer;
}

button:hover {
    background-color: #e0be14;
}
</style>
