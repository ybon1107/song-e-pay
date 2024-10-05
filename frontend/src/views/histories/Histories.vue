<script setup>
import { ref, computed, onMounted } from 'vue';
import moment from 'moment'; // moment.js로 Unix Timestamp를 변환
import historyApi from '../../api/historiesApi.js'; // API 파일 import
import HistoriesDetailModal from './HistoriesDetailModal.vue';
import ArgonPagination from '@/components/templates/ArgonPagination.vue';
import ArgonPaginationItem from '@/components/templates/ArgonPaginationItem.vue';
import FilterModal from './FilterModal.vue'; // FilterModal.vue 가져오기

 // 유저 권한 
import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const user = computed(() => auth.user);


// 필터 모달 가시성 상태 관리
const isFilterModalVisible = ref(false);
const searchQuery = ref('');
// 실제 필터가 저장될 변수 (확인 버튼 누르면 적용됨)
const filters = ref({
    selectedPeriod: 'Last 3 Month',
    selectedType: 'All',
    selectedSort: 'Newest',
    startDate: '',
    endDate: '',
    historyContent: '',
});

// 모달을 열고 닫는 함수
const toggleFilterModal = () => {
    isFilterModalVisible.value = !isFilterModalVisible.value;
};

// 필터된 거래 내역 가져오기
const applyTransactionFilters = async (resetPage = false) => {
    if (resetPage) {
        pageRequest.value.page = 1;
    }

    // 검색어를 필터에 반영
    filters.value.historyContent = searchQuery.value;

    // 정렬 조건 변환
    let sortOrder = 'DESC'; // 기본값을 최신순으로 설정 (내림차순)
    if (filters.value.selectedSort === 'Oldest') {
        sortOrder = 'ASC'; // 과거순일 경우 오름차순으로 설정
    }

    // 날짜 처리 로직
    let startDate = null;
    let endDate = moment().format('YYYY-MM-DD'); // 기본적으로 오늘로 설정

    // 필터에서 선택한 기간에 따라 시작일과 종료일 설정
    if (filters.value.selectedPeriod === 'Today') {
        startDate = endDate; // 오늘이 시작일과 종료일 모두가 됨
    } else if (filters.value.selectedPeriod === 'Last Month') {
        startDate = moment().subtract(1, 'months').startOf('day').toISOString();
        endDate = moment().endOf('day').toISOString(); // 23:59:59로 설정
    } else if (filters.value.selectedPeriod === 'Last 3 Month') {
        startDate = moment().subtract(3, 'months').startOf('day').toISOString();
        endDate = moment().endOf('day').toISOString(); // 23:59:59로 설정
    } else if (filters.value.selectedPeriod === '직접설정') {
        startDate = filters.value.startDate;
        endDate = filters.value.endDate
            ? moment(filters.value.endDate).endOf('day').toISOString()
            : null; // 직접 설정된 날짜의 23:59:59로 설정
    } else {
        console.log('직접 설정 기간이 누락되었습니다.');
    }
    // 선택된 유형에 따라 거래 유형 코드를 설정
    let typeCodesToSend = [];
    if (filters.value.selectedType === 'KRaccount') {
        typeCodesToSend = [1, 2, 5, 6]; // 원화 계좌
    } else if (filters.value.selectedType === 'SongEaccount') {
        typeCodesToSend = [3, 4, 5, 6]; // 송이 계좌
    } else {
        typeCodesToSend = null;
    }
    const filterOptions = {
        userNo:  user.value.userNo, // 로그인 구현시 바꿔줄것 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        typeCode: typeCodesToSend,
        beginDate: startDate, // 이 부분이 null이 아닌 올바른 날짜로 설정되어야 함
        endDate: endDate, // 이 부분도 마찬가지로 제대로 설정되어야 함
        sortOrder: sortOrder, // ASC 또는 DESC 전송
        historyContent: filters.value.historyContent || '', // 검색어 추가
    };

    try {
        const response = await historyApi.applyFilters(
            filterOptions,
            pageRequest.value
        );
        if (response && Array.isArray(response.list)) {
            totalItems.value = response.totalCount;
            // 정렬 기준에 따라 데이터를 정렬
            transactions.value = response.list.map((transaction) => ({
                ...transaction,
                historyDate: formatUnixTimestamp(transaction.historyDate),
                typeCode: convertTransactionType(transaction.typeCode),
                stateCode: convertTransactionStatus(transaction.stateCode),
            }));
        }
    } catch (error) {
        console.error('Error fetching filtered transactions:', error);
    }
};

