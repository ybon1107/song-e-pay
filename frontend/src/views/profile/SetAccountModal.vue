<!-- SetAccountModal.vue -->
<script setup>
import { ref,reactive  } from 'vue';
import Modal from '../../components/modal/Modal.vue';
import ArgonInput from "@/components/templates/ArgonInput.vue";
import settingApi from '../../api/settingApi';

const emit = defineEmits(['account-changed', 'close']);
const isModalVisible = ref(true);

//user
import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const userId = computed(() => auth.user.userId);

const formData = reactive({
  accountNo:'',
  accountPwd:'',     
  userId:''
});

const closeModal = () => {
  isModalVisible.value = false;
  // 부모 컴포넌트로 close 이벤트 전송
  emit('close');
};

// const handleConfirm = async(userId) => {
//   console.log('계정 확인 완료');
//   // 계정이 확인되었을 때 account-changed 이벤트를 부모 컴포넌트로 전송
//   emit('account-changed');
//   closeModal();
// };
// // 비동기 API 요청 함수
// const submitAccount = async (formData) => {
//   try {
//     const response = await api.post(`${BASE_URL}/register-account`, formData);
//     return response;
//   } catch (error) {
//     console.error('2차 비밀번호 제출 오류:', error.response.data);
//     throw error;
//   }
// };

// 계정 확인 버튼 클릭 시 실행
const handleConfirm = async () => {
  console.log('계정 확인 완료');
  
  // 입력한 폼 데이터 출력 (테스트 용)
  console.log('폼 데이터:', formData);

  try {
    // 폼 데이터를 제출
    // const response = await settingApi.submitAccount(formData);
    // console.log('응답:', response);
    emit('account-changed');
    closeModal();
  } catch (error) {
    console.error('계정 제출 실패:', error);
  }
};
</script>

<template>
  <Modal :isVisible="isModalVisible" title="Please enter your account information" cancelText="close" confirmText="submit"
    @close="closeModal" @confirm="handleConfirm">
    <form>
      <label for="account-no" class="form-label">Account Number</label>
    <argon-input isRequired id="account-no" type="text" placeholder="Enter your account number" />
    <label for="account-pwd" class="form-label">Secret Number</label>
    <argon-input type="text" id="ccount-pwd" placeholder="Enter your account secret number" />
    </form>
    
  </Modal>
</template>
