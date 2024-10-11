<template>
  <div class="container-fluid">
    <div class="d-grid gap-5">
      <h3 class="mb-0">{{ $t('exchangeRate--header-title') }}</h3>
      <SecondPasswordModal
        v-if="showModal"
        @close="closeModal"
        @password-verified="handlePasswordVerified"
      />

      <!-- USD to KRW Section -->
      <div class="card">
        <div class="card-body">
          <div class="row px-3">
            <div class="col-md-7 max-margin-bottom">
              <h4 class="mt-3">
                1 {{ customerunit }} = {{ currentToKrw.toFixed(2) }} KRW
              </h4>
              <ExchangeRateChart
                chartId="toexchangeChart"
                :period="toSelectedPeriod"
                chartType="to"
              />
              <div class="chart-button-container">
                <template
                  v-for="period in ['1y', '6m', '3m', '1m']"
                  :key="period"
                >
                  <button
                    class="chart-btn"
                    :class="{ selected: toSelectedPeriod === period }"
                    @click="setToPeriod(period)"
                  >
                    {{ period }}
                  </button>
                </template>
              </div>
            </div>

            <div class="col-md-5 d-flex flex-column justify-content-around">
              <div class="d-grid gap-3 max-margin-bottom">
                <h5>{{ $t('exchangeRate--header-CurrencyExchange') }}</h5>
                <div
                  class="d-flex justify-content-between flex-column-center gap-3"
                >
                  <input
                    type="number"
                    class="form-control"
                    v-model.number="usdAmount"
                    @input="convertToKrw"
                    aria-label="Amount in USD"
                  />
                  <span class="mt-2">{{ customerunit }}</span>
                  <span class="mt-2">=</span>
                  <input
                    type="text"
                    class="form-control"
                    :value="krwAmount"
                    readonly
                    aria-label="Amount in KRW"
                  />
                  <span class="mt-2">KRW</span>
                </div>
                <button
                  class="btn btn-info w-100"
                  @click="openModal('exchange')"
                >
                  {{ $t('exchangeRate--button-buy') }}
                </button>
              </div>

              <!-- 환율 알림 Section -->
              <div class="d-grid gap-3">
                <div>
                  <h5>{{ $t('exchangeRate--notification-setAlert') }}</h5>
                  <p>{{ $t('exchangeRate--placeholder-targetRate') }}</p>
                </div>
                <div
                  class="d-flex justify-content-between align-items-center gap-3"
                >
                  <span class="text-nowrap">1 {{ customerunit }} = </span>
                  <input
                    type="number"
                    v-model="alertRateUsdToKrw"
                    class="form-control"
                    @input="validateAlertRate"
                  />
                  <span>KRW</span>
                </div>
                <button
                  class="btn btn-primary w-100"
                  @click="saveAlertRate(countryCode, 0, alertRateUsdToKrw)"
                >
                  {{ $t('exchangeRate--button-confirmAlert') }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- KRW to USD Section -->
      <div class="card">
        <div class="card-body">
          <div class="row px-3">
            <div class="col-md-7 max-margin-bottom">
              <h4 class="mt-3">
                1000 KRW = {{ (currentFromKrw * 1000).toFixed(2) }}
                {{ customerunit }}
              </h4>
              <ExchangeRateChart
                chartId="fromexchangeChart"
                :period="fromSelectedPeriod"
                chartType="from"
              />
              <div class="chart-button-container">
                <template
                  v-for="period in ['1y', '6m', '3m', '1m']"
                  :key="period"
                >
                  <button
                    class="chart-btn"
                    :class="{ selected: fromSelectedPeriod === period }"
                    @click="setFromPeriod(period)"
                  >
                    {{ period }}
                  </button>
                </template>
              </div>
            </div>

            <div class="col-md-5 d-flex flex-column justify-content-around">
              <div class="d-grid gap-3 max-margin-bottom">
                <h5>{{ $t('exchangeRate--header-CurrencyExchange') }}</h5>
                <div
                  class="d-flex justify-content-between flex-column-min gap-3"
                >
                  <input
                    type="number"
                    class="form-control"
                    v-model.number="krwAmountReverse"
                    @input="convertToUsd"
                    aria-label="Amount in KRW"
                  /><span class="mt-2">KRW</span>
                  <span class="mt-2">=</span>
                  <input
                    type="number"
                    class="form-control"
                    :value="usdAmountReverse"
                    readonly
                    aria-label="Amount in USD"
                  /><span class="mt-2">{{ customerunit }}</span>
                </div>
                <button
                  class="btn btn-danger w-100"
                  @click="openModal('reExchange')"
                >
                  Sell
                </button>
              </div>

              <!-- 환율 알림 Section -->
              <div class="d-grid gap-3">
                <div>
                  <h5>{{ $t('exchangeRate--notification-setAlert') }}</h5>
                  <p>{{ $t('exchangeRate--placeholder-targetRate') }}</p>
                </div>
                <div
                  class="d-flex justify-content-between align-items-center gap-3"
                >
                  <span class="text-nowrap">1000 KRW = </span>
                  <input
                    type="number"
                    v-model="alertRateKrwToUsd"
                    class="form-control"
                    @input="validateAlertRateReverse"
                  />
                  <span>{{ customerunit }}</span>
                </div>
                <button
                  @click="saveAlertRate(0, countryCode, alertRateKrwToUsd)"
                  class="btn btn-primary w-100"
                >
                  {{ $t('exchangeRate--button-confirmAlert') }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="custom-spacer"></div>

      <!-- 자동 환전 설정 섹션 -->
      <h3 class="mb-0">{{ $t('exchangeRate--header-autoExchange') }}</h3>
      <div class="card">
        <div class="card-body">
          <div class="d-flex justify-content-center">
            <div class="d-flex flex-column tab-content-width gap-3">
              <div class="input-group">
                <span class="input-group-text">1 {{ customerunit }} =</span>
                <input
                  type="number"
                  v-model="targetExchange"
                  class="form-control"
                  :placeholder="$t('exchangeRate--placeholder-targetRate')"
                  @input="validateTargetExchange"
                />
                <span class="input-group-text">KRW</span>
              </div>

              <div class="input-group">
                <span class="input-group-text"
                  >{{ $t('exchangeRate--text-targetKRW') }} =</span
                >
                <input
                  type="number"
                  v-model="targetKrw"
                  class="form-control"
                  :placeholder="$t('exchangeRate--placeholder-targetAmount')"
                  @input="validateTargetKrw"
                />
                <span class="input-group-text">KRW</span>
              </div>

              <button
                class="btn btn-primary w-100 mb-0"
                @click="
                  confirmAutoExchange(countryCode, 0, targetExchange, targetKrw)
                "
              >
                {{ $t('exchangeRate--button-confirmAlert') }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="custom-spacer"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, watchEffect } from 'vue';
import { useExchangeStore } from '@/stores/exchangeStore';
import ExchangeRateChart from '@/views/Chart/ExchangeRateChart.vue';
import axios from 'axios';
import myaccountApi from '../../api/myaccountApi';
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';
import { useAuthStore } from '@/stores/auth';

const auth = useAuthStore();
const user = computed(() => auth.user);

const Id = computed(() => auth.userId);
import { CURRENCY_NAME } from '@/constants/countryCode';

const countryCode = computed(() => user.value?.countryCode || 1); // 기본값으로 1 설정
const customerunit = ref('USD'); // 기본값 설정

watchEffect(() => {
  if (user.value?.countryCode) {
    customerunit.value = CURRENCY_NAME[user.value.countryCode];
  }
});

import SecondPasswordModal from '@/views/MyAccounts/SecondPasswordModal.vue';

// Data variables
const usdAmount = ref(1);
const krwAmount = computed(() => {
  return (usdAmount.value * store.currentToKrw).toFixed(2);
});
const krwAmountReverse = ref(1000);
const usdAmountReverse = computed(() => {
  return (krwAmountReverse.value * store.currentFromKrw).toFixed(2);
});

const store = useExchangeStore();

const showModal = ref(false);
let currentAction = ref('');

// 비밀번호 입력 모달 열기
const openModal = (action) => {
  showModal.value = true;
  currentAction.value = action;
};

// 비밀번호 입력 모달 닫기
const closeModal = () => {
  showModal.value = false;
};

// 비밀번호가 확인되었을 때 호출되는 함수
const handlePasswordVerified = async () => {
  showModal.value = false; // 모달 숨김
  switch (currentAction.value) {
    case 'exchange':
      await handleExchange();
      break;
    case 'reExchange':
      await reExchange();
      break;
  }
};

// 가져온 값을 Pinia에서 사용
const currentToKrw = computed(() => store.currentToKrw);
const currentFromKrw = computed(() => store.currentFromKrw);

// Conversion functions
const convertToKrw = () => {
  krwAmount.value = (usdAmount.value * currentToKrw.value).toFixed(2);
};

const convertToUsd = () => {
  usdAmountReverse.value = (
    krwAmountReverse.value * currentFromKrw.value
  ).toFixed(2);
};

// Period state
const toSelectedPeriod = ref('1y');
const fromSelectedPeriod = ref('1y');

// Set period functions
const setToPeriod = (period) => {
  toSelectedPeriod.value = period;
};

const setFromPeriod = (period) => {
  fromSelectedPeriod.value = period;
};

// 환전 함수
const handleExchange = async () => {
  try {
    const userId = Id; // 실제 사용자 번호로 대체해야 합니다
    const krwNo = '1234'; // 실제 KRW 계좌 번호로 대체해야 합니다
    const songNo = '1234'; // 실제 송이 페이 계좌 번호로 대체해야 합니다
    const exchangeRate = currentToKrw.value;
    const amount = usdAmount.value;

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
        userId,
        songNo,
        krwNo,
        typeCode: 5, // 환전 코드
        stateCode: 1, // 상태 코드 (성공)
        historyContent: 'USD → KRW 환전',
        amount,
        exchangeRate,
      },
    });

    if (response && response.data) {
      console.log('환전 성공:', response.data);
      Swal.fire({
        title: '성공!',
        text: '환전이 성공적으로 완료되었습니다.',
        icon: 'success',
        confirmButtonText: '확인',
      });
    }
  } catch (error) {
    console.error('환전 중 오류 발생:', error);
    Swal.fire({
      title: '실패',
      text: '환전 중 오류가 발생했습니다. 다시 시도해 주세요.',
      icon: 'error',
      confirmButtonText: '확인',
    });
  }
};

