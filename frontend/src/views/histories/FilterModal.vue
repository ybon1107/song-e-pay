<script setup>
import { ref } from 'vue';

// Prop으로 부모 컴포넌트에서 전달받은 데이터와 이벤트 정의
const props = defineProps({
    isVisible: Boolean,
    filters: Object,
    periods: Array,
    types: Array,
    sorts: Array,
});

// 이벤트 emit 정의
const emit = defineEmits(['closeModal', 'applyFilters']);

// 직접설정 관련 상태
const isCustomDateSelected = ref(false);

// 직접설정 버튼을 클릭하면 기간 선택 필드를 보여주는 함수
const selectCustomDate = () => {
    isCustomDateSelected.value = true;
    props.filters.selectedPeriod = "$t('histories--filters-period-custom')"; // 직접설정 선택 시 값 설정
};

// 기간 선택 시 호출되는 함수
const selectPeriod = (period) => {
    props.filters.selectedPeriod = period;
    isCustomDateSelected.value = false; // 다른 기간을 선택하면 직접 설정 탭 숨기기
    props.filters.startDate = null;
    props.filters.endDate = null;
};

// 모달 닫기
const closeModal = () => {
    emit('closeModal');
};

// 필터 적용 버튼 클릭 시
const applyFilters = () => {
    emit('applyFilters', props.filters); // 필터 데이터를 함께 전달
    closeModal;
};
</script>

<template>
    <div
        v-if="isVisible"
        class="modal fade show d-block"
        tabindex="-1"
        role="dialog"
        aria-labelledby="filterModalLabel"
        aria-hidden="false"
        @click.self="closeModal"
    >
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <h4 class="modal-title" id="filterModalLabel">
                    {{ $t('histories--filter-modal-title') }}
                </h4>
                <div class="modal-body">
                    <div class="filter-options">
                        <!-- 조회기간 선택 -->
                        <div class="row mb-3">
                            <label class="form-label">{{
                                $t('histories--filter-modal-title-period')
                            }}</label>
                            <div class="btn-group">
                                <button
                                    class="btn btn-outline-info"
                                    :class="{
                                        active:
                                            props.filters.selectedPeriod ===
                                            periods[0],
                                    }"
                                    @click="selectPeriod(periods[0])"
                                >
                                    {{ $t(periods[0]) }}
                                </button>
                                <button
                                    class="btn btn-outline-info"
                                    :class="{
                                        active:
                                            props.filters.selectedPeriod ===
                                            periods[1],
                                    }"
                                    @click="selectPeriod(periods[1])"
                                >
                                    {{ $t(periods[1]) }}
                                </button>
                                <button
                                    class="btn btn-outline-info"
                                    :class="{
                                        active:
                                            props.filters.selectedPeriod ===
                                            periods[2],
                                    }"
                                    @click="selectPeriod(periods[2])"
                                >
                                    {{ $t(periods[2]) }}
                                </button>
                                <button
                                    class="btn btn-outline-info"
                                    :class="{
                                        active:
                                            props.filters.selectedPeriod ===
                                            '직접설정',
                                    }"
                                    @click="selectCustomDate"
                                >
                                    {{ $t(periods[3]) }}
                                </button>
                            </div>
                        </div>
                        <!-- <div class="row mb-3">
                            <label class="form-label">{{$t('histories--filter-modal-title-period')}}</label>
                            <div class="btn-group">
                                <button class="btn btn-outline-info" v-for="period in periods" :key="period" @click="selectPeriod(period)">
                                    {{$t(period)}}
                                </button>
                                <button class="btn btn-outline-info" @click="selectCustomDate">
                                    {{$t('histories--filters-period-custom')}}
                                </button>
                            </div>
                        </div> -->

                        <!-- 직접설정 기간 선택 탭 -->
                        <div class="row mb-3" v-if="isCustomDateSelected">
                            <div class="col-6">
                                <input
                                    type="date"
                                    v-model="filters.startDate"
                                    class="form-control"
                                    :placeholder="$t('histories--startDate')"
                                />
                            </div>
                            <div class="col-6">
                                <input
                                    type="date"
                                    v-model="filters.endDate"
                                    class="form-control"
                                    :placeholder="$t('histories--endDate')"
                                />
                            </div>
                        </div>

                        <!-- 유형 선택 -->
                        <div class="row mb-3">
                            <label class="form-label">{{
                                $t('histories--filter-modal-title-type')
                            }}</label>
                            <div class="btn-group">
                                <button
                                    class="btn btn-outline-secondary"
                                    :class="{
                                        active:
                                            props.filters.selectedType === type,
                                    }"
                                    v-for="type in types"
                                    :key="type"
                                    @click="filters.selectedType = type"
                                >
                                    {{ $t(type) }}
                                </button>
                            </div>
                        </div>

                        <!-- 정렬 선택 -->
                        <div class="row mb-3">
                            <label class="form-label">{{
                                $t('histories--filter-modal-title-sort')
                            }}</label>
                            <div class="btn-group">
                                <button
                                    class="btn btn-outline-secondary"
                                    :class="{
                                        active:
                                            props.filters.selectedSort === sort,
                                    }"
                                    v-for="sort in sorts"
                                    :key="sort"
                                    @click="filters.selectedSort = sort"
                                >
                                    {{ $t(sort) }}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button
                        class="btn btn-primary"
                        type="button"
                        @click="applyFilters"
                    >
                        {{ $t('common--btn-confirm') }}
                    </button>
                    <button
                        type="button"
                        class="btn btn-secondary"
                        @click="closeModal"
                    >
                        {{ $t('common--btn-close') }}
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.filter-modal {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 70%;
    background-color: white;
    z-index: 1000;
}

.modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 10px;
}

.btn-group button {
    margin-right: 5px;
}
</style>
