<script setup>
import { ref, computed, onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import { useSignupStore } from "@/stores/signupStore";
import ArgonAlert from "@/components/templates/ArgonAlert.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";
import ArgonInput from "@/components/templates/ArgonInput.vue";

const body = document.getElementsByTagName("body")[0];
const store = useStore();
const signupStore = useSignupStore();

const email = computed(() => signupStore.email);

const isButtonEnabled = ref(false); // 버튼 활성화 상태
// const timer = ref(60); // 1분 카운트다운
const timer = ref(5); // 5초 카운트다운
let interval = null;

onBeforeMount(() => {
  store.state.hideConfigButton = true;
  store.state.showNavbar = false;
  store.state.showSidenav = false;
  store.state.showFooter = false;
  body.classList.remove("bg-gray-100");

  // 카운트다운 시작
  interval = setInterval(() => {
    if (timer.value > 0) {
      timer.value--;
    } else {
      clearInterval(interval);
      isButtonEnabled.value = true; // 버튼 활성화
    }
  }, 1000); // 1초마다 감소
});
onBeforeUnmount(() => {
  store.state.hideConfigButton = false;
  store.state.showNavbar = true;
  store.state.showSidenav = true;
  store.state.showFooter = true;
  body.classList.add("bg-gray-100");

  // 타이머 정리
  if (interval) {
    clearInterval(interval);
  }
});
</script>
<template>
  <!-- 메인 콘텐츠 섹션 -->
  <main class="mt-0 main-content">
    <!-- 전체 페이지 헤더 섹션 -->
    <section>
      <div class="page-header min-vh-100">
        <div class="container">
          <div class="row justify-content-center">
            <!-- 카드 컨테이너 -->
            <div
              class="mx-auto col-xl-6 col-lg-6 col-md-8 col-sm-10 d-flex flex-column mx-lg-0"
            >
              <div class="card card-plain">
                <!-- 카드 헤더: 제목 -->
                <div class="pb-0 card-header text-center">
                  <img src="@/assets/img/send-mail.png" width="250px" />
                  <h2 class="font-weight-bolder">CHECK YOUR EMAIL</h2>
                </div>
                <!-- 카드 본문 -->
                <div class="card-body">
                  <!-- 인증 메일 안내 메시지 -->
                  <argon-alert color="info" icon="ni ni-send">
                    <span
                      >Check the verification code in the email we sent to
                      <strong class="text-dark">{{ email }}</strong
                      >.<br />
                      The email can take up to
                      <strong class="text-dark">1 minute</strong> to
                      arrive.</span
                    >
                  </argon-alert>
                  <!-- 인증 코드 입력 -->
                  <div class="mt-4">
                    <label for="digitCode" class="form-label"
                      >Verification code</label
                    >
                    <argon-input
                      isRequired
                      id="digitCode"
                      type="text"
                    ></argon-input>
                  </div>
                  <!-- 인증 메일 재전송 버튼 -->
                  <div class="text-center">
                    <argon-button
                      :disabled="!isButtonEnabled"
                      fullWidth
                      color="success"
                      variant="gradient"
                      class="my-4 mb-2"
                      >Resend email
                      <span v-if="!isButtonEnabled"
                        >{{ Math.floor(timer / 60) }}:{{
                          (timer % 60).toString().padStart(2, "0")
                        }}</span
                      >
                    </argon-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>
