<template>
  <div>
    <!-- 모달 구조 -->
    <div v-if="showModal" class="modal-overlay" @click="handleOverlayClick">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>⚠️ 잠깐 !!!!</h3>
        </div>
        <div class="modal-body">
          <p>사칭사기에 주의하세요.</p>
          <h2>결제 비밀번호</h2>

          <!-- 비밀번호 도트 -->
          <div class="password-dots">
            <span v-for="(digit, index) in 6" :key="index"
              :class="{ dot: true, filled: password.length >= index + 1 }"></span>
          </div>

          <!-- 숫자 키패드 -->
          <div class="keypad">
            <button v-for="num in 9" :key="num" @click="enterDigit(num)">{{ num }}</button>
            <button @click="clearAll">C</button>
            <button @click="enterDigit(0)">0</button>
            <button @click="clearLast"><i class="fas fa-backspace"></i></button>
          </div>
        </div>

        <!-- Removed footer section -->
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, defineEmits } from 'vue';

// 이벤트 설정
const emit = defineEmits(['close', 'password-verified']);

const showModal = ref(true);
const password = ref(''); // 비밀번호는 최대 6자리
const correctPassword = '111111'; // 실제 비밀번호로 교체

const closeModal = () => {
  showModal.value = false;
  emit('close');
};

const handleOverlayClick = () => {
  closeModal();
};

// 숫자 입력 처리
const enterDigit = (num) => {
  if (password.value.length < 6) {
    password.value += num;

    // 비밀번호 길이가 6자리일 때 자동으로 확인
    if (password.value.length === 6) {
      verifyPassword();
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
  password.value = '';
};

// 비밀번호 검증
const verifyPassword = () => {
  if (password.value === correctPassword) {
    emit('password-verified'); // 비밀번호가 맞을 경우 password-verified 이벤트 emit
    closeModal();
  } else {
    alert('비밀번호가 틀렸습니다.');
    password.value = ''; // 입력값 초기화
  }
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
