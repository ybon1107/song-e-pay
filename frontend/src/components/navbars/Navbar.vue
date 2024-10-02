<template>
  <nav
    class="navbar navbar-main navbar-expand-lg px-0 mx-4 shadow-none border-radius-xl"
    :class="isRTL ? 'top-0 position-sticky z-index-sticky' : ''"
    v-bind="$attrs"
    id="navbarBlur"
    data-scroll="true"
  >
    <div class="px-3 pyb-1 pt-4 container-fluid">
      <!-- 추후에 로고 이미지 추가 -->
      <!-- <img src="@/assets/img/songepay_logo.png" /> -->
      <!-- <h1>Song-E-Pay</h1> -->
      <div
        class="mt-2 collapse navbar-collapse mt-sm-0 me-md-0 me-sm-4"
        :class="isRTL ? 'px-0' : 'me-sm-4'"
        id="navbar"
      >
        <div
          class="pe-md-3 d-flex align-items-center"
          :class="isRTL ? 'me-md-auto' : 'ms-md-auto'"
        ></div>
        <ul class="navbar-nav justify-content-end">
          <!-- <li class="nav-item d-flex align-items-center">
            <router-link
              :to="{ name: 'Signin' }"
              class="px-0 nav-link font-weight-bold"
              target="_blank"
            >
              <i class="fa fa-user" :class="isRTL ? 'ms-sm-2' : 'me-sm-2'"></i>
              <span v-if="isRTL" class="d-sm-inline d-none">يسجل دخول</span>
              <span v-else class="d-sm-inline d-none">Sign In</span>
            </router-link>
          </li> -->
          <li class="nav-item d-xl-none ps-3 d-flex align-items-center">
            <a
              href="#"
              @click="minimizeSidebar"
              class="p-0 nav-link"
              id="iconNavbarSidenav"
            >
              <div class="sidenav-toggler-inner">
                <i class="sidenav-toggler-line"></i>
                <i class="sidenav-toggler-line"></i>
                <i class="sidenav-toggler-line"></i>
              </div>
            </a>
          </li>
          <!-- 알림 -->
          <li class="px-3 nav-item dropdown d-flex align-items-center">
            <a
              href="#"
              class="p-0 nav-link"
              :class="[showMenu ? 'show' : '']"
              id="dropdownMenuButton"
              data-bs-toggle="dropdown"
              aria-expanded="false"
              @click="showMenu = !showMenu"
              @blur="closeMenu"
            >
              <div class="icon-div">
                <i class="cursor-pointer fa-solid fa-bell"></i>
              </div>
            </a>
            <ul
              class="px-2 py-3 dropdown-menu dropdown-menu-end me-sm-n4"
              :class="showMenu ? 'show' : ''"
              aria-labelledby="dropdownMenuButton"
            >
              <li class="mb-2">
                <a class="dropdown-item border-radius-md" href="javascript:;">
                  <div class="py-1 d-flex">
                    <div class="my-auto">
                      <img
                        src="../../assets/img/team-2.jpg"
                        class="avatar avatar-sm me-3"
                        alt="user image"
                      />
                    </div>
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="mb-1 text-sm font-weight-normal">
                        <span class="font-weight-bold">New message</span> from
                        Laur
                      </h6>
                      <p class="mb-0 text-xs text-secondary">
                        <i class="fa fa-clock me-1"></i>
                        13 minutes ago
                      </p>
                    </div>
                  </div>
                </a>
              </li>
              <li class="mb-2">
                <a class="dropdown-item border-radius-md" href="javascript:;">
                  <div class="py-1 d-flex">
                    <div class="my-auto">
                      <img
                        src="../../assets/img/small-logos/logo-spotify.svg"
                        class="avatar avatar-sm bg-gradient-dark me-3"
                        alt="logo spotify"
                      />
                    </div>
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="mb-1 text-sm font-weight-normal">
                        <span class="font-weight-bold">New album</span> by
                        Travis Scott
                      </h6>
                      <p class="mb-0 text-xs text-secondary">
                        <i class="fa fa-clock me-1"></i>
                        1 day
                      </p>
                    </div>
                  </div>
                </a>
              </li>
              <li>
                <a class="dropdown-item border-radius-md" href="javascript:;">
                  <div class="py-1 d-flex">
                    <div
                      class="my-auto avatar avatar-sm bg-gradient-secondary me-3"
                    >
                      <svg
                        width="12px"
                        height="12px"
                        viewBox="0 0 43 36"
                        version="1.1"
                        xmlns="http://www.w3.org/2000/svg"
                        xmlns:xlink="http://www.w3.org/1999/xlink"
                      >
                        <title>credit-card</title>
                        <g
                          stroke="none"
                          stroke-width="1"
                          fill="none"
                          fill-rule="evenodd"
                        >
                          <g
                            transform="translate(-2169.000000, -745.000000)"
                            fill="#FFFFFF"
                            fill-rule="nonzero"
                          >
                            <g transform="translate(1716.000000, 291.000000)">
                              <g transform="translate(453.000000, 454.000000)">
                                <path
                                  class="color-background"
                                  d="M43,10.7482083 L43,3.58333333 C43,1.60354167 41.3964583,0 39.4166667,0 L3.58333333,0 C1.60354167,0 0,1.60354167 0,3.58333333 L0,10.7482083 L43,10.7482083 Z"
                                  opacity="0.593633743"
                                />
                                <path
                                  class="color-background"
                                  d="M0,16.125 L0,32.25 C0,34.2297917 1.60354167,35.8333333 3.58333333,35.8333333 L39.4166667,35.8333333 C41.3964583,35.8333333 43,34.2297917 43,32.25 L43,16.125 L0,16.125 Z M19.7083333,26.875 L7.16666667,26.875 L7.16666667,23.2916667 L19.7083333,23.2916667 L19.7083333,26.875 Z M35.8333333,26.875 L28.6666667,26.875 L28.6666667,23.2916667 L35.8333333,23.2916667 L35.8333333,26.875 Z"
                                />
                              </g>
                            </g>
                          </g>
                        </g>
                      </svg>
                    </div>
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="mb-1 text-sm font-weight-normal">
                        Payment successfully completed
                      </h6>
                      <p class="mb-0 text-xs text-secondary">
                        <i class="fa fa-clock me-1"></i>
                        2 days
                      </p>
                    </div>
                  </div>
                </a>
              </li>
            </ul>
          </li>
          <!-- 프로필 -->
          <li class="nav-item d-flex align-items-center">
            <template v-if="isLogin">
              <a class="p-0 nav-link" href="/profile">
                <div class="icon-div">
                  <!-- img-div 대신 icon-div 사용 -->
                  <i class="fa-solid fa-user profile-icon"></i>
                  <!-- FontAwesome 아이콘 추가 -->
                </div>
              </a>
            </template>
            <template v-else>
              <a class="p-0 nav-link" href="/login">
                <div class="icon-div">
                  <i class="fa-solid fa-user profile-icon"></i>
                  <!-- 로그인 전에도 FontAwesome 아이콘 -->
                </div>
              </a>
            </template>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { computed, ref, onMounted } from "vue";
