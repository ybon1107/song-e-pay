<script setup>
import { ref, computed, onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import axios from "axios";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";
import PhoneInput from "@/components/signUp/PhoneInput.vue";
import flatPickr from "vue-flatpickr-component";
import "flatpickr/dist/flatpickr.css";
import { parsePhoneNumberFromString } from "libphonenumber-js";
import { useI18n } from "vue-i18n";
import Swal from "sweetalert2";

const { t } = useI18n();

const body = document.getElementsByTagName("body")[0];
const store = useStore();
const router = useRouter();

// 에러 상태
const emailError = ref(false);
const emailCodeError = ref(false);
const passwordError = ref(false);
const confirmPasswordError = ref(false);
const firstNameError = ref(false);
const lastNameError = ref(false);
const birthError = ref(false);
const countryError = ref(false);
const genderError = ref(false);
const phoneNumberError = ref(false);

const TIMER_VALUE = 60;
const isButtonEnabled = ref(true); // 버튼 활성화 상태
const timer = ref(TIMER_VALUE); // 1분 카운트다운
let interval = null;

const startTimer = () => {
  if (email.value === "") {
    emailError.value = true;
    return;
  }
  emailError.value = false;
  isButtonEnabled.value = false; // 버튼 비활성화
  timer.value = TIMER_VALUE; // 타이머 초기화
  // 카운트다운 시작
  interval = setInterval(() => {
    if (timer.value > 0) {
      timer.value--;
    } else {
      clearInterval(interval);
      isButtonEnabled.value = true; // 버튼 활성화
    }
  }, 1000); // 1초마다 감소
};

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

  // 타이머 정리
  if (interval) {
    clearInterval(interval);
  }
});

// 이메일 입력 필드 상태
const email = ref("");

// 이메일 유효성 검사
const isEmailValid = computed(() => {
  // 이메일 주소 유효성 검사 정규식
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email.value);
});

