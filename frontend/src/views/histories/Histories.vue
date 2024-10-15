<script setup>
import { ref, computed, onMounted } from 'vue';
import moment from 'moment'; // moment.js로 Unix Timestamp를 변환
import historyApi from '../../api/historiesApi.js'; // API 파일 import
import HistoriesDetailModal from './HistoriesDetailModal.vue';
import ArgonPagination from '@/components/templates/ArgonPagination.vue';
import ArgonPaginationItem from '@/components/templates/ArgonPaginationItem.vue';
import FilterModal from './FilterModal.vue'; // FilterModal.vue 가져오기
import { CURRENCY_NAME } from '@/constants/countryCode';
import {
    TRANSACTION_TYPES,
    TRANSACTION_TYPES_KEY,
    TRANSACTION_STATES_KEY,
} from '@/constants/transactionType';
import Swal from 'sweetalert2'; // Swal 임포트 추가
// 유저 권한
import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const user = computed(() => auth.user);

// 필터 모달 가시성 상태 관리
const isFilterModalVisible = ref(false);
const searchQuery = ref('');
// 실제 필터가 저장될 변수 (확인 버튼 누르면 적용됨)

const songCurrencyUnit = computed(() => CURRENCY_NAME[user.value.countryCode]);

const i18n_PERIOD = [
    'histories--filters-period-today',
    'histories--filters-period-oneMonth',
    'histories--filters-period-threeMonth',
    'histories--filters-period-custom',
];
const i18n_TYPE = [
    'histories--filters-type-all',
    'histories--filters-type-wone',
    'histories--filters-type-songe',
];
const i18n_SORT = [
    'histories--filters-sort-newest',
    'histories--filters-sort-oldest',
];

const songTransactionType = [
    TRANSACTION_TYPES.DEPOSIT,
    TRANSACTION_TYPES.REFUND,
    TRANSACTION_TYPES.RE_EXCHANGE,
];
const wonTransactionType = [
    TRANSACTION_TYPES.PAYMENT,
    TRANSACTION_TYPES.TRANSFER,
    TRANSACTION_TYPES.EXCHANGE,
    TRANSACTION_TYPES.AUTO_EXCHANGE,
];

