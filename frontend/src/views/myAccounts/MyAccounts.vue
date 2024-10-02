<script setup>
import ArgonInput from '@/components/templates/ArgonInput.vue';
import AccountsCard from '@/views/Cards/AccountsCard2.vue';
import ArgonAmountInput from '@/components/yb_templates/ArgonAmountInput.vue';
import ArgonButton from '@/components/templates/ArgonButton.vue';
import { ref, onMounted, computed } from 'vue';
import myaccountApi from '../../api/myaccountApi';
import { useExchangeStore } from '@/stores/exchangeStore';
import SecondPasswordModal from '@/views/MyAccounts/SecondPasswordModal.vue';
import axios from 'axios';
import ExchangeRateChart from '@/views/Chart/ExchangeRateChart.vue';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';
import {
  TRANSACTION_TYPES,
} from "@/constants/transactionType";

import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const user = computed(() => auth.user);

import currencyFormatter from '../../js/currencyFormatter';
const { formatNumber } = currencyFormatter;

const emit = defineEmits(['password-verified', 'close']);
const store = useExchangeStore();

const SONGE = 'song-e';
const WONE = 'won-e';

const selectedAsset = ref(SONGE); // 기본적으로 Song-E Money가 선택됨

const songEMoneyBalance = ref(0); // Song-E Money의 잔액
const wonEMoneyBalance = ref(0); // Won-E Money의 잔액

const activeTab = ref(TRANSACTION_TYPES.DEPOSIT); // 기본적으로 충전 탭이 선택됨

const depositAmount = ref('');
const exchangeAmount = ref('');
const refundAmount = ref('');
const transferAmount = ref('');
const reExchangeAmount = ref('');

const customerunit = ref(user.value.country); //나라 설정에 따라 변경되게끔

const sendEmail = ref('');
const sendEmailConfirm = ref('');
const isMember = ref(null);
const showModal = ref(false);
let currentAction = ref('');
const usdAmount = ref(1);
const krwAmount = ref(0);
const krwAmountReverse = ref(1000);
const usdAmountReverse = ref(0);

const currentToKrw = computed(() => store.currentToKrw);
const currentFromKrw = computed(() => store.currentFromKrw);


const songEMoneyCardRef = ref(null);
const wonEMoneyCardRef = ref(null);

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
    case TRANSACTION_TYPES.DEPOSIT:
      await deposit(); // deposit이 완료될 때까지 기다림
      Swal.fire({
        title: '성공!',
        text: '입금이 완료되었습니다.',
        icon: 'success',
      });
      break;
    case TRANSACTION_TYPES.EXCHANGE:
      await exchange(); // exchange가 완료될 때까지 기다림
      Swal.fire({
        title: '성공!',
        text: '환전이 완료되었습니다.',
        icon: 'success',
      });
      break;
    case TRANSACTION_TYPES.REFUND:
      await refund(); // refund가 완료될 때까지 기다림
      Swal.fire({
        title: '성공!',
        text: '환불이 완료되었습니다.',
        icon: 'success',
      });
      break;
    case TRANSACTION_TYPES.TRANSFER:
      await transfer(); // transfer가 완료될 때까지 기다림
      Swal.fire({
        title: '성공!',
        text: '송금이 완료되었습니다.',
        icon: 'success',
      });
      break;
    case TRANSACTION_TYPES.RE_EXCHANGE:
      await reExchange(); // reExchange가 완료될 때까지 기다림
      Swal.fire({
        title: '성공!',
        text: '환급이 완료되었습니다.',
        icon: 'success',
      });
      break;
  }
  resetValue();
  await fetchBalances(); // 잔액을 다시 가져옴

  // AccountsCard2의 fetchBalance 함수 호출
  if (songEMoneyCardRef.value) {
    await songEMoneyCardRef.value.fetchBalance();
  }
  if (wonEMoneyCardRef.value) {
    await wonEMoneyCardRef.value.fetchBalance();
  }
};

//값이 입력되지 않으면 버튼 비활성화
const isValidAmount = (amount) => {
  return amount && !isNaN(amount) && parseFloat(amount) > 0;
};


