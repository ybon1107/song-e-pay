<template>
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
        <div class="modal-header">
          <h4 class="modal-title" id="maintenanceLabel">
            {{ $t("schedule--text--registerSchedule") }}
          </h4>
          <button type="button" class="close" @click="closeModal">
            <span>&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="row no-gutters mb-3">
            <label for="title" class="col-3">{{
              $t("schedule--text--schedule")
            }}</label>
            <div class="col-14">
              <input
                id="title"
                v-model="maintenanceCopy.title"
                class="form-control"
                type="text"
                :placeholder="$t('schedule--text--enterSchedule')"
                style="width: 100%"
              />
            </div>
          </div>
          <div class="row no-gutters mb-3">
            <label for="color" class="col-3">{{
              $t("schedule--text--selectColor")
            }}</label>
            <div class="col-14 btn-group">
              <button
                v-for="color in [
                  'primary',
                  'warning',
                  'success',
                  'danger',
                  'muted',
                ]"
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
            <label for="description" class="col-3">{{
              $t("schedule--text--details")
            }}</label>
            <div class="col-14">
              <textarea
                id="description"
                v-model="maintenanceCopy.description"
                class="form-control"
                rows="3"
                :placeholder="$t('schedule--text--enterDetails')"
                style="width: 100%"
              ></textarea>
            </div>
          </div>
          <div class="row no-gutters mb-3">
            <label class="col-3">{{ $t("schedule--text--period") }}</label>
            <div class="col-14">
              <div class="row no-gutters">
                <div class="col-6">
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
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" @click="saveEvent">
            {{ $t("schedule--text--save") }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from "vue";

const props = defineProps({
  isVisible: Boolean,
  maintenance: Object,
});

const emit = defineEmits(["saveEvent", "closeModal"]);

const maintenanceCopy = ref({ ...props.maintenance });

watch(
  () => props.isVisible,
  (newVisible) => {
    if (newVisible) {
      maintenanceCopy.value = { ...props.maintenance };
      maintenanceCopy.value.description = ""; // 기존 세부내용 초기화
      nextTick(() => {
        document.getElementById("title")?.focus(); // 모달 열리면 title 필드에 포커스
      });
    }
  },
  { immediate: true }
);

const getColor = (colorName) => {
  switch (colorName) {
    case "primary":
      return "#8EEFEF";
    case "warning":
      return "#FFD347";
    case "success":
      return "#4DD36A";
    case "danger":
      return "#FFA8E1";
    case "muted":
      return "#A1A5AB";
    default:
      return "#8EEFEF";
  }
};

const closeModal = () => {
  emit("closeModal");
};

const saveEvent = () => {
  maintenanceCopy.value.color = getColor(maintenanceCopy.value.color);
  emit("saveEvent", maintenanceCopy.value);
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
