<script setup>
import { onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import Navbar from "@/components/Navbars/Navbar.vue";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonCheckbox from "@/components/templates/ArgonCheckbox.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";
import ArgonTextarea from "@/components/templates/ArgonTextarea.vue";

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
            <!-- 약관 동의 폼을 담고 있는 카드 컨테이너 -->
            <div
              class="mx-auto col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-lg-0"
            >
              <div class="card card-plain">
                <!-- 카드 헤더: 제목 -->
                <div class="pb-0 card-header text-center">
                  <h4 class="font-weight-bolder">Our agreements with you</h4>
                </div>
                <!-- 카드 본문: 이용 약관 동의 폼 -->
                <div class="card-body">
                  <argon-checkbox>
                    <!-- 전체 동의 체크박스 -->
                    <label class="form-check-label" for="flexCheckDefault">
                      Agree with all terms and conditions
                    </label>
                  </argon-checkbox>
                  <form role="form">
                    <!-- 이용약관 동의 -->
                    <div class="mb-3 form-check">
                      <label for="terms">
                        <argon-checkbox id="terms"
                          ><span class="text-success">[Required]</span> I agree
                          the Terms of Service</argon-checkbox
                        >
                      </label>
                      <argon-textarea
                        readonly
                        disabled
                        id="terms"
                        rows="3"
                        value="약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다."
                      ></argon-textarea>
                      <!-- <textarea
                        readonly
                        disabled
                        key="terms"
                        class="form-control bg-white"
                        rows="3"
                        value="약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다. 약관동의 내용이 들어갑니다."
                      >
                      </textarea> -->
                    </div>
                    <!-- 개인정보 보호정책 동의 -->
                    <div class="mb-3">
                      <argon-checkbox>
                        <label class="form-check-label" for="flexCheckDefault">
                          <span class="text-success">[Required]</span>
                          I agree the Privacy Policy
                        </label>
                      </argon-checkbox>
                      <textarea
                        readonly
                        disabled
                        key="privacy"
                        class="form-control bg-white"
                        rows="3"
                        value="개인정보 보호정책 내용이 들어갑니다. 개인정보 보호정책 내용이 들어갑니다. 개인정보 보호정책 내용이 들어갑니다. 개인정보 보호정책 내용이 들어갑니다. 개인정보 보호정책 내용이 들어갑니다. 개인정보 보호정책 내용이 들어갑니다. 개인정보 보호정책 내용이 들어갑니다."
                      ></textarea>
                    </div>
                    <!-- 다음 버튼 -->
                    <div class="text-center">
                      <argon-button
                        disabled
                        fullWidth
                        color="success"
                        variant="gradient"
                        class="my-4 mb-2"
                        >Next</argon-button
                      >
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>
