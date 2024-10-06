<script setup>
import ArgonInput from '@/components/templates/ArgonInput.vue';
import AccountsCard from '@/views/Cards/AccountsCard.vue';
import ArgonAmountInput from '@/components/yb_templates/ArgonAmountInput.vue';
import { ref, onMounted, computed } from 'vue';
import myaccountApi from '../../api/myaccountApi';
import { useExchangeStore } from '@/stores/exchangeStore';
import SecondPasswordModal from '@/views/MyAccounts/SecondPasswordModal.vue';
import axios from 'axios';
import ExchangeRateChart from '@/views/Chart/ExchangeRateChart.vue';
import Swal from 'sweetalert2';
import { TRANSACTION_TYPES, TRANSACTION_TYPES_KEY } from "@/constants/transactionType";
import { COUNTRY_KEY, CURRENCY_NAME } from "@/constants/countryCode";

//i18n
import { useI18n } from 'vue-i18n';
const { t } = useI18n();

//user
import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const user = computed(() => auth.user);

//숫자 포맷팅
import currencyFormatter from '../../js/currencyFormatter';
const { formatNumber } = currencyFormatter;

const emit = defineEmits(['password-verified', 'close']);
const store = useExchangeStore();

const SONGE = 'song-e';
const WONE = 'won-e';

const songCoutryCode = user.value.countryCode;
const customerunit = ref(CURRENCY_NAME[songCoutryCode]); //나라 설정에 따라 변경되게끔
const customerCountry = ref(COUNTRY_KEY[songCoutryCode]);

const selectedAsset = ref(SONGE); // 기본적으로 Song-E Money가 선택됨

const songEMoneyBalance = ref(0); // Song-E Money의 잔액
const wonEMoneyBalance = ref(0); // Won-E Money의 잔액

const activeTab = ref(TRANSACTION_TYPES.DEPOSIT); // 기본적으로 충전 탭이 선택됨

const depositAmount = ref('');
const exchangeAmount = ref('');
const refundAmount = ref('');
const transferAmount = ref('');
const reExchangeAmount = ref('');



