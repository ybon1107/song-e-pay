<template>
  <nav class="navbar navbar-main navbar-expand-lg px-0 mx-4 shadow-none border-radius-xl"
    :class="isRTL ? 'top-0 position-sticky z-index-sticky' : ''" v-bind="$attrs" id="navbarBlur" data-scroll="true">
    <div class="px-3 pyb-1 pt-4 container-fluid">
      <!-- 추후에 로고 이미지 추가 -->
      <!-- <img src="@/assets/img/songepay_logo.png" /> -->
      <!-- <h1>Song-E-Pay</h1> -->
      <div class="mt-2 collapse navbar-collapse mt-sm-0 me-md-0 me-sm-4" :class="isRTL ? 'px-0' : 'me-sm-4'"
        id="navbar">
        <div class="pe-md-3 d-flex align-items-center" :class="isRTL ? 'me-md-auto' : 'ms-md-auto'"></div>
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
          <li class="nav-item d-xl-none ps-3 d-flex align-items-center me-3">
            <a href="#" @click="minimizeSidebar" class="p-0 nav-link" id="iconNavbarSidenav">
              <div class="sidenav-toggler-inner">
                <i class="sidenav-toggler-line fixed-width"></i>
                <i class="sidenav-toggler-line"></i>
                <i class="sidenav-toggler-line fixed-width"></i>
              </div>
            </a>
          </li>

          <!-- 언어 선택 -->
          <li class="nav-item dropdown language-dropdown border rounded">
            <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" id="languageDropdown" role="button"
              data-bs-toggle="dropdown" aria-expanded="false">
              <img :src="getFlagSrc(currentLanguage)" alt="Flag" class="me-2 flag-icon">
              {{ currentLanguage }}
            </a>
            <ul class="dropdown-menu" aria-labelledby="languageDropdown">
              <li v-for="lang in languages" :key="lang.code">
                <a class="dropdown-item d-flex align-items-center" href="#" @click="changeLanguage(lang.code)">
                  <img :src="lang.flag" :alt="`${lang.name} Flag`" class="me-2 flag-icon">
                  {{ lang.name }}
                </a>
              </li>
            </ul>
          </li>

          <!-- 알림 -->
          <li class="px-3 nav-item dropdown d-flex align-items-center notification-dropdown">
            <a href="#" class="p-0 nav-link" :class="[showMenu ? 'show' : '']" id="dropdownMenuButton"
              data-bs-toggle="dropdown" aria-expanded="false" @click="showMenu = !showMenu" @blur="closeMenu">
              <div class="icon-div">
                <i class="cursor-pointer fa fa-bell"></i>
              </div>
            </a>
            <ul class="px-2 py-3 dropdown-menu dropdown-menu-end me-sm-n4" :class="showMenu ? 'show' : ''"
              aria-labelledby="dropdownMenuButton">
              <li class="mb-2">
                <a class="dropdown-item border-radius-md" href="javascript:;">
                  <div class="py-1 d-flex">
                    <div class="my-auto">
                      <img src="../../assets/img/team-2.jpg" class="avatar avatar-sm me-3" alt="user image" />
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
                      <img src="../../assets/img/small-logos/logo-spotify.svg"
                        class="avatar avatar-sm bg-gradient-dark me-3" alt="logo spotify" />
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
                    <div class="my-auto avatar avatar-sm bg-gradient-secondary me-3">
                      <svg width="12px" height="12px" viewBox="0 0 43 36" version="1.1"
                        xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                        <title>credit-card</title>
                        <g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                          <g transform="translate(-2169.000000, -745.000000)" fill="#FFFFFF" fill-rule="nonzero">
                            <g transform="translate(1716.000000, 291.000000)">
                              <g transform="translate(453.000000, 454.000000)">
                                <path class="color-background"
                                  d="M43,10.7482083 L43,3.58333333 C43,1.60354167 41.3964583,0 39.4166667,0 L3.58333333,0 C1.60354167,0 0,1.60354167 0,3.58333333 L0,10.7482083 L43,10.7482083 Z"
                                  opacity="0.593633743" />
                                <path class="color-background"
                                  d="M0,16.125 L0,32.25 C0,34.2297917 1.60354167,35.8333333 3.58333333,35.8333333 L39.4166667,35.8333333 C41.3964583,35.8333333 43,34.2297917 43,32.25 L43,16.125 L0,16.125 Z M19.7083333,26.875 L7.16666667,26.875 L7.16666667,23.2916667 L19.7083333,23.2916667 L19.7083333,26.875 Z M35.8333333,26.875 L28.6666667,26.875 L28.6666667,23.2916667 L35.8333333,23.2916667 L35.8333333,26.875 Z" />
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
            <a class="p-0 nav-link" href="/profile">
              <div class="icon-div">
                <img :src="userImg" class="user-profile-img" />
              </div>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { computed, ref, onMounted, watchEffect } from "vue";