// 필터 적용 처리 함수 (applyFilters 이벤트 처리)
const handleFilterApply = async (filters) => {
    console.log('필터 적용됨:', filters);
    filters.value = { ...filters }; // 전달받은 필터 데이터를 저장
    isFilterModalVisible.value = false; // 모달 닫기

    await applyTransactionFilters(true); // 필터 적용 후 거래 내역 필터링
};

// 거래 내역 관련 기본 코드
const transactions = ref([]);
const totalItems = ref(0);
const pageRequest = ref({
    page: 1,
    amount: 10,
});

// 페이지 로드 시 거래 내역 가져오기
onMounted(async () => {
    await applyTransactionFilters();
});

// 메모 업데이트 처리 함수
const handleMemoUpdate = (updatedMemo) => {
    const transaction = transactions.value.find(
        (item) => item.historyNo === updatedMemo.historyNo
    );
    if (transaction) {
        transaction.memo = updatedMemo.memo;
    }
};

// const getTransactionList = async () => {
//     try {
//         const filterOptions = {
//             userNo: filters.value.userNo, // userNo 전달
//             pageRequest: {
//                 page: pageRequest.value.page,
//                 amount: pageRequest.value.amount,
//             },
//         };

//         const response = await historyApi.getTransactionList(pageRequest.value);
//         if (response && Array.isArray(response.list)) {
//             totalItems.value = response.totalCount;
//             transactions.value = response.list.map((transaction) => ({
//                 ...transaction,
//                 historyDate: formatUnixTimestamp(transaction.historyDate),
//                 typeCode: convertTransactionType(transaction.typeCode),
//                 stateCode: convertTransactionStatus(transaction.stateCode),
//             }));
//         }
//     } catch (error) {
//         console.error('Error fetching transaction list:', error);
//     }
// };

// 거래 유형 및 상태 코드 변환
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

const convertTransactionStatus = (code) => {
    const statuses = {
        1: '성공',
        2: '실패',
        3: '취소',
        4: '처리중',
    };
    return statuses[code] || '알 수 없는 상태';
};

// 페이지네이션 관련 함수
const totalPages = computed(() =>
    Math.ceil(totalItems.value / pageRequest.value.amount)
);

const goToPage = async (pageNum) => {
    if (pageNum > 0 && pageNum <= totalPages.value) {
        pageRequest.value.page = pageNum;
        await applyTransactionFilters(false); // 항상 필터를 유지한 상태로 페이지 이동
    }
};

const paginationItems = computed(() => {
    const items = [];
    const maxVisiblePages = 5;
    const halfVisible = Math.floor(maxVisiblePages / 2);
    let startPage = Math.max(pageRequest.value.page - halfVisible, 1);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages.value);
    if (endPage - startPage + 1 < maxVisiblePages) {
        startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }
    for (let i = startPage; i <= endPage; i++) {
        items.push({
            label: i.toString(),
            active: i === pageRequest.value.page,
            disabled: false,
        });
    }
    return items;
});

// Unix Timestamp 포맷팅 함수
const formatUnixTimestamp = (unixTimestamp) => {
    return unixTimestamp ? moment(unixTimestamp).format('YYYY-MM-DD') : '';
};

// 모달 관련 상태 관리
const isModalVisible = ref(false);
const selectedTransaction = ref(null);

