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
          <li class="nav-item d-xl-none ps-3 d-flex align-items-center me-3">
            <a
              href="#"
              @click="minimizeSidebar"
              class="p-0 nav-link"
              id="iconNavbarSidenav"
            >
              <div class="sidenav-toggler-inner">
                <i class="sidenav-toggler-line fixed-width"></i>
                <i class="sidenav-toggler-line"></i>
                <i class="sidenav-toggler-line fixed-width"></i>
              </div>
            </a>
          </li>

          <!-- 언어 선택 -->
          <li class="nav-item dropdown language-dropdown border rounded">
            <a
              class="nav-link dropdown-toggle d-flex align-items-center"
              href="#"
              id="languageDropdown"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              <img
                :src="getFlagSrc(currentLanguage)"
                alt="Flag"
                class="me-2 flag-icon"
              />
              {{ $t(currentLanguage) }}
            </a>
            <ul class="dropdown-menu" aria-labelledby="languageDropdown">
              <li v-for="lang in languages" :key="lang.code">
                <a
                  class="dropdown-item d-flex align-items-center"
                  href="#"
                  @click="changeLanguage(lang.code)"
                >
                  <img
                    :src="lang.flag"
                    :alt="`${lang.name} Flag`"
                    class="me-2 flag-icon"
                  />
                  {{ $t(lang.name) }}
                </a>
              </li>
            </ul>
          </li>

          <!-- 알림 -->
          <li
            class="px-3 nav-item dropdown d-flex align-items-center notification-dropdown"
          >
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
                <i class="cursor-pointer fa fa-bell"></i>
                <span v-if="unreadCount > 0" class="badge bg-danger">{{
                  unreadCount
                }}</span>
              </div>
            </a>

            <ul
              class="px-2 py-3 dropdown-menu dropdown-menu-end me-sm-n4 noti-ul"
              :class="showMenu ? 'show' : ''"
              aria-labelledby="dropdownMenuButton"
            >
              <div class="notifications-container">
                <li v-if="!noti || noti.length === 0" class="mb-2">
                  <a class="dropdown-item border-radius-md" href="javascript:;">
                    <div class="py-1 d-flex justify-content-center">
                      <p class="mb-0 text-sm">알림이 없습니다.</p>
                    </div>
                  </a>
                </li>

                <li
                  v-for="(notification) in noti"
                  :key="notification.notiNo"
                  class="mb-2 position-relative"
                  style="background-color: white; border-radius: 10px"
                >
                  <a
                    class="dropdown-item border-radius-md"
                    href="javascript:;"
                    :class="{
                      'read-notification': notification.check === '1',
                    }"
                    @click.stop="readNotification(notification.notiNo)"
                  >
                    <div class="py-1 d-flex">
                      <div class="my-auto">
                        <img
                          :src="notification.senderProfilePic || alertImg"
                          class="avatar avatar-sm me-3"
                          alt="user image"
                        />
                      </div>
                      <div
                        class="d-flex flex-column justify-content-center flex-grow-1"
                      >
                        <h6
                          class="mb-1 text-sm"
                          :class="{
                            'text-muted': notification.check === '1',
                          }"
                        >
                          {{ notification.content }}
                        </h6>
                        <p
                          class="mb-0 text-xs"
                          :class="
                            notification.check === '1' ? 'text-muted' : ''
                          "
                        >
                          <i class="fa fa-clock me-1"></i>
                          {{ formatDate(notification.createdAt) }}
                        </p>
                        <!-- 송금받기 버튼 추가 -->
                        <button
                          v-if="notification.amount > 0"
                          @click.stop="
                            receiveTransfer(
                              notification.notiNo,
                              notification.amount
                            )
                          "
                          class="btn btn-primary mt-2"
                        >
                          송금받기
                        </button>
                      </div>
                    </div>
                  </a>
                  <button
                    @click.stop="deleteNotification(notification.notiNo)"
                    class="btn-close-custom position-absolute top-0 end-0 mt-2 me-2"
                  >
                    <i class="fas fa-times"></i>
                  </button>
                </li>
              </div>
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
import {
  computed,
  ref,
  onMounted,
  watchEffect,
  reactive,
  onUnmounted,
} from "vue";
import axios from "axios";
import { useStore } from "vuex";
import { useExchangeStore } from "@/stores/exchangeStore";
import { CURRENCY_NAME } from "@/constants/countryCode";
import { useAuthStore } from "@/stores/auth";
import { useI18n } from "vue-i18n";
import { languages } from "@/constants/languages";
import notiApi from "@/api/notificationApi";
import { useWebSocket } from "@/utils/websocket";
import myaccountApi from "@/api/myaccountApi";
import alertImg from "@/assets/img/alert_img.png";
import exchangeApi from "../../api/exchangeApi";
const auth = useAuthStore();
const user = computed(() => auth.user);

