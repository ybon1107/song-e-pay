<script setup>
import ArgonInput from '@/components/templates/ArgonInput.vue';
import DefaultInfoCard from '@/views/Cards/AccountsCard.vue';
// import AccountsCard from '@/views/Cards/AccountsCard2.vue';
import ArgonAmountInput from '@/components/yb_templates/ArgonAmountInput.vue';
import ArgonButton from '@/components/templates/ArgonButton.vue';
import SecondPassword from '@/views/MyAccounts/SecondPassword.vue';
import { ref, onMounted, computed } from 'vue';
import myaccountApi from '../../api/myaccountApi';
import { useExchangeStore } from '@/stores/exchangeStore';
import SecondPasswordModal from '@/views/MyAccounts/SecondPasswordModal.vue';
import { useRoute } from 'vue-router';
const route = useRoute();
const emit = defineEmits(['password-verified', 'close']);
const store = useExchangeStore();
const selectedAsset = ref('Song-E Money'); // 기본적으로 Song-E Money가 선택됨

const songEMoneyBalance = ref(0); // Song-E Money의 잔액
const wonEMoneyBalance = ref(0); // Won-E Money의 잔액
// const formattedSongEMoneyBalance = computed(() => `${customerunit.value} ${formatNumber(songEMoneyBalance.value.toFixed(2))}`);
// const formattedWonEMoneyBalance = computed(() => `KRW ${formatNumber(wonEMoneyBalance.value.toFixed(2))}`);

const activeTab = ref('deposit'); // 기본적으로 충전 탭이 선택됨

const depositAmount = ref('');
const exchangeAmount = ref('');
const refundAmount = ref('');
const transferAmount = ref('');
const reExchangeAmount = ref('');

const customerunit = ref('USD'); //나라 설정에 따라 변경되게끔

const sendEmail = ref('');
const sendEmailConfirm = ref('');
const isMember = ref(null);
const formattedSongEMoneyBalance = computed(() => `${customerunit.value} ${formatNumber(songEMoneyBalance.value.toFixed(2))}`);
const formattedWonEMoneyBalance = computed(() => `KRW ${formatNumber(wonEMoneyBalance.value.toFixed(2))}`);
const showModal = ref(false);
let currentAction = ref('');
const usdAmount = ref(1);
const krwAmount = ref(0);
const krwAmountReverse = ref(1000);
const usdAmountReverse = ref(0);

const currentToKrw = computed(() => store.currentToKrw);
const currentFromKrw = computed(() => store.currentFromKrw);

onMounted(() => {
  // 라우터 쿼리에서 selectedAsset 값을 가져옴
  if (route.query.selectedAsset) {
    selectAsset(route.query.selectedAsset);
  }
  fetchBalances();
  fetchExchangeRates();
});

//비밀번호 관련 기능
// 비밀번호 입력 모달 열기
const openModal = () => {
  showModal.value = true;
  currentAction.value = activeTab.value;
};
// 비밀번호 입력 모달 닫기
const closeModal = () => {
  showModal.value = false;
};


// 비밀번호가 확인되었을 때 호출되는 함수
const handlePasswordVerified = async () => {
  showModal.value = false; // 모달 숨김
  switch (currentAction.value) {
    case 'deposit':
      await deposit(); // deposit이 완료될 때까지 기다림
      alert('입금이 완료되었습니다.'); // 작업 완료 메시지
      break;
    case 'exchange':
      await exchange(); // exchange가 완료될 때까지 기다림
      alert('환전이 완료되었습니다.'); // 작업 완료 메시지
      break;
    case 'refund':
      await refund(); // refund가 완료될 때까지 기다림
      alert('환불이 완료되었습니다.'); // 작업 완료 메시지
      break;
    case 'transfer':
      await transfer(); // transfer가 완료될 때까지 기다림
      alert('송금이 완료되었습니다.'); // 작업 완료 메시지
      break;
    case 'reExchange':
      await reExchange(); // reExchange가 완료될 때까지 기다림
      alert('환급이 완료되었습니다.'); // 작업 완료 메시지
      break;

  }
  resetValue();
  await fetchBalances(); // 잔액을 다시 가져옴
};

