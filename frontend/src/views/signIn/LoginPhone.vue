<script setup>
import { ref, computed, onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";

const body = document.getElementsByTagName("body")[0];
const store = useStore();
const router = useRouter();

onBeforeMount(() => {
  store.state.hideConfigButton = true;
  store.state.showNavbar = false;
  store.state.showSidenav = false;
  store.state.showFooter = false;
  body.classList.remove("bg-gray-100");
});
onBeforeUnmount(() => {
  store.state.hideConfigButton = false;
  store.state.showNavbar = true;
  store.state.showSidenav = true;
  store.state.showFooter = true;
  body.classList.add("bg-gray-100");
});

// 6자리 코드 입력 필드 상태
const digitCode = ref("");

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

// 폼 제출 처리
const handleSubmit = () => {
  if (isFormValid.value) {
    router.push("/home");
  } else {
    alert("Please enter a valid 6-digit code.");
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
                  <p class="mx-auto text-sm">
                    <router-link
                      to="/login/phone/another"
                      class="text-success text-gradient font-weight-bold"
                      >I didn't receive a code</router-link
                    >
                  </p>
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
