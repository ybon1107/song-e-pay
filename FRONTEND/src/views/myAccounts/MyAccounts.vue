<script setup>
import ArgonInput from '@/components/templates/ArgonInput.vue';
import DefaultInfoCard from '@/components/yb_templates/AccountsCard.vue';
import ArgonAmountInput from '@/components/yb_templates/ArgonAmountInput.vue';
import ArgonButton from '@/components/templates/ArgonButton.vue';
import { ref, onMounted, computed, customRef  } from 'vue';
import axios from 'axios';
// 자산 정보와 잔액
const selectedAsset = ref('Song-E Money'); // 기본적으로 Song-E Money가 선택됨
const activeTab = ref('charge'); // 기본적으로 충전 탭이 선택됨
const chargeAmount = ref('');
const exchangeAmount = ref('');
const withdrawAmount = ref('');
const transferAmount = ref('');
const refundAmount = ref('');
const customerunit = ref('USD');
const songEMoneyBalance = ref(0); // Song-E Money의 잔액
const wonEMoneyBalance = ref(0); // Won-E Money의 잔액
const withdrawEmail = ref('');
const withdrawEmailConfirm = ref('');
const formattedSongEMoneyBalance = computed(() => `${customerunit.value} ${songEMoneyBalance.value.toFixed(2)}`);
const formattedWonEMoneyBalance = computed(() => `KRW ${wonEMoneyBalance.value.toLocaleString()}`);
const exchangeRate = ref(null); // To store the fetched exchange rate

onMounted(() => {
  const fetchExchangeRate = () => {
    axios.get(`https://api.manana.kr/exchange/rate/${customerunit.value}/KRW.json`)
      .then(response => {
        exchangeRate.value = response.data[0].rate;
      })
      .catch(error => {
        console.error('환율 데이터를 가져오지 못했습니다:', error);
      });
  };

  // 최초 환율 가져오기
  fetchExchangeRate();

  // 1분마다 환율 갱신
  setInterval(fetchExchangeRate, 60000); // 60000ms = 1분
});

