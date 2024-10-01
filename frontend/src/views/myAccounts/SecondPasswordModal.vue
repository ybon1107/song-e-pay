<template>
  <Modal
    :isVisible="showModal"
    :showFooter="false"
    title="⚠️ 잠깐 !!!!"
    @close="closeModal"
  >
    <div class="modal-body">
      <p>사칭사기에 주의하세요.</p>
      <h2>결제 비밀번호</h2>

      <!-- 비밀번호 도트 -->
      <div class="password-dots">
        <span
          v-for="(digit, index) in 6"
          :key="index"
          :class="{ dot: true, filled: password.length >= index + 1 }"
        ></span>
      </div>

      <!-- 숫자 키패드 -->
      <div class="keypad">
        <button v-for="num in 9" :key="num" @click="enterDigit(num)">
          {{ num }}
        </button>
        <button @click="clearAll">C</button>
        <button @click="enterDigit(0)">0</button>
        <button @click="clearLast"><i class="fas fa-backspace"></i></button>
      </div>
    </div>
  </Modal>
</template>

<script setup>
import { ref, defineEmits } from "vue";
import myaccountApi from "../../api/myaccountApi";
import Modal from "../../components/modal/Modal.vue";
import Swal from "sweetalert2";
import "sweetalert2/dist/sweetalert2.min.css";

// 이벤트 설정
const emit = defineEmits(["close", "password-verified"]);

const showModal = ref(true);
const password = ref(""); // 비밀번호는 최대 6자리
const userNo = 1;

const handleOverlayClick = () => {
  closeModal();
};

// 숫자 입력 처리
const enterDigit = (num) => {
  if (password.value.length < 6) {
    password.value += num;

    // 비밀번호 길이가 6자리일 때 자동으로 확인
    if (password.value.length === 6) {
      verifyPassword(userNo);
    }
  }
};

// 마지막 입력 지우기
const clearLast = () => {
  if (password.value.length > 0) {
    password.value = password.value.slice(0, -1);
  }
};

// 전체 입력 지우기
const clearAll = () => {
  password.value = "";
};

// 2차 비밀번호 검증
const verifyPassword = async (userNo) => {
  try {
    // API 호출을 통해 correctPassword 가져오기
    const correctPassword = await myaccountApi.checkSecondPassword(userNo);
    // 사용자가 입력한 비밀번호와 correctPassword 비교
    if (password.value == correctPassword) {
      emit("password-verified"); // 비밀번호가 맞을 경우 password-verified 이벤트 emit
      closeModal(); // 모달 닫기
    } else {
      Swal.fire({
        title: "다시 입력하세요",
        text: "비밀번호가 틀렸습니다.",
        icon: "error",
      });
      password.value = ""; // 입력값 초기화
    }
  } catch (error) {
    console.error("비밀번호 검증 중 오류 발생:", error);
    Swal.fire({
      title: "다시 입력하세요",
      text: "비밀번호 확인에 실패했습니다. 다시 시도해 주세요.",
      icon: "error",
    });
  }
};

const closeModal = () => {
  showModal.value = false;
  emit("close");
};
</script>

<style scope>
/* 비밀번호 도트 */
.password-dots {
  display: flex;
  justify-content: space-between;
  margin: 20px 0;
}

.dot {
  width: 20px;
  height: 20px;
  border: 2px solid #333;
  border-radius: 50%;
  background-color: transparent;
}

.dot.filled {
  background-color: black;
}

/* 키패드 스타일 */
.keypad {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin: 20px 0;
  justify-items: center;
  align-items: center;
}

.keypad button {
  width: 60px;
  height: 60px;
  font-size: 24px;
  background-color: #f0f0f0;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.keypad button:hover {
  background-color: #ddd;
}
</style>
