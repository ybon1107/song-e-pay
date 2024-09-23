<script setup>
import { ref, watch, onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
// import Navbar from "@/views/pageLayout/Navbar.vue";
import AppFooter from "@/views/pageLayout/Footer.vue";
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

// 전화번호와 국가 코드 상태
const phoneNumber = ref("");
const countryCallingCode = ref("+1");

// 전화번호 유효성 검사
const isPhoneValid = ref(false);

// 전화번호 유효성 검증 함수
const validatePhoneNumber = () => {
  const phoneRegex = /^[0-9]{10,15}$/; // 10자리에서 15자리 숫자
  isPhoneValid.value = phoneRegex.test(phoneNumber.value);
};

// 전화번호 입력 변경 감지
watch(phoneNumber, validatePhoneNumber);

// 인증코드 전송 버튼 클릭 핸들러
const handleSendCode = () => {
  if (isPhoneValid.value) {
    router.push("/register/phone/submit");
    console.log("Sending verification code...");
    // 추가 로직 (예: API 호출)
  } else {
    console.log("Invalid phone number.");
  }
};
</script>
<template>
  <main class="main-content mt-0">
    <div class="page-header min-vh-100 pt-5 pb-11 m-3 border-radius-lg">
      <div class="container">
        <div class="row justify-content-center">
          <div
            class="mx-auto col-xl-5 col-lg-7 col-md-9 d-flex flex-column mx-lg-0"
          >
            <div class="card card-plain">
              <!-- 카드 헤더: 제목 -->
              <div class="pb-0 card-header text-center">
                <h4 class="font-weight-bolder">
                  Verify your phone number with a code
                </h4>
              </div>
              <!-- 카드 푸터: 회원가입 링크 -->
              <div class="pt-0 text-center card-footer">
                <p class="mx-auto text-sm">
                  It helps us keep your account secure.
                  <a
                    href="javascript:;"
                    class="text-success text-gradient font-weight-bold"
                    >Learn more</a
                  >
                </p>
              </div>
              <!-- 카드 본문 -->
              <div class="card-body">
                <form role="form">
                  <!-- 전화번호 입력 필드 -->
                  <label for="name" class="form-control-label"
                    >Your phone number</label
                  >
                  <div class="phone-input-group row g-3">
                    <div class="col-xl-4 col-md-3 col-sm-3">
                      <select
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
                    <div class="col-xl col-md col-sm">
                      <argon-input
                        id="phoneNumber"
                        type="tel"
                        placeholder="Phone number"
                        aria-label="Phone number"
                        v-model="phoneNumber"
                      />
                    </div>
                  </div>
                  <!-- 다음 버튼 -->
                  <div class="text-center">
                    <argon-button
                      :disabled="!isPhoneValid"
                      fullWidth
                      color="success"
                      variant="gradient"
                      class="my-4 mb-2"
                      @click="handleSendCode"
                      >Send verification code</argon-button
                    >
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