// 화면상에서 입력된 금액 기반 거래 후 잔액 계산
//송이페이계좌
let processAfterBalance = computed(() => {
  let balance = 0;
  // 충전일 경우 잔액 증가
  if (activeTab.value === TRANSACTION_TYPES.DEPOSIT) {
    balance = songEMoneyBalance.value + parseFloat(depositAmount.value || 0);
  }
  // 환전일 경우 잔액 감소
  if (activeTab.value === TRANSACTION_TYPES.EXCHANGE) {
    balance = songEMoneyBalance.value - parseFloat(exchangeAmount.value || 0);
  }
  // 환불일 경우 잔액 감소
  else if (activeTab.value === TRANSACTION_TYPES.REFUND) {
    balance = songEMoneyBalance.value - parseFloat(refundAmount.value || 0);
  }

  // 계산된 숫자를 포맷하여 반환
  return formatNumber(balance.toFixed(2)); // 소수점 두 자릿수까지 표시
});
//원화페이계좌
let processAfterWonBalance = computed(() => {
  let wonBalance = 0;

  // 충전일 경우 잔액 증가
  if (activeTab.value === TRANSACTION_TYPES.REFUND) {
    wonBalance = wonEMoneyBalance.value - parseFloat(transferAmount.value || 0);
  }

  // 환급일 경우 잔액 감소
  else if (activeTab.value === TRANSACTION_TYPES.RE_EXCHANGE) {
    wonBalance = wonEMoneyBalance.value - parseFloat(reExchangeAmount.value || 0);
  }

  // 계산된 숫자를 포맷하여 반환
  return formatNumber(wonBalance.toFixed(2)); // 소수점 두 자릿수까지 표시
});

// 자산 탭 선택
const selectAsset = (asset) => {
  selectedAsset.value = asset;
  // Song-E Money 선택 시 기본 탭을 deposit로 설정, Won-E Money 선택 시 기본 탭을 transfer로 설정
  activeTab.value = asset === SONGE ? TRANSACTION_TYPES.DEPOSIT : TRANSACTION_TYPES.TRANSFER;
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
      historyContent: `My Account → ${customerunit} 충전`,
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
      historyContent: `${customerunit} → KRW 환전`,
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
      historyContent: `${customerunit} → My Account 환불`,
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
      historyContent: `KRW → ${sendEmail} 송금`,
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
      historyContent: `KRW → ${customerunit} 환급`,
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
  if (selectedAsset.value === SONGE) {
    return (parseFloat(exchangeAmount.value) * currentToKrw.value).toFixed(2);
  } else if (selectedAsset.value === WONE) {
    return (parseFloat(reExchangeAmount.value) * currentFromKrw.value).toFixed(2);
  }
  return '0.00'; // 기본값 추가
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

// 자동 환전 내역을 저장할 ref
const autoConditions = ref([]);

// 환율 알림 내역을 저장할 ref
const alertConditions = ref([]);

// 사용자 번호를 설정 (임의로 1로 설정)
const userNo = 1;

// 자동 환전 예약 데이터를 가져오는 함수
const fetchAutoExchange = async () => {
  try {
    const response = await axios.get(`/api/exchange-reservation/setalert/${userNo}`);
    if (response.status === 200) {
      // 응답 데이터가 존재하면 autoConditions에 저장
      const reservations = response.data;
      autoConditions.value = reservations;
    }
  } catch (error) {
    console.error('자동 환전 데이터를 가져오는 중 오류 발생:', error);
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
      console.log('Fetched alert conditions:', alertConditions.value);
      // fetchAlertConditions(); // 알림을 다시 가져와서 업데이트
    }
  } catch (error) {
    console.error('환율 알림 데이터를 가져오는 중 오류 발생:', error);
  }
};

const confirmDelete = (resNo) => {
  Swal.fire({
    title: '정말로 삭제하시겠습니까?',
    text: '삭제된 알림 설정은 복구할 수 없습니다!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: '네, 삭제합니다',
    cancelButtonText: '취소',
    buttonsStyling: true,
  }).then((result) => {
    if (result.isConfirmed) {
      deleteAlertCondition(resNo);
    }
  });
};

