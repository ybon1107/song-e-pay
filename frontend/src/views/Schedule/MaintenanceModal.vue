<template>
<<<<<<< HEAD
  <div v-if="isVisible" class="modal fade show" tabindex="-1" style="display: block" :inert="!isVisible">
    <div class="modal-dialog" style="max-width: 500px">
      <div class="modal-content" style="padding-left: 20px; padding-right: 20px">
=======
  <div
    v-if="isVisible"
    class="modal fade show"
    tabindex="-1"
    style="display: block"
    :inert="!isVisible"
  >
    <div class="modal-dialog" style="max-width: 500px">
      <div
        class="modal-content"
        style="padding-left: 20px; padding-right: 20px"
      >
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
        <div class="modal-header">
          <h4 class="modal-title" id="maintenanceLabel">이벤트 등록</h4>
          <button type="button" class="close" @click="closeModal">
            <span>&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="row no-gutters mb-3">
            <label for="title" class="col-3">이벤트명</label>
            <div class="col-14">
<<<<<<< HEAD
              <input id="title" v-model="maintenanceCopy.title" class="form-control" type="text" placeholder="이벤트명을 입력하세요" style="width: 100%" />
=======
              <input
                id="title"
                v-model="maintenanceCopy.title"
                class="form-control"
                type="text"
                placeholder="이벤트명을 입력하세요"
                style="width: 100%"
              />
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
            </div>
          </div>
          <div class="row no-gutters mb-3">
            <label for="color" class="col-3">색상 선택</label>
            <div class="col-14 btn-group">
              <button
<<<<<<< HEAD
                v-for="color in ['primary', 'warning', 'success', 'danger', 'muted']"
=======
                v-for="color in [
                  'primary',
                  'warning',
                  'success',
                  'danger',
                  'muted',
                ]"
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
                :key="color"
                @click="maintenanceCopy.color = color"
                :style="{ backgroundColor: getColor(color) }"
                :class="{
                  active: maintenanceCopy.color === color,
                }"
                class="color-picker-btn"
              ></button>
            </div>
          </div>
          <div class="row no-gutters mb-3">
            <label for="description" class="col-3">세부내용</label>
            <div class="col-14">
<<<<<<< HEAD
              <textarea id="description" v-model="maintenanceCopy.description" class="form-control" rows="3" placeholder="세부내용을 입력하세요" style="width: 100%"></textarea>
=======
              <textarea
                id="description"
                v-model="maintenanceCopy.description"
                class="form-control"
                rows="3"
                placeholder="세부내용을 입력하세요"
                style="width: 100%"
              ></textarea>
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
            </div>
          </div>
          <div class="row no-gutters mb-3">
            <label class="col-3">기간</label>
            <div class="col-14">
              <div class="row no-gutters">
                <div class="col-6">
<<<<<<< HEAD
                  <input id="startDate" v-model="maintenanceCopy.startedAt" type="date" class="form-control" style="width: 100%" />
                </div>
                <div class="col-6">
                  <input id="endDate" v-model="maintenanceCopy.endedAt" type="date" class="form-control" style="width: 100%" />
=======
                  <input
                    id="startDate"
                    v-model="maintenanceCopy.startedAt"
                    type="date"
                    class="form-control"
                    style="width: 100%"
                  />
                </div>
                <div class="col-6">
                  <input
                    id="endDate"
                    v-model="maintenanceCopy.endedAt"
                    type="date"
                    class="form-control"
                    style="width: 100%"
                  />
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
<<<<<<< HEAD
          <button type="button" class="btn btn-primary" @click="saveEvent">저장</button>
=======
          <button type="button" class="btn btn-primary" @click="saveEvent">
            저장
          </button>
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue';

const props = defineProps({
  isVisible: Boolean,
  maintenance: Object,
});

const emit = defineEmits(['saveEvent', 'closeModal']);

const maintenanceCopy = ref({ ...props.maintenance });

watch(
  () => props.isVisible,
  (newVisible) => {
    if (newVisible) {
      maintenanceCopy.value = { ...props.maintenance };
      maintenanceCopy.value.description = ''; // 기존 세부내용 초기화
      nextTick(() => {
        document.getElementById('title')?.focus(); // 모달 열리면 title 필드에 포커스
      });
    }
  },
  { immediate: true }
);

const getColor = (colorName) => {
  switch (colorName) {
    case 'primary':
      return '#8EEFEF';
    case 'warning':
      return '#FFD347';
    case 'success':
      return '#4DD36A';
    case 'danger':
      return '#FFA8E1';
    case 'muted':
      return '#A1A5AB';
    default:
      return '#8EEFEF';
  }
};

const closeModal = () => {
  emit('closeModal');
};

const saveEvent = () => {
  maintenanceCopy.value.color = getColor(maintenanceCopy.value.color);
  emit('saveEvent', maintenanceCopy.value);
  closeModal();
};
</script>

<style scoped>
.modal {
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-footer {
  display: flex;
  justify-content: flex-end; /* 저장 버튼을 오른쪽으로 정렬 */
}

.color-picker-btn {
  border: none;
  cursor: pointer;
  margin-right: 10px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  transition: box-shadow 0.3s ease;
}

.color-picker-btn.active {
  box-shadow: 0 0 0 4px rgba(0, 0, 0, 0.3);
}
</style>
