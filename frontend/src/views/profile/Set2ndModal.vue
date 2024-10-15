<template>
    <div class="modal-body">
      <Modal :isVisible="showModal" :showFooter="false" :title="$t('myAccount--SecondPassword--modal-header')"
        @close="closeModal" v-if="showPasswordInput">
        <p class="warning-text">{{ $t('myAccount--SecondPassword--modal-header-title') }}</p>
        <h3 class="password-title">{{ $t('Enter a new secondary password') }}</h3>
        <!-- 비밀번호 도트 -->
        <div class="password-dots">
          <span v-for="(digit, index) in 6" :key="index"
            :class="{ dot: true, filled: password.length >= index + 1 }"></span>
        </div>
  
              <!-- 숫자 키패드 -->
              <div class="keypad">
                  <button
                      v-for="num in shuffledNumbers"
                      :key="num"
                      @click="enterDigit(num)"
                  >
                      {{ num }}
                  </button>
                  <button @click="clearAll" class="function-key">C</button>
                  <button @click="clearLast" class="function-key">
                      <i class="fas fa-backspace"></i>
                  </button>
              </div>
          </Modal>
      </div>
  </template>
  
  <script setup>
  import { ref, defineEmits, onMounted, computed } from 'vue';
  import settingApi from '../../api/settingApi';
  import Modal from '../../components/modal/Modal.vue';
  import Swal from 'sweetalert2';
  
  //user
  import { useAuthStore } from '@/stores/auth';
  const auth = useAuthStore();
  const userId = computed(() => auth.user.userId);
  
  //i18n
  import { useI18n } from "vue-i18n";
  const { t } = useI18n();
  
  // 이벤트 설정
  const emit = defineEmits(['close', 'password-changed']);
  
  const showModal = ref(true);
  const password = ref(''); // 비밀번호는 최대 6자리
  const showPasswordInput = ref(true);
  
  const shuffledNumbers = ref([]);
  
  onMounted(() => {
      shuffleNumbers();
  });
  
  const shuffleNumbers = () => {
      const numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 0];
      for (let i = numbers.length - 1; i > 0; i--) {
          const j = Math.floor(Math.random() * (i + 1));
          [numbers[i], numbers[j]] = [numbers[j], numbers[i]];
      }
      shuffledNumbers.value = numbers;
  };
  
  // 숫자 입력 처리
  const enterDigit = (num) => {
      if (password.value.length < 6) {
          password.value += num;
  
          // 비밀번호 길이가 6자리일 때 자동으로 확인
          if (password.value.length === 6) {
              verifyPassword(userId.value);
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
  
  // 비밀번호 검증 함수 수정
  const verifyPassword = async (userId) => {
      const formData = {
          userId,
          secPwd: password.value,
      };
  
    try {
      const response = await settingApi.changeSecPwd(formData);
      if (response.status === 200) {
        closeModal();
        emit('password-changed');
        
        
        
        
      } else {
        showPasswordInput.value = false;
        Swal.fire({
          title: t('myAccount--SecondPassword--modal-enterAgain'),
          text: t('myAccount--SecondPassword--modal-wrongPassword'),
          icon: 'error',
        }).then(() => {
          password.value = '';
          showPasswordInput.value = true;
        });
        throw new Error();
      }
    } catch (error) {
      console.error('비밀번호 검증 중 오류 발생:', error);
    }
  };
  
  const closeModal = () => {
      showModal.value = false;
      emit('close');
  };
  </script>
  
  <style scope>
  /* 모달 전체 크기 조정 */
  .compact-modal {
      max-height: 90vh;
      overflow-y: auto;
  }
  
  /* 비밀번호 도트 */
  .password-dots {
      display: flex;
      justify-content: center;
      align-items: center;
      margin: 40px 0;
  }
  
  .dot {
      width: 20px;
      height: 20px;
      margin: 0 10px;
      border: 2px solid #e0e0e0;
      border-radius: 50%;
      background-color: transparent;
      transition: all 0.3s ease;
  }
  
  .dot.filled {
      background-color: #4caf50;
      border-color: #4caf50;
      box-shadow: 0 0 10px rgba(76, 175, 80, 0.5);
      transform: scale(1.15);
  }
  
  /* 키패드 스타일 */
  .keypad {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 1rem;
      margin: 1rem auto;
      padding: 1rem;
      background-color: #f8f9fa;
      border-radius: 12px;
      width: 90%;
  }
  
  .keypad button {
    width: 70px;
    height: 70px;
    font-size: 24px;
    font-weight: bold;
    background-color: white;
    border: 1px solid #e0e0e0;
    border-radius: 50%;
    cursor: pointer;
    transition: all 0.2s ease;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 auto;
    /* 버튼을 그리드 셀 내에서 가운데 정렬 */
  }
  
  .keypad button:hover {
      background-color: #f0f0f0;
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  }
  
  .keypad button:active {
      transform: translateY(0);
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }
  
  .function-key {
      background-color: #e9ecef !important;
      color: #495057;
  }
  
  .warning-text {
    margin-bottom: 5px;
    /* 경고 텍스트 아래 간격 줄임 */
    color: #ff6b6b;
    /* 경고 텍스트 색상 변경 (선택사항) */
  }
  
  .password-title {
    margin-top: 0;
    /* 제목 위 간격 제거 */
    margin-bottom: 20px;
    /* 제목 아래 간격 추가 */
  }
  </style>
  