<script setup>
import { onBeforeUnmount, onBeforeMount } from "vue";
import { useStore } from "vuex";
import Navbar from "@/components/Navbars/Navbar.vue";
import ArgonInput from "@/components/templates/ArgonInput.vue";
import ArgonCheckbox from "@/components/templates/ArgonCheckbox.vue";
import ArgonAlert from "@/components/templates/ArgonAlert.vue";
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
            <!-- 카드 컨테이너 -->
            <div
              class="mx-auto col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-lg-0"
            >
              <div class="card card-plain">
                <!-- 카드 헤더: 제목 -->
                <div class="pb-0 card-header text-center">
                  <img src="@/assets/img/send-mail.png" width="250px" />
                  <h2 class="font-weight-bolder">CHECK YOUR EMAIL</h2>
                </div>
                <!-- 카드 본문 -->
                <div class="card-body">
                  <argon-alert color="info" icon="ni ni-send">
                    <span>
                      Follow the link in the email we sent to
                      <b class="text-dark">useremail@gmail.com</b>. The email
                      can take up to <b class="text-dark">1 minute</b> to
                      arrive.
                    </span>
                  </argon-alert>
                  <!-- 인증 메일 재전송 버튼 -->
                  <div class="text-center">
                    <argon-button
                      disabled
                      fullWidth
                      color="success"
                      variant="gradient"
                      class="my-4 mb-2"
                      >Resend email
                    </argon-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>
