<template>
    <div class="container-fluid">
        <div class="d-grid gap-5">
            <h3 class="mb-0">{{ $t('exchangeRate--header-title') }}</h3>
            <SecondPasswordModal v-if="showModal" @close="closeModal" @password-verified="handlePasswordVerified" />

            <!-- USD to KRW Section -->
            <div class="card">
                <div class="card-body">
                    <div class="row px-3">
                        <div class="col-md-7 max-margin-bottom">
                            <h4 class="mt-3">
                                1 {{ customerunit }} =
                                {{ currentToKrw.toFixed(5) }} KRW
                            </h4>
                            <ExchangeRateChart chartId="toexchangeChart" :period="toSelectedPeriod" chartType="to" />
                            <div class="chart-button-container">
                                <template v-for="period in ['1y', '6m', '3m', '1m']" :key="period">
                                    <button class="chart-btn" :class="{
                                        selected:
                                            toSelectedPeriod === period,
                                    }" @click="setToPeriod(period)">
                                        {{ period }}
                                    </button>
                                </template>
                            </div>
                        </div>

                        <div class="col-md-5 d-flex flex-column justify-content-around">
                            <div class="d-grid gap-3 max-margin-bottom">
                                <h5>
                                    {{
                                        $t(
                                            'exchangeRate--header-CurrencyExchange'
                                        )
                                    }}
                                </h5>
                                <div class="d-flex justify-content-between flex-column-center gap-3">
                                    <input type="number" class="form-control" v-model.number="usdAmount"
                                        @input="convertToKrw" aria-label="Amount in USD" />
                                    <span class="mt-2">{{ customerunit }}</span>
                                    <span class="mt-2">=</span>
                                    <input type="text" class="form-control" :value="krwAmount" readonly
                                        aria-label="Amount in KRW" />
                                    <span class="mt-2">KRW</span>
                                </div>
                                <button class="btn btn-info w-100" @click="openModal('exchange')">
                                    {{ $t('exchangeRate--button-buy') }}
                                </button>
                            </div>

                            <!-- 환율 알림 Section -->
                            <div class="d-grid gap-3">
                                <div>
                                    <h5>
                                        {{
                                            $t(
                                                'exchangeRate--notification-setAlert'
                                            )
                                        }}
                                    </h5>
                                    <p>
                                        {{
                                            $t(
                                                'exchangeRate--placeholder-targetRate'
                                            )
                                        }}
                                    </p>
                                </div>
                                <div class="d-flex justify-content-between align-items-center gap-3">
                                    <span class="text-nowrap">1 {{ customerunit }} =
                                    </span>
                                    <input type="number" v-model="alertRateUsdToKrw" class="form-control"
                                        @input="validateAlertRate" />
                                    <span>KRW</span>
                                </div>
                                <button class="btn btn-primary w-100" @click="
                                    saveAlertRate(
                                        countryCode,
                                        0,
                                        alertRateUsdToKrw
                                    )
                                    ">
                                    {{
                                        $t('exchangeRate--button-confirmAlert')
                                    }}
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
                                1 KRW = {{ currentFromKrw.toFixed(5) }}
                                {{ customerunit }}
                            </h4>
                            <ExchangeRateChart chartId="fromexchangeChart" :period="fromSelectedPeriod"
                                chartType="from" />
                            <div class="chart-button-container">
                                <template v-for="period in ['1y', '6m', '3m', '1m']" :key="period">
                                    <button class="chart-btn" :class="{
                                        selected:
                                            fromSelectedPeriod === period,
                                    }" @click="setFromPeriod(period)">
                                        {{ period }}
                                    </button>
                                </template>
                            </div>
                        </div>

                        <div class="col-md-5 d-flex flex-column justify-content-around">
                            <div class="d-grid gap-3 max-margin-bottom">
                                <h5>
                                    {{
                                        $t(
                                            'exchangeRate--header-CurrencyExchange'
                                        )
                                    }}
                                </h5>
                                <div class="d-flex justify-content-between flex-column-min gap-3">
                                    <input type="number" class="form-control" v-model.number="krwAmountReverse"
                                        @input="convertToUsd" aria-label="Amount in KRW" /><span class="mt-2">KRW</span>
                                    <span class="mt-2">=</span>
                                    <input type="number" class="form-control" :value="usdAmountReverse" readonly
                                        aria-label="Amount in USD" /><span class="mt-2">{{
                                            customerunit
                                        }}</span>
                                </div>
                                <button class="btn btn-danger w-100" @click="openModal('reExchange')">
                                    Sell
                                </button>
                            </div>

                            <!-- 환율 알림 Section -->
                            <div class="d-grid gap-3">
                                <div>
                                    <h5>
                                        {{
                                            $t(
                                                'exchangeRate--notification-setAlert'
                                            )
                                        }}
                                    </h5>
                                    <p>
                                        {{
                                            $t(
                                                'exchangeRate--placeholder-targetRate'
                                            )
                                        }}
                                    </p>
                                </div>
                                <div class="d-flex justify-content-between align-items-center gap-3">
                                    <span class="text-nowrap">1 KRW = </span>
                                    <input type="number" v-model="alertRateKrwToUsd" class="form-control"
                                        @input="validateAlertRateReverse" />
                                    <span>{{ customerunit }}</span>
                                </div>
                                <button @click="
                                    saveAlertRate(
                                        0,
                                        countryCode,
                                        alertRateKrwToUsd
                                    )
                                    " class="btn btn-primary w-100">
                                    {{
                                        $t('exchangeRate--button-confirmAlert')
                                    }}
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
                <div class="card-body py-6">
                    <div class="d-flex justify-content-center">
                        <div class="d-flex flex-column w-70 gap-4 align-items-center">
                            <div class="input-group">
                                <span class="input-group-text">1 {{ customerunit }} =</span>
                                <input type="number" v-model="targetExchange" class="form-control" :placeholder="$t(
                                    'exchangeRate--placeholder-targetRate'
                                )
                                    " @input="validateTargetExchange" />
                                <span class="input-group-text">KRW</span>
                            </div>

                            <div class="input-group">
                                <span class="input-group-text">{{
                                    $t('exchangeRate--text-targetKRW')
                                    }}
                                    =</span>
                                <input type="number" v-model="targetKrw" class="form-control" :placeholder="$t(
                                    'exchangeRate--placeholder-targetAmount'
                                )
                                    " @input="validateTargetKrw" />
                                <span class="input-group-text">KRW</span>
                            </div>
                            <button class="btn btn-primary w-50 mb-0" @click="
                                confirmAutoExchange(
                                    countryCode,
                                    0,
                                    targetExchange,
                                    targetKrw
                                )
                                ">
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
import { useI18n } from 'vue-i18n';
const { t } = useI18n(); // t 함수 정의
import exchangeReservationApi from '../../api/exchangeReservationApi';
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

