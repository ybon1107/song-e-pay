<script setup>
import { ref, computed, onBeforeUnmount, onBeforeMount } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import ArgonInput from '@/components/templates/ArgonInput.vue';
import ArgonButton from '@/components/templates/ArgonButton.vue';
import axios from 'axios';

const body = document.getElementsByTagName('body')[0];
const store = useStore();
const router = useRouter();

// 번호 입력 전/후 로직
onBeforeMount(() => {
  store.state.hideConfigButton = true;
  store.state.showNavbar = false;
  store.state.showSidenav = false;
  store.state.showFooter = false;
  body.classList.remove('bg-gray-100');
});
onBeforeUnmount(() => {
  store.state.hideConfigButton = false;
  store.state.showNavbar = true;
  store.state.showSidenav = true;
  store.state.showFooter = true;
  body.classList.add('bg-gray-100');
});

// 상태 변수
const digitCode = ref('');
const phoneNumber = ref('01022116854'); // 사용자 전화번호
const message = ref('');
const isSending = ref(false);
const errorMessage = ref('');

// 코드 입력 필드 길이 제한
const limitInputLength = () => {
  if (digitCode.value.length > 6) {
    digitCode.value = digitCode.value.slice(0, 6);
  }
};

// 코드 입력 필드 유효성 검사
const isDigitCodeValid = computed(() => {
  return digitCode.value.length === 6 && /^\d+$/.test(digitCode.value);
});

// 폼 유효성 검사
const isFormValid = computed(() => {
  return isDigitCodeValid.value;
});

// 인증번호 전송 함수
const sendVerificationCode = async () => {
  try {
    isSending.value = true;
    const response = await axios.post(
      '/api/users/send-verification-code',
      null,
      {
        params: { phoneNo: phoneNumber.value }, // params를 사용해 쿼리스트링 형식으로 전송
      }
    );
    alert('Verification code sent to ' + phoneNumber.value);
    isSending.value = false;
  } catch (error) {
    console.error('Failed to send verification code:', error);
    alert('Failed to send verification code.');
    isSending.value = false;
  }
};

// 코드 검증 및 제출 처리
const handleSubmit = async () => {
  if (isFormValid.value) {
    try {
      isSending.value = true;
      // 요청 시 URL 쿼리스트링 형식으로 데이터 전송
      const response = await axios.post('/api/users/verify-code', null, {
        params: {
          phoneNo: phoneNumber.value,
          code: digitCode.value,
        },
      });

      // 서버에서 인증 성공 시 홈으로 이동
      if (response.data === 'Verification successful. Proceed to next step.') {
        router.push('/home');
      } else {
        errorMessage.value = 'Invalid verification code. Please try again.';
      }
    } catch (error) {
      console.error('Verification failed:', error);
      errorMessage.value = 'Failed to verify code. Please try again later.';
    } finally {
      isSending.value = false;
    }
  } else {
    alert('Please enter a valid 6-digit code.');
  }
};
</script>

<template>
  <main class="main-content mt-0">
    <div class="page-header min-vh-100 pt-5 pb-11 m-3 border-radius-lg">
      <div class="container">
        <div class="row justify-content-center">
          <div
            class="mx-auto col-xl-4 col-lg-6 col-md-8 d-flex flex-column mx-lg-0"
          >
            <div class="card card-plain">
              <!-- 카드 헤더: 제목 -->
              <div class="pb-0 card-header text-center">
                <img
                  src="@/assets/img/send-sms.png"
                  width="150px"
                  class="pb-5"
                  @click="sendVerificationCode"
                />

                <h4 class="font-weight-bolder">We just sent you an SMS</h4>
              </div>
              <!-- 카드 푸터  -->
              <div class="pt-0 text-center card-footer">
                <p class="mx-auto text-sm">
                  To log in, enter the security code we sent to
                  <strong>*********1234.</strong>
                  It will expire in 5 minutes.
                  <a
                    href="javascript:;"
                    class="text-success text-gradient font-weight-bold"
                    >Learn more</a
                  >
                </p>
              </div>
              <!-- 카드 본문 -->
              <div class="card-body">
                <form @submit.prevent="handleSubmit">
                  <!-- 6자리 코드 입력 필드 -->
                  <label for="digit-code" class="form-label"
                    >Your 6-digit code</label
                  >
                  <argon-input
                    id="digit-code"
                    type="number"
                    aria-label="6-digit code"
                    v-model="digitCode"
                    @input="limitInputLength"
                  />
                  <!-- 하단 인증코드 못 받았을 때는 제외해둠(추가해야함) -->
                  <!-- <p class="mx-auto text-sm">
                    <router-link
                      to="/login/phone/another"
                      class="text-success text-gradient font-weight-bold"
                      >I didn't receive a code</router-link
                    >
                  </p> -->
                  <!-- done 버튼 -->
                  <div class="text-center">
                    <argon-button
                      :disabled="!isFormValid"
                      fullWidth
                      color="success"
                      variant="gradient"
                      class="my-4 mb-2"
                      type="submit"
                      >Done</argon-button
                    >
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>
