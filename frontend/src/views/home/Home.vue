<template>
  <div class="container-fluid">
    <h3>Welcome to Song-E-pay!</h3>
    <!-- Currency Cards Section -->
    <div class="row mt-3">
      <!-- USD Wallet -->
      <div class="col-lg-4 col-md-5">
        <!-- Song-E Money 카드 -->
        <!-- <AccountsCard title="Song-E Money" :balance="songEMoneyBalance" :currency="USD" backgroundImage="/images/song-e-money.png"
          icon="/images/america.png" @click="navigateToMyAccounts('Song-E Money')"/> -->
        <AccountsCard assetType="song-e" currency="USD" @click="navigateToMyAccounts('Song-E Money')"
          :isSelected="selectedAsset === 'Song-E Money'" />

      </div>

      <!-- KRW Wallet -->
      <div class="col-lg-4 col-md-5">
        <!-- Won-E Money 카드 -->
        <!-- <AccountsCard title="Won-E Money" :balance="wonEMoneyBalance" :currency="KRW"
          backgroundImage="/images/won-e-money.png" icon="/images/korea.png"
          @click="navigateToMyAccounts('Won-E Money')" /> -->
        <AccountsCard assetType="won-e" currency="KRW" @click="navigateToMyAccounts('Won-E Money')"
          :isSelected="selectedAsset === 'Won-E Money'" />
      </div>
    </div>

    <!-- Graph and Conversion Section -->
    <div class="card mt-3">
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
    <div class="row mt-3">
      <div class="col-lg-12">
        <div class="calendar-container">
          <!-- Insert calendar here -->
          <calendar></calendar>
        </div>
      </div>
    </div>

    <!-- Map Section -->
    <div class="row mt-3">
      <div class="col-lg-12">
        <div class="map-area">
          <MapComponent />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useExchangeStore } from '@/stores/exchangeStore';
import ExchangeRateChart from '@/views/Chart/ExchangeRateChart.vue';
import Calendar from '../Templates/Calendar.vue';
import MapComponent from '@/views/maps/MapComponent.vue';
import AccountsCard from '@/views/Cards/AccountsCard2.vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const store = useExchangeStore();

const songEMoneyBalance = ref(0);
const wonEMoneyBalance = ref(0);

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
    console.log(' 확인');
    convertToKrw();
    convertToUsd();
  } catch (error) {
    console.error('환율 데이터를 가져오는 중 오류 발생:', error);
  }
};

// 클릭시 나의 자산으로 이동
const navigateToMyAccounts = (assetType) => {
  router.push({
    name: 'MyAccounts',
    query: { selectedAsset: assetType }
  });
};

onMounted(() => {
  fetchExchangeRates();
});
</script>

<style scoped>
.wallet-balance h3 {
  margin-bottom: 20px;
}

.flag-icon {
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  width: 24px;
  height: 16px;
}

/* Responsiveness */
@media (max-width: 768px) {
  .calendar-container {
    height: auto;
  }
}
</style>
