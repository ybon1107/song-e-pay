<script setup>
import { onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
// import Navbar from "@/components/Navbars/Navbar.vue";
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

// 로그인 페이지 이동 버튼 클릭 핸들러
const loginHandler = () => {
  router.push("/login");
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
            <!-- 카드 컨테이너 -->
            <div
              class="mx-auto col-xl-4 col-lg-6 col-md-8 col-sm-10 d-flex flex-column mx-lg-0"
            >
              <div class="card card-plain">
                <!-- 카드 헤더: 제목 -->
                <div class="pb-0 card-header text-center">
                  <img src="@/assets/img/check.png" width="150px" />
                  <h2 class="font-weight-bolder">
                    {{ $t("signUp--Title-success") }}
                  </h2>
                </div>
                <!-- 카드 푸터 -->
                <div class="pt-0 text-center card-footer">
                  <p class="mx-auto text-sm">
                    {{ $t("signUp--successText1") }}<br />
                    {{ $t("signUp--successText2") }}
                  </p>
                </div>
                <!-- 카드 본문-->
                <div class="pt-0 card-body">
                  <div class="text-center">
                    <argon-button
                      fullWidth
                      color="success"
                      variant="gradient"
                      class="my-4 mb-2"
                      @click="loginHandler"
                      >{{ $t("common--text-login") }}
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