import SecondPasswordModal from "@/components/modal/SecondPasswordModal.vue";

// Data variables
const usdAmount = ref(1);
const krwAmount = computed(() => {
    return (usdAmount.value * store.currentToKrw).toFixed(5);
});
const krwAmountReverse = ref(1);
const usdAmountReverse = computed(() => {
    return (krwAmountReverse.value * store.currentFromKrw).toFixed(5);
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
    krwAmount.value = (usdAmount.value * currentToKrw.value).toFixed(5);
};

const convertToUsd = () => {
    usdAmountReverse.value = (
        krwAmountReverse.value * currentFromKrw.value
    ).toFixed(5);
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
        const krwNo = user.value.krwNo; // 실제 KRW 계좌 번호로 대체해야 합니다
        const songNo = user.value.songNo; // 실제 송이 페이 계좌 번호로 대체해야 합니다
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
                userId: Id.value,
                songNo,
                krwNo,
                typeCode: 5, // 환전 코드
                stateCode: 1, // 상태 코드 (성공)
                historyContent: `${customerunit.value} → KRW ${t('transaction_types_Exchange')}`,
                amount,
                exchangeRate,
            },
        });

        if (response && response.data) {
            console.log('환전 성공:', response.data);
            Swal.fire({
                title: t('exchangeRate-alert-success'),
                text: t('exchangeRate-alert-exchange-success'),
                icon: 'success',
                confirmButtonText: t('exchangeRate-alert-confirm'),
            });
        }
    } catch (error) {
        console.error('환전 중 오류 발생:', error);
        Swal.fire({
            title: t('exchangeRate-alert-fail'),
            text: t('exchangeRate-alert-exchange-error'),
            icon: 'error',
            confirmButtonText: t('exchangeRate-alert-confirm'),
        });
    }
};

