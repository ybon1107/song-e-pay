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

// 이메일 입력 필드 상태
const email = ref("");

// 이메일 유효성 검사
const isEmailValid = computed(() => {
  // 이메일 주소 유효성 검사 정규식
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email.value);
});

// 다음 버튼 클릭 핸들러
const handleNext = () => {
  if (isEmailValid.value) {
    router.push("/register/email/check");
  } else {
    console.log("Invalid email address.");
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
                <h4 class="font-weight-bolder">
                  Create your Song-E Pay account
                </h4>
              </div>
              <!-- 카드 푸터: 로그인 링크 -->
              <div class="pt-0 text-center card-footer">
                <p class="mx-auto text-sm">
                  Already have an account?
                  <a
                    href="/login"
                    class="text-success text-gradient font-weight-bold"
                    >Sign in</a
                  >
                </p>
              </div>
              <!-- 카드 본문 -->
              <div class="card-body">
                <form role="form">
                  <!-- 이메일 입력 필드 -->
                  <label for="email" class="form-label"
                    >First, enter your email address</label
                  >
                  <argon-input
                    id="email"
                    type="email"
                    placeholder="Email"
                    aria-label="Email"
                    v-model="email"
                  />
                  <!-- 다음 버튼 -->
                  <div class="text-center">
                    <argon-button
                      :disabled="!isEmailValid"
                      fullWidth
                      color="success"
                      variant="gradient"
                      class="my-4 mb-2"
                      @click="handleNext"
                      >Next</argon-button
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