// const kwd = "?";
// console.log(t('myAccount--swal-content', { kwd }));

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
  let kwd;
  switch (currentAction.value) {
    case TRANSACTION_TYPES.DEPOSIT:
      await deposit(); // deposit이 완료될 때까지 기다림
      kwd = TRANSACTION_TYPES_KEY[TRANSACTION_TYPES.DEPOSIT];

      break;
    case TRANSACTION_TYPES.EXCHANGE:
      await exchange(); // exchange가 완료될 때까지 기다림
      kwd = TRANSACTION_TYPES_KEY[TRANSACTION_TYPES.EXCHANGE];
      break;
    case TRANSACTION_TYPES.REFUND:
      await refund(); // refund가 완료될 때까지 기다림
      kwd = TRANSACTION_TYPES_KEY[TRANSACTION_TYPES.REFUND];
      break;
    case TRANSACTION_TYPES.TRANSFER:
      await transfer(); // transfer가 완료될 때까지 기다림
      kwd = TRANSACTION_TYPES_KEY[TRANSACTION_TYPES.TRANSFER];
      break;
    case TRANSACTION_TYPES.RE_EXCHANGE:
      await reExchange(); // reExchange가 완료될 때까지 기다림
      kwd = TRANSACTION_TYPES_KEY[TRANSACTION_TYPES.RE_EXCHANGE];
      break;
  }
  kwd = t(kwd);
  Swal.fire({
    title: t('swal--title-success'),
    text: t('myAccount--swal-content', { kwd }),
    icon: 'success',
  });
  resetValue();
  await fetchBalances(); // 잔액을 다시 가져옴

  // AccountsCard의 fetchBalance 함수 호출
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
  // return formatCurrency(balance,INTL_LOCALE[user.value.countryCode],CURRENCY_NAME[user.value.countryCode]);
});
//원화페이계좌
let processAfterWonBalance = computed(() => {
  let wonBalance = 0;

  // 충전일 경우 잔액 증가
  if (activeTab.value === TRANSACTION_TYPES.TRANSFER) {
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
  myaccountApi.fetchkrwAccountBalance(user.value.krwNo).then((balance) => {
    wonEMoneyBalance.value = balance;
  });

  myaccountApi.fetchsongeAccountBalance(user.value.songNo).then((balance) => {
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

const flagIcon = computed(() => {
  return `/images/flag_${songCoutryCode}.png`;
});

// 컴포넌트가 마운트될 때 데이터를 가져옴
onMounted(() => {
  fetchExchangeRates();
  fetchAutoExchange();
  fetchAlertConditions();
  fetchBalances();
});
</script>

<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
    integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA=="
    crossorigin="anonymous" referrerpolicy="no-referrer" />
  <div class="container-fluid">
    <SecondPasswordModal v-if="showModal" @close="closeModal" @password-verified="handlePasswordVerified" />

    <div id="my-account" class="d-grid gap-5">
      <h3 class="mb-0">My account</h3>
      <!-- <div class="custom-spacer"></div> -->
      <div class="row">
        <!-- USD Wallet -->
        <div class="col-lg-4 col-md-5 max-margin-bottom">
          <!-- Song-E Money 카드 -->
          <AccountsCard ref="songEMoneyCardRef" :assetType=SONGE @click="selectAsset(SONGE)"
            :class="{ selected: selectedAsset === SONGE }" />
        </div>

        <!-- KRW Wallet -->
        <div class="col-lg-4 col-md-5">
          <!-- Won-E Money 카드 -->
          <AccountsCard ref="wonEMoneyCardRef" :assetType=WONE @click="selectAsset(WONE)"
            :class="{ selected: selectedAsset === WONE }" />
        </div>
      </div>
      <div class="card">
        <!-- Song-E Money 선택 시 -->
        <template v-if="selectedAsset === SONGE">
          <nav class="nav custom-nav nav-underline nav-justified">
            <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === TRANSACTION_TYPES.DEPOSIT }"
              @click="activeTab = TRANSACTION_TYPES.DEPOSIT" aria-current="page"> 충전 </a>
            <a class="flex-sm-fill text-sm-center nav-link"
              :class="{ active: activeTab === TRANSACTION_TYPES.EXCHANGE }"
              @click="activeTab = TRANSACTION_TYPES.EXCHANGE"> 환전 </a>
            <a class="flex-sm-fill text-sm-center nav-link" :class="{ active: activeTab === TRANSACTION_TYPES.REFUND }"
              @click="activeTab = TRANSACTION_TYPES.REFUND"> 환불 </a>
          </nav>
        </template>

        <!-- Won-E Money 선택 시 -->
        <template v-if="selectedAsset === WONE">
          <nav class="nav custom-nav nav-underline nav-justified">
            <a class="flex-sm-fill text-sm-center nav-link"
              :class="{ active: activeTab === TRANSACTION_TYPES.TRANSFER }"
              @click="activeTab = TRANSACTION_TYPES.TRANSFER" aria-current="page"> 송금 </a>
            <a class="flex-sm-fill text-sm-center nav-link"
              :class="{ active: activeTab === TRANSACTION_TYPES.RE_EXCHANGE }"
              @click="activeTab = TRANSACTION_TYPES.RE_EXCHANGE"> 환급 </a>
          </nav>
        </template>

        <!-- Song-E Money의 탭 내용 -->
        <div class="card-body" v-if="selectedAsset === SONGE">
          <div class="d-flex justify-content-center">
            <div class="d-flex flex-column tab-content-width">

              <div v-if="activeTab === TRANSACTION_TYPES.DEPOSIT" class="tab-pane fade show active d-grid gap-4">
                <div>
                  <labe class="d-flex align-items-center">
                    <div class="icon-container me-2">
                      <img :src=flagIcon alt="icon" class="flag-icon-img">
                    </div>
                    <div class="input-label-text">{{ $t(customerCountry) }} 계좌</div>
                  </labe>
                  <ArgonAmountInput v-model="depositAmount" placeholder="충전할 금액을 입력하세요" :unit="customerunit" />
                  <small class="text-muted">
                    충전계좌:
                    {{ selectedAsset === SONGE ? '내 계좌' : 'KRW 계좌' }}
                  </small>
                </div>
                <div>
                  <labe class="d-flex align-items-center">
                    <div class="icon-container me-2">
                      <img :src=flagIcon alt="icon" class="flag-icon-img">
                    </div>
                    <div class="input-label-text">{{ $t('SONGE-E MONEY') }}</div>
                  </labe>
                  <div class="balance-text">{{ processAfterBalance }}{{ customerunit }}</div>
                </div>
                <button type="submit" class="btn btn-primary w-100 fs-4" @click="openModal"
                  :disabled="!isValidAmount(depositAmount)" variant="gradient">SONG-E 채우기</button>
              </div>

              <div v-if="activeTab === TRANSACTION_TYPES.EXCHANGE">
                <div>
                </div>

                <p>
                  <small>현재 환율: </small>
                  1 KRW = {{ currentFromKrw }} {{ customerunit }}
                </p>
                <p>
                  <small>보내는 금액</small>
                  <ArgonAmountInput v-model="exchangeAmount" placeholder="얼마를 환전할까요?" :unit="customerunit"
                    :selectedAsset="selectedAsset" :songEMoneyBalance="songEMoneyBalance" :activeTab="activeTab" />
                </p>
                <p>
                  <small>받는 금액: </small>
                  {{ receivedAmount }} KRW
                </p>
                <p>
                  <small>환급계좌:</small>
                  {{ selectedAsset === SONGE ? '내 계좌' : 'KRW 계좌' }}
                </p>
                <p>
                  <small>거래 후 잔액:</small>
                  {{ processAfterBalance }} {{ customerunit }}
                </p>
                <button type="submit" class="btn btn-primary w-100" @click="openModal"
                  :disabled="!isValidAmount(exchangeAmount)" variant="gradient">환전하기</button>
              </div>

              <div v-if="activeTab === TRANSACTION_TYPES.REFUND">
                <p>
                  <small>환불할 금액을 입력하세요</small>
                  <ArgonAmountInput v-model="refundAmount" placeholder="얼마를 환불할까요?" :unit="customerunit"
                    :selectedAsset="selectedAsset" :songEMoneyBalance="songEMoneyBalance" :activeTab="activeTab" />
                </p>
                <p>
                  <small> 환불계좌: </small>
                  {{ selectedAsset === SONGE ? '내 계좌' : 'KRW 계좌' }}
                </p>
                <p>
                  <small> 거래 후 잔액:</small>
                  {{ processAfterBalance }} {{ customerunit }}
                </p>
                <button type="submit" class="btn btn-primary w-100" @click="openModal"
                  :disabled="!isValidAmount(refundAmount)" variant="gradient">환불하기</button>
              </div>
            </div>
          </div>
        </div>

        <!-- Won-E Money의 탭 내용 -->
        <div class="card-body" v-if="selectedAsset === WONE">
          <div class="d-flex justify-content-center">
            <div class="d-flex flex-column tab-content-width">
              <div v-if="activeTab === TRANSACTION_TYPES.TRANSFER">

                <p>
                <div class="d-flex align-items-center mb-1">
                  <small class="me-3">받는 이메일</small>
                  <button class="btn btn-sm btn-secondary mb-0" @click="emailConfirm" size="sm" variant="outline"
                    :disabled="sendEmail === ''">이메일
                    확인</button>
                  <!-- 회원/비회원 표시 -->
                  <small v-if="isMember === 'member'">회원 이메일</small>
                  <small v-else-if="isMember === 'no-member'">비회원 이메일</small>
                </div>

                <ArgonInput v-model="sendEmail" placeholder="받는 분의 이메일을 입력하세요" @input="onInput" variant="gradient"
                  :class="{ 'is-invalid': errorMessage }" :error="errorMessage !== ''" :success="success"
                  style="margin-bottom: 0" />
                <div v-if="errorMessage !== ''" class="invalid-feedback text-xs mb-1">
                  {{ errorMessage }}
                </div>
                </p>

                <p class="mb-5">
                  <small>이메일 확인</small>
                  <ArgonInput v-model="sendEmailConfirm" placeholder="이메일을 다시 입력하세요"
                    :class="{ 'is-invalid': errorMessageCheck }" :error="errorMessageCheck !== ''"
                    :success="checkSucess" style="margin-bottom: 0" @input="onInputCheck" />
                <div v-if="errorMessageCheck" class="invalid-feedback text-xs mb-1">
                  {{ errorMessageCheck }}
                </div>
                </p>

                <p>
                  <small>송금할 금액을 입력하세요</small>
                  <ArgonAmountInput v-model="transferAmount" placeholder="얼마를 송금할까요?" unit="KRW"
                    :selectedAsset="selectedAsset" :wonEMoneyBalance="wonEMoneyBalance" :activeTab="activeTab" />
                </p>

                <p>
                  <small>송금 후 잔액:</small>
                  {{ processAfterWonBalance }} KRW
                </p>

                <button type="submit" class="btn btn-primary w-100" @click="openModal"
                  :disabled="!isValidAmount(transferAmount) || errorMessage !== '' || errorMessageCheck !== ''"
                  variant="gradient">송금하기</button>

              </div>

              <div v-if="activeTab === TRANSACTION_TYPES.RE_EXCHANGE">
                <p>
                  <small>현재 환율: </small>
                  1{{ customerunit }} = {{ currentToKrw }} KRW
                </p>
                <p>
                  <small>보내는 금액</small>
                  <ArgonAmountInput v-model="reExchangeAmount" placeholder="얼마를 환급할까요?" unit="KRW"
                    :selectedAsset="selectedAsset" :wonEMoneyBalance="wonEMoneyBalance" :activeTab="activeTab" />
                </p>
                <p>
                  <small>받는 금액</small>
                  {{ receivedAmount }} USD
                </p>

                <p>
                  <small>거래 후 잔액: </small>
                  {{ processAfterWonBalance }} KRW
                </p>

                <button type="submit" class="btn btn-primary w-100" @click="openModal"
                  :disabled="!isValidAmount(reExchangeAmount)" variant="gradient">환급하기</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="custom-spacer"></div>

    <!-- Graph and Conversion Section -->
    <div id="exchange-rate" class="d-grid gap-5">
      <h3 clas="mb-0">Exchange Rate</h3>
      <div class="card">
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
    </div>

    <div class="custom-spacer"></div>

    <div id="set-alert" class="d-grid gap-5">
      <div>
        <span @click="$router.push('/exchange-rate')" class="alert-text me-3 mb-0" role="button" tabindex="0">
          자동 환전 및 알람
          <i class="fa-solid fa-angle-right alert-icon"></i>
        </span>
      </div>
      <div class="card">
        <div class="card-body">
          <div class="d-grid gap-3">
            <div>
              <label for="autoCondition" class="form-label">자동 환전 설정 내역</label>
              <ul v-if="autoConditions.length > 0" class="list-group">
                <li class="list-group-item">
                  <div class="d-flex justify-content-between align-items-center">
                    <div class="d-flex gap-3 flex-column-min">
                      <div>기준 통화: {{ autoConditions[0]?.baseCode }}
                      </div>
                      <div>대상 통화: {{ autoConditions[0]?.targetCode }}
                      </div>
                      <div>목표 환율: {{ autoConditions[0]?.targetExchange }}
                      </div>
                      <div>목표 KRW 금액: {{ autoConditions[0]?.targetKrw }}
                      </div>
                    </div>
                    <button class="btn btn-sm btn-danger mb-0"
                      @click="confirmDelete(autoConditions[0]?.resNo)">삭제</button>
                  </div>
                </li>
              </ul>
              <p v-else style="margin-left: 0.5rem">자동 환전 예약 내역이 없습니다.</p>
            </div>

            <div>
              <label for="alertConditions" class="form-label">환율 알림 설정 내역</label>
              <ul v-if="alertConditions.length > 0" class="list-group">
                <li v-for="condition in alertConditions" :key="condition.resNo" class="list-group-item">
                  <div class="d-flex justify-content-between align-items-center">
                    <div class="d-flex gap-3 flex-column-min">
                      <div>기준 통화: {{ condition.baseCode }} </div>
                      <div>대상 통화: {{ condition.targetCode }}</div>
                      <div>목표 환율: {{ condition.targetExchange }}</div>
                    </div>
                    <button class="btn btn-sm btn-danger mb-0" @click="confirmDelete(condition.resNo)">삭제</button>
                  </div>
                </li>
              </ul>
              <p v-if="alertConditions.length === 0" style="margin-left: 0.5rem">환율 알림 예약 내역이 없습니다.</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="custom-spacer"></div>
  </div>

</template>

<style scoped>
.input-label-text {
  font-size: 1.5rem;
  font-weight: 600;
}

.balance-text {
  font-size: 1.75rem;
  font-weight: 600;
}
</style>