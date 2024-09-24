<template>
  <div class="container-fluid py-4">
    <h3>Compare Exchange Rate</h3>

    <!-- USD to KRW Section -->
    <div class="card my-4">
      <div class="card-body">
        <div class="row px-3">
          <div class="col-md-8">
            <h6 class="mt-3">1 USD = {{ currentToKrw }} KRW</h6>
            <ExchangeRateChart chartId="toexchangeChart" :period="toSelectedPeriod" chartType="to" />
            <div class="chart-button-container">
              <template v-for="period in ['1y', '6m', '3m', '1m']" :key="period">
                <button class="chart-btn" :class="{ selected: toSelectedPeriod === period }"
                  @click="setToPeriod(period)">
                  {{ period }}
                </button>
              </template>
            </div>
          </div>
          <div class="col-md-4 d-flex flex-column justify-content-center">
            <input type="number" class="form-control mb-3" v-model.number="usdAmount" @input="convertToKrw"
              aria-label="Amount in USD" />
            <span class="text-center mb-3">=</span>
            <input type="text" class="form-control mb-3" :value="krwAmount" readonly aria-label="Amount in KRW" />
            <button class="btn btn-primary w-100">Buy</button>
          </div>
        </div>
      </div>
    </div>

    <!-- KRW to USD Section -->
    <div class="card my-4">
      <div class="card-body">
        <div class="row px-3">
          <div class="col-md-8">
            <h6 class="mt-3">1 KRW = {{ currentFromKrw }} USD</h6>
                <ExchangeRateChart chartId="fromexchangeChart" :period="fromSelectedPeriod" chartType="from" />
                <div class="chart-button-container">
                  <template v-for="period in ['1y', '6m', '3m', '1m']" :key="period">
                    <button class="chart-btn" :class="{ selected: fromSelectedPeriod === period }"
                      @click="setFromPeriod(period)">
                      {{ period }}
                    </button>
                  </template>
                </div>
          </div>
          <div class="col-md-4 d-flex flex-column justify-content-center">
            <input type="number" class="form-control mb-3" v-model.number="krwAmountReverse" @input="convertToUsd"
              aria-label="Amount in KRW" />
            <span class="text-center mb-3">=</span>
            <input type="number" class="form-control mb-3" :value="usdAmountReverse" readonly
              aria-label="Amount in USD" />
            <button class="btn btn-danger w-100">Sell</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Notification & Auto Settings Section -->
    <div class="card mt-4">
      <div class="card-body">
        <div class="row">
          <div class="col-lg-12">
            <div @click="$router.push('/set-alert')" class="alert alert-info clickable-alert" role="button" tabindex="0"
              @keypress.enter="$router.push('/set-alert')" aria-label="Set an alert for exchange rates">
              <span>Set an alert for exchange rates</span>
            </div>
            <div class="form-group">
              <label for="autoCondition">Auto Condition</label>
              <input type="text" class="form-control" id="autoCondition"
                value="Target Exchange: 1,330 KRW. Current rate: 1,000,000 KRW = 90 USD" readonly />
            </div>
            <div class="form-group">
              <label for="targetRate1">Target Rate 1</label>
              <input type="text" class="form-control" id="targetRate1" value="1,330 KRW" readonly />
            </div>
            <div class="form-group">
              <label for="targetRate2">Target Rate 2</label>
              <input type="text" class="form-control" id="targetRate2" value="1,329 KRW" readonly />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useExchangeStore } from "@/stores/exchangeStore";
import ExchangeRateChart from "@/views/Chart/ExchangeRateChart.vue";

// Data variables
const usdAmount = ref(1);
const krwAmount = ref(0);
const krwAmountReverse = ref(1000);
const usdAmountReverse = ref(0);

const store = useExchangeStore();

// 가져온 값을 Pinia에서 사용
const currentToKrw = computed(() => store.currentToKrw);
const currentFromKrw = computed(() => store.currentFromKrw);

// Conversion functions
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
    convertToKrw();
    convertToUsd();
  } catch (error) {
    console.error("환율 데이터를 가져오는 중 오류 발생:", error);
  }
};

onMounted(() => {
  fetchExchangeRates();
});

// Period state
const toSelectedPeriod = ref("1y");
const fromSelectedPeriod = ref("1y");

// Set period functions
const setToPeriod = (period) => {
  toSelectedPeriod.value = period;
};

const setFromPeriod = (period) => {
  fromSelectedPeriod.value = period;
};
</script>

<style scoped>
.clickable-alert {
  text-align: center;
  cursor: pointer;
}

.flag-icon {
  width: 50px;
  /* 적절한 너비로 설정 */
  height: auto;
  /* 자동 높이 조정 */
  vertical-align: middle;
  /* 텍스트와 이미지 정렬 맞추기 */
}

.chart-button-container {
  display: flex;
  justify-content: center;
  width: 100%;
  padding: 1rem;
}


.chart-btn {
  flex-grow: 1;
  /* 버튼들이 남은 공간을 균등하게 차지 */
  padding: 1px 0;
  /* 버튼 높이 */
  background-color: #e0e0e0;
  /* 기본 배경 색상 */
  color: #333;
  /* 기본 텍스트 색상 */
  border: none;
  border-radius: 20px;
  /* 둥근 모서리 */
  text-align: center;
  cursor: pointer;
  margin: 0 5px;
  /* 버튼 간 좌우 간격 */
}

.chart-btn.selected {
  background-color: #ffd700;
  /* 선택된 버튼의 배경색 */
  color: #fff;
  /* 선택된 버튼의 텍스트 색상 */
}

.chart-btn:hover {
  background-color: #f0f0f0;
  /* 호버 시 버튼 배경색 */
}
</style>
