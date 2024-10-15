<script setup>
import { ref, reactive, computed, toRefs } from 'vue';
import Modal from '../../components/modal/Modal.vue';
import ArgonInput from "@/components/templates/ArgonInput.vue";
import settingApi from '../../api/settingApi';
import Swal from 'sweetalert2';

//i18n
import { useI18n } from "vue-i18n";
const { t } = useI18n();

const emit = defineEmits(['account-changed', 'close']);
const isModalVisible = ref(true);

// props로 userId를 받음
const props = defineProps({
  userId: String, // 전달된 userId 값을 받을 수 있도록 설정
});

const formData = reactive({
  accountNo: '',
  accountPwd: '',
  userId: props.userId, // 받은 userId를 formData에 저장
});

const closeModal = () => {
  isModalVisible.value = false;
  emit('close');
};

// 계정 확인 버튼 클릭 시 실행
const handleConfirm = async () => {
  console.log('폼 데이터:', formData);

  try {
    // 폼 데이터를 제출
    const response = await settingApi.submitAccount(formData);
    // console.log('응답:', response);
    if (response.data === 0) {
      closeModal();
      Swal.fire({
        title: t('swal--title-fail'),
        text: t('Invalid password.'),
        icon: 'error',
      }).then(() => {
        formData.accountNo = '';
        formData.accountPwd = '';
      });
    } else {
      closeModal();
      emit('account-changed');

    }

  } catch (error) {
    console.error('계정 제출 실패:', error);
  }
};
</script>

<template>
  <Modal :isVisible="isModalVisible" title="Please enter your account information" cancelText="close"
    confirmText="submit" @close="closeModal" @confirm="handleConfirm">
    <form>
      <label for="account-no" class="form-label">Account Number</label>
      <argon-input v-model="formData.accountNo" isRequired id="account-no" type="text"
        placeholder="Enter your account number" />
      <label for="account-pwd" class="form-label">Secret Number</label>
      <argon-input v-model="formData.accountPwd" type="password" id="ccount-pwd"
        placeholder="Enter your account secret number" />
    </form>
  </Modal>
</template>
