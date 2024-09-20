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
            <span v-for="(digit, index) in 6" :key="index" :class="{ dot: true, filled: password.length >= index + 1 }"></span>
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
/* 모달 오버레이 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

/* 모달 컨텐츠 */
.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px; /* 기본 크기 */
  max-width: 80%; /* 화면에 맞게 줄어들도록 설정 */
  text-align: center;
  z-index: 1001;
}

/* 모달 헤더 */
.modal-header h3 {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

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

/* 반응형 스타일 */
@media (max-width: 768px) {
  .modal-content {
    width: 90%; /* 작은 화면에서 너비를 90%로 설정 */
  }

  .keypad button {
    width: 50px; /* 버튼 크기 조정 */
    height: 50px; /* 버튼 크기 조정 */
    font-size: 20px; /* 폰트 크기 조정 */
  }
}

@media (max-width: 480px) {
  .modal-content {
    width: 95%; /* 더욱 작은 화면에서 너비를 95%로 설정 */
  }

  .modal-header h3 {
    font-size: 20px; /* 제목 크기 조정 */
  }

  .keypad button {
    width: 40px; /* 버튼 크기 조정 */
    height: 40px; /* 버튼 크기 조정 */
    font-size: 18px; /* 폰트 크기 조정 */
  }
}
</style>