const openModal = (transaction) => {
    selectedTransaction.value = transaction;
    isModalVisible.value = true;
};

const closeModal = () => {
    isModalVisible.value = false;
};
</script>

<template>
    <div class="container-fluid">
        <h3>이용 내역</h3>
        <!-- 검색 필터와 정렬 드롭다운을 포함한 상단 -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <div class="input-group" style="width: 60%">
                <span class="input-group-text">
                    <i class="fas fa-search"></i>
                </span>
                <input
                    type="text"
                    class="form-control"
                    placeholder="검색어를 입력하세요"
                    v-model="searchQuery"
                    @keyup.enter="applyTransactionFilters(true)"
                />
            </div>
            <button
                class="btn btn-outline-secondary btn-sm mt-2 mt-md-0"
                type="button"
                @click="toggleFilterModal"
                style="padding: 4px 8px; font-size: 0.875rem"
            >
                {{ filters.selectedPeriod }} · {{ filters.selectedType }} ·{{
                    filters.selectedSort
                }}
                <i class="fas fa-chevron-down"></i>
            </button>
        </div>
        <!-- 테이블 부분 -->
        <div class="card p-5 mt-3">
            <div class="table-responsive">
                <table class="table table-bordered align-middle">
                    <thead>
                        <tr>
                            <th style="width: 10%">날짜/시간</th>
                            <th style="width: 20%">거래 유형</th>
                            <th
                                class="text-center d-none d-md-table-cell"
                                style="width: 50%"
                            >
                                거래 내역
                            </th>
                            <th style="width: 20%">거래 금액</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr
                            v-for="transaction in transactions"
                            :key="transaction.historyNo"
                            @click="openModal(transaction)"
                            style="cursor: pointer"
                        >
                            <td class="text-center">
                                {{ transaction.historyDate }}
                            </td>
                            <td class="text-center">
                                {{ transaction.typeCode }}
                            </td>
                            <td class="text-center">
                                {{ transaction.historyContent }}
                            </td>
                            <td class="text-end">
                                {{ transaction.amount.toLocaleString() }}
                                <span
                                    v-if="
                                        ['충전', '환전', '환불'].includes(
                                            transaction.typeCode
                                        )
                                    "
                                >
                                    USD
                                </span>
                                <span
                                    v-else-if="
                                        ['환급', '송금'].includes(
                                            transaction.typeCode
                                        )
                                    "
                                >
                                    KRW
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- 페이지네이션 컨트롤 -->
            <div class="d-flex justify-content-center">
                <argon-pagination class="mb-0">
                    <argon-pagination-item
                        prev
                        @click="goToPage(pageRequest.page - 1)"
                        :disabled="pageRequest.page === 1"
                    />
                    <argon-pagination-item
                        v-for="item in paginationItems"
                        :key="item.label"
                        :label="item.label"
                        :active="item.active"
                        :disabled="item.disabled"
                        @click="goToPage(parseInt(item.label))"
                    />
                    <argon-pagination-item
                        next
                        @click="goToPage(pageRequest.page + 1)"
                        :disabled="pageRequest.page === totalPages"
                    />
                </argon-pagination>
            </div>
        </div>
        <!-- 필터 모달 -->
        <FilterModal
            :isVisible="isFilterModalVisible"
            :filters="filters"
            @closeModal="toggleFilterModal"
            @applyFilters="handleFilterApply"
        />

        <!-- 이용내역 상세 모달 -->
        <HistoriesDetailModal
            :transaction="selectedTransaction"
            :isVisible="isModalVisible"
            @updateMemo="handleMemoUpdate"
            @close="closeModal"
        />
    </div>
</template>

<style scoped>
thead {
    background-color: #f4f4f4;
    text-align: center;
}

@media (max-width: 767px) {
    th:nth-child(3),
    td:nth-child(3) {
        display: none;
    }
}
</style>
