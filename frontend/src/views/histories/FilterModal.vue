<script setup>
import { ref } from 'vue';

// Prop으로 부모 컴포넌트에서 전달받은 데이터와 이벤트 정의
const props = defineProps({
  isVisible: Boolean,
  filters: Object,
});

// 이벤트 emit 정의
const emit = defineEmits(['closeModal', 'applyFilters']);

// 직접설정 관련 상태
const isCustomDateSelected = ref(false);

// 직접설정 버튼을 클릭하면 기간 선택 필드를 보여주는 함수
const selectCustomDate = () => {
  isCustomDateSelected.value = true;
  props.filters.selectedPeriod = '직접설정'; // 직접설정 선택 시 값 설정
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
        <h4 class="modal-title" id="filterModalLabel">필터링 옵션</h4>
        <div class="modal-body">
          <div class="filter-options">
            <!-- 조회기간 선택 -->
            <div class="row mb-3">
              <label class="form-label">조회기간</label>
              <div class="btn-group">
                <button
                  class="btn btn-outline-info"
                  @click="selectPeriod('Today')"
                >
                  오늘
                </button>
                <button
                  class="btn btn-outline-info"
                  @click="selectPeriod('Last 3 Month')"
                >
                  3개월
                </button>
                <button
                  class="btn btn-outline-info"
                  @click="selectPeriod('Last Month')"
                >
                  1개월
                </button>
                <button class="btn btn-outline-info" @click="selectCustomDate">
                  직접설정
                </button>
              </div>
            </div>

            <!-- 직접설정 기간 선택 탭 -->
            <div class="row mb-3" v-if="isCustomDateSelected">
              <div class="col-6">
                <input
                  type="date"
                  v-model="filters.startDate"
                  class="form-control"
                  placeholder="시작 날짜"
                />
              </div>
              <div class="col-6">
                <input
                  type="date"
                  v-model="filters.endDate"
                  class="form-control"
                  placeholder="종료 날짜"
                />
              </div>
            </div>

            <!-- 유형 선택 -->
            <div class="row mb-3">
              <label class="form-label">유형선택</label>
              <div class="btn-group">
                <button
                  class="btn btn-outline-secondary"
                  @click="filters.selectedType = 'All'"
                >
                  전체
                </button>
                <button
                  class="btn btn-outline-secondary"
                  @click="filters.selectedType = 'SongEaccount'"
                >
                  송이계좌
                </button>
                <button
                  class="btn btn-outline-secondary"
                  @click="filters.selectedType = 'KRaccount'"
                >
                  원화계좌
                </button>
              </div>
            </div>

            <!-- 정렬 선택 -->
            <div class="row mb-3">
              <label class="form-label">정렬선택</label>
              <div class="btn-group">
                <button
                  class="btn btn-outline-secondary"
                  @click="filters.selectedSort = 'Newest'"
                >
                  최신순
                </button>
                <button
                  class="btn btn-outline-secondary"
                  @click="filters.selectedSort = 'Oldest'"
                >
                  과거순
                </button>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-primary" type="button" @click="applyFilters">
            확인
          </button>
          <button type="button" class="btn btn-secondary" @click="closeModal">
            닫기
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