// 환급 함수
const reExchange = async () => {
  try {
    const userId = Id; // 실제 사용자 번호로 대체해야 합니다
    const krwNo = '1234'; // 실제 KRW 계좌 번호로 대체해야 합니다
    const songNo = '1234'; // 실제 송이 페이 계좌 번호로 대체해야 합니다
    const exchangeRate = currentFromKrw.value * 1000; // 1000 KRW 기준
    const amount = krwAmountReverse.value;

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
        userId,
        songNo,
        krwNo,
        typeCode: 6, // 환급 코드
        stateCode: 1, // 상태 코드 (성공)
        historyContent: 'KRW → USD 환급',
        amount,
        exchangeRate,
      },
    });

    if (response && response.data) {
      console.log('환급 성공:', response.data);
      Swal.fire({
        title: '성공!',
        text: '환급이 성공적으로 완료되었습니다.',
        icon: 'success',
        confirmButtonText: '확인',
      });
    }
  } catch (error) {
    console.error('환급 중 오류 발생:', error);
    Swal.fire({
      title: '실패',
      text: '환급 중 오류가 발생했습니다. 다시 시도해 주세요.',
      icon: 'error',
      confirmButtonText: '확인',
    });
  }
};

// 알림 저장 함수
const alertRateUsdToKrw = ref(null); // USD -> KRW 알림 목표 환율
const alertRateKrwToUsd = ref(null); // KRW -> USD 알림 목표 환율

