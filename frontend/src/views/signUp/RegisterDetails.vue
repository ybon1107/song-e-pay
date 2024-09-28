<script setup>
import { ref, computed, onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { useSignupStore } from "@/stores/signupStore";
import axios from "axios";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";
import PhoneInput from "@/components/signUp/PhoneInput.vue";
import flatPickr from "vue-flatpickr-component";
import "flatpickr/dist/flatpickr.css";
import { parsePhoneNumberFromString } from "libphonenumber-js";

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

// 이메일 상태
const email = computed(() => signupStore.email);

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
const country = ref("Country");

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
const gender = ref("Gender");

// 전화번호와 국가 코드 상태
const countryCallingCode = computed(() => signupStore.countryCallingCode);
const phoneNumber = computed(() => signupStore.phoneNumber);

// 에러 상태
const passwordError = ref(false);
const confirmPasswordError = ref(false);
const firstNameError = ref(false);
const lastNameError = ref(false);
const birthError = ref(false);
const countryError = ref(false);
const genderError = ref(false);

// 모든 입력값 유효성 검사
const isFormValid = computed(() => {
  return (
    password.value.length >= 8 &&
    confirmPassword.value.length >= 8 &&
    isPasswordMatch.value &&
    country.value !== "Country" &&
    firstName.value !== "" &&
    lastName.value !== "" &&
    birth.value !== "" &&
    gender.value !== "Gender"
  );
});

// 폼 제출 처리
const handleSubmit = async () => {
  passwordError.value = password.value.length < 8;
  confirmPasswordError.value =
    confirmPassword.value.length < 8 || !isPasswordMatch.value;
  countryError.value = country.value === "Country";
  firstNameError.value = !firstName.value;
  lastNameError.value = !lastName.value;
  birthError.value = !birth.value;
  genderError.value = gender.value === "Gender";

  if (isFormValid.value) {
    try {
      const phoneNumberE164 = parsePhoneNumberFromString(
        `${countryCallingCode.value}${phoneNumber.value}`
      ).format("E.164");

      const response = await axios.post("/api/users/register", {
        userId: email.value,
        password: password.value,
        countryCode: country.value,
        firstName: firstName.value,
        lastName: lastName.value,
        birthday: birth.value,
        gender: gender.value,
        phoneNo: phoneNumberE164,
      });

      if (response.data === "success") {
        signupStore.clearSessionStorage();
        router.push("/login");
      } else {
        // 등록 실패
        alert("Registration failed. Please try again.");
      }
    } catch (error) {
      console.error("Registration error:", error);
      alert("An error occurred during registration. Please try again.");
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
                <h4 class="font-weight-bolder">Tell us about yourself</h4>
              </div>
              <!-- 카드 본문 -->
              <div class="card-body container-fluid">
                <form @submit.prevent="handleSubmit">
                  <!-- 사용자 이메일 필드 -->
                  <div class="col-md-12">
                    <label for="email" class="form-control-label"
                      >Your email address</label
                    >
                    <!-- 이전에 입력받은 값 pinia에서 가져와서 넣고 입력변경 막기 -->
                    <argon-input
                      disabled
                      id="email"
                      type="email"
                      v-model="email"
                    ></argon-input>
                  </div>
                  <!-- 비밀번호 입력 필드 -->
                  <div class="col-md-12">
                    <label for="password" class="form-control-label"
                      >Your password</label
                    >
                    <argon-input
                      isRequired
                      id="password"
                      type="password"
                      v-model="password"
                      @input="handlePasswordInput"
                      :class="{
                        'is-invalid': passwordError && password.length < 8,
                      }"
                      :error="passwordError && password.length < 8"
                    ></argon-input>
                    <div
                      v-if="passwordError"
                      class="invalid-feedback text-xs mb-1"
                    >
                      Please provide a password with at least 8 characters.
                    </div>
                    <!-- 비밀번호 입력 확인 필드 -->
                    <label for="confirm-password" class="form-control-label"
                      >Confirm your password</label
                    >
                    <argon-input
                      isRequired
                      id="confirm-password"
                      type="password"
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
                        !confirmPasswordError &&
                        0 < confirmPassword.length &&
                        confirmPassword.length < 8 &&
                        !isPasswordMatch
                      "
                    />
                    <div
                      v-if="confirmPasswordError"
                      class="invalid-feedback text-xs mb-1"
                    >
                      Please confirm your password with at least 8 characters.
                    </div>
                    <div
                      v-if="confirmPassword !== '' && !isPasswordMatch"
                      class="invalid-feedback text-xs"
                    >
                      Passwords do not match or are less than 8 characters.
                    </div>
                  </div>
                  <!-- 거주 국가 입력 필드 -->
                  <div class="col-md-12">
                    <label for="country" class="form-control-label"
                      >Country of residence</label
                    >
                    <div class="form-group">
                      <select
                        id="country"
                        class="form-select"
                        v-model="country"
                        :class="{
                          'is-invalid': country === 'Country' && countryError,
                        }"
                      >
                        <option disabled hidden>Country</option>
                        <option value="1">🇺🇸 United States</option>
                        <option value="2">🇮🇩 Indonesia</option>
                        <option value="3">🇻🇳 Vietnam</option>
                      </select>
                      <div
                        v-if="country === 'Country'"
                        class="invalid-feedback text-xs"
                      >
                        Please select a country.
                      </div>
                    </div>
                  </div>
                  <!-- 이름 입력 필드 -->
                  <div class="col-md-12">
                    <label for="first-name" class="form-control-label"
                      >Full legal first and middle name(s)</label
                    >
                    <argon-input
                      isRequired
                      id="first-name"
                      type="text"
                      v-model="firstName"
                      :class="{
                        'is-invalid': firstNameError && firstName === '',
                      }"
                      :error="firstNameError && firstName === ''"
                    />
                    <div v-if="firstNameError" class="invalid-feedback text-xs">
                      Please provide your full legal first and middle name(s).
                    </div>
                  </div>
                  <!-- 성 입력 필드 -->
                  <div class="col-md-12">
                    <label for="last-name" class="form-control-label"
                      >Full legal last name(s)</label
                    >
                    <argon-input
                      isRequired
                      id="last-name"
                      type="text"
                      v-model="lastName"
                      :class="{
                        'is-invalid': lastNameError && lastName === '',
                      }"
                      :error="lastNameError && lastName === ''"
                    />
                    <div v-if="lastNameError" class="invalid-feedback text-xs">
                      Please provide your full legal last name(s).
                    </div>
                  </div>
                  <!-- 생년월일 입력 필드 -->
                  <div class="col-md-12 form-group">
                    <label for="birth" class="form-control-label"
                      >Date of birth</label
                    >
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
                    <div v-if="birthError" class="invalid-feedback text-xs">
                      Please provide a valid date of birth.
                    </div>
                  </div>
                  <!-- 성별 입력 필드 -->
                  <div class="col-md-12">
                    <label for="gender" class="form-control-label"
                      >Gender</label
                    >
                    <div class="form-group">
                      <select
                        id="gender"
                        class="form-select"
                        v-model="gender"
                        :class="{
                          'is-invalid': gender === 'Gender' && genderError,
                        }"
                      >
                        <option disabled hidden>Gender</option>
                        <option value="1">Male</option>
                        <option value="2">Female</option>
                      </select>
                      <div
                        v-if="gender === 'Gender'"
                        class="invalid-feedback text-xs"
                      >
                        Please select a gender.
                      </div>
                    </div>
                    <!-- 전화번호 입력 필드 -->
                    <div class="col-md-12">
                      <label for="name" class="form-control-label"
                        >Your phone number</label
                      >
                      <PhoneInput
                        disabled
                        v-model="phoneNumber"
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
                        >Continue</argon-button
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
