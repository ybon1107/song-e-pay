<template>
  <div class="container-fluid py-4">
    <h5>Compare Exchange Rate</h5>
    <div class="row">
      <!-- USD to KRW Section -->
      <div class="col-lg-6 col-md-12">
        <div class="card">
          <div class="card-body">
            <div class="d-flex justify-content-between">
              <h6>1 USD = {{ currentToKrw }} KRW</h6>
            </div>
            <ExchangeRateChart chartId="toexchangeChart" />
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
            <button class="btn btn-primary w-100">Buy</button>
          </div>
        </div>
      </div>

            <!-- KRW to USD Section -->
            <div class="col-lg-6 col-md-12">
                <div class="card">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <h6>1 KRW = {{ currentFromKrw }} USD</h6>
                        </div>
                        <div class="chart-container">
                            <canvas id="krwToUsdChart"></canvas>
                        </div>
                        <div class="input-group my-3">
                            <input
                                type="number"
                                class="form-control"
                                v-model.number="krwAmountReverse"
                                @input="convertToUsd"
                            />
                            <div class="input-group-append">
                                <img
                                    src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg"
                                    alt="한국 국기"
                                    class="flag-icon"
                                />
                            </div>
                            <span class="input-group-text">=</span>
                            <input
                                type="number"
                                class="form-control"
                                :value="usdAmountReverse"
                                readonly
                            />
                            <div class="input-group-append">
                                <span class="input-group-text">USD</span>
                            </div>
                        </div>
                        <button class="btn btn-danger w-100">Sell</button>
                    </div>
                </div>
            </div>
            <ExchangeRateChart chartId="fromexchangeChart" />
            <div class="input-group my-3">
              <input
                type="number"
                class="form-control"
                v-model.number="krwAmountReverse"
                @input="convertToUsd"
              />
              <div class="input-group-append">
                <img
                  src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg"
                  alt="한국 국기"
                  class="flag-icon"
                />
              </div>
              <span class="input-group-text">=</span>
              <input
                type="number"
                class="form-control"
                :value="usdAmountReverse"
                readonly
              />
              <div class="input-group-append">
                <span class="input-group-text">USD</span>
              </div>
            </div>
            <button class="btn btn-danger w-100">Sell</button>
          </div>
        </div>

        <!-- Notification & Auto Settings Section -->
        <div class="row my-4">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-body">
                        <div
                            @click="$router.push('/set-alert')"
                            class="alert alert-info clickable-alert"
                            role="button"
                        >
                            <span>Set an alert for exchange rates</span>
                        </div>
                        <div class="form-group">
                            <label for="autoCondition">Auto Condition</label>
                            <input
                                type="text"
                                class="form-control"
                                id="autoCondition"
                                value="Target Exchange: 1,330 KRW. Current rate: 1,000,000 KRW = 90 USD"
                            />
                        </div>
                        <div class="form-group">
                            <label for="targetRate1">Target Rate 1</label>
                            <input
                                type="text"
                                class="form-control"
                                id="targetRate1"
                                value="1,330 KRW"
                            />
                        </div>
                        <div class="form-group">
                            <label for="targetRate2">Target Rate 2</label>
                            <input
                                type="text"
                                class="form-control"
                                id="targetRate2"
                                value="1,329 KRW"
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useExchangeStore } from "@/stores/exchangeStore";
import ExchangeRateChart from "@/views/Chart/ExchangeRateChart.vue";

// API URL과 API 키를 설정합니다.
const usdToKrwUrl = "/api/exchange/pair/USD/KRW";
const krwToUsdUrl = "/api/exchange/pair/KRW/USD";

// 데이터 변수
const usdAmount = ref(1); // 초기값을 1로 설정
const krwAmount = ref(0); // 초기값을 0으로 설정, 계산 후 업데이트
const krwAmountReverse = ref(1000); // 초기값을 1로 설정
const usdAmountReverse = ref(0); // 초기값을 0으로 설정, 계산 후 업데이트
const currentToKrw = ref(0);
const currentFromKrw = ref(0);

const store = useExchangeStore();

// 환율 데이터를 가져오는 함수
const fetchExchangeRates = async () => {
    try {
        const usdToKrwResponse = await fetch(usdToKrwUrl);
        if (!usdToKrwResponse.ok) {
            throw new Error('Network response was not ok for USD to KRW');
        }
        const usdToKrwData = await usdToKrwResponse.json();
        currentToKrw.value = usdToKrwData.conversion_rate;

        const krwToUsdResponse = await fetch(krwToUsdUrl);
        if (!krwToUsdResponse.ok) {
            throw new Error('Network response was not ok for KRW to USD');
        }
        const krwToUsdData = await krwToUsdResponse.json();
        currentFromKrw.value = krwToUsdData.conversion_rate;

        store.setCurrentToKrw(currentToKrw.value);

        // 환율 데이터를 가져온 후 계산된 값으로 초기화
        krwAmount.value = (usdAmount.value * currentToKrw.value).toFixed(2);
        usdAmountReverse.value = (
            krwAmountReverse.value * currentFromKrw.value
        ).toFixed(2);
    } catch (error) {
        console.error('Error fetching exchange rate data:', error);
    }
};

// 환율 변환 함수
const convertToKrw = () => {
    krwAmount.value = (usdAmount.value * currentToKrw.value).toFixed(2);
};

const convertToUsd = () => {
    usdAmountReverse.value = (
        krwAmountReverse.value * currentFromKrw.value
    ).toFixed(2);
};

// Watchers to update values when exchange rates change
watch(currentToKrw, convertToKrw);
watch(currentFromKrw, convertToUsd);

// 컴포넌트가 마운트된 후 환율 데이터 가져오기
onMounted(() => {
    fetchExchangeRates();
});
</script>

<style scoped>
.chart-container {
  position: relative;
  height: 40vh;
  width: 100%;
}

.clickable-alert {
    text-align: center;
    cursor: pointer;
}

.flag-icon {
    width: 50px; /* 적절한 너비로 설정 */
    height: auto; /* 자동 높이 조정 */
    vertical-align: middle; /* 텍스트와 이미지 정렬 맞추기 */
}
</style>
