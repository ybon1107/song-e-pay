<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import settingApi from "../../api/settingApi";

import Swal from "sweetalert2";

//i18n
import { useI18n } from 'vue-i18n';
const { t } = useI18n();

//user
import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
// const user = computed(() => auth.user);
const userId = computed(() => auth.user.userId);

const router = useRouter();

// 상태 관리
const password = ref(["", "", "", "", "", ""]);
const inputRefs = ref([]);
const errorMessage = ref("");
const isLoading = ref(false);

// 비밀번호가 모두 입력되었는지 확인
const isPasswordComplete = computed(() => {
  return password.value.every((digit) => digit !== "");
});

// 입력 이벤트 처리
const onInput = (index) => {
  if (password.value[index] && index < 5) {
    focusNextInput(index);
  }
};

// 백스페이스 처리
const onKeyDown = (event, index) => {
  if (event.key === "Backspace" && !password.value[index] && index > 0) {
    focusPreviousInput(index);
  }
};

// 다음 입력 필드로 포커스 이동
const focusNextInput = (index) => {
  if (index < 5) {
    inputRefs.value[index + 1].focus();
  }
};

// 이전 입력 필드로 포커스 이동
const focusPreviousInput = (index) => {
  if (index > 0) {
    inputRefs.value[index - 1].focus();
  }
};

// 비밀번호 제출
const onSubmit = async () => {
  if (isPasswordComplete.value) {
    const passwordString = password.value.join("");
    isLoading.value = true;

    try {
      const formData = {
        userId: userId.value,
        secPwd: passwordString
      };
      // 비밀번호 제출
      const response = await settingApi.submitSecPwd(formData);
      if (response.data === true) {
        console.log(response);
        router.push("/payment/qr");
      } else {
        Swal.fire({
          title: t("payment--swal-password-wrong-title"),
          text: t("payment--swal-password-wrong-text"),
          icon: "error",
        });
        resetPassword(); // 비밀번호 초기화 함수 호출
        // throw new Error();
      }
    } catch (error) {
      // 에러 처리
      console.error("Error submitting password:", error.response.data);
      errorMessage.value = "비밀번호 제출 중 오류가 발생했습니다.";
      resetPassword(); // 오류 발생 시에도 비밀번호 초기화
    } finally {
      isLoading.value = false;
    }
  }
};

const resetPassword = () => {
  password.value = ["", "", "", "", "", ""];
  if (inputRefs.value[0]) {
    inputRefs.value[0].focus(); // 첫 번째 입력 필드로 포커스 이동
  }
};
</script>

<template>
  <div class="password-input-page">
    <h1 class="title">{{ $t('payment--passwordInput--title') }}</h1>
    <p class="instruction">{{ $t('payment--passwordInput--instruction') }}</p>

    <form @submit.prevent="onSubmit" class="text-center">
      <div class="password-input-container">
        <input v-for="(digit, index) in password" :key="index" :ref="(el) => (inputRefs[index] = el)"
          v-model="password[index]" type="tel" maxlength="1" @input="onInput(index)" @keydown="onKeyDown($event, index)"
          class="password-digit" />
      </div>
      <button type="submit" :disabled="!isPasswordComplete" class="btn btn-primary">
        {{ $t('common-btn--submit') }}
      </button>
    </form>
  </div>
</template>

<style scoped>
.password-input-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 80vh;
  padding: 20px;
  background-color: #f5f5f5;
}

.title {
  font-size: 24px;
  margin-bottom: 10px;
}

.instruction {
  font-size: 16px;
  margin-bottom: 20px;
  color: #666;
}

.password-input-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.password-digit {
  width: 40px;
  height: 50px;
  margin: 0 5px;
  text-align: center;
  font-size: 24px;
  border: 1px solid #ccc;
  border-radius: 5px;
  outline: none;
}

.password-digit:focus {
  border-color: #007bff;
}

/*
.submit-button {
    padding: 10px 20px;
    font-size: 18px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

.submit-button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}
*/
</style>
