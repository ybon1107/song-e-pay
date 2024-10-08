<script setup>
import { ref, computed, onBeforeUnmount, onBeforeMount, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import axios from "axios";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonSwitch from "@/components/templates/ArgonSwitch.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";

const body = document.getElementsByTagName("body")[0];
const store = useStore();
const router = useRouter();
const auth = useAuthStore();

const member = ref({
<<<<<<< HEAD
    username: "",
    password: "",
  },);
=======
  username: "",
  password: "",
});
>>>>>>> LBY

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

  // if (isFormValid.value) {
  //   try {
  //     const response = await axios.post("http://localhost:8080/login", {
  //       email: email.value,
  //       password: password.value,
  //     });

  //     if (response.data.success) {
  //       router.push("/login/phone");
  //     } else {
  //       // 로그인 실패 처리
  //       alert("Invalid email or password");
  //     }
  //   } catch (error) {
  //     console.error("Login error:", error);
  //     alert("An error occurred during login. Please try again.");
  //   }
  // }
  if (isFormValid.value) {
    try {
      await auth.login(member);
      if (localStorage.getItem("auth") != " ") {
        window.location.href = "/my-accounts";
      }
    } catch (e) {
      // 로그인 에러
      console.log("에러=======", e);
      error.value = e.response.data;
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
                  <h4 class="font-weight-bolder">Welcome back!</h4>
                </div>
                <!-- 카드 푸터: 회원가입 링크 -->
                <div class="pt-0 text-center card-footer">
                  <p class="mx-auto text-sm">
                    Don't have Song-E Pay account?
                    <router-link
                      to="/register/legal"
                      class="text-success text-gradient font-weight-bold"
                      >Sign up</router-link
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
                    <!-- <form action="http://localhost:8080/login" method="post"> -->
                    <!-- 이메일 입력 필드 -->
                    <div class="mb-3">
                      <label key="email" for="email" class="form-label"
                        >Your email address</label
                      >
                      <argon-input
                        isRequired
                        id="email"
                        type="email"
                        placeholder="Email"
                        name="username"
                        size="lg"
                        v-model="username"
                        :class="{ 'is-invalid': emailError }"
<<<<<<< HEAD
                        :error="(username !== '' || emailError) && !isEmailValid"
=======
                        :error="
                          (username !== '' || emailError) && !isEmailValid
                        "
>>>>>>> LBY
                        errorText="Please provide a valid email."
                      />
                      <!-- <div v-if="emailError" class="invalid-feedback text-xs">
                        Please provide a valid email.
                      </div> -->
                    </div>
                    <!-- 비밀번호 입력 필드 -->
                    <div class="mb-3">
                      <label key="password" for="password" class="form-label"
                        >Your password</label
                      >
                      <argon-input
                        isRequired
                        id="password"
                        type="password"
                        placeholder="Password"
                        name="password"
                        size="lg"
                        v-model="password"
                        :class="{ 'is-invalid': passwordError }"
                        :error="
                          (password !== '' || passwordError) && !isPasswordValid
                        "
                        errorText="Please fill in the password field with at least 8 characters."
                      />
                      <!-- <div
                        v-if="passwordError"
                        class="invalid-feedback text-xs"
                      >
                        Please fill in the password field with at least 8
                        characters.
                      </div> -->
                    </div>
                    <!-- "Remember me" 토글 스위치 (일단 주석 처리 해둠) -->
                    <!-- <argon-switch id="rememberMe" name="remember-me"
                      >Remember me</argon-switch
                    > -->
                    <!-- 로그인 버튼 -->
                    <div v-if="error" class="text-xs">{{ error }}</div>
                    <div class="text-center">
                      <argon-button
                        class="mt-4"
                        variant="gradient"
                        color="success"
                        fullWidth
                        size="lg"
                        type="submit"
                        >Sign in</argon-button
                      >
                    </div>
                  </form>
                  <!-- 로그인 문제 발생시 안내 링크 -->
                  <p class="mx-auto text-sm pt-3">
                    <router-link
                      to="/login/issue-info"
                      class="text-success text-gradient font-weight-bold"
                      >Trouble logging in?</router-link
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
