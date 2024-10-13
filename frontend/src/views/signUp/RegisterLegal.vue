<script setup>
import { ref, watch, onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import ArgonCheckbox from "@/components/templates/ArgonCheckbox.vue";
import ArgonButton from "@/components/templates/ArgonButton.vue";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

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

// 동의 체크박스 상태들
const agreeAll = ref(false);
const agreeTerms = ref(false);
const agreePrivacy = ref(false);

// 전체 동의와 개별 동의 동기화 로직
watch([agreeTerms, agreePrivacy], ([newAgreeTerms, newAgreePrivacy]) => {
  agreeAll.value = newAgreeTerms && newAgreePrivacy;
});

watch(agreeAll, (newAgreeAll) => {
  if (newAgreeAll) {
    agreeTerms.value = true;
    agreePrivacy.value = true;
  } else {
    agreeTerms.value = false;
    agreePrivacy.value = false;
  }
});

// 다음 버튼 클릭 핸들러
const handleNext = () => {
  if (agreeTerms.value && agreePrivacy.value) {
    router.push("/register/detail");
  } else {
    console.log("Please agree to the required terms.");
  }
};

// 이용 약관과 개인정보 보호정책(다국어 지원)
const termsOfService = t("signUp--termsOfService");
const privacyPolicy = t("signUp--privacyPolicy");
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
                  <h4 class="font-weight-bolder">
                    {{ $t("signUp--Title-agreements") }}
                  </h4>
                </div>
                <!-- 카드 푸터: 로그인 링크 -->
                <div class="py-0 text-center card-footer">
                  <p class="mx-auto text-sm">
                    {{ $t("signUp--loginPrompt") }}
                    <router-link
                      to="/register/legal"
                      class="text-success text-gradient font-weight-bold"
                      >{{ $t("common--text-login") }}</router-link
                    >
                  </p>
                </div>
                <!-- 카드 본문: 이용 약관 동의 폼 -->
                <div class="card-body">
                  <form role="form">
                    <!-- 전체 동의 체크박스 -->
                    <argon-checkbox v-model="agreeAll" id="agreeAll"
                      ><strong>{{ $t("signUp--agreeAllTermsLabel") }}</strong>
                    </argon-checkbox>
                    <!-- 이용약관 동의 -->
                    <div class="form-group">
                      <argon-checkbox v-model="agreeTerms" id="agreeTerms"
                        ><strong
                          ><span class="text-success"
                            >[{{ $t("signUp--requireText") }}]</span
                          >
                          {{ $t("signUp--agreeTermsOfServiceLabel") }}</strong
                        ></argon-checkbox
                      >
                      <div
                        class="form-control bg-white"
                        style="height: 72px; overflow-y: auto"
                        v-html="termsOfService"
                      ></div>
                    </div>
                    <!-- 개인정보 보호정책 동의 -->
                    <div class="form-group">
                      <argon-checkbox v-model="agreePrivacy" id="agreePrivacy"
                        ><strong>
                          <span class="text-success"
                            >[{{ $t("signUp--requireText") }}]</span
                          >
                          {{ $t("signUp--agreePrivacyPolicyLabel") }}</strong
                        ></argon-checkbox
                      >
                      <div
                        class="form-control bg-white"
                        style="height: 72px; overflow-y: auto"
                        v-html="privacyPolicy"
                      ></div>
                    </div>
                    <!-- 다음 버튼 -->
                    <div class="text-center">
                      <argon-button
                        :disabled="!agreeTerms || !agreePrivacy"
                        fullWidth
                        color="success"
                        variant="gradient"
                        class="my-4 mb-2"
                        @click="handleNext"
                        >{{ $t("common--text-next") }}</argon-button
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