const saveAlertRate = async (baseCode, targetCode, targetExchange) => {
  // 검증 로직
  if (!targetExchange || isNaN(targetExchange)) {
    Swal.fire({
      title: '경고',
      text: '유효한 목표 환율을 입력해주세요.',
      icon: 'warning',
      confirmButtonText: '확인',
    });
    return;
  }

  if (baseCode === 1 && parseFloat(targetExchange) > currentToKrw.value) {
    Swal.fire({
      title: '경고',
      text: '현재 환율보다 높은 값입니다.',
      icon: 'warning',
      confirmButtonText: '확인',
    });
    return;
  }

  if (
    baseCode === 0 &&
    parseFloat(targetExchange) < currentFromKrw.value * 1000
  ) {
    Swal.fire({
      title: '경고',
      text: '현재 환율보다 낮은 값입니다.',
      icon: 'warning',
      confirmButtonText: '확인',
    });
    return;
  }

  try {
    // const token = localStorage.getItem("jwt_token"); // JWT 토큰 가져오기
    const userId = Id; // userNo를 추가(임시)
    console.log(userId, baseCode, targetCode, targetExchange);
    // 서버에 POST 요청 보내기
    const response = await axios.post(
      '/api/exchange-reservation',
      {
        userId: userId,
        baseCode: baseCode,
        targetCode: targetCode,
        targetExchange: targetExchange,
      }
      // {
      //   headers: {
      //     Authorization: `Bearer ${token}`, // 인증 헤더에 토큰 추가
      //   },
      // }
    );

    if (response.status === 200) {
      Swal.fire({
        title: '성공!',
        text: '환율 알림이 성공적으로 저장되었습니다.',
        icon: 'success',
        confirmButtonText: '확인',
      });
      router.push('/my-accounts');
    }
  } catch (error) {
    console.error('환율 알림 저장 중 오류 발생:', error);
    Swal.fire({
      title: '실패',
      html: `환율 알림 저장에 실패했습니다.<br>이유 : ${error.response?.data || '알 수 없는 오류'}`,
      icon: 'error',
      confirmButtonText: '확인',
    });
  }
};

