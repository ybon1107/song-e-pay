<script setup>
import { ref, onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import ArgonButton from "@/components/templates/ArgonButton.vue";
import Accordion from "@/components/signIn/Accordion.vue";

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

const accordionItems = ref([
  {
    title: "I've forgotten my password",
    content:
      "If you've forgotten your password, you can reset it by clicking the 'Forgot password?' link on the login page.",
    expanded: false,
  },
  {
    title: "2-step verification isn't working",
    content:
      "If you can't remember the email address you used to sign up, please contact our support team.",
    expanded: false,
  },
  {
    title: "I need to change my phone number",
    content:
      "If you're not receiving the SMS code, please check that you've entered the correct phone number. If you're still having trouble, please contact our support team.",
    expanded: false,
  },
  {
    title: "I need to change my email",
    content:
      "If you're not receiving the email code, please check your spam folder. If you're still having trouble, please contact our support team.",
    expanded: false,
  },
  {
    title: "Logging in with a new device",
    content:
      "If you're logging in from a new device, you may be asked to verify your identity. Please check your email or SMS for a verification code. If you don't receive a code, please contact our support team.",
    expanded: false,
  },
  {
    title: "My account was deactivated",
    content:
      "If your account was deactivated, please contact our support team for assistance.",
    expanded: false,
  },
]);

const goBack = () => {
  router.back();
};
</script>
<template>
  <!-- 메인 콘텐츠 섹션 -->
  <main class="mt-0 main-content">
    <!-- 전체 페이지 헤더 섹션 -->
    <section>
      <div class="page-header min-vh-100">
        <div class="container">
          <div class="row justify-content-center">
            <!-- 로그인 폼을 담고 있는 카드 컨테이너 -->
            <div
              class="mx-auto col-xl-4 col-lg-5 col-md-7 col-sm-9 d-flex flex-column mx-lg-0"
            >
              <div class="card card-plain">
                <!-- 카드 헤더: 제목 -->
                <div class="card-header text-center">
                  <h4 class="font-weight-bolder">Trouble Logging in</h4>
                </div>
                <!-- 카드 본문 -->
                <div class="card-body">
                  <div class="px-3">
                    <strong>Select an issue</strong>
                    <hr />
                  </div>
                  <!-- 아코디언 컴포넌트 -->
                  <Accordion :items="accordionItems" />
                </div>
                <!-- 뒤로 가기 버튼 -->
                <div class="text-center">
                  <argon-button color="secondary" @click="goBack">
                    Go Back
                  </argon-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>
