<script setup>
import { onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import Navbar from "@/components/Navbars/Navbar.vue";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonSwitch from "@/components/templates/ArgonSwitch.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";
const body = document.getElementsByTagName("body")[0];

const store = useStore();
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
              class="mx-auto col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-lg-0"
            >
              <div class="card card-plain">
                <!-- 카드 헤더: 제목 -->
                <div class="pb-0 card-header text-center">
                  <h4 class="font-weight-bolder">Welcome back!</h4>
                </div>
                <!-- 카드 푸터: 회원가입 링크 -->
                <div class="pt-0 text-center card-footer">
                  <p class="mx-auto text-sm">
                    Don't have Song-E Pay account?
                    <a
                      href="/register/legal"
                      class="text-success text-gradient font-weight-bold"
                      >Sign up</a
                    >
                  </p>
                </div>
                <!-- 카드 본문: 로그인 폼 -->
                <div class="card-body">
                  <form role="form">
                    <!-- 이메일 입력 필드 -->
                    <div class="mb-3">
                      <label key="email" for="email" class="form-label"
                        >Your email address</label
                      >
                      <argon-input
                        id="email"
                        type="email"
                        placeholder="Email"
                        name="email"
                        size="lg"
                      />
                    </div>
                    <!-- 비밀번호 입력 필드 -->
                    <div class="mb-3">
                      <label key="password" for="password" class="form-label"
                        >Your password</label
                      >
                      <argon-input
                        id="password"
                        type="password"
                        placeholder="Password"
                        name="password"
                        size="lg"
                      />
                    </div>
                    <!-- "Remember me" 토글 스위치 (일단 주석 처리 해둠) -->
                    <!-- <argon-switch id="rememberMe" name="remember-me"
                      >Remember me</argon-switch
                    > -->
                    <!-- 로그인 버튼 -->
                    <div class="text-center">
                      <argon-button
                        class="mt-4"
                        variant="gradient"
                        color="success"
                        fullWidth
                        size="lg"
                        >Sign in</argon-button
                      >
                    </div>
                  </form>
                  <!-- 로그인 문제 발생시 안내 링크 -->
                  <p class="mx-auto text-sm pt-3">
                    <a
                      href="javascript:;"
                      class="text-success text-gradient font-weight-bold"
                      >Trouble logging in?</a
                    >
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>