const exchangeStore = useExchangeStore();
const userImg = ref("");

const isLogin = computed(() => auth.isLogin);
const userId = computed(() => auth.userId);

const countryCode = ref(1);

const noti = reactive([]);
const unreadCount = ref(0);
const { connect, disconnect, isConnected } = useWebSocket();

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
  const currentLang = languages.find((lang) => lang.code === locale.value);
  return currentLang ? currentLang.name : "Unknown";
});

const changeLanguage = (langCode) => {
  locale.value = langCode;
  localStorage.setItem("Language", langCode);
};

const getFlagSrc = (languageName) => {
  const lang = languages.find((lang) => lang.name === languageName);
  return lang ? lang.flag : "";
};

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

    // const currentToKrw = usdToKrwData.conversion_rate;
    // const currentFromKrw = krwToUsdData.conversion_rate;
    const currentToKrw = parseFloat(usdToKrwData.conversion_rate.toFixed(5)); //json에서 환율 값만 추출
    const currentFromKrw = parseFloat(krwToUsdData.conversion_rate.toFixed(5));

    exchangeStore.setCurrentToKrw(currentToKrw);
    exchangeStore.setCurrentFromKrw(currentFromKrw);

    console.log("환율 데이터가 성공적으로 로드되었습니다.");

    // 백엔드로 환율 데이터 전송
    // await saveExchangeRates([
    //     {
    //         baseCode: countryCode.value, // 외화 코드
    //         targetCode: 0, // KRW 코드
    //         exchangeRate: currentToKrw,
    //     },
    //     {
    //         baseCode: 0, // KRW 코드
    //         targetCode: countryCode.value, // 외와 코드
    //         exchangeRate: currentFromKrw,
    //     },
    // ]);
  } catch (error) {
    console.error("Error fetching exchange rate data", error);
  }
};

const saveExchangeRates = async (rates) => {
  try {
    const response = await exchangeApi.setExchange(rates);
    console.log("환율 데이터가 성공적으로 저장되었습니다:", response.data);
  } catch (error) {
    console.error(
      "환율 데이터 저장 중 오류 발생:",
      error.response ? error.response.data : error.message
    );
  }
};