// watch 구문 수정
watch([currentToKrw, usdAmount], () => {
  convertToKrw();
});

watch([currentFromKrw, krwAmountReverse], () => {
  convertToUsd();
});

const router = useRouter();

// 상태 변수들을 ref로 선언
const targetExchange = ref(null); // 자동 환전 목표 환율
const targetKrw = ref(null); // 목표 금액

// 자동 환전 저장 함수
const confirmAutoExchange = async (
  baseCode,
  targetCode,
  targetExchange,
  targetKrw
) => {
  // 검증 로직
  if (
    !targetExchange ||
    isNaN(targetExchange) ||
    !targetKrw ||
    isNaN(targetKrw)
  ) {
    Swal.fire({
      title: '경고',
      text: '유효한 목표 환율과 목표 금액을 입력해주세요.',
      icon: 'warning',
      confirmButtonText: '확인',
    });
    return;
  }

  if (parseFloat(targetExchange) > currentToKrw.value) {
    Swal.fire({
      title: '경고',
      text: '현재 환율보다 높은 값입니다.',
      icon: 'warning',
      confirmButtonText: '확인',
    });
    return;
  }

  if (parseFloat(targetKrw) < 1000) {
    Swal.fire({
      title: '경고',
      text: '목표 금액은 1000 KRW 이상이어야 합니다.',
      icon: 'warning',
      confirmButtonText: '확인',
    });
    return;
  }

  try {
    // const token = localStorage.getItem("jwt_token"); // JWT 토큰 가져오기
    const userId = Id; // userNo를 추가(임시)
    console.log(userId, baseCode, targetCode, targetExchange, targetKrw);
    // 서버에 POST 요청 보내기
    const response = await axios.post(
      '/api/exchange-reservation/setalert',
      {
        userId: userId,
        baseCode: baseCode,
        targetCode: targetCode,
        targetExchange: targetExchange,
        targetKrw: targetKrw,
      }
      // {
      //   headers: {
      //     Authorization: `Bearer ${token}`, // 인증 헤더에 토큰 추가
      //   },
      // }
    );

    if (response.status === 200) {
      Swal.fire({
        title: '성공!',
        text: '자동 환전 예약이 성공적으로 저장되었습니다.',
        icon: 'success',
        confirmButtonText: '확인',
      });
      router.push('/my-accounts');
    }
  } catch (error) {
    console.error('자동 환전 예약 중 오류 발생:', error);
    Swal.fire({
      title: '실패',
      html: `자동 환전 예약에 실패했습니다.<br>이유 : ${error.response?.data || '알 수 없는 오류'}`,
      icon: 'error',
      confirmButtonText: '확인',
    });
  }
};
</script>

<style scoped>
input[type='number']::-webkit-outer-spin-button,
input[type='number']::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type='number'] {
  -moz-appearance: textfield;
}
</style>
