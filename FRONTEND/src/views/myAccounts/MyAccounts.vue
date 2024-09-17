<script setup>
import DefaultInfoCard from '@/components/yb_templates/AccountsCard.vue';
import ArgonInput from '@/components/yb_templates/ArgonAmountInput.vue';
import ArgonButton from '@/components/templates/ArgonButton.vue';
import { ref, onMounted, computed  } from 'vue';
import axios from 'axios';

// Reactive state to manage the active tab
const activeTab = ref('');
// isActive computed property
const isActive = computed(() => activeTab.value !== '');
const chargeAmount = ref(''); 
const exchangeAmount = ref(''); 
const transferAmount = ref(''); 
const songiBalance = ref(''); //송이페이 계좌
const account = ref(''); //기본 계좌

// 기본 계좌 정보 로드
// const loadAccount = async () => {
//   try {
//     const response = await axios.get('/api/accounts');
//     if (response.data.length > 0) {
//       account.value = response.data[0].account; // 첫 번째 계좌를 가져옵니다.
//     }
//   } catch (error) {
//     console.error('Error fetching account data:', error);
//   }
// };

// // 송이페이 계좌 잔액 로드
// const loadSongiBalance = async () => {
//   try {
//     const response = await axios.get('/api/songipay/balance'); // API 엔드포인트는 상황에 맞게 조정
//     songiBalance.value = `$${response.data.balance}`; // 서버에서 응답받은 데이터 처리
//   } catch (error) {
//     console.error('Error fetching balance data:', error);
//   }
// };

// // 컴포넌트가 마운트되면 데이터 로드
// onMounted(() => {
//   loadAccount();
//   loadSongiBalance();
// });
</script>

<template>
  <div class="container-fluid" style="margin-left: 20px; margin-right: 20px;">
    <div class="py-4">
      <h4>나의 자산</h4>
    </div>
    <div class="row justify-content-center align-items-center">
      <!-- 송이페이 머니 -->
      <div class="col-12 col-md-4 mb-1 d-flex justify-content-center align-items-center">
        <DefaultInfoCard title="Song-e-money" value="USD 400.00" img-src="/images/song-e-money.png" img="/images/america.png"> </DefaultInfoCard>
        <!-- :value="songiBalance" -->
      </div>

      <!-- 화살표 이미지 -->
      <div class="col-12 col-md-1 d-flex justify-content-center align-items-center">
        <img src="/images/arrow.png" alt="Arrow" class="arrow-image" />
      </div>

      <!-- 원화 머니 사용 -->
      <div class="col-12 col-md-4 mb-1 d-flex justify-content-center align-items-center">
        <DefaultInfoCard title="Won-e-money" value="KRW 4,000" img-src="/images/won-e-money.png" img="/images/korea.png"> </DefaultInfoCard> 
        <!-- :value="wonBalance" -->
      </div>
    </div>
    
    <div class="nav-wrapper mt-5">
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
            <i class="ni ni-money-coins mr-2"></i> Charge
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
            <i class="fa fa-exchange" aria-hidden="true"></i> Exchange
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
            <i class="ni ni-send mr-2"></i> Transfer
          </a>
        </li>
      </ul>

      <!-- Tab Content -->
      <div class="card shadow">
        <div class="card-body" v-if="isActive">
          <div class="tab-content">
            <div v-if="activeTab === 'charge'" id="tabs-charge" class="tab-pane fade show active">
            <!-- Charge Content -->
            <p>Charge Section</p>
            <form @submit.prevent="handleSubmit('charge')">
              <div class="content-container" style="width: 70%; margin: 0 auto;">
                <ArgonInput v-model="chargeAmount" placeholder="금액을 입력하세요" />
                <div class="d-flex justify-content-between align-items-center">
                  <p class="mb-3 mt-3">충전 계좌 :</p>
                  <p class="mb-3 mt-3">버섯 은행(9910-****)</p>
                </div>
                <div class="d-flex justify-content-between align-items-center">
                  <p class="mb-5 mt-2">계좌 잔액 :</p>
                  <p class="mb-5 mt-2">USD 400.00</p>
                </div>
                <div class="d-flex justify-content-center align-items-center mt-3">
                  <argon-button type="submit" color="success" size="lg" class="w-100">충전하기</argon-button>
                </div>
              </div>
            </form>
            </div>
            <div v-if="activeTab === 'exchange'" id="tabs-exchange" class="tab-pane fade show active">
              <!-- Exchange Content -->
              <p>Exchange Section</p>
              <form @submit.prevent="handleSubmit('exchange')">
                <div class="content-container" style="width: 70%; margin: 0 auto;">
                  <ArgonInput v-model="exchangeAmount" placeholder="금액을 입력하세요" />
                  <div class="d-flex justify-content-between align-items-center">
                    <p class="mb-3 mt-3">충전 계좌 :</p>
                    <p class="mb-3 mt-3">버섯 은행(9910-****)</p>
                  </div>
                  <div class="d-flex justify-content-between align-items-center">
                    <p class="mb-5 mt-2">계좌 잔액 :</p>
                    <p class="mb-5 mt-2">USD 400.00</p>
                  </div>
                  <div class="d-flex justify-content-center align-items-center mt-3">
                    <argon-button type="submit" color="success" size="lg" class="w-100">충전하기</argon-button>
                  </div>
                </div>
              </form>
            </div>
            <div v-if="activeTab === 'transfer'" id="tabs-transfer" class="tab-pane fade show active">
              <!-- Transfer Content -->
              <p>Transfer Section</p>
              <form @submit.prevent="handleSubmit('transfer')">
                <div class="content-container" style="width: 70%; margin: 0 auto;">
                  <ArgonInput v-model="transferAmount" placeholder="금액을 입력하세요" />
                  <div class="d-flex justify-content-between align-items-center">
                    <p class="mb-3 mt-3">충전 계좌 :</p>
                    <p class="mb-3 mt-3">버섯 은행(9910-****)</p>
                  </div>
                  <div class="d-flex justify-content-between align-items-center">
                    <p class="mb-5 mt-2">계좌 잔액 :</p>
                    <p class="mb-5 mt-2">USD 400.00</p>
                  </div>
                  <div class="d-flex justify-content-center align-items-center mt-3">
                    <argon-button type="submit" color="success" size="lg" class="w-100">충전하기</argon-button>
                  </div>
                </div>
              </form>
            </div>
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
/* 줄인 너비에 맞추어 카드와 탭들의 크기를 조정합니다 */
.nav-wrapper {
  max-width: 800px; /* 전체 너비를 800px로 제한 */
  margin: 0 auto; /* 수평 가운데 정렬 */
}

.nav-pills .nav-link {
  text-align: center;
}
</style>
