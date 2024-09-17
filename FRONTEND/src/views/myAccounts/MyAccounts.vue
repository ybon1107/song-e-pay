<script setup>
import ArgonInput from '@/components/templates/ArgonInput.vue';
import DefaultInfoCard from '@/components/yb_templates/AccountsCard.vue';
import ArgonAmountInput from '@/components/yb_templates/ArgonAmountInput.vue';
import ArgonButton from '@/components/templates/ArgonButton.vue';
import { ref, onMounted, computed  } from 'vue';
import axios from 'axios';
// 자산 정보와 잔액
const selectedAsset = ref('Song-E Money'); // 기본적으로 Song-E Money가 선택됨
const activeTab = ref('charge'); // 기본적으로 충전 탭이 선택됨
const chargeAmount = ref('');
const exchangeAmount = ref('');
const withdrawAmount = ref('');
const transferAmount = ref('');
const refundAmount = ref('');
const songEMoneyBalance = ref(0); // Song-E Money의 잔액
const wonEMoneyBalance = ref(0); // Won-E Money의 잔액
const withdrawEmail = ref('');
const withdrawEmailConfirm = ref('');
const formattedSongEMoneyBalance = computed(() => `USD ${songEMoneyBalance.value.toFixed(2)}`);
const formattedWonEMoneyBalance = computed(() => `KRW ${wonEMoneyBalance.value.toLocaleString()}`);

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
  <div class="my-assets-container">
    <div class="assets-list">
      <!-- Song-E Money 카드 -->
      <DefaultInfoCard 
        title="Song-E Money" 
        :value=formattedSongEMoneyBalance
        img-src="/images/song-e-money.png" 
        img="/images/america.png" 
        @click="selectAsset('Song-E Money')"
        :class="{ selected: selectedAsset === 'Song-E Money' }" 
      />

      <!-- Won-E Money 카드 -->
      <DefaultInfoCard 
        title="Won-E Money" 
        :value=formattedWonEMoneyBalance
        img-src="images/won-e-money.png" 
        img="/images/korea.png" 
        @click="selectAsset('Won-E Money')"
        :class="{ selected: selectedAsset === 'Won-E Money' }" 
      />
      
    </div>

    <div class="asset-details" :class="{'song-e-bg': selectedAsset === 'Song-E Money', 'won-e-bg': selectedAsset === 'Won-E Money'}">
      <div class="tabs">
        <!-- Song-E Money 선택 시 -->
        <template v-if="selectedAsset === 'Song-E Money'">
          <button @click="activeTab = 'charge'" :class="{ active: activeTab === 'charge' }">충전</button>
          <button @click="activeTab = 'exchange'" :class="{ active: activeTab === 'exchange' }">환전</button>
          <button @click="activeTab = 'withdraw'" :class="{ active: activeTab === 'withdraw' }">환불</button>
        </template>

        <!-- Won-E Money 선택 시 -->
        <template v-if="selectedAsset === 'Won-E Money'">
          <button @click="activeTab = 'transfer'" :class="{ active: activeTab === 'transfer' }">송금</button>
          <button @click="activeTab = 'refund'" :class="{ active: activeTab === 'refund' }">환급</button>
        </template>
      </div>

      <!-- Song-E Money의 탭 내용 -->
      <div v-if="selectedAsset === 'Song-E Money'">
        <div v-if="activeTab === 'charge'">
          <p>충전 금액</p>
          <ArgonAmountInput v-model="chargeAmount" placeholder="금액을 입력하세요" unit="USD" />
          <p>충전계좌: {{ selectedAsset === 'Song-E Money' ? '내 계좌' : 'KRW 계좌' }}</p>
          <p>거래 후 잔액: {{ transactionAfterBalance }} USD</p>
          <button class="action-btn" @click="charge">충전하기</button>
        </div>

        <div v-if="activeTab === 'exchange'">
          <p>현재 환율</p> 
          <p>보내는 금액</p>
          <ArgonAmountInput v-model="exchangeAmount" placeholder="얼마를 환전할까요?" unit="USD"/>
          <p>받는 금액</p>
          <p>환급계좌: {{ selectedAsset === 'Song-E Money' ? '내 계좌' : 'KRW 계좌' }}</p>
          <p>거래 후 잔액: {{ transactionAfterBalance }} USD</p>
          <button class="action-btn" @click="exchange">환전하기</button>
        </div>

        <div v-if="activeTab === 'withdraw'">
          <p>환불할 금액을 입력하세요</p>
          <ArgonAmountInput v-model="withdrawAmount" placeholder="얼마를 환불할까요?" unit="USD"/>
          <p>환불계좌: {{ selectedAsset === 'Song-E Money' ? '내 계좌' : 'KRW 계좌' }}</p>
          <p>거래 후 잔액: {{ transactionAfterBalance }} USD</p>
          <button class="action-btn" @click="withdraw">환불하기</button>
        </div>
      </div>

      <!-- Won-E Money의 탭 내용 -->
      <div v-if="selectedAsset === 'Won-E Money'">
        <div v-if="activeTab === 'transfer'">
          <div class="text-btn">
            <p style="margin-top: 1rem; margin-bottom: 1rem;">보낼 이메일</p>

            <button class="action-btn2" @click="emailConfirm">이메일 확인하기</button>
          </div>
          <ArgonInput v-model="withdrawEmail" placeholder="받는 분의 이메일을 입력하세요"/>
          <p>이메일 확인</p>
          <ArgonInput v-model="withdrawEmailConfirm" placeholder="이매일을 다시 입력하세요"/>
          
          <p>송금할 금액을 입력하세요</p>
          <ArgonAmountInput v-model="transferAmount" placeholder="얼마를 송금할까요?" unit="KRW"/>
          <p>송금 계좌 : </p>
          <p>송금 후 잔액: {{ transactionAfterBalance }} KRW</p>
          <button class="action-btn" @click="transfer">송금하기</button>
        </div>
        <div v-if="activeTab === 'refund'">
          <p>현재 환율</p> 
          <p>보내는 금액</p>
          <ArgonAmountInput v-model="refundAmount" placeholder="얼마를 환급할까요?" unit="KRW" />
          <p>받는 금액</p>
          <p>환급계좌: {{ selectedAsset === 'Song-E Money' ? '내 계좌' : 'KRW 계좌' }}</p>
          <p>거래 후 잔액: {{ transactionAfterBalance }} KRW</p>
          <button class="action-btn" @click="refund">환급하기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.my-assets-container {
  display: flex;
  justify-content: space-between;
  padding: 75px 50px;
  background-color: #f7f7f7;
}

