<template>
  <div class="container-fluid py-4">
    <h4>Welcome to Song-E-pay!</h4>
    <!-- Currency Cards Section -->
    <div class="row my-3">
      <!-- USD Wallet -->
      <div class="col-lg-6 col-md-12">
        <!-- <AccountsCard/> -->
        <DefaultInfoCard title="Song-E Money" :value="100" img-src="/images/song-e-money.png" img="/images/america.png"
          @click="selectAsset('Song-E Money')" :class="{ selected: selectedAsset === 'Song-E Money' }" />


        <!-- <div class="card text-center">
          <div class="card-body">
            <h6>My Wallet (USD)</h6>
            <div class="wallet-balance">
              <h3>0 USD</h3>
            </div>
            <button class="btn btn-primary">Deposit</button>
            <button class="btn btn-secondary">Withdraw</button>
          </div>
        </div> -->
      </div>

      <!-- KRW Wallet -->
      <div class="col-lg-6 col-md-12">
        <!-- Won-E Money 카드 -->
        <DefaultInfoCard title="Won-E Money" :value="formattedWonEMoneyBalance" img-src="images/won-e-money.png"
          img="/images/korea.png" @click="selectAsset('Won-E Money')"
          :class="{ selected: selectedAsset === 'Won-E Money' }" />

        <!-- <div class="card text-center">
          <div class="card-body">
            <h6>My Wallet (KRW)</h6>
            <div class="wallet-balance">
              <h3>0 KRW</h3>
            </div>
            <button class="btn btn-primary">Deposit</button>
            <button class="btn btn-secondary">Withdraw</button>
          </div>
        </div> -->
      </div>
    </div>

    <!-- Graph and Conversion Section -->
    <div class="card">
      <div class="card-body">
        <div class="row">
          <!-- Exchange Rate Graph Section -->
          <div class="col-lg-7 col-md-12 my-3">
            <h6>Exchange Rate</h6>
            <div class="chart-container">
              <ExchangeRateChart chartId="toexchangeChart" period="1m" chartType="to" />
            </div>
          </div>

          <div class="col-lg-5 col-md-12 d-flex flex-column justify-content-center my-3">
            <div class="mb-3">
              <h6>Convert USD to KRW</h6>
              <div class="d-flex align-items-center">
                <div class="position-relative flex-grow-1">
                  <input type="number" class="form-control" v-model.number="usdAmount" @input="convertToKrw"
                    aria-label="Amount in USD" />
                  <img src="@/assets/img/icons/flags/US.png" alt="USA Flag" class="flag-icon" />
                </div>
                <span class="mx-3">=</span>
                <div class="position-relative flex-grow-1">
                  <input type="text" class="form-control" :value="krwAmount" readonly aria-label="Amount in KRW" />
                  <img src="@/assets/img/icons/flags/KR.png" alt="KRW Flag" class="flag-icon" />
                </div>
              </div>
            </div>
            <div>
              <h6>Convert KRW to USD</h6>
              <div class="d-flex align-items-center">
                <div class="position-relative flex-grow-1">
                  <input type="number" class="form-control" v-model.number="krwAmountReverse" @input="convertToUsd" />
                  <img src="@/assets/img/icons/flags/KR.png" alt="KRW Flag" class="flag-icon" />
                </div>
                <span class="mx-3">=</span>
                <div class="position-relative flex-grow-1">
                  <input type="text" class="form-control" :value="usdAmountReverse" readonly />
                  <img src="@/assets/img/icons/flags/US.png" alt="USA Flag" class="flag-icon" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Calendar Section -->
    <div class="row my-3">
      <div class="col-lg-12">
        <div class="calendar-container">
          <!-- Insert calendar here -->
          <calendar></calendar>
        </div>
      </div>
    </div>

    <div class="row my-3">
    <div class="col-lg-12">
      <div class="map-area">
        <MapComponent />
      </div>
    </div>
  </div>
  </div>

  


</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useExchangeStore } from "@/stores/exchangeStore";
import ExchangeRateChart from "@/views/Chart/ExchangeRateChart.vue";
import Calendar from "../Templates/Calendar.vue";
import MapComponent from "@/views/maps/MapComponent.vue";
import DefaultInfoCard from '@/views/Cards/AccountsCard.vue';
import AccountsCard from '@/views/Cards/AccountsCard2.vue';
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

/* Additional responsiveness */
@media (max-width: 768px) {
  .calendar-container {
    height: auto;
  }
}

.flag-icon {
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  width: 24px;
  height: 16px;
}
</style>