const formatNumber = (num) => {
  // 입력값이 숫자이면 포맷, 아니면 그대로 반환
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const transactionAfterBalance = computed(() => {
  let balance = 0;

  // 충전일 경우 잔액 증가
  if (activeTab.value === 'charge') {
    balance = songEMoneyBalance.value + parseFloat(chargeAmount.value || 0);
  }
  // 출금일 경우 잔액 감소
  else if (activeTab.value === 'withdraw') {
    balance = songEMoneyBalance.value - parseFloat(withdrawAmount.value || 0);
  }

  // 계산된 숫자를 포맷하여 반환
  return formatNumber(balance.toFixed(2)); // 소수점 두 자릿수까지 표시
});


// 자산 선택
const selectAsset = (asset) => {
  selectedAsset.value = asset;
  // Song-E Money 선택 시 기본 탭을 charge로 설정, Won-E Money 선택 시 기본 탭을 transfer로 설정
  activeTab.value = asset === 'Song-E Money' ? 'charge' : 'transfer';
};

// 충전 처리
const charge = () => {
  songEMoneyBalance.value += parseFloat(chargeAmount.value);
  chargeAmount.value = ''; // 충전 후 입력 초기화
  transactionAfterBalance ='';
};

// 환전 처리
const exchange = () => {
  
};

// 환불 처리
const withdraw = () => {
  songEMoneyBalance.value -= parseFloat(withdrawAmount.value);
  withdrawAmount.value = ''; // 충전 후 입력 초기화
  transactionAfterBalance ='';
};

// 송금 처리
const transfer = () => {
  wonEMoneyBalance.value -= parseFloat(transferAmount.value);
  transferAmount.value = ''; // 충전 후 입력 초기화
};

// 환급 처리
const refund = () => {
  
};

const emailConfirm = () => {
//이메일 확인 버튼 눌렀을 시 회원 이메일 혹은 비회원 이메일 뜨게 하기
};

</script>

<template>
  <div class="container-fluid" style="width: 80%;">
    <div class="assets-list">
      <!-- Song-E Money 카드 -->
      <DefaultInfoCard 
        title="Song-E Money" 
        :value="formattedSongEMoneyBalance"
        img-src="/images/song-e-money.png" 
        img="/images/america.png" 
        @click="selectAsset('Song-E Money')"
        :class="{ selected: selectedAsset === 'Song-E Money' }" 
      />

      <!-- Won-E Money 카드 -->
      <DefaultInfoCard 
        title="Won-E Money" 
        :value="formattedWonEMoneyBalance"
        img-src="images/won-e-money.png" 
        img="/images/korea.png" 
        @click="selectAsset('Won-E Money')"
        :class="{ selected: selectedAsset === 'Won-E Money' }" 
      />
    </div>

    <div class="card">
      <!-- Song-E Money 선택 시 -->
      <template v-if="selectedAsset === 'Song-E Money'">
        <nav class="nav flex-column flex-sm-row">
          <a class="flex-sm-fill text-sm-center nav-link" 
             :class="{ active: activeTab === 'charge' }" 
             @click="activeTab = 'charge'" aria-current="page">
            충전
          </a>
          <a class="flex-sm-fill text-sm-center nav-link" 
             :class="{ active: activeTab === 'exchange' }" 
             @click="activeTab = 'exchange'">
            환전
          </a>
          <a class="flex-sm-fill text-sm-center nav-link" 
             :class="{ active: activeTab === 'withdraw' }" 
             @click="activeTab = 'withdraw'">
            환불
          </a>
        </nav>
      </template>

      <!-- Won-E Money 선택 시 -->
      <template v-if="selectedAsset === 'Won-E Money'">
        <nav class="nav flex-column flex-sm-row">
          <a class="flex-sm-fill text-sm-center nav-link" 
             :class="{ active: activeTab === 'transfer' }" 
             @click="activeTab = 'transfer'" aria-current="page">
            송금
          </a>
          <a class="flex-sm-fill text-sm-center nav-link" 
             :class="{ active: activeTab === 'refund' }" 
             @click="activeTab = 'refund'">
            환급
          </a>
        </nav>
      </template>

      <!-- Song-E Money의 탭 내용 -->
      <div class="card-body" v-if="selectedAsset === 'Song-E Money'">
        <div v-if="activeTab === 'charge'" class="tab-pane fade show active">
          <p>충전 금액</p>
          <ArgonAmountInput v-model="chargeAmount" placeholder="금액을 입력하세요" :unit="customerunit" />
          <p>충전계좌: {{ selectedAsset === 'Song-E Money' ? '내 계좌' : 'KRW 계좌' }}</p>
          <p>거래 후 잔액: {{ transactionAfterBalance }} {{customerunit}}</p>
          <argon-button type="submit" color="success" size="lg" class="w-100" @click="charge">충전하기</argon-button>
        </div>

        <div v-if="activeTab === 'exchange'">
          <p>현재 환율</p>
          <p>1KRW = {{ exchangeRate }} {{ customerunit }}</p>
          <p>보내는 금액</p>
          <ArgonAmountInput v-model="exchangeAmount" placeholder="얼마를 환전할까요?" :unit="customerunit"/>
          <p>받는 금액</p>
          <p>환급계좌: {{ selectedAsset === 'Song-E Money' ? '내 계좌' : 'KRW 계좌' }}</p>
          <p>거래 후 잔액: {{ transactionAfterBalance }} {{ customerunit }}</p>
          <argon-button type="submit" color="success" size="lg" class="w-100" @click="exchange">환전하기</argon-button>
        </div>

        <div v-if="activeTab === 'withdraw'">
          <p>환불할 금액을 입력하세요</p>
          <ArgonAmountInput v-model="withdrawAmount" placeholder="얼마를 환불할까요?" :unit="customerunit"/>
          <p>환불계좌: {{ selectedAsset === 'Song-E Money' ? '내 계좌' : 'KRW 계좌' }}</p>
          <p>거래 후 잔액: {{ transactionAfterBalance }} {{ customerunit }}</p>
          <argon-button type="submit" color="success" size="lg" class="w-100" @click="withdraw">환불하기</argon-button>
        </div>
      </div>

      <!-- Won-E Money의 탭 내용 -->
      <div class="card-body" v-if="selectedAsset === 'Won-E Money'">
        <div v-if="activeTab === 'transfer'">
          <div class="text-btn">
            <p style="margin-top: 1rem; margin-bottom: 1rem;">보낼 이메일</p>
            <button class="action-btn2" @click="emailConfirm">이메일 확인하기</button>
          </div>
          <ArgonInput v-model="withdrawEmail" placeholder="받는 분의 이메일을 입력하세요"/>
          <p>이메일 확인</p>
          <ArgonInput v-model="withdrawEmailConfirm" placeholder="이메일을 다시 입력하세요"/>
          <p>송금할 금액을 입력하세요</p>
          <ArgonAmountInput v-model="transferAmount" placeholder="얼마를 송금할까요?" unit="KRW"/>
          <p>송금 후 잔액: {{ transactionAfterBalance }} KRW</p>
          <argon-button type="submit" color="success" size="lg" class="w-100" @click="transfer">송금하기</argon-button>
        </div>

        <div v-if="activeTab === 'refund'">
          <p>현재 환율</p> 
          <p>보내는 금액</p>
          <ArgonAmountInput v-model="refundAmount" placeholder="얼마를 환급할까요?" unit="KRW" />
          <p>거래 후 잔액: {{ transactionAfterBalance }} KRW</p>
          <argon-button type="submit" color="success" size="lg" class="w-100" @click="refund">환급하기</argon-button>
        </div>
      </div>
    </div>
  </div>
</template>


<style scoped>
/* 기본 탭 스타일 */
.nav-link {
  cursor: pointer;
  color: #555;
  padding: 10px 15px;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

/* 탭 활성화 스타일 */
.nav-link.active {
  background-color: #2CCE89;
  color: white;
}

/* 비활성화된 탭 스타일 */
.nav-link:hover {
  background-color: #e9ecef;
}

/* 기타 스타일 */
.assets-list {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%; /* 카드를 가로로 전체 너비에 맞추기 */
  margin-bottom: 20px; /* 탭과의 간격 설정 */
}

.asset-card {
  border-radius: 15px;
  border: 2px solid #ddd;
  padding: 20px;
  margin-bottom: 20px;
  background-color: #fff;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text-btn {
  display: flex;
  align-items: center; /* 세로 중앙 정렬 */
  gap: 10px; /* p 태그와 버튼 사이의 간격을 설정 (필요에 따라 조정) */
}

</style>