<template>
  <div v-if="showModal" class="modal">
    <div class="custom-modal">
      <div class="modal-header">
        <input
          v-model="title"
          type="text"
          class="title-input"
          placeholder="Add title"
        />
        <button class="save-btn" @click="submitData">SAVE</button>
        <span class="close-btn" @click="closeModal">&times;</span>
      </div>
      <div class="modal-body">
        <div class="modal-row">
          <i class="icon-clock"></i>
          <div class="text-section">
            <!-- modalData가 존재할 경우에만 start와 end 값에 접근 -->
            <span class="date-text">{{
              modalData?.start || "No start date"
            }}</span>
            <span class="duration-text">{{
              modalData?.end || "No end date"
            }}</span>
            <p v-if="modalData?.allDay">이 이벤트는 하루 종일입니다.</p>
          </div>
        </div>
        <div class="modal-row">
          <i class="icon-location"></i>
          <input
            v-model="location"
            type="text"
            class="location-input"
            placeholder="Add location"
          />
        </div>
        <div class="modal-row">
          <i class="icon-calendar"></i>
          <select v-model="calendarOption" class="dropdown">
            <option value="glue">Glue</option>
            <option value="other">Other</option>
          </select>
        </div>
        <div class="modal-row">
          <i class="icon-status"></i>
          <select v-model="statusOption" class="dropdown">
            <option value="busy">Busy</option>
            <option value="available">Available</option>
          </select>
        </div>
      </div>
    </div>
    <button @click="$emit('close')">Close Modal</button>
  </div>
</template>

<script setup>
import { ref } from "vue";

// props로 부모 컴포넌트에서 전달된 데이터 받기
const props = defineProps({
  modalData: {
    type: Object,
    default: () => ({ start: "", end: "", allDay: false }), // 기본값 설정
  },
  showModal: Boolean,
});

// 데이터 바인딩을 위한 ref 변수 선언
const title = ref("");
const location = ref("");
const calendarOption = ref("glue");
const statusOption = ref("busy");

// 부모 컴포넌트에 이벤트를 전달하는 emit 정의
const emit = defineEmits(["submit", "close"]);

// 저장 버튼 클릭 시 실행되는 함수
const submitData = () => {
  const submitData = {
    title: title.value,
    location: location.value,
    calendarOption: calendarOption.value,
    statusOption: statusOption.value,
    start: props.modalData.start, // 부모로부터 받은 modalData 활용
    end: props.modalData.end, // 부모로부터 받은 modalData 활용
    allDay: props.modalData.allDay, // 부모로부터 받은 modalData 활용
  };
  emit("submit", submitData); // 입력된 데이터를 부모 컴포넌트로 전달
  closeModal(); // 모달 닫기
};

// 모달 닫기 함수
const closeModal = () => {
  emit("close"); // 부모 컴포넌트에 close 이벤트 전달
};
</script>

<style scoped>
.custom-modal {
  width: 350px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
  position: relative;
  padding: 15px;
  box-sizing: border-box;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #1976d2;
  padding: 10px;
  border-radius: 8px 8px 0 0;
}

.title-input {
  width: 70%;
  padding: 8px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
}

.save-btn {
  background-color: #1976d2;
  color: #ffffff;
  border: none;
  cursor: pointer;
  font-size: 16px;
  margin-left: 10px;
  padding: 8px 16px;
}

.save-btn:hover {
  background-color: #1565c0;
}

.close-btn {
  font-size: 20px;
  color: #ffffff;
  cursor: pointer;
  margin-left: auto;
}

.modal-body {
  margin-top: 10px;
}

.modal-row {
  display: flex;
  align-items: center;
  margin: 15px 0;
}

.icon-clock,
.icon-location,
.icon-calendar,
.icon-status {
  font-size: 24px;
  margin-right: 10px;
  color: #757575;
}

.text-section {
  display: flex;
  flex-direction: column;
}

.date-text {
  font-size: 16px;
  color: #333;
}

.duration-text {
  font-size: 14px;
  color: #999;
}

.location-input,
.dropdown {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.dropdown {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-color: #f9f9f9;
}

.dropdown:focus,
.location-input:focus {
  border-color: #1976d2;
  outline: none;
}
</style>
