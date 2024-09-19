<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="mx-auto col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-lg-0">
        <div class="d-none d-xl-block">
          <div>
            <img src="../../assets/img/mushroom.png" class="p-3 w-100" alt="mushroom" />
          </div>
          <h4 class="text-center">Our payment system is only available in a mobile environment.</h4>
        </div>
        <div class="d-xl-none">
          <div class="text-center mb-3">
            <img :src="qrCodeUrl" alt="QR Code">
            <p>{{ formattedCountdown }}</p>
          </div>
          <div class="btn btn-sm btn-warning mb-0 mx-4 d-flex justify-content-between">
            <p class="mb-0">보유잔액</p>
            <p class="mb-0">10,000원</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'

export default {
  setup() {
    const REFRESH_INTERVAL = 60; //갱신 기간 60초
    const qrCodeUrl = ref(''); // QR 코드 이미지의 URL
    const countdown = ref(REFRESH_INTERVAL); // QR 코드 갱신까지 남은 시간을 저장하는 변수
    let countdownInterval = null; // 인터벌을 저장할 변수

    const generateQRCode = () => {
      const url = encodeURIComponent('https://www.google.com/?hl=ko')
      qrCodeUrl.value = `http://localhost:8888/api/payment/qr?url=${url}&_=${new Date().getTime()}`;
    };

    const startCountdown = () => {
      countdown.value = REFRESH_INTERVAL; // 카운트다운을 초기화

      countdownInterval = setInterval(() => {
        if (countdown.value > 0) {
          countdown.value--; // 매 초마다 countdown을 감소시킴
        } else {
          generateQRCode(); // 카운트다운이 0이 되면 QR 코드 갱신
          countdown.value = REFRESH_INTERVAL;    // 다시 카운트다운을 리셋
        }
      }, 1000);
    }

    // 분:초 형식으로 카운트다운을 표시
    const formattedCountdown = computed(() => {
      const minutes = Math.floor(countdown.value / 60);
      const seconds = countdown.value % 60;
      return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    });

    onMounted(() => {
      // 컴포넌트가 마운트되면 초기 QR 코드 생성
      generateQRCode();

      // QR 코드 갱신을 위한 카운트다운 시작
      startCountdown();
    });

    onBeforeUnmount(() => {
      // 컴포넌트가 파괴되기 전에 인터벌 클리어
      if (countdownInterval) {
        clearInterval(countdownInterval);
      }
    });

    return {
      qrCodeUrl,
      formattedCountdown
    };
  }
};
</script>