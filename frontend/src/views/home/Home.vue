<template>
  <div class="container-fluid py-4">
    <!-- Currency Cards Section -->
    <div class="row">
      <!-- USD Wallet -->
      <div class="col-lg-6 col-md-12">
        <div class="card text-center">
          <div class="card-body">
            <h6>My Wallet (USD)</h6>
            <div class="wallet-balance">
              <h3>0 USD</h3>
            </div>
            <button class="btn btn-primary">Deposit</button>
            <button class="btn btn-secondary">Withdraw</button>
          </div>
        </div>
      </div>

      <!-- KRW Wallet -->
      <div class="col-lg-6 col-md-12">
        <div class="card text-center">
          <div class="card-body">
            <h6>My Wallet (KRW)</h6>
            <div class="wallet-balance">
              <h3>0 KRW</h3>
            </div>
            <button class="btn btn-primary">Deposit</button>
            <button class="btn btn-secondary">Withdraw</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Graph and Conversion Section -->
    <div class="row my-4">
      <!-- Exchange Rate Graph Section -->
      <div class="col-lg-8 col-md-12">
        <div class="card">
          <div class="card-body">
            <h6>Exchange Rate</h6>
            <div class="chart-container">
              <ExchangeRateChart chartId="toexchangeChart" />
            </div>
          </div>
        </div>
      </div>

      <!-- Conversion Section -->
      <div class="col-lg-4 col-md-12">
        <!-- USD to KRW Conversion -->
        <div class="card mb-4">
          <div class="card-body">
            <h6>Convert USD to KRW</h6>
            <div class="input-group my-3">
              <input type="number" class="form-control" v-model.number="usdAmount" @input="convertToKrw" />
              <div class="input-group-append">
                <span class="input-group-text">USD</span>
              </div>
              <span class="input-group-text">=</span>
              <input type="text" class="form-control" :value="krwAmount" readonly />
              <div class="input-group-append">
                <span class="input-group-text">KRW</span>
              </div>
            </div>
          </div>
        </div>

        <!-- KRW to USD Conversion -->
        <div class="card">
          <div class="card-body">
            <h6>Convert KRW to USD</h6>
            <div class="input-group my-3">
              <input type="number" class="form-control" v-model.number="krwAmountReverse" @input="convertToUsd" />
              <div class="input-group-append">
                <span class="input-group-text">KRW</span>
              </div>
              <span class="input-group-text">=</span>
              <input type="text" class="form-control" :value="usdAmountReverse" readonly />
              <div class="input-group-append">
                <span class="input-group-text">USD</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Calendar Section -->
    <div class="row my-4">
      <div class="col-lg-12">
        <div class="calendar-container">
          <!-- Insert calendar here -->
          <calendar></calendar>
        </div>
      </div>
    </div>

    <div class="map-area">
      <!-- 클릭 시 /maps 페이지로 이동 -->
      <button @click="goToMap" class="go-to-map-btn">Go to Map</button>

      <!-- Kakao 지도 표시 -->
      <div id="mini-map"></div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import ExchangeRateChart from "@/views/Chart/ExchangeRateChart.vue";
import Calendar from "../Templates/Calendar.vue";

// Define reactive variables
const usdAmount = ref(0);
const krwAmount = ref(0);
const krwAmountReverse = ref(0);
const usdAmountReverse = ref(0);

// Define conversion functions
const convertToKrw = () => {
  krwAmount.value = usdAmount.value * 1330; // Example conversion rate
};

const convertToUsd = () => {
  usdAmountReverse.value = krwAmountReverse.value / 1330; // Example conversion rate
};


import { useRouter } from 'vue-router';
import { onMounted } from 'vue';

const router = useRouter();

// 지도 페이지로 이동하는 함수
const goToMap = () => {
  router.push('/maps');
};

// Kakao 지도 API 키
const MAP_API_KEY = '33cf94244b357b4aa60393fea33c07ba';

onMounted(() => {
  // Kakao 지도 API 비동기 로드
  const script = document.createElement('script');
  script.onload = () => {
    if (window.kakao && window.kakao.maps) {
      window.kakao.maps.load(() => initializeMap());
    }
  };
  script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${MAP_API_KEY}&libraries=services`;
  document.head.appendChild(script);
});

function initializeMap() {
  const mapContainer = document.getElementById('mini-map');
  const mapOption = {
    center: new window.kakao.maps.LatLng(37.497942, 127.027621), // 서울시 강남구 좌표
    level: 5, // 지도의 확대 레벨
  };

  // 지도 객체 생성
  const map = new window.kakao.maps.Map(mapContainer, mapOption);

  // 지도에 마커 추가 (서울 강남구 국민은행 예시)
  const markerPosition = new window.kakao.maps.LatLng(37.497942, 127.027621);
  const marker = new window.kakao.maps.Marker({
    position: markerPosition,
    map: map,
  });
}
</script>

<style scoped>
.main-page-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-start;
}

.content-area {
  flex: 2;
  padding: 20px;
  background-color: #f8f9fa;
}

.map-area {
  flex: 1;
  padding: 10px;
  background-color: #e9ecef;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
}

#mini-map {
  width: 100%;
  height: 200px;
  /* 미니맵의 높이 설정 */
  border-radius: 8px;
}

/* Go to Map 버튼 스타일 */
.go-to-map-btn {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  /* 중앙 정렬을 위한 transform */
  padding: 10px 20px;
  /* 버튼의 크기를 키우기 위해 padding을 조절 */
  font-size: 16px;
  /* 글자 크기를 조금 더 키움 */
  color: black;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  z-index: 10;
  /* 지도 위에 버튼을 보이도록 설정 */
}

.go-to-map-btn:hover {
  background-color: #0056b3;
}

.container-fluid {
  max-width: 1200px;
}

.card {
  margin-bottom: 20px;
}

.wallet-balance h3 {
  margin-bottom: 20px;
}

.btn {
  margin: 5px;
}

.chart-container {
  display: flex;
  height: 50vh;
  /* Adjusts the height based on 50% of the viewport height */
  width: 100%;
  /* Ensures full width */
  align-items: center;
  /* Centers chart vertically */
  justify-content: center;
  /* Centers chart horizontally */
}

/* Additional responsiveness */
@media (max-width: 768px) {
  .calendar-container {
    height: auto;
  }
}
</style>