.assets-list {
  display: flex;
  flex-direction: column;
  width: 20%;
}
.assets-list > * {
  margin-bottom: 10%; /* 카드 간격을 퍼센트 단위로 설정 (예: 5%) */
}

/* 마지막 카드의 margin-bottom을 제거하여 불필요한 간격이 생기지 않도록 설정 */
.assets-list > *:last-child {
  margin-bottom: 0;
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

.asset-card.selected.song-e-bg {
  background-color: #D6D6D6;
}

.asset-card.selected.won-e-bg {
  background-color: #CECEE1;
}

.asset-card img {
  width: 40px;
  height: 40px;
}

.balance {
  font-size: 18px;
  font-weight: bold;
}

.asset-details {
  width: 75%;
  padding: 20px;
  background-color: #fff;
  border-radius: 15px;
}

.song-e-bg {
  background-color: #D6D6D6;
}

.won-e-bg {
  background-color: #CECEE1;
}

.tabs {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.tabs button {
  flex: 1;
  padding: 10px;
  background-color: #f0f0f0;
  border: none;
  border-radius: 10px;
  margin-right: 10px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.tabs button:last-child {
  margin-right: 0;
}

.tabs button.active {
  background-color: #2CCE89;
}

.action-btn {
  display: block;
  width: 100%;
  padding: 10px;
  background-color: #2CCE89;
  border: none;
  border-radius: 10px;
  color: white;
  font-size: 16px;
  cursor: pointer;
  margin-top: 20px;
  transition: background-color 0.3s;
}

.action-btn:hover .action-btn2:hover {
  background-color: #2CCE89;
}

.action-btn2 {
  display: block;
  flex-direction: column;
  width: 20%;
  padding: 10px;
  background-color: #2CCE89;
  border: none;
  border-radius: 10px;
  color: white;
  font-size: 16px;
  cursor: pointer;
  margin-top: 1rem;
  margin-bottom: 1rem;
  transition: background-color 0.3s;
}

.text-btn {
  display: flex;
  align-items: center; /* 세로 중앙 정렬 */
  gap: 10px; /* p 태그와 버튼 사이의 간격을 설정 (필요에 따라 조정) */
}

/* 화면 크기가 1200px 이하일 때 */
@media (max-width: 1200px) {
  .my-assets-container {
    padding: 50px 20px; /* 좌우 패딩 줄이기 */
  }

  .assets-list {
    width: 20%;
  }
  .asset-details {
    width: 70%; /* 비율 조정 */
  }
}

/* 화면 크기가 992px 이하일 때 */
@media (max-width: 992px) {
  .my-assets-container {
    padding: 30px 10px; /* 좌우 패딩 더 줄이기 */
    flex-direction: column; /* 세로 방향으로 변경 */
    align-items: center; /* 중앙 정렬 */
  }

  .assets-list {
    display: flex;
    flex-direction: row;
    width: 100%;
  }
  .assets-list > * {
    margin-right: 10%; /* 카드 간격을 퍼센트 단위로 설정 (예: 5%) */
  }

  /* 마지막 카드의 margin-bottom을 제거하여 불필요한 간격이 생기지 않도록 설정 */
  .assets-list > *:last-child {
    margin-left: 0;
  }
}


/* 화면 크기가 768px 이하일 때 */
@media (max-width: 768px) {
  .assets-list {
    display: flex;
    flex-direction: row;
    width: 85%;
  }
  .assets-list > * {
    margin-right: 10%; /* 카드 간격을 퍼센트 단위로 설정 (예: 5%) */
  }

  /* 마지막 카드의 margin-bottom을 제거하여 불필요한 간격이 생기지 않도록 설정 */
  .assets-list > *:last-child {
    margin-left: 0;
  }

  .asset-details {
    margin-top : 10px;
    padding: 10px; /* 패딩 줄이기 */
  }
}
</style>