import { useStore } from "vuex";
// import { useRoute } from "vue-router";
import { useExchangeStore } from "@/stores/exchangeStore";

import { useAuthStore } from "@/stores/auth";
const auth = useAuthStore();
const exchangeStore = useExchangeStore();

const isLogin = computed(() => auth.isLogin);
const userNo = computed(() => auth.userNo);
const user = computed(() => auth.user);

console.log("nav isLogin : ", isLogin);
console.log("nav userNo : ", userNo);
console.log("nav user : ", user);

const showMenu = ref(false);
const store = useStore();
const isRTL = computed(() => store.state.isRTL);

// const route = useRoute();

// const currentRouteName = computed(() => {
//   return route.name;
// });
// const currentDirectory = computed(() => {
//   let dir = route.path.split("/")[1];
//   return dir.charAt(0).toUpperCase() + dir.slice(1);
// });

const minimizeSidebar = () => store.commit("sidebarMinimize");

const closeMenu = () => {
  setTimeout(() => {
    showMenu.value = false;
  }, 100);
};
const noLoginImg =
  "https://song-e-pay.s3.ap-northeast-2.amazonaws.com/profile/noLogin.png";

const usdToKrwUrl = `https://v6.exchangerate-api.com/v6/${import.meta.env.VITE_EXCHANGE_RATE_API_KEY}/pair/USD/KRW`;
const krwToUsdUrl = `https://v6.exchangerate-api.com/v6/${import.meta.env.VITE_EXCHANGE_RATE_API_KEY}/pair/KRW/USD`;

const fetchExchangeRates = async () => {
  try {
    const [usdToKrwResponse, krwToUsdResponse] = await Promise.all([
      fetch(usdToKrwUrl),
      fetch(krwToUsdUrl),
    ]);

    if (!usdToKrwResponse.ok || !krwToUsdResponse.ok) {
      throw new Error("Network response was not ok");
    }

    const [usdToKrwData, krwToUsdData] = await Promise.all([
      usdToKrwResponse.json(),
      krwToUsdResponse.json(),
    ]);

    exchangeStore.setCurrentToKrw(usdToKrwData.conversion_rate);
    exchangeStore.setCurrentFromKrw(krwToUsdData.conversion_rate);

    console.log("환율 데이터가 성공적으로 로드되었습니다.");
    return {
      currentToKrw: usdToKrwData.conversion_rate,
      currentFromKrw: krwToUsdData.conversion_rate,
    };
  } catch (error) {
    console.error("Error fetching exchange rate data", error);
  }
};

onMounted(() => {
  fetchExchangeRates();
});
</script>

<style>
.img-div {
  width: 20px;
  height: 20px;
  position: relative;
}

.nav-lmg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 10px;
}


@font-face {
  font-family: "HakgyoansimDunggeunmisoTTF-B";
  src: url("https://fastly.jsdelivr.net/gh/projectnoonnu/2408-5@1.0/HakgyoansimDunggeunmisoTTF-B.woff2")
    format("woff2");
  font-weight: 700;
  font-style: normal;
}
</style>
