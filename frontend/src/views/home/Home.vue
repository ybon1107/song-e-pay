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
              <ExchangeRateChart
                chartId="toexchangeChart"
                period="1m"
                chartType="to"
              />
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
              <input
                type="number"
                class="form-control"
                v-model.number="usdAmount"
                @input="convertToKrw"
              />
              <div class="input-group-append">
                <span class="input-group-text">USD</span>
              </div>
              <span class="input-group-text">=</span>
              <input
                type="text"
                class="form-control"
                :value="krwAmount"
                readonly
              />
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
              <input
                type="number"
                class="form-control"
                v-model.number="krwAmountReverse"
                @input="convertToUsd"
              />
              <div class="input-group-append">
                <span class="input-group-text">KRW</span>
              </div>
              <span class="input-group-text">=</span>
              <input
                type="text"
                class="form-control"
                :value="usdAmountReverse"
                readonly
              />
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
  </div>

  <div class="map-area">
    <MapComponent />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useExchangeStore } from "@/stores/exchangeStore";
import ExchangeRateChart from "@/views/Chart/ExchangeRateChart.vue";
import Calendar from "../Templates/Calendar.vue";
import MapComponent from "@/views/maps/MapComponent.vue";

const store = useExchangeStore();

// Define reactive variables
const usdAmount = ref(1);
const krwAmount = ref(0);
const krwAmountReverse = ref(1000);
const usdAmountReverse = ref(0);

const currentToKrw = computed(() => store.currentToKrw);
const currentFromKrw = computed(() => store.currentFromKrw);

const convertToKrw = () => {
  krwAmount.value = (usdAmount.value * currentToKrw.value).toFixed(2);
};

const convertToUsd = () => {
  usdAmountReverse.value = (
    krwAmountReverse.value * currentFromKrw.value
  ).toFixed(2);
};

// 환율 데이터를 가져오는 함수
const fetchExchangeRates = async () => {
  try {
    console.log(" 확인");
    convertToKrw();
    convertToUsd();
  } catch (error) {
    console.error("환율 데이터를 가져오는 중 오류 발생:", error);
  }
};

onMounted(() => {
  fetchExchangeRates();
});
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
