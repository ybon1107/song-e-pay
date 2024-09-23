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
</script>

<style>
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
  height: 50vh; /* Adjusts the height based on 50% of the viewport height */
  width: 100%; /* Ensures full width */
  align-items: center; /* Centers chart vertically */
  justify-content: center; /* Centers chart horizontally */
}
/* Additional responsiveness */
@media (max-width: 768px) {
  .calendar-container {
    height: auto;
  }
}
</style>
