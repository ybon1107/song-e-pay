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

// 비밀번호 입력 필드 상태
const password = ref("");
const confirmPassword = ref("");

// 비밀번호 일치 여부 검사
const isPasswordMatch = computed(() => {
  if (password.value === "" || confirmPassword.value === "") {
    return false;
  }
  return password.value === confirmPassword.value;
});

// 생년월일 입력 필드 상태
const month = ref("");
const day = ref("");
const year = ref("");

// 전화번호와 국가 코드 상태
const phoneNumber = ref("");
const countryCallingCode = ref("+1");

// Day 입력 필드 제어 함수
const handleDayInput = (event) => {
  let value = event.target.value.replace(/\D/g, ""); // 숫자만 허용
  if (value.length > 2) {
    value = value.slice(0, 2); // 최대 2자리
  }
  // 01부터 31까지의 값만 허용하는 정규식
  const dayRegex = /^(0[1-9]|[12][0-9]|3[01])$/;
  if (!dayRegex.test(value) && value !== "") {
    value = value.slice(0, -1); // 마지막 입력 제거
  }
  day.value = value;
};

// Year 입력 필드 제어 함수
const handleYearInput = (event) => {
  let value = event.target.value.replace(/\D/g, ""); // 숫자만 허용
  if (value.length > 4) {
    value = value.slice(0, 4); // 최대 4자리
  }
  year.value = value;
};

// 생년월일 유효성 검사
const isDateOfBirthValid = computed(() => {
  const monthRegex = /^(0?[1-9]|1[0-2])$/;
  const dayRegex = /^(0[1-9]|[12][0-9]|3[01])$/;
  const yearRegex = /^\d{4}$/;
  return (
    monthRegex.test(month.value) &&
    dayRegex.test(day.value) &&
    yearRegex.test(year.value) &&
    month.value !== "" &&
    day.value !== "" &&
    year.value !== ""
  );
});

// 모든 입력값 유효성 검사
const isFormValid = computed(() => {
  return isDateOfBirthValid.value && isPasswordMatch.value;
});
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
                <form role="form">
                  <div class="row">
                    <!-- 사용자 이메일 필드 -->
                    <div class="col-md-12">
                      <label for="email" class="form-control-label"
                        >Your email address</label
                      >
                      <!-- 이전에 입력받은 값 pinia에서 가져와서 넣고 입력변경 막기 -->
                      <argon-input disabled id="email" type="email" />
                    </div>
                    <!-- 비밀번호 입력 필드 -->
                    <div class="col-md-12">
                      <label for="password" class="form-control-label"
                        >Your password</label
                      >
                      <argon-input
                        id="password"
                        type="password"
                        v-model="password"
                        :success="password.value !== '' && isPasswordMatch"
                        :error="password.value !== '' && !isPasswordMatch"
                      />
                    </div>
                    <!-- 비밀번호 입력 확인 필드 -->
                    <div class="col-md-12">
                      <label for="confirm-password" class="form-control-label"
                        >Confirm your password</label
                      >
                      <argon-input
                        id="confirm-password"
                        type="password"
                        v-model="confirmPassword"
                        :success="
                          confirmPassword.value !== '' && isPasswordMatch
                        "
                        :error="
                          confirmPassword.value !== '' && !isPasswordMatch
                        "
                      />
                    </div>
                    <!-- 거주 국가 입력 필드 -->
                    <div class="col-md-12">
                      <label for="country" class="form-control-label"
                        >Country of residence</label
                      >
                      <argon-input id="country" type="text" />
                    </div>
                    <!-- 이름 입력 필드 -->
                    <div class="col-md-12">
                      <label for="first-name" class="form-control-label"
                        >Full legal first and middle name(s)</label
                      >
                      <argon-input id="first-name" type="text" />
                    </div>
                    <!-- 성 입력 필드 -->
                    <div class="col-md-12">
                      <label for="last-name" class="form-control-label"
                        >Full legal last name(s)</label
                      >
                      <argon-input id="last-name" type="text" />
                    </div>
                    <!-- 생년월일 입력 필드 -->
                    <div class="col-md-12">
                      <label for="birth-input-group" class="form-control-label"
                        >Date of birth</label
                      >
                      <div id="birth-input-group" class="birth-input-group row">
                        <!-- 월 입력 필드 -->
                        <div class="col-md-4">
                          <label for="month" class="form-control-label"
                            >Month</label
                          >
                          <select
                            id="month"
                            placeholder="Month"
                            class="form-select"
                            v-model="month"
                          >
                            <option disabled hidden selected>Month</option>
                            <option value="1">01</option>
                            <option value="2">02</option>
                            <option value="3">03</option>
                            <option value="4">04</option>
                            <option value="5">05</option>
                            <option value="6">06</option>
                            <option value="7">07</option>
                            <option value="8">08</option>
                            <option value="9">09</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                          </select>
                          <!-- <argon-input type="text" /> -->
                        </div>
                        <!-- 일 입력 필드 -->
                        <div class="col-md-4">
                          <label for="day" class="form-control-label"
                            >Day</label
                          >
                          <argon-input
                            id="day"
                            type="text"
                            placeholder="DD"
                            v-model="day"
                            @input="handleDayInput"
                          />
                        </div>
                        <!-- 년도 입력 필드 -->
                        <div class="col-md-4">
                          <label for="year" class="form-control-label"
                            >Year</label
                          >
                          <argon-input
                            id="year"
                            type="text"
                            placeholder="YYYY"
                            v-model="year"
                            @input="handleYearInput"
                          />
                        </div>
                      </div>
                    </div>
                    <!-- 전화번호 입력 필드 -->
                    <div class="col-md-12">
                      <label for="name" class="form-control-label"
                        >Your phone number</label
                      >
                      <div class="phone-input-group row g-3">
                        <!-- 국가 코드 선택 필드 -->
                        <div class="col-xl-4 col-md-4 col-sm-4">
                          <!-- 이전에 입력받은 값 pinia에서 가져와서 넣고 입력변경 막기 -->
                          <select
                            disabled
                            id="countryCallingCode"
                            class="form-select"
                            aria-label="Country"
                            v-model="countryCallingCode"
                          >
                            <option value="+1">&#43;1 USA</option>
                            <option value="+62">&#43;62 IDN</option>
                            <option value="+81">&#43;81 JPN</option>
                            <option value="+86">&#43;86 CHN</option>
                            <option value="+886">&#43;886 TWN</option>
                          </select>
                        </div>
                        <!-- 전화번호 입력 필드 -->
                        <div class="col-xl col-md col-sm">
                          <!-- 이전에 입력받은 값 pinia에서 가져와서 넣고 입력변경 막기 -->
                          <argon-input
                            disabled
                            id="phoneNumber"
                            type="tel"
                            placeholder="Phone number"
                            aria-label="Phone number"
                            v-model="phoneNumber"
                          />
                        </div>
                      </div>
                    </div>
                    <!-- 다음 버튼 -->
                    <div class="text-center">
                      <argon-button
                        :disabled="!isFormValid"
                        fullWidth
                        color="success"
                        variant="gradient"
                        class="my-4 mb-2"
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