import { useStore } from "vuex";
// import { useRoute } from "vue-router";
import { useExchangeStore } from "@/stores/exchangeStore";
import { CURRENCY_NAME } from "@/constants/countryCode";

import { useAuthStore } from "@/stores/auth";
import userApi from "@/api/userApi";
import axios from "axios";
import { useI18n } from 'vue-i18n';
import { languages } from '@/constants/languages';

const auth = useAuthStore();
const user = computed(() => auth.user);

const exchangeStore = useExchangeStore();
const userImg = ref('');

const isLogin = computed(() => auth.isLogin);
const userId = computed(() => auth.userId);

const countryCode = ref(1);

watchEffect(() => {
  if (user.value?.countryCode) {
    countryCode.value = user.value.countryCode;
  }
});

console.log("nav isLogin : ", isLogin);
console.log("nav userId : ", userId);
console.log("nav user : ", user);

const showMenu = ref(false);
const store = useStore();
const isRTL = computed(() => store.state.isRTL);

const { locale } = useI18n();

const currentLanguage = computed(() => {
  const currentLang = languages.find(lang => lang.code === locale.value);
  return currentLang ? currentLang.name : 'Unknown';
});

const changeLanguage = (langCode) => {
  locale.value = langCode;
};

const getFlagSrc = (languageName) => {
  const lang = languages.find(lang => lang.name === languageName);
  return lang ? lang.flag : '';
};

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


const fetchExchangeRates = async () => {
  try {
    const countryName = CURRENCY_NAME[countryCode.value];
    const usdToKrwUrl = `https://v6.exchangerate-api.com/v6/${import.meta.env.VITE_EXCHANGE_RATE_API_KEY}/pair/${countryName}/KRW`;
    const krwToUsdUrl = `https://v6.exchangerate-api.com/v6/${import.meta.env.VITE_EXCHANGE_RATE_API_KEY}/pair/KRW/${countryName}`;
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

    const currentToKrw = usdToKrwData.conversion_rate;
    const currentFromKrw = krwToUsdData.conversion_rate;

    exchangeStore.setCurrentToKrw(currentToKrw);
    exchangeStore.setCurrentFromKrw(currentFromKrw);

    console.log("환율 데이터가 성공적으로 로드되었습니다.");

    // 백엔드로 환율 데이터 전송
    await saveExchangeRates([
      {
        baseCode: countryCode, // 외화 코드
        targetCode: 0, // KRW 코드
        exchangeRate: currentToKrw,
      },
      {
        baseCode: 0, // KRW 코드
        targetCode: countryCode, // 외와 코드
        exchangeRate: currentFromKrw * 1000,
      },
    ]);
  } catch (error) {
    console.error("Error fetching exchange rate data", error);
  }
};

const saveExchangeRates = async (rates) => {
  try {
    const response = await axios.post("/api/exchange/rates", rates);
    console.log("환율 데이터가 성공적으로 저장되었습니다:", response.data);
  } catch (error) {
    console.error(
      "환율 데이터 저장 중 오류 발생:",
      error.response ? error.response.data : error.message
    );
  }
};

onMounted(async () => {
  if (auth.userId) {
    await auth.fetchUser(auth.userId);
    userImg.value = user.value?.profilePic
    fetchExchangeRates();
  } else {
    console.error('사용자 ID를 찾을 수 없습니다.');
  }
});
</script>

<style scoped>
.img-div {
  width: 20px;
  height: 20px;
  position: relative;
}

.user-profile-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

@font-face {
  font-family: "HakgyoansimDunggeunmisoTTF-B";
  src: url("https://fastly.jsdelivr.net/gh/projectnoonnu/2408-5@1.0/HakgyoansimDunggeunmisoTTF-B.woff2") format("woff2");
  font-weight: 700;
  font-style: normal;
}

.sidenav-toggler-line {
  transform: none !important;
}

.sidenav-toggler-line.fixed-width {
  width: 100% !important;
}

.flag-icon {
  width: 20px;
  height: 15px;
  object-fit: cover;
}

.language-dropdown {
  padding: 5px;
  border-color: #e9ecef;
}

.language-dropdown .dropdown-toggle::after {
  display: none;
}

.language-dropdown .nav-link {
  padding: 0.25rem 0.5rem;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
}

.language-dropdown .language-menu {
  min-width: 120px;
}

.language-dropdown .dropdown-item {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
}
</style>