const deleteAlertCondition = async (resNo) => {
  try {
    console.log('삭제할 예약 번호:', resNo);
    if (resNo) {
      await axios.delete(`/api/exchange-reservation/${resNo}`);
      Swal.fire({
        title: '삭제 완료!',
        text: '알림 설정이 삭제되었습니다.',
        icon: 'success',
      });
      fetchAlertConditions(); // 알림을 다시 가져와서 업데이트
      fetchAutoExchange(); // 예약을 다시 가져와서 업데이트
    }
  } catch (error) {
    console.error('알림 설정 삭제 중 오류 발생:', error);
    Swal.fire({
      title: '오류 발생',
      text: '알림 설정 삭제 중 문제가 발생했습니다.',
      icon: 'error',
    });
  }
};

// 컴포넌트가 마운트될 때 데이터를 가져옴
onMounted(() => {
  fetchExchangeRates();
  fetchAutoExchange();
  fetchAlertConditions();
  // // 라우터 쿼리에서 selectedAsset 값을 가져옴
  // if (route.query.selectedAsset) {
  //   selectAsset(route.query.selectedAsset);
  // }
  fetchBalances();
});
</script>

<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
    integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA=="
    crossorigin="anonymous" referrerpolicy="no-referrer" />
  <div class="container-fluid">
    <h3>My account</h3>

    <SecondPasswordModal v-if="showModal" @close="closeModal" @password-verified="handlePasswordVerified" />

    <div class="row mt-3">
      <!-- USD Wallet -->
      <div class="col-md-5 card-up">
        <!-- Song-E Money 카드 -->
        <AccountsCard ref="songEMoneyCardRef" :assetType=SONGE @click="selectAsset(SONGE)"
          :class="{ selected: selectedAsset === SONGE }" />
      </div>

      <!-- KRW Wallet -->
      <div class="col-md-5">
        <!-- Won-E Money 카드 -->
        <AccountsCard ref="wonEMoneyCardRef" :assetType=WONE @click="selectAsset(WONE)"
          :class="{ selected: selectedAsset === WONE }" />
      </div>
    </div>
    <!-- <div class="assets-list">
      <DefaultInfoCard title="Song-E Money" :value="formattedSongEMoneyBalance" img-src="/images/song-e-money.png"
        img="/images/america.png" @click="selectAsset('Song-E Money')"
        :class="{ selected: selectedAsset === 'Song-E Money' }" />

      <DefaultInfoCard title="Won-E Money" :value="formattedWonEMoneyBalance" img-src="images/won-e-money.png"
        img="/images/korea.png" @click="selectAsset('Won-E Money')"
        :class="{ selected: selectedAsset === 'Won-E Money' }" />
    </div> -->

    <div class="card mt-3">
      <!-- Song-E Money 선택 시 -->
      <template v-if="selectedAsset === SONGE">
        <nav class="nav custom-nav nav-underline flex-column flex-sm-row">
          <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === TRANSACTION_TYPES.DEPOSIT }"
            @click="activeTab = TRANSACTION_TYPES.DEPOSIT" aria-current="page"> 충전 </a>
          <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === TRANSACTION_TYPES.EXCHANGE }"
            @click="activeTab = TRANSACTION_TYPES.EXCHANGE"> 환전 </a>
          <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === TRANSACTION_TYPES.REFUND }"
            @click="activeTab = TRANSACTION_TYPES.REFUND"> 환불 </a>
        </nav>
      </template>

      <!-- Won-E Money 선택 시 -->
      <template v-if="selectedAsset === WONE">
        <nav class="nav custom-nav nav-underline flex-column flex-sm-row">
          <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === TRANSACTION_TYPES.TRANSFER }"
            @click="activeTab = TRANSACTION_TYPES.TRANSFER" aria-current="page"> 송금 </a>
          <a class="flex-sm-fill text-sm-center nav-link"
            :class="{ active: activeTab === TRANSACTION_TYPES.RE_EXCHANGE }"
            @click="activeTab = TRANSACTION_TYPES.RE_EXCHANGE"> 환급 </a>
        </nav>
      </template>

      <!-- Song-E Money의 탭 내용 -->
      <div class="card-body" v-if="selectedAsset === SONGE">
        <div v-if="activeTab === TRANSACTION_TYPES.DEPOSIT" class="tab-pane fade show active">
          <p>
            <label class="text-muted">충전 금액</label>
            <ArgonAmountInput v-model="depositAmount" placeholder="금액을 입력하세요" :unit="customerunit" />
          </p>
          <p>
            <small class="text-muted">
              충전계좌:
              {{ selectedAsset === SONGE ? '내 계좌' : 'KRW 계좌' }}
            </small>
          </p>
          <p>
            <small class="text-muted">
              거래 후 잔액: {{ processAfterBalance }}
              {{ customerunit }}
            </small>
          </p>

          <button type="submit" class="btn btn-primary w-100" @click="openModal"
            :disabled="!isValidAmount(depositAmount)" variant="gradient">충전하기</button>
        </div>

        <div v-if="activeTab === TRANSACTION_TYPES.EXCHANGE">
          <p>
            <small class="text-muted">현재 환율</small><br />
            <small class="text-muted">1 KRW = {{ currentFromKrw }} {{ customerunit }}</small>
          </p>
          <p>
            <small class="text-muted">보내는 금액</small>
            <ArgonAmountInput v-model="exchangeAmount" placeholder="얼마를 환전할까요?" :unit="customerunit"
              :selectedAsset="selectedAsset" :songEMoneyBalance="songEMoneyBalance" :activeTab="activeTab" />
          </p>
          <p>
            <small class="text-muted">받는 금액</small><br />
            <small class="text-muted">{{ receivedAmount }} KRW</small>
          </p>
          <p>
            <small class="text-muted">
              환급계좌:
              {{ selectedAsset === SONGE ? '내 계좌' : 'KRW 계좌' }}
            </small>
          </p>
          <p>
            <small class="text-muted">
              거래 후 잔액: {{ processAfterBalance }}
              {{ customerunit }}
            </small>
          </p>

          <button type="submit" class="btn btn-primary w-100" @click="openModal"
            :disabled="!isValidAmount(exchangeAmount)" variant="gradient">환전하기</button>
        </div>

        <div v-if="activeTab === TRANSACTION_TYPES.REFUND">
          <p>
            <small class="text-muted">환불할 금액을 입력하세요</small>
            <ArgonAmountInput v-model="refundAmount" placeholder="얼마를 환불할까요?" :unit="customerunit"
              :selectedAsset="selectedAsset" :songEMoneyBalance="songEMoneyBalance" :activeTab="activeTab" />
          </p>

          <p>
            <small class="text-muted">
              환불계좌:
              {{ selectedAsset === SONGE ? '내 계좌' : 'KRW 계좌' }}
            </small>
          </p>
          <p>
            <small class="text-muted">
              거래 후 잔액: {{ processAfterBalance }}
              {{ customerunit }}
            </small>
          </p>

          <button type="submit" class="btn btn-primary w-100" @click="openModal"
            :disabled="!isValidAmount(refundAmount)" variant="gradient">환불하기</button>
        </div>
      </div>

      <!-- Won-E Money의 탭 내용 -->
      <div class="card-body" v-if="selectedAsset === WONE">
        <div v-if="activeTab === TRANSACTION_TYPES.TRANSFER">

          <div class="d-flex align-items-center">
            <small class="text-muted me-3">받는 이메일</small>
            <button class="btn btn-sm btn-secondary mb-0" @click="emailConfirm" size="sm" variant="outline"
              :disabled="sendEmail === ''">이메일
              확인</button>
            <!-- 회원/비회원 표시 -->
            <small class="text-muted" v-if="isMember === 'member'">회원 이메일</small>
            <small class="text-muted" v-else-if="isMember === 'no-member'">비회원 이메일</small>
          </div>
          <ArgonInput v-model="sendEmail" placeholder="받는 분의 이메일을 입력하세요" @input="onInput" variant="gradient"
            :class="{ 'is-invalid': errorMessage }" :error="errorMessage !== ''" :success="success"
            style="margin-bottom: 0" />
          <div v-if="errorMessage !== ''" class="invalid-feedback text-xs mb-1">
            {{ errorMessage }}
          </div>


          <small class="text-muted">이메일 확인</small>
          <ArgonInput v-model="sendEmailConfirm" placeholder="이메일을 다시 입력하세요"
            :class="{ 'is-invalid': errorMessageCheck }" :error="errorMessageCheck !== ''" :success="checkSucess"
            style="margin-bottom: 0" @input="onInputCheck" />

          <div v-if="errorMessageCheck" class="invalid-feedback text-xs mb-1">
            {{ errorMessageCheck }}
          </div>

          <p>
            <small class="text-muted">송금할 금액을 입력하세요</small>
            <ArgonAmountInput v-model="transferAmount" placeholder="얼마를 송금할까요?" unit="KRW"
              :selectedAsset="selectedAsset" :wonEMoneyBalance="wonEMoneyBalance" :activeTab="activeTab" />
          </p>

          <p>
            <small class="text-muted">송금 후 잔액: {{ processAfterWonBalance }} KRW</small>
          </p>

          <button type="submit" class="btn btn-primary w-100" @click="openModal"
            :disabled="!isValidAmount(transferAmount) || errorMessage !== '' || errorMessageCheck !== ''"
            variant="gradient">송금하기</button>
        </div>

        <div v-if="activeTab === TRANSACTION_TYPES.RE_EXCHANGE">
          <p>
            <small class="text-muted">현재 환율</small><br />
            <small class="text-muted">1{{ customerunit }} = {{ currentToKrw }} KRW</small>
          </p>
          <p>
            <small class="text-muted">보내는 금액</small><br />
            <small class="text-muted">
              <ArgonAmountInput v-model="reExchangeAmount" placeholder="얼마를 환급할까요?" unit="KRW"
                :selectedAsset="selectedAsset" :wonEMoneyBalance="wonEMoneyBalance" :activeTab="activeTab" />
            </small>
          </p>
          <p>
            <small class="text-muted">받는 금액</small><br />
            <small class="text-muted">{{ receivedAmount }} USD</small>
          </p>
          <p>
            <small class="text-muted">거래 후 잔액: {{ processAfterWonBalance }} KRW</small>
          </p>

          <button type="submit" class="btn btn-primary w-100" @click="openModal"
            :disabled="!isValidAmount(reExchangeAmount)" variant="gradient">환급하기</button>
        </div>
      </div>
    </div>
    <br />




    <!-- Graph and Conversion Section -->
    <h3>Exchange Rate</h3>
    <div class="card mt-3">
      <div class="card-body">
        <div class="row">
          <!-- Exchange Rate Graph Section -->
          <div class="col-lg-7 col-md-12 my-3">
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
                  <img src="@/assets/img/icons/flags/US.png" alt="USA Flag" class="input-flag-icon" />
                </div>
                <span class="mx-3">=</span>
                <div class="position-relative flex-grow-1">
                  <input type="text" class="form-control" :value="krwAmount" readonly aria-label="Amount in KRW" />
                  <img src="@/assets/img/icons/flags/KR.png" alt="KRW Flag" class="input-flag-icon" />
                </div>
              </div>
            </div>
            <div>
              <h6>Convert KRW to USD</h6>
              <div class="d-flex align-items-center">
                <div class="position-relative flex-grow-1">
                  <input type="number" class="form-control" v-model.number="krwAmountReverse" @input="convertToUsd" />
                  <img src="@/assets/img/icons/flags/KR.png" alt="KRW Flag" class="input-flag-icon" />
                </div>
                <span class="mx-3">=</span>
                <div class="position-relative flex-grow-1">
                  <input type="text" class="form-control" :value="usdAmountReverse" readonly />
                  <img src="@/assets/img/icons/flags/US.png" alt="USA Flag" class="input-flag-icon" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>




    <br />
    <div @click="$router.push('/exchange-rate')" class="clickable-alert" role="button" tabindex="0">
      <span class="alert-text">자동 환전 및 알람</span>
      <i class="fa-solid fa-angle-right alert-icon"></i>
    </div>
    <div class="card mt-4">
      <div class="card-body">
        <div class="row">
          <div class="col-lg-12">
            <div class="form-group">
              <label for="autoCondition" class="label">자동 환전 설정 내역</label>
              <ul v-if="autoConditions.length > 0" class="list-group">
                <li class="list-group-item">
                  <div class="d-flex justify-content-between align-items-center">
                    <span>
                      <span>기준 통화:</span>
                      {{ autoConditions[0]?.baseCode }}
                      <span>대상 통화:</span>
                      {{ autoConditions[0]?.targetCode }}
                      <span>목표 환율:</span>
                      {{ autoConditions[0]?.targetExchange }}
                      <span>목표 KRW 금액:</span>
                      {{ autoConditions[0]?.targetKrw }}
                    </span>
                    <button class="btn delete-btn btn-sm align-self-center"
                      @click="confirmDelete(autoConditions[0]?.resNo)">삭제</button>
                  </div>
                </li>
              </ul>
              <p v-else style="margin-left: 0.5rem">자동 환전 예약 내역이 없습니다.</p>
            </div>
            <div class="form-group">
              <label for="alertConditions" class="label">환율 알림 설정 내역</label>
              <ul v-if="alertConditions.length > 0" class="list-group">
                <li v-for="condition in alertConditions" :key="condition.resNo" class="list-group-item">
                  <div class="d-flex justify-content-between align-items-center">
                    <span>
                      <span>기준 통화:</span> {{ condition.baseCode }} <span>대상 통화:</span> {{ condition.targetCode }}
                      <span>목표
                        환율:</span> {{ condition.targetExchange }}
                    </span>
                    <button class="btn delete-btn btn-sm" @click="confirmDelete(condition.resNo)">삭제</button>
                  </div>
                </li>
              </ul>
              <p v-if="alertConditions.length === 0" style="margin-left: 0.5rem">환율 알림 예약 내역이 없습니다.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.clickable-alert {
  display: flex;
  justify-content: space-between;
  /* 텍스트와 아이콘을 좌우로 배치 */
  align-items: center;
  background-color: transparent;
  /* 기본 배경색 없음 */
  padding: 10px;
  transition: background-color 0.3s ease;
  cursor: pointer;
  outline: none;
}