// 이메일 코드 전송 함수
const sendEmailCode = async () => {
  // 전송 전 이메일 유효성 검사
  emailError.value = !isEmailValid.value;

  // 이메일이 유효하고 빈 문자열이 아닐 때
  if (email.value !== "" && isEmailValid.value) {
    try {
      const response = await axios.post(
        "/api/register/email",
        {
          userId: email.value,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      console.log("Response data:", response);

      if (response.data) {
        // 인증 코드 전송 성공
        console.log("Verification code sent successfully.");
        Swal.fire({
          title: t("signUp--alertTitle-codeSendSuccess"),
          // text: "",
          icon: "success",
          confirmButtonText: "OK",
        }).then(() => {
          startTimer(); // 이메일 코드 전송 성공 시 타이머 시작
        });
      } else {
        // 이미 등록된 이메일일 경우
        console.log("Already registered email. Please use another email.");
        Swal.fire({
          title: t("signUp--alertTitle-alreadyRegistered"),
          text: t("signUp--alertText-alreadyRegistered"),
          icon: "warning",
          confirmButtonText: "Close",
        });
      }
    } catch (error) {
      Swal.fire({
        title: t("signUp--alertTitle-codeSendFailed"),
        // text: "Please use another email.",
        icon: "error",
        confirmButtonText: "Close",
      });
      console.error("Email code sending error:", error);
      if (error.response) {
        // 서버가 응답했지만 상태 코드가 2xx 범위에 있지 않음
        console.error("Server responded with status:", error.response.status);
        console.error("Response data:", error.response.data);
      } else if (error.request) {
        // 요청이 만들어졌으나 응답을 받지 못함
        console.error("No response received:", error.request);
      } else {
        // 요청을 설정하는 중에 에러가 발생함
        console.error("Error setting up request:", error.message);
      }
    }
  } else {
    emailError.value = true;
  }
};

// 이메일 인증 코드 입력 필드 상태
const emailCode = ref("");

// 인증 코드 입력 숫자만 허용(최대 6자리)
const handleEmailCode = (event) => {
  emailCode.value = event.target.value.replace(/\D/g, "").slice(0, 6);
};

// 인증 코드 검증 상태
const isVerified = ref(false);

// 인증 코드 검증 함수
const verifyCode = async () => {
  if (emailCode.value !== "") {
    try {
      const response = await axios.post(
        "/api/register/check",
        {
          code: emailCode.value,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data) {
        // 인증 코드 검증 성공
        isVerified.value = true;
        console.log("Email verification successful.");
        Swal.fire({
          title: t("signUp--alertTitle-verifySuccess"),
          // text: "",
          icon: "success",
          confirmButtonText: "OK",
        });
      } else {
        // 인증 코드 검증 실패
        console.log("Invalid verification code. Please try again.");
        Swal.fire({
          title: t("signUp--alertTitle-verifyFailed"),
          text: t("signUp--alertText-tryAgain"),
          icon: "error",
          confirmButtonText: "Close",
        });
      }
    } catch (error) {
      console.error("Email code verification error:", error);
      if (error.response) {
        // 서버가 응답했지만 상태 코드가 2xx 범위에 있지 않음
        console.error("Server responded with status:", error.response.status);
        console.error("Response data:", error.response.data);
      } else if (error.request) {
        // 요청이 만들어졌으나 응답을 받지 못함
        console.error("No response received:", error.request);
      } else {
        // 요청을 설정하는 중에 에러가 발생함
        console.error("Error setting up request:", error.message);
      }
    }
  }
};

// 비밀번호 입력 필드 상태
const password = ref("");
const confirmPassword = ref("");

// 비밀번호 입력 시 공백 제거
const handlePasswordInput = (event) => {
  password.value = event.target.value.replace(/\s/g, "");
};

const handleConfirmPasswordInput = (event) => {
  confirmPassword.value = event.target.value.replace(/\s/g, "");
};

// 비밀번호 일치 여부 검사
const isPasswordMatch = computed(() => {
  return password.value.length >= 8 && password.value === confirmPassword.value;
});

// 거주 국가 상태
const country = ref(t("signUp--countryCodeLabel"));

// 성 및 이름 입력 필드 상태
const firstName = ref("");
const lastName = ref("");

// 생년월일 입력 필드 상태
const birth = ref("");

// 특정 년수를 뺀 날짜를 반환하는 함수
function calculateDateYearsAgo(years) {
  const date = new Date();
  date.setFullYear(date.getFullYear() - years);
  return date.toISOString().split("T")[0];
}

// 성별 상태
const gender = ref(t("signUp--genderLabel"));

// 전화번호와 국가 코드 상태
const countryCallingCode = ref("+1");
const phoneNumber = ref("");

// 모든 입력값 유효성 검사
const isFormValid = computed(() => {
  return (
    email.value !== "" &&
    isEmailValid.value &&
    emailCode.value !== "" &&
    password.value.length >= 8 &&
    confirmPassword.value.length >= 8 &&
    isPasswordMatch.value &&
    country.value !== "Country" &&
    firstName.value !== "" &&
    lastName.value !== "" &&
    birth.value !== "" &&
    gender.value !== "Gender" &&
    phoneNumber.value !== ""
  );
});

// 폼 제출 처리
const handleSubmit = async () => {
  emailError.value = !isEmailValid.value;
  emailCodeError.value = emailCode.value === "";
  passwordError.value = password.value.length < 8;
  confirmPasswordError.value =
    confirmPassword.value.length < 8 || !isPasswordMatch.value;
  countryError.value = country.value === "Country";
  firstNameError.value = !firstName.value;
  lastNameError.value = !lastName.value;
  birthError.value = !birth.value;
  genderError.value = gender.value === "Gender";
  phoneNumberError.value = phoneNumber.value === "";

  if (
    emailError.value ||
    emailCodeError.value ||
    passwordError.value ||
    confirmPasswordError.value ||
    countryError.value ||
    firstNameError.value ||
    lastNameError.value ||
    birthError.value ||
    genderError.value ||
    phoneNumberError.value
  ) {
    // 필수 입력값이 비어있을 경우
    console.log("Please fill out all required fields.");
    Swal.fire({
      title: t("signUp--alertTitle-submitFailed"),
      // text: "",
      icon: "error",
      confirmButtonText: "Close",
    });
    return;
  }

  if (isFormValid.value) {
    try {
      const phoneNumberE164 = parsePhoneNumberFromString(
        `${countryCallingCode.value}${phoneNumber.value}`
      ).format("E.164");

      const requestData = {
        userId: email.value,
        password: password.value,
        countryCode: country.value,
        firstName: firstName.value,
        lastName: lastName.value,
        birthday: birth.value,
        gender: gender.value,
        phoneNo: phoneNumberE164,
      };

      console.log(requestData);

      const response = await axios.post("/api/users/register", requestData, {
        headers: {
          "Content-Type": "application/json",
        },
      });

      console.log(response);

      console.log(response.data);

      if (response.data === "success") {
        router.push("/register/success");
      } else {
        // 등록 실패
        console.log("Registration failed. Please try again.");
        Swal.fire({
          title: t("signUp--alertTitle-registerFailed"),
          text: t("signUp--alertText-tryAgain"),
          icon: "error",
          confirmButtonText: "Close",
        });
      }
    } catch (error) {
      console.error("Registration error:", error);
      // 등록 중 에러 발생
      console.log("An error occurred during registration. Please try again.");
      Swal.fire({
        title: t("signUp--alertTitle-registerError"),
        text: t("signUp--alertText-tryAgain"),
        icon: "error",
        confirmButtonText: "Close",
      });
    }
  }
};
</script>
<template>
  <!-- 메인 콘텐츠 섹션 -->
  <main class="main-content mt-0">
    <!-- 페이지 헤더 섹션 -->
    <div class="page-header min-vh-100 pt-5 pb-11 m-3 border-radius-lg">
      <div class="container">
        <div class="row justify-content-center">
          <!-- 카드 컨테이너 -->
          <div
            class="mx-auto col-xl-5 col-lg-6 col-md-8 d-flex flex-column mx-lg-0"
          >
            <div class="card card-plain">
              <!-- 카드 헤더: 제목 -->
              <div class="pb-0 card-header text-center">
                <h4 class="font-weight-bolder">
                  {{ $t("signUp--Title-page") }}
                </h4>
              </div>
              <!-- 카드 본문 -->
              <div class="card-body container-fluid">
                <form @submit.prevent="handleSubmit">
                  <!-- 사용자 이메일 필드 -->
                  <div class="col-md-12">
                    <label for="email" class="form-control-label">{{
                      $t("signUp--email")
                    }}</label>
                    <div class="row mb-0">
                      <argon-input
                        isRequired
                        id="email"
                        type="email"
                        class="col-xl col-md col-sm"
                        :placeholder="$t('signUp--emailPlaceholder')"
                        aria-label="Email"
                        v-model="email"
                        :class="{ 'is-invalid': emailError }"
                        :error="emailError"
                        :errorText="$t('signUp--errorText-email')"
                        :disabled="isVerified"
                      />
                      <!-- 인증 메일 전송/재전송 버튼 -->
                      <div class="col-xl-4 col-md-5">
                        <argon-button
                          fullWidth
                          :disabled="isVerified"
                          color="info"
                          variant="gradient"
                          class="mb-3"
                          type="button"
                          @click="sendEmailCode"
                        >
                          <span v-if="isButtonEnabled">{{
                            $t("signUp--button-sendCode")
                          }}</span>
                          <span v-else
                            >{{ $t("signUp--button-emailResend") }}
                            {{ Math.floor(timer / 60) }}:{{
                              (timer % 60).toString().padStart(2, "0")
                            }}</span
                          >
                        </argon-button>
                      </div>
                    </div>
                  </div>
                  <!-- 인증 코드 입력 -->
                  <div class="col-md-12">
                    <label for="emailCode" class="form-label">{{
                      $t("signUp--emailCode")
                    }}</label>
                    <div class="row mb-0">
                      <argon-input
                        isRequired
                        id="emailCode"
                        type="text"
                        class="col-xl col-md col-sm"
                        :placeholder="$t('signUp--emailCodePlaceholder')"
                        v-model="emailCode"
                        @input="handleEmailCode"
                        :class="{ 'is-invalid': emailCodeError }"
                        :success="!emailCodeError && isVerified"
                        :error="
                          emailCode === '' && emailCodeError && !isVerified
                        "
                        :errorText="$t('signUp--errorText-emailCode')"
                      ></argon-input>
                      <!-- 인증코드 확인 버튼 -->
                      <div class="col-xl-4 col-md-4">
                        <argon-button
                          fullWidth
                          color="info"
                          variant="gradient"
                          class="mb-3"
                          type="button"
                          @click="verifyCode"
                          >{{ $t("signUp--verify") }}
                        </argon-button>
                      </div>
                    </div>
                  </div>
                  <!-- 비밀번호 입력 필드 -->
                  <div class="col-md-12">
                    <label for="password" class="form-control-label">{{
                      $t("signUp--pw")
                    }}</label>
                    <argon-input
                      isRequired
                      id="password"
                      type="password"
                      :placeholder="$t('signUp--pwPlaceholder')"
                      v-model="password"
                      @input="handlePasswordInput"
                      :class="{
                        'is-invalid': passwordError && password.length < 8,
                      }"
                      :error="passwordError && password.length < 8"
                      :errorText="$t('signUp--errorText-pw')"
                    ></argon-input>
                    <!-- 비밀번호 입력 확인 필드 -->
                    <label for="confirm-password" class="form-control-label">{{
                      $t("signUp--confirmPw")
                    }}</label>
                    <argon-input
                      isRequired
                      id="confirm-password"
                      type="password"
                      :placeholder="$t('signUp--confirmPwPlaceholder')"
                      v-model="confirmPassword"
                      @input="handleConfirmPasswordInput"
                      :class="{
                        'is-invalid':
                          (confirmPasswordError &&
                            confirmPassword.length < 8) ||
                          !isPasswordMatch,
                      }"
                      :success="isPasswordMatch"
                      :error="
                        confirmPasswordError ||
                        (confirmPassword !== '' &&
                          confirmPassword.length < 8 &&
                          !isPasswordMatch)
                      "
                      :errorText="$t('signUp--errorText-confirmPw')"
                    />
                  </div>
                  <!-- 거주 국가 입력 필드 -->
                  <div class="col-md-12">
                    <label for="country" class="form-control-label">{{
                      $t("signUp--country")
                    }}</label>
                    <div class="form-group">
                      <select
                        id="country"
                        class="form-select"
                        v-model="country"
                        :class="{
                          'is-invalid':
                            country === $t('signUp--countryCodeLabel') &&
                            countryError,
                        }"
                      >
                        <option disabled hidden>
                          {{ $t("signUp--countryCodeLabel") }}
                        </option>
                        <option value="1">
                          🇺🇸 {{ $t("signUp--countryUS") }}
                        </option>
                        <option value="2">
                          🇮🇩 {{ $t("signUp--countryID") }}
                        </option>
                        <option value="3">
                          🇻🇳 {{ $t("signUp--countryVI") }}
                        </option>
                      </select>
                      <p
                        v-if="country === $t('signUp--countryCodeLabel')"
                        class="invalid-feedback text-xs"
                      >
                        Please select a country.
                      </p>
                    </div>
                  </div>
                  <!-- 이름 입력 필드 -->
                  <div class="col-md-12">
                    <label for="first-name" class="form-control-label">{{
                      $t("signUp--firstName")
                    }}</label>
                    <argon-input
                      isRequired
                      id="first-name"
                      type="text"
                      :placeholder="$t('signUp--firstNamePlaceholder')"
                      v-model="firstName"
                      :class="{
                        'is-invalid': firstNameError && firstName === '',
                      }"
                      :error="firstNameError && firstName === ''"
                      :errorText="$t('signUp--errorText-firstName')"
                    />
                  </div>
                  <!-- 성 입력 필드 -->
                  <div class="col-md-12">
                    <label for="last-name" class="form-control-label">{{
                      $t("signUp--lastName")
                    }}</label>
                    <argon-input
                      isRequired
                      id="last-name"
                      type="text"
                      :placeholder="$t('signUp--lastNamePlaceholder')"
                      v-model="lastName"
                      :class="{
                        'is-invalid': lastNameError && lastName === '',
                      }"
                      :error="lastNameError && lastName === ''"
                      :errorText="$t('signUp--errorText-lastName')"
                    />
                  </div>
                  <!-- 생년월일 입력 필드 -->
                  <div class="col-md-12 form-group">
                    <label for="birth" class="form-control-label">{{
                      $t("signUp--birthday")
                    }}</label>
                    <flat-pickr
                      id="birth"
                      v-model="birth"
                      class="form-control bg-white"
                      placeholder="YYYY-MM-DD"
                      :config="{
                        dateFormat: 'Y-m-d',
                        maxDate: calculateDateYearsAgo(14),
                        minDate: calculateDateYearsAgo(110),
                      }"
                      :class="{
                        'is-invalid': birth === '' && birthError,
                      }"
                    />
                    <p v-if="birthError" class="invalid-feedback text-xs">
                      {{ $t("signUp--errorText-birth") }}
                    </p>
                  </div>
                  <!-- 성별 입력 필드 -->
                  <div class="col-md-12">
                    <label for="gender" class="form-control-label">{{
                      $t("signUp--gender")
                    }}</label>
                    <div class="form-group">
                      <select
                        required
                        id="gender"
                        class="form-select"
                        v-model="gender"
                        :class="{
                          'is-invalid':
                            gender === $t('signUp--genderLabel') && genderError,
                        }"
                      >
                        <option disabled hidden>
                          {{ $t("signUp--genderLabel") }}
                        </option>
                        <option value="1">
                          {{ $t("signUp--genderMale") }}
                        </option>
                        <option value="2">
                          {{ $t("signUp--genderFemale") }}
                        </option>
                      </select>
                      <p
                        v-if="gender === $t('signUp--genderLabel')"
                        class="invalid-feedback text-xs"
                      >
                        {{ $t("signUp--errorText-gender") }}
                      </p>
                    </div>
                    <!-- 전화번호 입력 필드 -->
                    <div class="col-md-12">
                      <label for="name" class="form-control-label">{{
                        $t("signUp--phone")
                      }}</label>
                      <PhoneInput
                        v-model="phoneNumber"
                        :error="phoneNumberError"
                        :countryCallingCode="countryCallingCode"
                      />
                    </div>
                    <!-- 다음 버튼 -->
                    <div class="text-center">
                      <argon-button
                        fullWidth
                        color="success"
                        variant="gradient"
                        class="my-4 mb-2"
                        type="submit"
                        >{{ $t("signUp--button-submit") }}</argon-button
                      >
                    </div>
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