//값이 입력되지 않으면 버튼 비활성화
const isValidAmount = (amount) => {
  return amount && !isNaN(amount) && parseFloat(amount) > 0;
};

// 입력된 금액 타입 확인
const formatNumber = (num) => {
  // 입력값이 숫자이면 포맷, 아니면 그대로 반환
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

// 화면상에서 입력된 금액 기반 거래 후 잔액 계산
//송이페이계좌
let processAfterBalance = computed(() => {
  let balance = 0;
  // 충전일 경우 잔액 증가
  if (activeTab.value === 'deposit') {
    balance = songEMoneyBalance.value + parseFloat(depositAmount.value || 0);
  }
  // 환전일 경우 잔액 감소
  if (activeTab.value === 'exchange') {
    balance = songEMoneyBalance.value - parseFloat(exchangeAmount.value || 0);
  }
  // 환불일 경우 잔액 감소
  else if (activeTab.value === 'refund') {
    balance = songEMoneyBalance.value - parseFloat(refundAmount.value || 0);
  }

  // 계산된 숫자를 포맷하여 반환
  return formatNumber(balance.toFixed(2)); // 소수점 두 자릿수까지 표시
});
//원화페이계좌
let processAfterWonBalance = computed(() => {
  let wonBalance = 0;

  // 충전일 경우 잔액 증가
  if (activeTab.value === 'transfer') {
    wonBalance = wonEMoneyBalance.value - parseFloat(transferAmount.value || 0);
  }

  // 환급일 경우 잔액 감소
  else if (activeTab.value === 'reExchange') {
    wonBalance = wonEMoneyBalance.value - parseFloat(reExchangeAmount.value || 0);
  }

  // 계산된 숫자를 포맷하여 반환
  return formatNumber(wonBalance.toFixed(2)); // 소수점 두 자릿수까지 표시
});

// 자산 탭 선택
const selectAsset = (asset) => {
  selectedAsset.value = asset;
  // Song-E Money 선택 시 기본 탭을 deposit로 설정, Won-E Money 선택 시 기본 탭을 transfer로 설정
  activeTab.value = asset === 'Song-E Money' ? 'deposit' : 'transfer';
};

// 충전 함수 정의
const deposit = async () => {
  const userNo = '1';
  const accountNo = '123'; // 실제 계좌 번호 사용
  const songNo = '1234'; // 송이 페이 계좌 번호
  const krwNo = '1234';
  const amount = depositAmount.value; // 충전하려는 금액
  // 송이 페이머니 충전 요청
  const response = await myaccountApi.deposit({
    amount,
    accountDTO: {
      accountNo,
    },
    songAccountDTO: {
      songNo,
    },
    historyDTO: {
      userNo,
      songNo,
      krwNo,
      typeCode: 3, //거래 코드 충전 3
      stateCode: 1,
      historyContent: 'SongE money 충전',
      amount,
    },
  });

  if (response != '') {
    console.log(response);
  }
};

// 환전 처리
const exchange = async () => {
  const userNo = '1';
  const krwNo = '1234'; // krw 계좌 번호 사용
  const songNo = '1234'; // 송이 페이 계좌 번호
  const exchangeRate = currentFromKrw.value * 1000;
  const amount = exchangeAmount.value; // 환전하려는 금액
  const response = await myaccountApi.exchange({
    amount,
    exchangeRate,
    songAccountDTO: {
      songNo,
    },
    krwAccountDTO: {
      krwNo,
    },
    historyDTO: {
      userNo,
      songNo,
      krwNo,
      typeCode: 5,
      stateCode: 1,
      historyContent: 'SongE money → WonE money',
      amount,
      exchangeRate,
    },
  });

  if (response != '') {
    console.log(response);
  }
};

// 환불 처리
const refund = async () => {
  const userNo = '1';
  const accountNo = '123'; // 실제 계좌 번호 사용
  const songNo = '1234'; // 송이 페이 계좌 번호
  const krwNo = '1234'; // krw 계좌 번호 사용
  const amount = refundAmount.value; // 충전하려는 금액
  // 송이 페이머니 충전 요청
  const response = await myaccountApi.refund({
    amount,
    accountDTO: {
      accountNo,
    },
    songAccountDTO: {
      songNo,
    },
    historyDTO: {
      userNo,
      songNo,
      krwNo,
      typeCode: 4,
      stateCode: 1,
      historyContent: 'SongE money → My Account',
      amount,
    },
  });

  if (response != '') {
    console.log(response);
  }
};

// 송금 처리
const transfer = async () => {
  const userNo = '1';
  const songNo = '1234'; // 송이 페이 계좌 번호
  const krwNo = '1234'; // krw 계좌 번호 사용
  const target_krwNo = '1234'; // 보내는 사람 페이 계좌 번호
  const amount = transferAmount.value; // 충전하려는 금액
  // 송이 페이머니 충전 요청
  const response = await myaccountApi.transfer({
    amount,
    target_krwNo,
    krwAccountDTO: {
      krwNo,
    },
    historyDTO: {
      userNo,
      songNo,
      krwNo,
      typeCode: 2, //거래 코드 충전 2
      stateCode: 1,
      historyContent: `나의 WonE money → ${sendEmail}`,
      amount,
    },
  });

  if (response != '') {
    console.log(response);
  }
};

// 환급 처리
const reExchange = async () => {
  const userNo = '1';
  const krwNo = '1234'; // krw 계좌 번호 사용
  const songNo = '1234'; // 송이 페이 계좌 번호
  const exchangeRate = currentToKrw.value;
  const amount = reExchangeAmount.value; // 환급하려는 금액
  console.log('exchangeRate 확인' + exchangeRate);
  const response = await myaccountApi.reExchange({
    amount,
    exchangeRate,
    krwAccountDTO: {
      krwNo,
    },
    songAccountDTO: {
      songNo,
    },
    historyDTO: {
      userNo,
      songNo,
      krwNo,
      typeCode: 6,
      stateCode: 1,
      historyContent: 'WonE money → SongE money',
      amount,
      exchangeRate,
    },
  });

  if (response != '') {
    console.log(response);
  }
};

const resetValue = () => {
  refundAmount.value = '';
  reExchangeAmount.value = '';
  transferAmount.value = '';
  exchangeAmount.value = '';
  depositAmount.value = '';
  sendEmail.value = '';
  sendEmailConfirm.value = '';
};

const convertToKrw = () => {
  krwAmount.value = (usdAmount.value * currentToKrw.value).toFixed(2);
};

const convertToUsd = () => {
  usdAmountReverse.value = (krwAmountReverse.value * currentFromKrw.value).toFixed(2);
};
// 환율 데이터를 가져오는 함수
const fetchExchangeRates = async () => {
  try {
    convertToKrw();
    convertToUsd();
  } catch (error) {
    console.error('환율 데이터를 가져오는 중 오류 발생:', error);
  }
};
const fetchBalances = () => {
  myaccountApi.fetchkrwAccountBalance('1234').then((balance) => {
    wonEMoneyBalance.value = balance;
  });

  myaccountApi.fetchsongeAccountBalance('1234').then((balance) => {
    songEMoneyBalance.value = balance;
  });
};

// 받는 금액 계산
const receivedAmount = computed(() => {
  if (selectedAsset.value === 'Song-E Money') {
    return (exchangeAmount.value * currentToKrw.value).toFixed(2);
  } else if (selectedAsset.value === 'Won-E Money') {
    return (reExchangeAmount.value * currentFromKrw.value).toFixed(2);
  }
});

// 이메일 입력 유효성 검증
let isconfirmed = false;
let success = false;
const emailConfirm = async () => {
  isconfirmed = true;
  const userId = sendEmail.value;
  const response = await myaccountApi.confirmEmail(userId);
  if (response) {
    if (userId === 'test@gmail.com') {
      //userId 세션에서 가져오기(로그인할때 사용된 이메일)
      isMember.value = 'self';
      success = false;
      errorMessage.value = '본인이 아닌 다른 회원의 이메일을 입력하세요';
    } else {
      isMember.value = 'member'; // 회원 이메일로 표시
      success = true;
      errorMessage.value = '';
    }
  } else if (sendEmail.value !== '' && emailPattern.test(sendEmail.value)) {
    isMember.value = 'no-member'; // 비회원 이메일로 표시
    success = true;
    errorMessage.value = '';
  } else {
    isMember.value = 'error';
    success = false;
  }
};
// 이메일 형식 확인을 위한 정규식
const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

const errorMessageCheck = ref('');
let checkSucess = false;
const onInputCheck = (event) => {
  if (sendEmailConfirm.value === '' && checkSucess) {
    errorMessageCheck.value = '이메일 재입력하세요.';
  } else {
    if (sendEmail.value !== event.target.value) {
      errorMessageCheck.value = '입력하신 이메일과 일치하지 않습니다.';
    } else {
      errorMessageCheck.value = '';
      checkSucess = true;
    }
  }
};
const errorMessage = ref('');
const onInput = (event) => {
  sendEmail.value = event.target.value;
  isconfirmed = false;

  if (sendEmail.value == '' && isconfirmed) {
    errorMessage.value = '이메일을 입력해주세요.';
    isconfirmed = false;
  } else if (!emailPattern.test(sendEmail.value)) {
    errorMessage.value = '올바른 이메일 형식을 지켜야 합니다.';
  } else if (isconfirmed == false) {
    errorMessage.value = '이메일 확인이 필요합니다.';
  } else if (isMember.value === 'self') {
    errorMessage.value = '본인이 아닌 다른 회원의 이메일을 입력하세요';
  } else {
    errorMessage.value = '';
  }
  if (sendEmailConfirm.value !== '') {
    onInputCheck({ target: { value: sendEmailConfirm.value } });
  }
};
</script>

<template>
  <div class="container-fluid" style="width: 80%" id="responsive-container">
    <h3>My account</h3>

    <SecondPasswordModal v-if="showModal" @close="closeModal" @password-verified="handlePasswordVerified" />

    <!-- <div class="row my-3">
      <div class="col-lg-4 col-md-5">
        <AccountsCard title="Song-E Money" :balance="songEMoneyBalance" :currency="customerunit"
          backgroundImage="/images/song-e-money.png" icon="/images/america.png"
          @click="selectAsset('Song-E Money')" :isSelected="selectedAsset === 'Song-E Money'"/>
>
      </div>
      <div class="col-lg-4 col-md-5">
        <AccountsCard title="Won-E Money" :balance="wonEMoneyBalance" currency="KRW"
          backgroundImage="/images/won-e-money.png" icon="/images/korea.png"
          @click="selectAsset('Won-E Money')" :isSelected="selectedAsset === 'Won-E Money'"/>

      </div>
    </div> -->

    <div class="assets-list">

      <DefaultInfoCard
        title="Song-E Money"
        :value="formattedSongEMoneyBalance"
        img-src="/images/song-e-money.png"
        img="/images/america.png"
        @click="selectAsset('Song-E Money')"
        :class="{ selected: selectedAsset === 'Song-E Money' }"
      />

      <DefaultInfoCard title="Won-E Money" :value="formattedWonEMoneyBalance" img-src="images/won-e-money.png"
        img="/images/korea.png" @click="selectAsset('Won-E Money')"
        :class="{ selected: selectedAsset === 'Won-E Money' }" />
    </div>

    <div class="card">
      <!-- Song-E Money 선택 시 -->
      <template v-if="selectedAsset === 'Song-E Money'">
        <nav class="nav flex-column flex-sm-row">
          <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === 'deposit' }"
            @click="activeTab = 'deposit'" aria-current="page"> 충전 </a>
          <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === 'exchange' }"
            @click="activeTab = 'exchange'"> 환전 </a>
          <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === 'refund' }"
            @click="activeTab = 'refund'"> 환불 </a>
        </nav>
      </template>

      <!-- Won-E Money 선택 시 -->
      <template v-if="selectedAsset === 'Won-E Money'">
        <nav class="nav flex-column flex-sm-row">
          <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === 'transfer' }"
            @click="activeTab = 'transfer'" aria-current="page"> 송금 </a>
          <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === 'reExchange' }"
            @click="activeTab = 'reExchange'"> 환급 </a>
        </nav>
      </template>

      <!-- Song-E Money의 탭 내용 -->
      <div class="card-body" v-if="selectedAsset === 'Song-E Money'">
        <div v-if="activeTab === 'deposit'" class="tab-pane fade show active">
          <p>
            <small class="text-muted">충전 금액</small>
            <ArgonAmountInput v-model="depositAmount" placeholder="금액을 입력하세요" :unit="customerunit" />
          </p>
          <p>
            <small class="text-muted">
              충전계좌:
              {{ selectedAsset === 'Song-E Money' ? '내 계좌' : 'KRW 계좌' }}
            </small>
          </p>
          <p>
            <small class="text-muted">
              거래 후 잔액: {{ processAfterBalance }}
              {{ customerunit }}
            </small>
          </p>

          <argon-button type="submit" color="success" size="lg" class="w-100" @click="openModal" :disabled="!isValidAmount(depositAmount)" variant="gradient"
            >충전하기</argon-button
          >
        </div>

        <div v-if="activeTab === 'exchange'">
          <p>
            <small class="text-muted">현재 환율</small><br />
            <small class="text-muted">1 KRW = {{ currentFromKrw }} {{ customerunit }}</small>
          </p>
          <p>
            <small class="text-muted">보내는 금액</small>
            <ArgonAmountInput
              v-model="exchangeAmount"
              placeholder="얼마를 환전할까요?"
              :unit="customerunit"
              :selectedAsset="selectedAsset"
              :songEMoneyBalance="songEMoneyBalance"
              :activeTab="activeTab"
            />
          </p>
          <p>
            <small class="text-muted">받는 금액</small><br />
            <small class="text-muted">{{ receivedAmount }} KRW</small>
          </p>
          <p>
            <small class="text-muted">
              환급계좌:
              {{ selectedAsset === 'Song-E Money' ? '내 계좌' : 'KRW 계좌' }}
            </small>
          </p>
          <p>
            <small class="text-muted">
              거래 후 잔액: {{ processAfterBalance }}
              {{ customerunit }}
            </small>
          </p>

          <argon-button type="submit" color="success" size="lg" class="w-100" @click="openModal" :disabled="!isValidAmount(exchangeAmount)" variant="gradient"
            >환전하기</argon-button
          >
        </div>

        <div v-if="activeTab === 'refund'">
          <p>
            <small class="text-muted">환불할 금액을 입력하세요</small>
            <ArgonAmountInput
              v-model="refundAmount"
              placeholder="얼마를 환불할까요?"
              :unit="customerunit"
              :selectedAsset="selectedAsset"
              :songEMoneyBalance="songEMoneyBalance"
              :activeTab="activeTab"
            />
          </p>

          <p>
            <small class="text-muted">
              환불계좌:
              {{ selectedAsset === 'Song-E Money' ? '내 계좌' : 'KRW 계좌' }}
            </small>
          </p>
          <p>
            <small class="text-muted">
              거래 후 잔액: {{ processAfterBalance }}
              {{ customerunit }}
            </small>
          </p>

          <argon-button type="submit" color="success" size="lg" class="w-100" @click="openModal" :disabled="!isValidAmount(refundAmount)" variant="gradient">환불하기</argon-button>
        </div>
      </div>

      <!-- Won-E Money의 탭 내용 -->
      <div class="card-body" v-if="selectedAsset === 'Won-E Money'">
        <div v-if="activeTab === 'transfer'">
          <div class="text-btn">
            <small class="text-muted" style="margin-top: 1rem; margin-bottom: 1rem">받는 이메일</small>
            <argon-button class="action-btn2" @click="emailConfirm" size="sm" variant="outline" :disabled="sendEmail === ''">이메일 확인</argon-button>
            <!-- 회원/비회원 표시 -->
            <small class="text-muted" v-if="isMember === 'member'">회원 이메일</small>
            <small class="text-muted" v-else-if="isMember === 'no-member'">비회원 이메일</small>
          </div>
          <ArgonInput
            v-model="sendEmail"
            placeholder="받는 분의 이메일을 입력하세요"
            @input="onInput"
            variant="gradient"
            :class="{ 'is-invalid': errorMessage }"
            :error="errorMessage !== ''"
            :success="success"
            style="margin-bottom: 0"
          />
          <div v-if="errorMessage !== ''" class="invalid-feedback text-xs mb-1">{{ errorMessage }}</div>
          <small class="text-muted">이메일 확인</small>
          <ArgonInput
            v-model="sendEmailConfirm"
            placeholder="이메일을 다시 입력하세요"
            :class="{ 'is-invalid': errorMessageCheck }"
            :error="errorMessageCheck !== ''"
            :success="checkSucess"
            style="margin-bottom: 0"
            @input="onInputCheck"
          />
          <div v-if="errorMessageCheck" class="invalid-feedback text-xs mb-1">{{ errorMessageCheck }}</div>

          <p>
            <small class="text-muted">송금할 금액을 입력하세요</small>
            <ArgonAmountInput
              v-model="transferAmount"
              placeholder="얼마를 송금할까요?"
              unit="KRW"
              :selectedAsset="selectedAsset"
              :wonEMoneyBalance="wonEMoneyBalance"
              :activeTab="activeTab"
            />
          </p>

          <p>
            <small class="text-muted">송금 후 잔액: {{ processAfterWonBalance }} KRW</small>
          </p>

          <argon-button
            type="submit"
            color="success"
            size="lg"
            class="w-100"
            @click="openModal"
            :disabled="!isValidAmount(transferAmount) || errorMessage !== '' || errorMessageCheck !== ''"
            variant="gradient"
            >송금하기</argon-button
          >
        </div>

        <div v-if="activeTab === 'reExchange'">
          <p>
            <small class="text-muted">현재 환율</small><br />
            <small class="text-muted">1{{ customerunit }} = {{ currentToKrw }} KRW</small>
          </p>
          <p>
            <small class="text-muted">보내는 금액</small><br />
            <small class="text-muted">
              <ArgonAmountInput
                v-model="reExchangeAmount"
                placeholder="얼마를 환급할까요?"
                unit="KRW"
                :selectedAsset="selectedAsset"
                :wonEMoneyBalance="wonEMoneyBalance"
                :activeTab="activeTab"
              />
            </small>
          </p>
          <p>
            <small class="text-muted">받는 금액</small><br />
            <small class="text-muted">{{ receivedAmount }} USD</small>
          </p>
          <p>
            <small class="text-muted">거래 후 잔액: {{ processAfterWonBalance }} KRW</small>
          </p>

          <argon-button type="submit" color="success" size="lg" class="w-100" @click="openModal" :disabled="!isValidAmount(reExchangeAmount)" variant="gradient"
            >환급하기</argon-button
          >
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
  border-radius: 3px;
  transition: background-color 0.3s ease;
}

/* 탭 활성화 스타일 */
.nav-link.active {
  background-color: #2cce89;
  border-radius: 3px;
  color: white;
}

/* 비활성화된 탭 스타일 */
.nav-link:hover {
  background-color: #e9ecef;
  border-radius: 3px;
}

/* 기타 스타일 */
.assets-list {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  /* 카드를 가로로 전체 너비에 맞추기 */
  margin-bottom: 20px;
  /* 탭과의 간격 설정 */
}
/* For mobile screens */
@media (max-width: 768px) {
  .assets-list {
    flex-direction: column; /* Vertical alignment on smaller screens */
    gap: 15px; /* Adjust the gap for smaller screens */
    align-items: center;
  }

  .assets-list > * {
    width: 70%; /* Each item takes up full width on mobile */
    max-width: none; /* Remove the max-width on mobile */
  }
  #responsive-container {
    width: 100% !important;
  }
}

.text-btn {
  display: flex;
  align-items: center;
  /* 세로 중앙 정렬 */
  gap: 10px;
  /* p 태그와 버튼 사이의 간격을 설정 (필요에 따라 조정) */
}
</style>
