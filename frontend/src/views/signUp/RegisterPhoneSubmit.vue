<script setup>
import { ref, computed, onBeforeUnmount, onBeforeMount, onMounted } from "vue";
import { useStore } from "vuex";
import { useSignupStore } from "@/stores/signupStore";
import { useRouter } from "vue-router";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";

const body = document.getElementsByTagName("body")[0];
const store = useStore();
const signupStore = useSignupStore();
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

const countryCallingCode = computed(() => signupStore.countryCallingCode);
const phoneNumber = computed(() => signupStore.phoneNumber);

// 인증 코드 유효성 검사
const verificationCode = ref("");

const isCodeValid = computed(() => {
  return /^\d{6}$/.test(verificationCode.value);
});

// 인증 코드 입력 제한 함수
const handleInput = (event) => {
  if (event.target.value.length > 6) {
    event.target.value = event.target.value.slice(0, 6);
  }
  verificationCode.value = event.target.value;
};

// submit 버튼 클릭 핸들러
const handleSubmit = () => {
  if (isCodeValid.value) {
    router.push("/register/detail");
  } else {
    console.log("Invalid verification code.");
  }
};
</script>
<template>
  <main class="main-content mt-0">
    <div class="page-header min-vh-100 pt-5 pb-11 m-3 border-radius-lg">
      <div class="container">
        <div class="row justify-content-center">
          <div
            class="mx-auto col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-lg-0"
          >
            <div class="card card-plain">
              <!-- 카드 헤더: 제목 -->
              <div class="pb-0 card-header text-center">
                <h4 class="font-weight-bolder">Enter the 6-digit code</h4>
              </div>
              <!-- 카드 푸터: -->
              <div class="pt-0 text-center card-footer">
                <p class="mx-auto text-sm">
                  We sent to {{ countryCallingCode }} {{ phoneNumber }}
                  <a
                    href="/register/phone"
                    class="text-success text-gradient font-weight-bold"
                    >Change phone number</a
                  >
                </p>
              </div>
              <!-- 카드 본문 -->
              <div class="card-body">
                <form role="form">
                  <!-- 인증 코드 입력 필드 -->
                  <label for="verificationCode" class="form-label"
                    >Your 6-digit code</label
                  >
                  <argon-input
                    id="verificationCode"
                    type="text"
                    placeholder=""
                    aria-label="verificationCode"
                    v-model="verificationCode"
                    @input="handleInput"
                  />
                  <!-- 인증 코드 재전송 링크 -->
                  <p class="text-sm">
                    <a
                      href="/register/phone/another"
                      class="text-success text-gradient font-weight-bold"
                      >I didn't receive a code</a
                    >
                  </p>
                  <!-- 다음 버튼 -->
                  <div class="text-center">
                    <argon-button
                      :disabled="!isCodeValid"
                      fullWidth
                      color="success"
                      variant="gradient"
                      class="my-4 mb-2"
                      @click="handleSubmit"
                      >Submit</argon-button
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
