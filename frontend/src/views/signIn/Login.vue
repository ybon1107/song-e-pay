<script setup>
import { ref, computed, onBeforeUnmount, onBeforeMount, onMounted } from "vue";
import { useStore } from "vuex";
import { useAuthStore } from "@/stores/auth";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonSwitch from "@/components/templates/ArgonSwitch.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";
import { useI18n } from "vue-i18n";
import Swal from "sweetalert2";

const { t } = useI18n();

const body = document.getElementsByTagName("body")[0];
const store = useStore();
const auth = useAuthStore();

const member = ref({
  username: "",
  password: "",
});

const error = ref("");

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
// 부트스트랩 유효성 검사 스크립트
onMounted(() => {
  const forms = document.querySelectorAll(".needs-validation");
  Array.prototype.slice.call(forms).forEach(function (form) {
    form.addEventListener(
      "submit",
      function (event) {
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
          form.classList.remove("was-validated");
        } else {
          form.classList.remove("was-validated");
        }
      },
      false
    );
  });
});

// 이메일과 비밀번호 입력 필드 상태
const username = ref("");
const password = ref("");

// 이메일 유효성 검사
const isEmailValid = computed(() => {
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailPattern.test(username.value);
});

// 비밀번호 유효성 검사
const isPasswordValid = computed(() => {
  return password.value.length >= 8;
});

// 에러 상태
const emailError = ref(false);
const passwordError = ref(false);

// 폼 유효성 검사
const isFormValid = computed(() => {
  return isEmailValid.value && isPasswordValid.value;
});

// 폼 제출 처리
const handleSubmit = async () => {
  // member 객체에 이메일과 비밀번호 할당
  member.value.username = username.value;
  member.value.password = password.value;

  console.log("try login: ", member);
  emailError.value = !isEmailValid.value;
  passwordError.value = !isPasswordValid.value;

  if (isFormValid.value) {
    try {
      await auth.login(member);

      const loginState = JSON.parse(localStorage.getItem("auth"));
      const token = loginState ? loginState.token : null;
      if (token) {
        console.log("Token after login: ", token);
        window.location.href = "/my-page";
      }
    } catch (e) {
      Swal.fire({
        title: t("signIn--alertTitle-loginError"),
        text: t("signIn--alertText-loginError"),
        icon: "error",
        confirmButtonText: "Close",
      });
      // 로그인 에러
      console.log("에러=======", e);
    }
  }
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
                <div class="pb-0 card-header text-center">
                  <h4 class="font-weight-bolder">
                    {{ $t("signIn--header-welcome") }}
                  </h4>
                </div>
                <!-- 카드 푸터: 회원가입 링크 -->
                <div class="pt-0 text-center card-footer">
                  <p class="mx-auto text-sm">
                    {{ $t("signIn--footer-signUpLink") }}
                    <router-link
                      to="/register/legal"
                      class="text-success text-gradient font-weight-bold"
                      >{{ $t("signIn--footer-signUp") }}</router-link
                    >
                  </p>
                </div>
                <!-- 카드 본문: 로그인 폼 -->
                <div class="card-body">
                  <form
                    @submit.prevent="handleSubmit"
                    class="needs-validation"
                    novalidate
                  >
                    <!-- 이메일 입력 필드 -->
                    <div class="mb-3">
                      <label key="email" for="email" class="form-label">{{
                        $t("signIn--form-emailLabel")
                      }}</label>
                      <argon-input
                        isRequired
                        id="email"
                        type="email"
                        :placeholder="$t('signIn--form-emailPlaceholder')"
                        name="username"
                        size="lg"
                        v-model="username"
                        :class="{
                          'is-invalid': emailError,
                        }"
                        :error="
                          (username !== '' || emailError) && !isEmailValid
                        "
                        :errorText="$t('signIn--errorText-email')"
                      />
                    </div>
                    <!-- 비밀번호 입력 필드 -->
                    <div class="mb-3">
                      <label key="password" for="password" class="form-label">{{
                        $t("signIn--form-passwordLabel")
                      }}</label>
                      <argon-input
                        isRequired
                        id="password"
                        type="password"
                        :placeholder="$t('signIn--form-passwordPlaceholder')"
                        name="password"
                        size="lg"
                        v-model="password"
                        :class="{
                          'is-invalid': passwordError,
                        }"
                        :error="
                          (password !== '' || passwordError) && !isPasswordValid
                        "
                        :errorText="$t('signIn--errorText-pw')"
                      />
                    </div>
                    <!-- "Remember me" 토글 스위치 (일단 주석 처리 해둠) -->
                    <!-- <argon-switch id="rememberMe" name="remember-me"
                      >Remember me</argon-switch
                    > -->
                    <!-- 로그인 버튼 -->
                    <div v-if="error" class="text-xs">
                      {{ error }}
                    </div>
                    <div class="text-center">
                      <argon-button
                        class="mt-4"
                        variant="gradient"
                        color="success"
                        fullWidth
                        size="lg"
                        type="submit"
                        >{{ $t("common--text-login") }}</argon-button
                      >
                    </div>
                  </form>
                  <!-- 로그인 문제 발생시 안내 링크 -->
                  <p class="mx-auto text-sm pt-3">
                    <router-link
                      to="/login/issue-info"
                      class="text-success text-gradient font-weight-bold"
                      >{{ $t("signIn--link-troubleLoggingIn") }}</router-link
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
