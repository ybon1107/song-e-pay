<script setup>
import { ref, computed } from 'vue';

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
        date: '2024-09-03 15:06:22', // 기존 형식 그대로 유지
        type: '환전',
        detail: '외화 계좌 → 원화 계좌',
        amount: '120580KRW (90USD)',
        balance: '120,580 USD',
        status: '완료',
    },
    {
        id: 2,
        date: '2024-09-11 15:30:00', // 형식에 맞게 수정
        type: '결제',
        detail: 'GS 편의점',
        amount: '3000KRW',
        balance: '117,580 KRW',
        status: '완료',
    },
    {
        id: 3,
        date: '2024-09-11 17:30:00', // 형식에 맞게 수정
        type: '송금',
        detail: 'ABC',
        amount: '100000KRW',
        balance: '107,580 KRW',
        status: '완료',
    },
    {
        id: 4,
        date: '2024-09-12 09:00:00', // 형식에 맞게 수정
        type: '환급',
        detail: '원화 계좌 → 외화 계좌',
        amount: '150000KRW (110USD)',
        balance: '150,000 USD',
        status: '완료',
    },
    {
        id: 5,
        date: '2024-09-12 10:00:00', // 형식에 맞게 수정
        type: '결제',
        detail: '스타벅스',
        amount: '5000KRW',
        balance: '145,000 KRW',
        status: '완료',
    },
    {
        id: 6,
        date: '2024-09-12 11:30:00', // 형식에 맞게 수정
        type: '송금',
        detail: 'DEF',
        amount: '250000KRW',
        balance: '120,000 KRW',
        status: '처리중',
    },
    {
        id: 7,
        date: '2024-09-13 13:00:00', // 형식에 맞게 수정
        type: '환전',
        detail: '외화 계좌 → 원화 계좌',
        amount: '200000KRW (150USD)',
        balance: '150,000 USD',
        status: '완료',
    },
    {
        id: 8,
        date: '2024-09-13 14:15:00', // 형식에 맞게 수정
        type: '충전',
        detail: 'ATM 충전',
        amount: '50000KRW',
        balance: '200,000 KRW',
        status: '완료',
    },
    {
        id: 9,
        date: '2024-09-14 15:30:00', // 형식에 맞게 수정
        type: '결제',
        detail: '이마트',
        amount: '15000KRW',
        balance: '185,000 KRW',
        status: '완료',
    },
    {
        id: 10,
        date: '2024-09-14 16:45:00', // 형식에 맞게 수정
        type: '환급',
        detail: '외화 계좌 → 원화 계좌',
        amount: '50000KRW (35USD)',
        balance: '185,000 USD',
        status: '완료',
    },
    {
        id: 11,
        date: '2024-09-15 09:00:00', // 형식에 맞게 수정
        type: '환전',
        detail: '원화 계좌 → 외화 계좌',
        amount: '30000KRW (22USD)',
        balance: '155,000 USD',
        status: '완료',
    },
    {
        id: 12,
        date: '2024-09-15 10:30:00', // 형식에 맞게 수정
        type: '환불',
        detail: '원래 계좌',
        amount: '10000KRW',
        balance: '145,000 KRW',
        status: '완료',
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
            const ForeignTransaction = [
                '충전',
                '환급',
                '환전',
                '환불',
            ].includes(transaction.type);

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
                amountColor = ['환전', '환급'].includes(transaction.type)
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
</script>

<template>
    <div class="transaction-history">
        <h2>이용 내역 (Transaction history)</h2>

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