const filters = ref({
    selectedPeriod: i18n_PERIOD[2],
    selectedType: i18n_TYPE[0],
    selectedSort: i18n_SORT[0],
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
    if (filters.value.selectedSort === i18n_SORT[1]) {
        sortOrder = 'ASC'; // 과거순일 경우 오름차순으로 설정
    }

    // 날짜 처리 로직
    let startDate = null;
    let endDate = moment().format('YYYY-MM-DD'); // 기본적으로 오늘로 설정

    // 필터에서 선택한 기간에 따라 시작일과 종료일 설정
    if (filters.value.selectedPeriod === i18n_PERIOD[0]) {
        startDate = endDate; // 오늘이 시작일과 종료일 모두가 됨
    } else if (filters.value.selectedPeriod === i18n_PERIOD[1]) {
        startDate = moment().subtract(1, 'months').startOf('day').toISOString();
        endDate = moment().endOf('day').toISOString(); // 23:59:59로 설정
    } else if (filters.value.selectedPeriod === i18n_PERIOD[2]) {
        startDate = moment().subtract(3, 'months').startOf('day').toISOString();
        endDate = moment().endOf('day').toISOString(); // 23:59:59로 설정
    } else if (filters.value.selectedPeriod === i18n_PERIOD[3]) {
        startDate = filters.value.startDate;
        endDate = filters.value.endDate
            ? moment(filters.value.endDate).endOf('day').toISOString()
            : null; // 직접 설정된 날짜의 23:59:59로 설정
    } else {
        console.log('직접 설정 기간이 누락되었습니다.');
    }
    // 선택된 유형에 따라 거래 유형 코드를 설정
    let typeCodesToSend = [];
    if (filters.value.selectedType === i18n_TYPE[1]) {
        typeCodesToSend = wonTransactionType; // 원화 계좌
    } else if (filters.value.selectedType === i18n_TYPE[2]) {
        typeCodesToSend = songTransactionType; // 송이 계좌
    } else {
        typeCodesToSend = null;
    }
    const filterOptions = {
        // userNo: 1, // 나중에 바꿀것
        userId: auth.userId, // 로그인 구현시 바꿔줄것 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
                i18nType: TRANSACTION_TYPES_KEY[transaction.typeCode],
                i18nState: TRANSACTION_STATES_KEY[transaction.stateCode],
                // typeCode: convertTransactionType(transaction.typeCode),
                // stateCode: convertTransactionStatus(transaction.stateCode),
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

    // Swal.fire를 사용하여 필터 적용 알림 표시
    Swal.fire({
        title: t('histories--filter-applied'),
        text: t('histories--filter-applied-message'),
        icon: 'success',
        confirmButtonText: t('histories--confirm'),
    });
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
    await auth.fetchUser(auth.userId);
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

const getBadgeClass = (typeCode) => {
    const baseClasses = 'badge rounded-pill';
    if ([TRANSACTION_TYPES.DEPOSIT].includes(typeCode)) {
        return `${baseClasses} bg-primary`;
    } else if (
        [TRANSACTION_TYPES.REFUND, TRANSACTION_TYPES.RE_EXCHANGE].includes(
            typeCode
        )
    ) {
        return `${baseClasses} bg-danger`;
    } else if (
        [
            TRANSACTION_TYPES.PAYMENT,
            TRANSACTION_TYPES.TRANSFER,
            TRANSACTION_TYPES.RECEIVE,
        ].includes(typeCode)
    ) {
        return `${baseClasses} bg-success`;
    }
    return `${baseClasses} bg-secondary`;
};
</script>

<template>
    <div class="container-fluid">
        <h3>{{ $t('histories--transactionHistory') }}</h3>
        <!-- 검색 필터와 정렬 드롭다운을 포함한 상단 -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <div class="input-group" style="width: 60%">
                <span class="input-group-text">
                    <i class="fas fa-search"></i>
                </span>
                <input
                    type="text"
                    class="form-control"
                    :placeholder="$t('histories--searchQuery')"
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
                {{ $t(filters.selectedPeriod) }} ·
                {{ $t(filters.selectedType) }} ·
                {{ $t(filters.selectedSort) }}
                <i class="fas fa-chevron-down"></i>
            </button>
        </div>
        <!-- 테이블 부분 -->
        <div class="card p-5">
            <div class="table-responsive p-0">
                <table class="table align-items-center">
                    <thead>
                        <tr>
                            <th
                                style="width: 40%"
                                class="text-secondary text-xxs font-weight-bolder opacity-7"
                            >
                                {{ $t('histories--header-transactionDetail') }}
                            </th>
                            <th
                                style="width: 30%"
                                class="text-secondary text-xxs font-weight-bolder opacity-7 text-center"
                            >
                                {{ $t('histories--header-transactionType') }}
                            </th>
                            <th
                                style="width: 20%"
                                class="text-secondary text-xxs font-weight-bolder opacity-7"
                            >
                                {{ $t('histories--header-transactionAmount') }}
                            </th>
                            <th
                                style="width: 10%"
                                class="text-secondary text-xxs font-weight-bolder opacity-7"
                            >
                                {{ $t('histories--header-dateTime') }}
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr
                            v-for="transaction in transactions"
                            :key="transaction.historyNo"
                            @click="openModal(transaction)"
                            style="cursor: pointer"
                        >
                            <td>
                                {{ $t(transaction.historyContent) }}
                            </td>
                            <td class="text-center">
                                <span
                                    :class="
                                        getBadgeClass(transaction?.typeCode)
                                    "
                                    >{{ $t(transaction.i18nType) }}</span
                                >
                            </td>
                            <td class="d-flex align-items-center">
                                <span
                                    v-if="
                                        transaction.typeCode ===
                                        TRANSACTION_TYPES.DEPOSIT
                                    "
                                >
                                    <i class="ni ni-fat-add text-secondary"></i>
                                </span>
                                <span
                                    v-else-if="
                                        transaction.typeCode ===
                                        TRANSACTION_TYPES.REFUND
                                    "
                                >
                                    <i
                                        class="ni ni-fat-delete text-secondary"
                                    ></i>
                                </span>
                                {{ transaction.amount.toLocaleString() }}
                                <small
                                    v-if="
                                        songTransactionType.includes(
                                            transaction.typeCode
                                        )
                                    "
                                    class="ms-1"
                                >
                                    {{ songCurrencyUnit }}
                                </small>
                                <small
                                    v-else-if="
                                        wonTransactionType.includes(
                                            transaction.typeCode
                                        )
                                    "
                                    class="ms-1"
                                >
                                    KRW
                                </small>
                            </td>
                            <td>
                                {{ transaction.historyDate }}
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- 페이지네이션 컨트롤 -->
            <div class="d-flex justify-content-center">
                <argon-pagination class="mb-0" :color="'secondary'">
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
            :periods="i18n_PERIOD"
            :types="i18n_TYPE"
            :sorts="i18n_SORT"
            @closeModal="toggleFilterModal"
            @applyFilters="handleFilterApply"
        />

        <!-- 이용내역 상세 모달 -->
        <HistoriesDetailModal
            :transaction="selectedTransaction"
            :isVisible="isModalVisible"
            :songTransactionType="songTransactionType"
            :wonTransactionType="wonTransactionType"
            @updateMemo="handleMemoUpdate"
            @close="closeModal"
        />
    </div>
</template>

<style scoped>
/* thead {
    background-color: #f4f4f4;
    text-align: center;
} */

@media (max-width: 767px) {
    th:nth-child(0),
    td:nth-child(1),
    th:nth-child(2),
    td:nth-child(3),
    th:nth-child(3),
    td:nth-child(4) {
        display: none;
    }
}
</style>
