<script setup>
import { ref, onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import ArgonButton from "@/components/templates/ArgonButton.vue";
import Accordion from "@/components/signIn/Accordion.vue";
import { useI18n } from "vue-i18n";

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

const { t } = useI18n();

const accordionItems = ref([
  {
    title: t("signIn--LoginIssueInfo--ForgotMyPassword-title"),
    content: t("signIn--LoginIssueInfo--ForgotMyPassword-content"),
    expanded: false,
  },
  {
    title: t("signIn--LoginIssueInfo--2stepNotworking-title"),
    content: t("signIn--LoginIssueInfo--2stepNotworking-content"),
    expanded: false,
  },
  {
    title: t("signIn--LoginIssueInfo--changePhoneNumber-title"),
    content: t("signIn--LoginIssueInfo--changePhoneNumber-content"),
    expanded: false,
  },
  {
    title: t("signIn--LoginIssueInfo--changeMyEmail-title"),
    content: t("signIn--LoginIssueInfo--changeMyEmail-content"),
    expanded: false,
  },
  {
    title: t("signIn--LoginIssueInfo--newDevice-title"),
    content: t("signIn--LoginIssueInfo--newDevice-content"),
    expanded: false,
  },
  {
    title: t("signIn--LoginIssueInfo--wasDeactivated-title"),
    content: t("signIn--LoginIssueInfo--wasDeactivated-content"),
    expanded: false,
  },
]);

const goBack = () => {
  router.back();
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
              class="mx-auto col-xl-6 col-lg-7 col-md-8 col-sm-8 d-flex flex-column mx-lg-0"
            >
              <div class="card card-plain">
                <!-- 카드 헤더: 제목 -->
                <div class="card-header text-center">
                  <h4 class="font-weight-bolder">
                    {{ $t("signIn--LoginIssueInfo--header-title") }}
                  </h4>
                </div>
                <!-- 카드 본문 -->
                <div class="card-body">
                  <div class="px-3">
                    <strong>{{
                      $t("signIn--LoginIssueInfo--body-selectIssue")
                    }}</strong>
                    <hr />
                  </div>
                  <!-- 아코디언 컴포넌트 -->
                  <Accordion :items="accordionItems" />
                </div>
                <!-- 뒤로 가기 버튼 -->
                <div class="text-center">
                  <argon-button color="secondary" @click="goBack">
                    Go Back
                  </argon-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>
