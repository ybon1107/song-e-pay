<script setup>
import DefaultInfoCard from '@/views/Cards/DefaultInfoCard.vue';
import ArgonInput from '@/components/yb_templates/ArgonAmountInput.vue';
import { ref } from 'vue';

// Reactive state to manage the active tab
const activeTab = ref('charge');
</script>
<template>
  <div class="py-4 container-fluid">
    <h4>나의 자산</h4>
  </div>
  <div class="row align-items-center">
    <!-- 송이페이 머니 -->
    <div class="col-12 col-md-4 mb-1">
      <default-info-card title="Song-e-money" value="$455.00" img-src="/images/song-e-money.png" img="/images/america.png"> </default-info-card>
    </div>

    <!-- 화살표 이미지 -->
    <div class="col-12 col-md-1 d-flex justify-content-center align-items-center">
      <img src="/images/arrow.png" alt="Arrow" class="arrow-image" />
    </div>

    <!-- 원화 머니 사용 -->
    <div class="col-12 col-md-4 mb-1">
      <default-info-card title="Won-e-money" value="$455.00" img-src="/images/won-e-money.png" img="/images/korea.png"> </default-info-card>
    </div>
  </div>

  <div class="nav-wrapper mt-4">
    <ul class="nav nav-pills nav-fill flex-column flex-md-row" id="tabs-icons-text" role="tablist">
      <li class="nav-item">
        <a
          class="nav-link mb-sm-3 mb-md-0"
          :class="{ active: activeTab === 'charge' }"
          id="tabs-charge-tab"
          @click="activeTab = 'charge'"
          data-bs-toggle="tab"
          href="#tabs-charge"
          role="tab"
          aria-controls="tabs-charge"
          aria-selected="true"
        >
          <i class="ni ni-money-coins mr-2"></i>Charge
        </a>
      </li>
      <li class="nav-item">
        <a
          class="nav-link mb-sm-3 mb-md-0"
          :class="{ active: activeTab === 'exchange' }"
          id="tabs-exchange-tab"
          @click="activeTab = 'exchange'"
          data-bs-toggle="tab"
          href="#tabs-exchange"
          role="tab"
          aria-controls="tabs-exchange"
          aria-selected="false"
        >
          <i class="fa fa-exchange" aria-hidden="true"></i>Exchange
        </a>
      </li>
      <li class="nav-item">
        <a
          class="nav-link mb-sm-3 mb-md-0"
          :class="{ active: activeTab === 'transfer' }"
          id="tabs-transfer-tab"
          @click="activeTab = 'transfer'"
          data-bs-toggle="tab"
          href="#tabs-transfer"
          role="tab"
          aria-controls="tabs-transfer"
          aria-selected="false"
        >
          <i class="ni ni-send mr-2"></i>Transfer
        </a>
      </li>
    </ul>

    <!-- Tab Content -->
    <div class="card shadow">
      <div class="card-body">
        <div class="tab-content">
          <div v-if="activeTab === 'charge'" id="tabs-charge" class="tab-pane fade show active">
            <!-- Charge Content -->
            <p>Charge Section</p>
            <ArgonInput id="charge-amount" name="chargeAmount" placeholder="Enter amount to charge" :modelValue="chargeAmount" @update:modelValue="handleChargeInput" />
          </div>
          <div v-if="activeTab === 'exchange'" id="tabs-exchange" class="tab-pane fade show active">
            <!-- Exchange Content -->
            <p>Exchange Section</p>
          </div>
          <div v-if="activeTab === 'transfer'" id="tabs-transfer" class="tab-pane fade show active">
            <!-- Transfer Content -->
            <p>Transfer Section</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.arrow-image {
  max-width: 30px; /* 화살표 이미지의 크기 조절 */
  height: auto; /* 비율 유지 */
}

.row {
  display: flex;
  flex-wrap: wrap; /* 화면 크기에 따라 flex 아이템을 줄바꿈 가능하게 설정 */
}

.col-12 {
  flex: 0 0 100%; /* 모바일 화면에서 전체 너비를 사용하도록 설정 */
}

.col-md-4 {
  flex: 0 0 26%; /* 중간 화면 크기에서 3분의 1 너비를 사용하도록 설정 */
}

.col-md-1 {
  flex: 0 0 5%; /* 화살표를 감싸는 div의 너비를 줄여 간격 좁히기 */
}

.mb-1 {
  margin-bottom: 0.5rem; /* 카드와 화살표 사이의 간격을 더 줄이기 */
}
</style>
