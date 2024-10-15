<template>
  <div class="container">
    <div class="row justify-content-center">
      <div
        class="mx-auto col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-lg-0"
      >
        <div class="d-xl-none">
          <div class="text-center mb-3">
            <form @submit.prevent="handleQRScan">
              <button type="submit" class="btn btn-primary me-3">확인</button>
            </form>

            <img v-if="qrCodeUrl" :src="qrCodeUrl" alt="QR Code" />
            <p>{{ formattedCountdown }}</p>
          </div>

          <div
            class="btn btn-sm btn-warning mb-0 mx-4 d-flex justify-content-between"
          >
            <p class="mb-0">{{ $t("payment--qrScan--balanceLabel") }}</p>
            <p class="mb-0">{{ woneMoneyBalance }} KRW</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from "vue";
import { useRouter } from "vue-router";
import api from "../../api";
import paymentApi from "../../api/paymentApi";
import Swal from "sweetalert2";

import myaccountApi from "../../api/myaccountApi";

import { useAuthStore } from "@/stores/auth";
const auth = useAuthStore();
const user = computed(() => auth.user);

//i18n
import { useI18n } from "vue-i18n";
const { t } = useI18n();

const router = useRouter(); // Router 사용

const REFRESH_INTERVAL = 60; // 갱신 기간 60초
const qrCodeUrl = ref(""); // QR 코드 이미지의 URL
const countdown = ref(REFRESH_INTERVAL); // QR 코드 갱신까지 남은 시간을 저장하는 변수

const woneMoneyBalance = ref(0);

let countdownInterval = null; // 인터벌을 저장할 변수

const fetchKrwBalance = async () => {
  try {
    const balance = await myaccountApi.fetchkrwAccountBalance(user.value.krwNo);
    woneMoneyBalance.value = balance;
  } catch (error) {
    console.error("KRW 계좌 잔액 조회 중 오류 발생:", error);
  }
};

const generateQRCode = async () => {
  try {
    const dynamicUrl = encodeURIComponent(`/api/payment/qr-scan`);
    const response = await api.get("/api/payment/qr", {
      params: { url: dynamicUrl, _: new Date().getTime() },
      responseType: "blob",
    });

    // console.log("QR Code response: ", response);

    if (response && response.data) {
      // Blob 데이터를 URL로 변환
      const qrCodeBlob = response.data;
      const qrCodeObjUrl = URL.createObjectURL(qrCodeBlob);
      qrCodeUrl.value = qrCodeObjUrl;
      // console.log("QR Code URL: ", qrCodeUrl);
    } else {
      console.error("QR Code response data is invalid", response);
    }
  } catch (error) {
    console.error("QR 코드 생성 중 오류 발생:", error);
  }
};

const startCountdown = () => {
  countdown.value = REFRESH_INTERVAL; // 카운트다운을 초기화

  countdownInterval = setInterval(() => {
    if (countdown.value > 0) {
      countdown.value--; // 매 초마다 countdown을 감소시킴
    } else {
      generateQRCode(); // 카운트다운이 0이 되면 QR 코드 갱신
      countdown.value = REFRESH_INTERVAL; // 다시 카운트다운을 리셋
    }
  }, 1000);
};

// 분:초 형식으로 카운트다운을 표시
const formattedCountdown = computed(() => {
  const minutes = Math.floor(countdown.value / 60);
  const seconds = countdown.value % 60;
  return `${minutes}:${seconds.toString().padStart(2, "0")}`;
});

const req = {
  amount: 1000,
  userId: user.value.userId,
  krwNo: user.value.krwNo,
  songNo: user.value.songNo,
  historyContent: "SongSong Restaurant",
  // userDTO: user.value
};

// 새로고침시 /payment로 이동
const handleUnload = (event) => {
  event.preventDefault();
  router.push("/payment");
};

// QR 스캔 후 처리
const handleQRScan = async () => {
  try {
    const response = await paymentApi.scanQRCode(req);

    console.log(response);
    if (response.status === 200) {
      window.addEventListener("beforeunload", handleUnload);
      Swal.fire({
        title: t("swal--title-success"),
        text: t("payment--swal-success-text"),
        icon: "success",
      }).then(() => {
        router.push("/my-page");
      });
    }
    console.log(response);
  } catch (error) {
    console.log(error);
    Swal.fire({
      title: t("swal--title-fail"),
      text: t("payment--swal-fail-text"),
      icon: "error",
    });
  }
};

onMounted(() => {
  // KRW 계좌 잔액 조회
  fetchKrwBalance();

  // 초기 QR 코드 생성
  generateQRCode();

  // QR 코드 갱신을 위한 카운트다운 시작
  startCountdown();

  // window.addEventListener("beforeunload", handleUnload);
});

onBeforeUnmount(() => {
  // 컴포넌트가 파괴되기 전에 인터벌 클리어
  if (countdownInterval) {
    clearInterval(countdownInterval);
  }

  window.removeEventListener("beforeunload", handleUnload);
});
</script>
