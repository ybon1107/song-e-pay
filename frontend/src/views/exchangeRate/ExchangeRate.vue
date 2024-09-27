<template>
  <div class="container-fluid py-4">
    <h3>Compare Exchange Rate</h3>

    <!-- USD to KRW Section -->
    <div class="card my-4">
      <div class="card-body">
        <div class="row px-3">
          <div class="col-md-8">
            <h6 class="mt-3">1 USD = {{ currentToKrw }} KRW</h6>
            <ExchangeRateChart
              chartId="toexchangeChart"
              :period="toSelectedPeriod"
              chartType="to"
            />
            <div class="chart-button-container">
              <template
                v-for="period in ['1y', '6m', '3m', '1m']"
                :key="period"
              >
                <button
                  class="chart-btn"
                  :class="{ selected: toSelectedPeriod === period }"
                  @click="setToPeriod(period)"
                >
                  {{ period }}
                </button>
              </template>
            </div>
          </div>
          <div class="col-md-4 d-flex flex-column justify-content-center mt-6">
            <input
              type="number"
              class="form-control mb-1"
              v-model.number="usdAmount"
              @input="convertToKrw"
              aria-label="Amount in USD"
            />
            <span class="text-center mb-1">=</span>
            <input
              type="text"
              class="form-control mb-3"
              :value="krwAmount"
              readonly
              aria-label="Amount in KRW"
            />
            <button class="btn btn-primary w-100">Buy</button>
            <!-- 환율 알림 Section -->
            <div class="exchange-alert-container">
              <span class="alert-title">환율 알림 설정</span>
              <span class="alert-content">목표 환율 입력하시오.</span>
              <div class="exchange-input">
                <span>1 USD</span>
                <span class="equals-symbol">=</span>
                <input
                  type="number"
                  v-model="alertRateUsdToKrw"
                  class="form-control alert-input"
                />
                <span>KRW</span>
              </div>
              <button
                class="btn btn-warning w-100 mt-3"
                @click="saveAlertRate(1, 0, alertRateUsdToKrw)"
              >
                알림 설정
              </button>
            </div>
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
            <ExchangeRateChart
              chartId="fromexchangeChart"
              :period="fromSelectedPeriod"
              chartType="from"
            />
            <div class="chart-button-container">
              <template
                v-for="period in ['1y', '6m', '3m', '1m']"
                :key="period"
              >
                <button
                  class="chart-btn"
                  :class="{ selected: fromSelectedPeriod === period }"
                  @click="setFromPeriod(period)"
                >
                  {{ period }}
                </button>
              </template>
            </div>
          </div>
          <div class="col-md-4 d-flex flex-column justify-content-center mt-6">
            <input
              type="number"
              class="form-control mb-1"
              v-model.number="krwAmountReverse"
              @input="convertToUsd"
              aria-label="Amount in KRW"
            />
            <span class="text-center mb-1">=</span>
            <input
              type="number"
              class="form-control mb-3"
              :value="usdAmountReverse"
              readonly
              aria-label="Amount in USD"
            />
            <button class="btn btn-danger w-100">Sell</button>
            <!-- 환율 알림 Section -->
            <div class="exchange-alert-container">
              <span class="alert-title">환율 알림 설정</span>
              <span class="alert-content">목표 환율 입력하시오.</span>
              <div class="exchange-input">
                <span>1 KRW</span>
                <span class="equals-symbol">=</span>
                <input
                  type="number"
                  v-model="alertRateKrwToUsd"
                  class="form-control alert-input"
                />
                <span>USD</span>
              </div>
              <button
                @click="saveAlertRate(0, 1, alertRateKrwToUsd)"
                class="btn btn-warning w-100 mt-3"
              >
                알림 설정
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Notification & Auto Settings Section -->
    <div class="card mt-4">
      <div class="card-body">
        <div class="row">
          <div class="col-lg-12">
            <div
              @click="$router.push('/set-alert')"
              class="alert alert-info clickable-alert"
              role="button"
              tabindex="0"
            >
              <span>자동 환전 설정 페이지</span>
            </div>
            <div class="form-group">
              <label for="autoCondition">자동 환전 설정 내역</label>
              <button
                class="btn btn-danger btn-sm ml-2"
                @click="deleteAlertCondition(autoConditions[0]?.resNo)"
              >
                삭제
              </button>
              <ul v-if="autoConditions.length > 0" class="list-group">
                <li class="list-group-item">
                  <p>
                    <strong>예약 번호:</strong> {{ autoConditions[0]?.resNo }} |
                    <strong>기준 통화:</strong>
                    {{ autoConditions[0]?.baseCode }} |
                    <strong>대상 통화:</strong>
                    {{ autoConditions[0]?.targetCode }} |
                    <strong>목표 환율:</strong>
                    {{ autoConditions[0]?.targetExchange }} |
                    <strong>목표 KRW 금액:</strong>
                    {{ autoConditions[0]?.targetKrw }}
                  </p>
                </li>
              </ul>
              <p v-else>자동 환전 예약 내역이 없습니다.</p>
            </div>
            <div class="form-group">
              <label for="targetRate1">환율 알림 설정 내역 1</label>
              <button
                class="btn btn-danger btn-sm ml-2"
                @click="deleteAlertCondition(alertConditions[0]?.resNo)"
              >
                삭제
              </button>
              <ul v-if="alertConditions.length > 0" class="list-group">
                <li class="list-group-item">
                  <p>
                    <strong>기준 통화:</strong>
                    {{ alertConditions[0]?.baseCode }} |
                    <strong>대상 통화:</strong>
                    {{ alertConditions[0]?.targetCode }} |
                    <strong>목표 환율:</strong>
                    {{ alertConditions[0]?.targetExchange }} |
                  </p>
                </li>
              </ul>
              <p v-else>환율 알림 예약 내역이 없습니다.</p>
            </div>

            <div class="form-group">
              <label for="targetRate2">환율 알림 설정 내역 2</label>
              <button
                class="btn btn-danger btn-sm ml-2"
                @click="deleteAlertCondition(alertConditions[1]?.resNo)"
              >
                삭제
              </button>
              <ul v-if="alertConditions.length > 1" class="list-group">
                <li class="list-group-item">
                  <p>
                    <strong>기준 통화:</strong>
                    {{ alertConditions[1]?.baseCode }} |
                    <strong>대상 통화:</strong>
                    {{ alertConditions[1]?.targetCode }} |
                    <strong>목표 환율:</strong>
                    {{ alertConditions[1]?.targetExchange }} |
                  </p>
                </li>
              </ul>
              <p v-else>환율 알림 예약 내역이 없습니다.</p>
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
import axios from "axios";

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