// 알림창 내용
const formatDate = (dateString) => {
  const date = new Date(dateString);
  const now = new Date();
  const diffInSeconds = Math.floor((now - date) / 1000);
  
  if (diffInSeconds < 60) return "방금 전";
  if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}분 전`;
  if (diffInSeconds < 86400)
    return `${Math.floor(diffInSeconds / 3600)}시간 전`;
  if (diffInSeconds < 604800)
    return `${Math.floor(diffInSeconds / 86400)}일 전`;

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};

// 알림 읽기
const readNotification = async (notiNo) => {
  try {
    await notiApi.readNotification(notiNo);
    const notificationIndex = noti.findIndex((item) => item.notiNo === notiNo);
    if (notificationIndex !== -1) {
      noti[notificationIndex].check = "1";
      // 읽지 않은 알림 수 업데이트
      unreadCount.value = noti.filter((n) => n.check === "0").length;
    }
    // 변경 사항을 Vue에 알림
    noti.push({});
    noti.pop();
  } catch (error) {
    console.error("알림 읽기 실패:", error);
  }
};

// 알림 삭제
const deleteNotification = async (notiNo) => {
  console.log("삭제할 알림 ID : ", notiNo);
  try {
    await notiApi.deleteNotification(notiNo);
    console.log("알림이 성공적으로 삭제되었습니다.");

    // 화면에서 해당 알림 제거
    const index = noti.findIndex((item) => item.notiNo === notiNo);
    if (index !== -1) {
      noti.splice(index, 1);
    }

    // 읽지 않은 알림 수 업데이트
    unreadCount.value = noti.filter((n) => n.check === "0").length;

    // 변경 사항을 Vue에 알림
    noti.push({});
    noti.pop();
  } catch (error) {
    console.error("알림 삭제 중 오류 발생:", error);
    alert("알림 삭제 중 오류가 발생했습니다. 다시 시도해 주세요.");
  }
};


const fetchNotifications = async () => {
  try {
    const notifications = await notiApi.getNotification(auth.userId);
    noti.splice(0, noti.length, ...notifications);
    unreadCount.value = notifications.filter((n) => n.check === "0").length;
  } catch (error) {
    console.error("알림 데이터를 가져오는 중 오류 발생:", error);
  }
};

// 송금받기
const receiveTransfer = async (notiNo, amount) => {
  const transferData = {
    userId: user.value.userId,
    notiNo: notiNo,
    amount: amount,
  };

  if (confirm("송금받기를 하시겠습니까?")) {
    try {
      await myaccountApi.receiveTransfer(transferData);
      alert("송금받기가 성공적으로 완료되었습니다.");

      // 송금 후 amount를 0으로 설정
      const notificationIndex = noti.findIndex(
        (item) => item.notiNo === notiNo
      );
      if (notificationIndex !== -1) {
        noti[notificationIndex].amount = 0;
        // 변경 사항을 Vue에 알림
        noti.push({});
        noti.pop();
      }
    } catch (error) {
      console.error("송금받기 중 오류 발생:", error);
      alert("송금받기 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
  } else {
    alert("송금받기가 취소되었습니다.");
  }
};

// 웹소켓
const handleNewNotification = (newNotification) => {
  // newNotification의 구조를 확인
  console.log('New Notification:', newNotification);

  noti.unshift(newNotification);
  unreadCount.value++;
};

onMounted(async () => {
  // 언어 설정
  const savedLanguage = localStorage.getItem("Language");
  if (savedLanguage) {
    locale.value = savedLanguage;
  }

  // 유정 정보 설정
  if (auth.userId) {
    await auth.fetchUser(auth.userId);
    userImg.value = user.value?.profilePic;
    fetchExchangeRates();
    connect(handleNewNotification);

    // 초기 알림 가져오기
    await fetchNotifications();

  } else {
    console.error("사용자 ID를 찾을 수 없습니다.");
  }
});

onUnmounted(() => {
  disconnect();

});
</script>

<style scoped>
.noti-ul {
  background-color: #dadada;
}

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
  src: url("https://fastly.jsdelivr.net/gh/projectnoonnu/2408-5@1.0/HakgyoansimDunggeunmisoTTF-B.woff2")
    format("woff2");
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
  height: 20px;
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

.read-notification {
  background-color: #f0f0f0 !important;
}

.text-muted {
  color: rgb(157, 159, 161) !important;
}

.notifications-container {
  max-height: 220px;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #888 #f1f1f1;
}

.notifications-container::-webkit-scrollbar {
  width: 6px;
}

.notifications-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.notifications-container::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 3px;
}

.notifications-container::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.dropdown-item {
  padding-right: 2rem;
}

.btn-close-custom {
  font-size: 0.8rem;
  padding: 0.5rem;
  width: 2rem;
  height: 2rem;
  background-color: transparent;
  border: 1px solid #dee2e6;
  /* 테두리 추가 */
  border-radius: 50%;
  /* 원형 모양으로 만들기 */
  color: #6c757d;
  opacity: 0.5;
  cursor: pointer;
  transition:
    opacity 0.2s ease-in-out,
    background-color 0.2s ease-in-out;
  /* 배경색 전환 효과 추가 */
  z-index: 10;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-close-custom:hover {
  opacity: 1;
  background-color: #f8f9fa;
  /* 호버 시 배경    변경 */
}

/* 새로운 스타일 추가 */
.dropdown-item {
  position: relative;
  z-index: 1;
}

.btn-close-custom i {
  font-size: 1rem;
  /* 아이콘 크기 증가 */
}

.flex-grow-1 {
  flex-grow: 1;
}

.badge {
  position: absolute;
  top: -5px;
  right: -5px;
  font-size: 0.7rem;
}
</style>
