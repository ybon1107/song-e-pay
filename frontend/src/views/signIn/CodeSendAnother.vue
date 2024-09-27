<script setup>
import { ref, computed, onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
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

const goBack = () => {
  router.back();
};
</script>
<template>
  <!-- 메인 콘텐츠 섹션 -->
  <main class="mt-0 main-content">
    <!-- 전체 페이지 헤더 섹션 -->
    <section>
      <div class="page-header min-vh-70">
        <div class="container">
          <div class="row justify-content-center">
            <!-- 로그인 폼을 담고 있는 카드 컨테이너 -->
            <div
              class="mx-auto col-xl-4 col-lg-5 col-md-7 col-sm-9 d-flex flex-column mx-lg-0"
            >
              <div class="card card-plain">
                <!-- 카드 헤더: 제목 -->
                <div class="card-header text-center">
                  <h4 class="font-weight-bolder">Approve another way</h4>
                </div>
                <!-- 카드 본문 -->
                <div class="card-body">
                  <div
                    class="d-flex justify-content-between align-items-center pb-3"
                  >
                    <div class="ni ni-email-83 fs-5"></div>
                    <div class="fw-bold fs-5">Resend code by SMS</div>
                    <div class="ni ni-bold-right"></div>
                  </div>
                  <div
                    class="d-flex justify-content-between align-items-center py-3"
                  >
                    <div class="ni ni-headphones fs-5"></div>
                    <div class="fw-bold fs-5">Send Code by voice call</div>
                    <div class="ni ni-bold-right"></div>
                  </div>
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