// 알림 저장 함수
const alertRateUsdToKrw = ref(null); // USD -> KRW 알림 목표 환율
const alertRateKrwToUsd = ref(null); // KRW -> USD 알림 목표 환율

const saveAlertRate = async (baseCode, targetCode, targetExchange) => {
  try {
    // const token = localStorage.getItem("jwt_token"); // JWT 토큰 가져오기
    // userNo를 추가(임시)
    const userNo = 1;
    console.log(userNo, baseCode, targetCode, targetExchange);
    // 서버에 POST 요청 보내기
    const response = await axios.post(
      "/api/exchange-reservation",
      {
        userNo: userNo,
        baseCode: baseCode,
        targetCode: targetCode,
        targetExchange: targetExchange,
      }
      // {
      //   headers: {
      //     Authorization: `Bearer ${token}`, // 인증 헤더에 토큰 추가
      //   },
      // }
    );

    if (response.status === 200) {
      alert("환율 알림이 성공적으로 저장되었습니다.");
    }
  } catch (error) {
    console.error("환율 알림 저장 중 오류 발생:", error);
    alert("환율 알림 저장에 실패했습니다. 오류: " + error.response.data);
  }
};

// 자동 환전 내역을 저장할 ref
const autoConditions = ref([]);

// 환율 알림 내역을 저장할 ref
const alertConditions = ref([]);

// 사용자 번호를 설정 (임의로 1로 설정)
const userNo = 1;

// 자동 환전 예약 데이터를 가져오는 함수
const fetchAutoExchange = async () => {
  try {
    const response = await axios.get(
      `/api/exchange-reservation/setalert/${userNo}`
    );
    if (response.status === 200) {
      // 응답 데이터가 존재하면 autoConditions에 저장
      const reservations = response.data;
      autoConditions.value = reservations;
    }
  } catch (error) {
    console.error("자동 환전 데이터를 가져오는 중 오류 발생:", error);
  }
};

// 환율 알림 데이터를 가져오는 함수
const fetchAlertConditions = async () => {
  try {
    const response = await axios.get(`/api/exchange-reservation/${userNo}`);
    if (response.status === 200) {
      // 응답 데이터가 존재하면 alertConditions에 최대 2개 저장
      const alerts = response.data.slice(0, 2); // 최대 2개만 가져옴
      alertConditions.value = alerts;
      console.log("Fetched alert conditions:", alertConditions.value);
      fetchAlertConditions(); // 알림을 다시 가져와서 업데이트
    }
  } catch (error) {
    console.error("환율 알림 데이터를 가져오는 중 오류 발생:", error);
  }
};

const deleteAlertCondition = async (resNo) => {
  try {
    console.log("삭제할 예약 번호:", resNo);
    if (resNo) {
      console.log("삭제할 예약 번호:", resNo);
      const response = await axios.delete(`/api/exchange-reservation/${resNo}`);
      if (response.status === 200) {
        alert("알림 설정이 삭제되었습니다.");
        fetchAlertConditions(); // 알림을 다시 가져와서 업데이트
        fetchAutoExchange(); // 예약을 다시 가져와서 업데이트
      }
    } else {
      console.error("예약 번호(resNo)가 없습니다.");
    }
  } catch (error) {
    console.error("알림 설정 삭제 중 오류 발생:", error);
  }
};

// 컴포넌트가 마운트될 때 데이터를 가져옴
onMounted(() => {
  fetchExchangeRates();
  fetchAutoExchange();
  fetchAlertConditions();
});
</script>

<style scoped>
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type="number"] {
  -moz-appearance: textfield;
}

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

.exchange-alert-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 1rem;
  text-align: center;
}

.alert-title {
  font-weight: bold;
  margin-bottom: 0.5rem;
  text-align: left;
  width: 100%;
}

.alert-content {
  margin-bottom: 0.5rem;
  text-align: left;
  width: 100%;
  font-size: small;
}

.exchange-input {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  width: 100%;
}

.alert-input {
  width: 60%;
  text-align: center;
  white-space: nowrap;
}

.equals-symbol {
  margin: 0 0.2rem;
  font-weight: bold;
}

.btn-warning {
  background-color: #ffcc00;
  border: none;
}

.btn-warning:hover {
  background-color: #ffbb00;
}
</style>