.clickable-alert:hover,
.clickable-alert:focus {
  background-color: #e0f7fa;
  /* 클릭 시 배경 색상 */
}

.alert-text {
  font-size: 1.5rem;
  /* 텍스트 크기를 더 크게 설정 */
  margin: 0;
  /* 기본 여백 제거 */
  font-weight: bolder;
}

.alert-icon {
  font-size: 1.2rem;
  margin-left: auto;
  /* 아이콘을 오른쪽으로 정렬 */
}

/* 반응형 스타일 */
@media (max-width: 768px) {
  .clickable-alert {
    padding: 8px;
  }

  .alert-text {
    font-size: 0.9rem;
  }

  .alert-icon {
    font-size: 1rem;
  }

  .card-up {
    margin-bottom: 1rem;
  }
}



/* 
.list-group-item {
  border-radius: 5px;
}

.list-group-item>div {
  display: flex;
  justify-content: space-between;
  align-items: stretch;
}

.delete-btn {
  padding: 2px 8px;
  font-size: 0.8rem;
  height: 100%;
  display: flex;
  align-items: center;
  margin-bottom: 0 !important;
}

.mt-2 {
  margin-top: 0.5rem;
  font-size: 0.9rem;
}

.label {
  font-size: 1.1rem;
}

.btn {
  padding: 7px 19px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 600;
  text-transform: uppercase;
  transition: all 0.15s ease;
} */
</style>