// 환급 함수
const reExchange = async () => {
    try {
        const userId = Id; // 실제 사용자 번호로 대체해야 합니다
        const krwNo = user.value.krwNo; // 실제 KRW 계좌 번호로 대체해야 합니다
        const songNo = user.value.songNo; // 실제 송이 페이 계좌 번호로 대체해야 합니다
        const exchangeRate = currentFromKrw.value;
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
                userId: Id.value,
                songNo,
                krwNo,
                typeCode: 6, // 환급 코드
                stateCode: 1, // 상태 코드 (성공)
                historyContent: `KRW → ${customerunit.value} ${t('transaction_types_reExchange')}`,
                amount,
                exchangeRate,
            },
        });

        if (response && response.data) {
            console.log('환급 성공:', response.data);
            Swal.fire({
                title: t('exchangeRate-alert-success'),
                text: t('exchangeRate-alert-refund-success'),
                icon: 'success',
                confirmButtonText: t('exchangeRate-alert-confirm'),
            });
        }
    } catch (error) {
        console.error('환급 중 오류 발생:', error);
        Swal.fire({
            title: t('exchangeRate-alert-fail'),
            text: t('exchangeRate-alert-refund-error'),
            icon: 'error',
            confirmButtonText: t('exchangeRate-alert-confirm'),
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
            title: t('exchangeRate-alert-warning'),
            text: t('exchangeRate-alert-valid-rate'),
            icon: 'warning',
            confirmButtonText: t('exchangeRate-alert-confirm'),
        });
        return;
    }

    if (baseCode === 1 && parseFloat(targetExchange) > currentToKrw.value) {
        Swal.fire({
            title: t('exchangeRate-alert-warning'),
            text: t('exchangeRate-alert-higher-than-current'),
            icon: 'warning',
            confirmButtonText: t('exchangeRate-alert-confirm'),
        });
        return;
    }

    if (baseCode === 0 && parseFloat(targetExchange) < currentFromKrw.value) {
        Swal.fire({
            title: t('exchangeRate-alert-warning'),
            text: t('exchangeRate-alert-lower-than-current'),
            icon: 'warning',
            confirmButtonText: t('exchangeRate-alert-confirm'),
        });
        return;
    }

    try {
        // 서버에 POST 요청 보내기
        const req = {
            userId: Id.value,
            baseCode: baseCode,
            targetCode: targetCode,
            targetExchange: targetExchange,
        }
        const response = await exchangeReservationApi.setReservation(req);

        if (response.status === 200) {
            Swal.fire({
                title: t('exchangeRate-alert-success'),
                text: t('exchangeRate-alert-notification-success'),
                icon: 'success',
                confirmButtonText: t('exchangeRate-alert-confirm'),
            });
            router.push('/my-page');
        }
    } catch (error) {
        console.error('환율 알림 저장 중 오류 발생:', error);
        Swal.fire({
            title: t('exchangeRate-alert-fail'),
            text: t('exchangeRate-alert-notification-error'),
            icon: 'error',
            confirmButtonText: t('exchangeRate-alert-confirm'),
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
            title: t('exchangeRate-alert-warning'),
            text: t('exchangeRate-alert-valid-rate-amount'),
            icon: 'warning',
            confirmButtonText: t('exchangeRate-alert-confirm'),
        });
        return;
    }

    if (parseFloat(targetExchange) > currentToKrw.value) {
        Swal.fire({
            title: t('exchangeRate-alert-warning'),
            text: t('exchangeRate-alert-higher-than-current'),
            icon: 'warning',
            confirmButtonText: t('exchangeRate-alert-confirm'),
        });
        return;
    }

    if (parseFloat(targetKrw) < 1000) {
        Swal.fire({
            title: t('exchangeRate-alert-warning'),
            text: t('exchangeRate-alert-minimum-amount'),
            icon: 'warning',
            confirmButtonText: t('exchangeRate-alert-confirm'),
        });
        return;
    }

    try {
        // 서버에 POST 요청 보내기
        const req = {
            userId: Id.value,
            baseCode: baseCode,
            targetCode: targetCode,
            targetExchange: targetExchange,
            targetKrw: targetKrw,
        }
        const response = await exchangeReservationApi.setConditions(req);

        if (response.status === 200) {
            Swal.fire({
                title: t('exchangeRate-alert-success'),
                text: t('exchangeRate-alert-auto-exchange-success'),
                icon: 'success',
                confirmButtonText: t('exchangeRate-alert-confirm'),
            });
            router.push('/my-page');
        }
    } catch (error) {
        console.error('자동 환전 예약 중 오류 발생:', error);
        Swal.fire({
            title: t('exchangeRate-alert-fail'),
            text: t('exchangeRate-alert-auto-exchange-error'),
            icon: 'error',
            confirmButtonText: t('exchangeRate-alert-confirm'),
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
