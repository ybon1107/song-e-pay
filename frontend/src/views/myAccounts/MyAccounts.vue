<script setup>
import ArgonInput from '@/components/templates/ArgonInput.vue';
import AccountsCard from '@/views/Cards/AccountsCard.vue';
import ArgonAmountInput from '@/components/yb_templates/ArgonAmountInput.vue';
import ExchangeAmountInput from '@/components/yb_templates/ExchangeAmountInput.vue';
import { ref, onMounted, computed, watchEffect } from 'vue';
import myaccountApi from '../../api/myaccountApi';
import { useExchangeStore } from '@/stores/exchangeStore';
import SecondPasswordModal from '@/views/MyAccounts/SecondPasswordModal.vue';
import axios from 'axios';
import ExchangeRateChart from '@/views/Chart/ExchangeRateChart.vue';
import Swal from 'sweetalert2';
import {
    TRANSACTION_TYPES,
    TRANSACTION_TYPES_KEY,
} from '@/constants/transactionType';
import {
    COUNTRY_CODE,
    COUNTRY_KEY,
    CURRENCY_NAME,
} from '@/constants/countryCode';
import api from '@/api';

//i18n
import { useI18n } from 'vue-i18n';
const { t } = useI18n();

//user
import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const user = computed(() => auth.user);
const userId = computed(() => auth.user.userId);

//숫자 포맷팅
import currencyFormatter from '../../js/currencyFormatter';
const { formatNumber } = currencyFormatter;

const emit = defineEmits(['password-verified', 'close']);
const store = useExchangeStore();

const SONGE = 'song-e';
const WONE = 'won-e';

const i18n_TRANSFER = TRANSACTION_TYPES_KEY[TRANSACTION_TYPES.TRANSFER];
const i18n_DEPOSIT = TRANSACTION_TYPES_KEY[TRANSACTION_TYPES.DEPOSIT];
const i18n_REFUND = TRANSACTION_TYPES_KEY[TRANSACTION_TYPES.REFUND];
const i18n_EXCHANGE = TRANSACTION_TYPES_KEY[TRANSACTION_TYPES.EXCHANGE];
const i18n_RE_EXCHANGE = TRANSACTION_TYPES_KEY[TRANSACTION_TYPES.RE_EXCHANGE];

const songCountryCode = ref(null);
const customerunit = ref(null);
const customerCountry = ref(null);

const wonCoutryCode = COUNTRY_CODE.KR;
const wonUnit = CURRENCY_NAME[wonCoutryCode];
const wonCountry = COUNTRY_KEY[wonCoutryCode];

const selectedAsset = ref(SONGE); // 기본적으로 Song-E Money가 선택됨

const songEMoneyBalance = ref(0); // Song-E Money의 잔액
const wonEMoneyBalance = ref(0); // Won-E Money의 잔액

const activeTab = ref(TRANSACTION_TYPES.DEPOSIT); // 기본적으로 충전 탭이 선택됨

const depositAmount = ref('');
const refundAmount = ref('');
const transferAmount = ref('');

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
        balance =
            songEMoneyBalance.value + parseFloat(depositAmount.value || 0);
    }
    // 환전일 경우 잔액 감소
    if (activeTab.value === TRANSACTION_TYPES.EXCHANGE) {
        balance =
            songEMoneyBalance.value - parseFloat(exchangeAmount.value || 0);
    }
    // 환불일 경우 잔액 감소
    else if (activeTab.value === TRANSACTION_TYPES.REFUND) {
        balance = songEMoneyBalance.value - parseFloat(refundAmount.value || 0);
    }

    // 계산된 숫자를 포맷하여 반환
    return formatNumber(balance.toFixed(5)); // 소수점 두 자릿수까지 표시
    // return formatCurrency(balance,INTL_LOCALE[user.value.countryCode],CURRENCY_NAME[user.value.countryCode]);
});

//원화페이계좌
let processAfterWonBalance = computed(() => {
    let wonBalance = 0;

    // 충전일 경우 잔액 증가
    if (activeTab.value === TRANSACTION_TYPES.TRANSFER) {
        wonBalance =
            wonEMoneyBalance.value - parseFloat(transferAmount.value || 0);
    }

    // 환급일 경우 잔액 감소
    else if (activeTab.value === TRANSACTION_TYPES.RE_EXCHANGE) {
        wonBalance =
            wonEMoneyBalance.value - parseFloat(reExchangeAmount.value || 0);
    }

    // 계산된 숫자를 포맷하여 반환
    return formatNumber(wonBalance.toFixed(5)); // 소수점 두 자릿수까지 표시
});

// 자산 탭 선택
const selectAsset = (asset) => {
    selectedAsset.value = asset;
    // Song-E Money 선택 시 기본 탭을 deposit로 설정, Won-E Money 선택 시 기본 탭을 transfer로 설정
    activeTab.value =
        asset === SONGE
            ? TRANSACTION_TYPES.DEPOSIT
            : TRANSACTION_TYPES.RE_EXCHANGE;
};


const handlePasswordVerified = async () => {
    showModal.value = false; // 모달 숨김
    let kwd;
    let response;

    try {
        switch (currentAction.value) {
            case TRANSACTION_TYPES.DEPOSIT:
                response = await deposit();
                kwd = i18n_DEPOSIT;
                if (!response.data) throw new Error('My account has an insufficient balance.');
                break;
            case TRANSACTION_TYPES.EXCHANGE:
                response = await exchange();
                kwd = i18n_EXCHANGE;
                break;
            case TRANSACTION_TYPES.REFUND:
                response = await refund();
                if (!response.data) throw new Error('My Song-E has an insufficient balance.');
                kwd = i18n_REFUND;
                break;
            case TRANSACTION_TYPES.TRANSFER:
                response = await transfer();
                kwd = i18n_TRANSFER;
                break;
            case TRANSACTION_TYPES.RE_EXCHANGE:
                response = await reExchange();
                kwd = i18n_RE_EXCHANGE;
                break;
        }

        kwd = t(kwd);

        // ResponseEntity의 상태 코드 확인
        if (response.data) {
            Swal.fire({
                title: t('myAccount--swal-title'),
                text: t('myAccount--swal-content', { kwd: kwd }),
                icon: 'success',
            });


            await fetchBalances(); // 잔액을 다시 가져옴

            // AccountsCard의 fetchBalance 함수 호출
            if (songEMoneyCardRef.value) {
                await songEMoneyCardRef.value.fetchBalance();
            }
            if (wonEMoneyCardRef.value) {
                await wonEMoneyCardRef.value.fetchBalance();
            }
        } else {
            // 실패 시 에러를 throw
            throw new Error(response.data?.message || t('swal--default-error-message'));
        }
        resetValue();
    } catch (error) {
        await Swal.fire({
            title: t('swal--title-fail'),
            html: error.message || t('swal--default-error-message'),
            icon: 'error',
        });
        resetValue();
    }
};

// API 호출 함수
const callAccountApi = async (apiFunction, params) => {
    try {
        const response = await apiFunction(params);
        console.log('API 응답 (callAccountApi):', response);
        return response;
    } catch (error) {
        console.error('API 호출 중 오류 발생:', error);
        throw error;
    }
};

const ACCOUNT = computed(() => {
    const { userId, accountNo, songNo, krwNo } = user.value; // 구조 분해 할당 사용
    return {
        accountDTO: { accountNo },
        songAccountDTO: { songNo },
        krwAccountDTO: { krwNo },
        historyDTO: { userId, songNo, krwNo },
    };
});

// 충전 함수 정의
const deposit = async () => {
    const amount = depositAmount.value; // 충전하려는 금액
    const params = {
        ...ACCOUNT.value,
        amount,
    };
    return callAccountApi(myaccountApi.deposit, params);
};

// 환불 처리
const refund = async () => {
    const amount = refundAmount.value; // 충전하려는 금액
    const params = {
        ...ACCOUNT.value,
        amount,
    };
    return callAccountApi(myaccountApi.refund, params);
};

// 환전 처리
const exchange = async () => {
    // const amount = exchangeAmount.value; // 환전하려는 금액
    const amount = receiveAmount.value; //외화기준 금액 넣어주는 걸로 변경
    const exchangeRate = currentFromKrw.value;
    // const exchangeRate = 0.00074;
    const params = {
        ...ACCOUNT.value,
        amount,
        exchangeRate,
    };
    return callAccountApi(myaccountApi.exchange, params);
};

// 환급 처리
const reExchange = async () => {
    // const amount = reExchangeAmount.value; // 환급하려는 금액
    const amount = receiveAmount.value; //외화기준 금액 넣어주는 걸로 변경
    const exchangeRate = currentToKrw.value;
    const params = {
        ...ACCOUNT.value,
        amount,
        exchangeRate,
    };
    return callAccountApi(myaccountApi.reExchange, params);
};

// 송금 처리
// const transfer = async () => {
//   let target_krwNo;
//   if (isMember.value === 'no-member') {
//     target_krwNo = sendEmail.value;
//   } else {
//     target_krwNo = await myaccountApi.getKrwNo(sendEmail.value);
//   }
//   const amount = transferAmount.value; // 송금하려는 금액
//   // const stateCode = isMember.value === 'no-member' ? 4 : 1;

//   const params = {
//     ...ACCOUNT.value,
//     isMember: isMember.value,
//     amount,
//     target_krwNo,
//   }
//   return callAccountApi(myaccountApi.transfer, params);
// }
const transfer = async () => {
    const { userId, songNo, krwNo } = user.value;
    let target_krwNo;
    const amount = transferAmount.value;
    const stateCode = isMember.value === 'no-member' ? 4 : 1;
    const targetHistoryContent = `${userId} → KRW  입금`;
    if (isMember.value === 'no-member') {
        target_krwNo = sendEmail.value;
    } else {
        target_krwNo = await myaccountApi.getKrwNo(sendEmail.value);
    }

    try {
        // 송이 페이머니 충전 요청
        const response = await myaccountApi.transfer({
            targetHistoryContent,
            isMember: isMember.value,
            amount,
            target_krwNo,
            krwAccountDTO: {
                krwNo,
            },
            historyDTO: {
                userId,
                songNo,
                krwNo,
                typeCode: 2, //거래 코드 충전 2
                stateCode: stateCode,
                historyContent: `KRW → ${sendEmail.value} 송금`,
                amount,
            },
        });

        console.log('송금 응답:', response);
        return response; // API 응답 반환
    } catch (error) {
        console.error('송금 중 오류 발생:', error);
        throw error; // 오류를 상위로 전파
    }
};

const resetValue = () => {
    console.log('reset????');
    refundAmount.value = '';
    reExchangeAmount.value = '';
    transferAmount.value = '';
    exchangeAmount.value = '';
    depositAmount.value = '';
    sendEmail.value = '';
    sendEmailConfirm.value = '';
    success = false;
    isMember.value = '';
    isconfirmed = false;
    checkSucess = false;
    receiveInput.value = '';
};

const convertToKrw = () => {
    krwAmount.value = (usdAmount.value * currentToKrw.value).toFixed(5);
};

const convertToUsd = () => {
    usdAmountReverse.value = (
        krwAmountReverse.value * currentFromKrw.value
    ).toFixed(5);
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

// 입력 값 설정
// const exchangeInput = ref('');
// const reExchangeInput = ref('');
// const receiveInput = ref('');
// const selectInput = ref('exchange');
// // 포커스 이벤트 핸들러
// const onfocus = (inputType) => {
//   selectInput.value = inputType;
//   console.log('선택한 입력:', selectInput.value);
// };
// const errorAmountMessage = ref('');
// const onblur = () => {
//   receiveAmount.value = '';
//   exchangeAmount.value = '';
//   reExchangeAmount.value = '';
//   errorAmountMessage.value = '';
//   errorMessage.value = '';
//   errorMessageCheck.value = '';
// };

// 입력 값 설정
const exchangeInput = ref('');
const reExchangeInput = ref('');
const receiveInput = ref('');
const selectInput = ref('exchange');

// 포커스 이벤트 핸들러
const onfocus = (inputType) => {
    selectInput.value = inputType;
    console.log('선택한 입력:', selectInput.value);
};

const errorAmountMessage = ref('');

// 각 입력 필드에 대한 개별 blur 핸들러
const onblurReceive = () => {
    if (receiveAmount.value === '') {
        errorAmountMessage.value = '';
    }
};

const onblurExchange = () => {
    if (exchangeAmount.value === '') {
        errorAmountMessage.value = '';
    }
};

const onblurReExchange = () => {
    if (reExchangeAmount.value === '') {
        errorAmountMessage.value = '';
    }
};

// 에러 메시지 초기화 함수
const clearErrorMessages = () => {
    errorMessage.value = '';
    errorMessageCheck.value = '';
};

const songEMoneyBalance_toKRW = computed(() => {
    return songEMoneyBalance.value * currentToKrw.value;
});
const wonEMoneyBalance_FromKRW = computed(() => {
    return wonEMoneyBalance.value * currentFromKrw.value;
});
// 받는 금액 계산
const receiveAmount = computed({
    get() {
        if (selectInput.value === 'exchange' && exchangeInput.value) {
            const exchangeAmt = parseFloat(exchangeInput.value);
            return !isNaN(exchangeAmt)
                ? (exchangeAmt * currentToKrw.value).toFixed(5)
                : '';
        } else if (
            selectInput.value === 'reExchange' &&
            reExchangeInput.value
        ) {
            const reExchangeAmt = parseFloat(reExchangeInput.value);
            return !isNaN(reExchangeAmt)
                ? (reExchangeAmt * currentFromKrw.value).toFixed(5)
                : '';
        }
        return receiveInput.value; // 현재 값 반환
    },
    set(value) {
        receiveInput.value = value; // 입력 값 설정
    },
});

// 환전 금액 계산
const exchangeAmount = computed({
    get() {
        if (selectInput.value === 'receive' && receiveInput.value) {
            const receiveAmt = parseFloat(receiveInput.value);
            return !isNaN(receiveAmt)
                ? (receiveAmt / currentToKrw.value).toFixed(5)
                : '';
        }
        return exchangeInput.value; // 현재 값 반환
    },
    set(value) {
        exchangeInput.value = value; // 입력 값 설정
    },
});
// 환급 금액 계산
const reExchangeAmount = computed({
    get() {
        if (selectInput.value === 'receive' && receiveInput.value) {
            const receiveAmt = parseFloat(receiveInput.value);
            return !isNaN(receiveAmt)
                ? (receiveAmt / currentFromKrw.value).toFixed(5)
                : '';
        }
        return reExchangeInput.value; // 현재 값 반환
    },
    set(value) {
        reExchangeInput.value = value; // 입력 값 설정
    },
});
// 이메일 입력 유효성 검증
let isconfirmed = false;
let success = false;
const emailConfirm = async () => {
    isconfirmed = true;
    const userId = sendEmail.value;
    const response = await myaccountApi.confirmEmail(userId);
    if (response) {
        if (userId === auth.userId) {
            //userId 세션에서 가져오기(로그인할때 사용된 이메일)
            isMember.value = 'self';
            success = false;
            errorMessage.value = t('myAccount-transferto-notificationInput');
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
        errorMessageCheck.value = t('myAccount-transferto-checkEmailAgain');
    } else {
        if (sendEmail.value !== event.target.value) {
            errorMessageCheck.value = t('myAccount-transferto-emailNotCorrect');
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
        errorMessage.value = t('myAccount-transferto-insertEmail');
        isconfirmed = false;
    } else if (!emailPattern.test(sendEmail.value)) {
        errorMessage.value = t('myAccount-transferto-notificationForm');
    } else if (isconfirmed == false) {
        errorMessage.value = t('myAccount-transferto-checkEmail');
    } else if (isMember.value === 'self') {
        errorMessage.value = t('myAccount-transferto-notificationInput');
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

// 자동 환전 예약 데이터를 가져오는 함수
const fetchAutoExchange = async () => {
    try {
        const response = await api.get(
            `/api/exchange-reservation/setalert/${userId.value}`
        );
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
        const response = await api.get(
            `/api/exchange-reservation/${userId.value}`
        );
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
        title: t('myAccount--Alert-swal-requestDelete'),
        text: t('myAccount--Alert-swal-warningDeleteAlert'),
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: t('myAccount--Alert-swal-deleteAlert'),
        cancelButtonText: t('transaction_states_cancelled'),
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
            await api.delete(`/api/exchange-reservation/${resNo}`);
            Swal.fire({
                title: t('myAccount--Alert-swal-suceess'),
                text: t('myAccount--Alert-swal-setSuccess'),
                icon: 'success',
            });
            fetchAlertConditions(); // 알림을 다시 가져와서 업데이트
            fetchAutoExchange(); // 예약을 다시 가져와서 업데이트
        }
    } catch (error) {
        console.error('알림 설정 삭제 중 오류 발생:', error);
        Swal.fire({
            title: t('myAccount--Alert-swal-fail'),
            text: t('myAccount--error-notificationAlert'),
            icon: 'error',
        });
    }
};

const flagIcon = (code) => {
    return `/images/flag_${code}.png`;
};

watchEffect(() => {
    if (user.value) {
        songCountryCode.value = user.value.countryCode;
        customerunit.value = CURRENCY_NAME[songCountryCode.value];
        customerCountry.value = COUNTRY_KEY[songCountryCode.value];
        fetchBalances();
        fetchExchangeRates();
        fetchAutoExchange();
        fetchAlertConditions();
    }
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
            <div class="row justify-content-center gap-3">
                <!-- USD Wallet -->
                <div class="col-lg-4 col-md-5 max-margin-bottom d-flex justify-content-center">
                    <!-- Song-E Money 카드 -->
                    <AccountsCard ref="songEMoneyCardRef" :assetType="SONGE" @click="selectAsset(SONGE)"
                        :class="{ selected: selectedAsset === SONGE }" />
                </div>

                <!-- KRW Wallet -->
                <div class="col-lg-4 col-md-5 d-flex justify-content-center">
                    <!-- Won-E Money 카드 -->
                    <AccountsCard ref="wonEMoneyCardRef" :assetType="WONE" @click="selectAsset(WONE)"
                        :class="{ selected: selectedAsset === WONE }" />
                </div>
            </div>
            <div class="card">
                <!-- Song-E Money 선택 시 -->
                <template v-if="selectedAsset === SONGE">
                    <nav class="nav custom-nav nav-underline nav-justified">
                        <a class="flex-sm-fill text-sm-center nav-link" :class="{
                            active: activeTab === TRANSACTION_TYPES.DEPOSIT,
                        }" @click="activeTab = TRANSACTION_TYPES.DEPOSIT" aria-current="page">
                            {{ $t('myAccount--nav-songE-fill') }}
                        </a>
                        <a class="flex-sm-fill text-sm-center nav-link" :class="{
                            active:
                                activeTab === TRANSACTION_TYPES.EXCHANGE,
                        }" @click="activeTab = TRANSACTION_TYPES.EXCHANGE">
                            {{ $t(i18n_EXCHANGE) }}
                        </a>
                        <a class="flex-sm-fill text-sm-center nav-link" :class="{
                            active: activeTab === TRANSACTION_TYPES.REFUND,
                        }" @click="activeTab = TRANSACTION_TYPES.REFUND">
                            {{ $t('myAccount--nav-songE-vacate') }}
                        </a>
                    </nav>
                </template>

                <!-- Won-E Money 선택 시 -->
                <template v-if="selectedAsset === WONE">
                    <nav class="nav custom-nav nav-underline nav-justified">
                        <a class="flex-sm-fill text-sm-center nav-link" :class="{
                            active:
                                activeTab === TRANSACTION_TYPES.RE_EXCHANGE,
                        }" @click="activeTab = TRANSACTION_TYPES.RE_EXCHANGE">
                            {{ $t(i18n_EXCHANGE) }}
                        </a>
                        <a class="flex-sm-fill text-sm-center nav-link" :class="{
                            active:
                                activeTab === TRANSACTION_TYPES.TRANSFER,
                        }" @click="activeTab = TRANSACTION_TYPES.TRANSFER" aria-current="page">
                            {{ $t(i18n_TRANSFER) }}
                        </a>
                    </nav>
                </template>

                <!-- Song-E Money의 탭 내용 -->
                <div class="card-body" v-if="selectedAsset === SONGE">
                    <div class="d-flex justify-content-center">
                        <div class="d-flex flex-column tab-content-width">
                            <div v-if="activeTab === TRANSACTION_TYPES.DEPOSIT"
                                class="tab-pane fade show active d-grid gap-4">
                                <div>
                                    <label class="d-flex align-items-center">
                                        <div class="icon-container me-2">
                                            <img v-if="songCountryCode" :src="flagIcon(songCountryCode)" alt="icon"
                                                class="flag-icon-img" />
                                        </div>
                                        <div class="input-label-text">
                                            {{ $t('myAccount--songE-title') }}
                                        </div>
                                    </label>
                                    <ArgonAmountInput v-model="depositAmount"
                                        :placeholder="`${$t('transaction_types_deposit')} ${$t('myAccount--input-placeholder')}`"
                                        :unit="customerunit" />
                                </div>
                                <div>
                                    {{ t('myAccount-input-expectedAmount') }}:
                                    <span class="balance-text" :class="{
                                        'text-danger': depositAmount !== '',
                                    }">{{ processAfterBalance }}</span>{{ customerunit }}
                                </div>
                                <button type="submit" class="btn btn-primary w-100 fs-4" @click="openModal"
                                    :disabled="!isValidAmount(depositAmount)" variant="gradient">
                                    {{ t('myAccount--songE-button-fill') }}
                                </button>
                            </div>

                            <div v-if="activeTab === TRANSACTION_TYPES.EXCHANGE" class="d-grid gap-4">
                                <div>
                                    <label class="d-flex align-items-center">
                                        <div class="icon-container me-2">
                                            <img :src="flagIcon(wonCoutryCode)" alt="icon" class="flag-icon-img" />
                                        </div>
                                        <div class="input-label-text">
                                            WON-E
                                        </div>
                                    </label>
                                    <ExchangeAmountInput v-model="receiveAmount"
                                        :placeholder="`${$t('transaction_types_exchange')} ${$t('myAccount--input-placeholder')}`"
                                        :unit="wonUnit" :selectedAsset="selectedAsset" :songEMoneyBalance="songEMoneyBalance_toKRW
                                            " :activeTab="activeTab" :errorAmountMessage="errorAmountMessage"
                                        @update:errorAmountMessage="
                                            errorAmountMessage = $event
                                            " @focus="onfocus('receive')" @blur="onblurReceive" />
                                    <!-- <ExchangeAmountInput v-model="receiveAmount"
                  :placeholder="`${$t('transaction_types_exchange')} ${$t('myAccount--input-placeholder')}`"
                  :unit="wonUnit" :selectedAsset="selectedAsset" :songEMoneyBalance="songEMoneyBalance_toKRW"
                  :activeTab="activeTab" :errorAmountMessage="errorAmountMessage"
                  @update:errorAmountMessage="errorAmountMessage = $event" @focus="onfocus('receive')"
                  @blur="onblur" /> -->
                                </div>
                                <div>
                                    <label class="d-flex align-items-center">
                                        <div class="icon-container me-2">
                                            <img v-if="songCountryCode" :src="flagIcon(songCountryCode)" alt="icon"
                                                class="flag-icon-img" />
                                        </div>
                                        <div class="input-label-text">
                                            {{ $t('myAccount--songE-title') }}
                                        </div>
                                    </label>
                                    <!-- 현재환율 * exchangeamount -->
                                    <div style="height: 60px">
                                        <ExchangeAmountInput v-model="exchangeAmount"
                                            :placeholder="`${$t('transaction_types_exchange')} ${$t('myAccount--input-placeholder')}`"
                                            :unit="customerunit" :selectedAsset="selectedAsset" :songEMoneyBalance="songEMoneyBalance
                                                " :activeTab="activeTab" :errorMessage="errorAmountMessage"
                                            @update:errorAmountMessage="
                                                errorAmountMessage = $event
                                                " @focus="onfocus('exchange')" @blur="onblurExchange" />
                                    </div>
                                    <!-- <div class="balance-text">{{ receivedAmount }} {{ customerunit }}</div> -->
                                    <small>{{
                                        t('myAccount--songE-exchangeRate')
                                    }}: 1 {{ wonUnit }} =
                                        {{ currentFromKrw }}
                                        {{ customerunit }}
                                    </small>
                                </div>
                                <div>
                                    {{ t('myAccount-input-expectedAmount') }}:
                                    <span class="balance-text" :class="{
                                        'text-blue': exchangeAmount !== '',
                                    }">{{ processAfterBalance }}</span>{{ customerunit }}
                                </div>
                                <!-- <div class="balance-text">{{t('myAccount-input-expectedAmount')}}: {{ processAfterBalance }}{{ customerunit }}</div> -->
                                <button type="submit" class="btn btn-primary w-100 fs-4" @click="openModal"
                                    :disabled="!isValidAmount(exchangeAmount)" variant="gradient">
                                    {{ t('myAccount--wonE-button-exchange') }}
                                </button>
                            </div>

                            <div v-if="activeTab === TRANSACTION_TYPES.REFUND" class="d-grid gap-4">
                                <div>
                                    <label class="d-flex align-items-center">
                                        <div class="icon-container me-2">
                                            <img v-if="songCountryCode" :src="flagIcon(songCountryCode)" alt="icon"
                                                class="flag-icon-img" />
                                        </div>
                                        <div class="input-label-text">
                                            {{
                                                t(
                                                    'myAccount-deposit-myaccountNo'
                                                )
                                            }}
                                        </div>
                                    </label>
                                    <ArgonAmountInput v-model="refundAmount"
                                        :placeholder="`${$t('transaction_types_refund')} ${$t('myAccount--input-placeholder')}`"
                                        :unit="customerunit" :selectedAsset="selectedAsset"
                                        :songEMoneyBalance="songEMoneyBalance" :activeTab="activeTab" />
                                </div>
                                <div>
                                    <label class="d-flex align-items-center">
                                        <div class="icon-container me-2">
                                            <img v-if="songCountryCode" :src="flagIcon(songCountryCode)" alt="icon"
                                                class="flag-icon-img" />
                                        </div>
                                        <div class="input-label-text">
                                            {{ $t('myAccount--songE-title') }}
                                        </div>
                                    </label>
                                    <div>
                                        {{
                                            t('myAccount-input-expectedAmount')
                                        }}:
                                        <span class="balance-text" :class="{
                                            'text-blue':
                                                refundAmount !== '',
                                        }">{{ processAfterBalance }}</span>{{ customerunit }}
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary w-100 fs-4" @click="openModal"
                                    :disabled="!isValidAmount(refundAmount)" variant="gradient">
                                    {{ t('myAccount--songE-button-vacate') }}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Won-E Money의 탭 내용 -->
                <div class="card-body" v-if="selectedAsset === WONE">
                    <div class="d-flex justify-content-center">
                        <div class="d-flex flex-column tab-content-width">
                            <div v-if="activeTab === TRANSACTION_TYPES.TRANSFER" class="d-grid gap-4">
                                <div>
                                    <div class="mb-3">
                                        <div class="d-flex align-items-center mb-1">
                                            <div class="input-label-text">
                                                {{
                                                    t(
                                                        'myAccount-transferto-sendtoEmail'
                                                    )
                                                }}
                                            </div>
                                            <!-- 회원/비회원 표시 -->
                                            <small v-if="isMember === 'member'">{{
                                                t(
                                                    'myAccount-transferto-member'
                                                )
                                            }}</small>
                                            <small v-else-if="
                                                isMember === 'no-member'
                                            ">{{
                                                    t(
                                                        'myAccount-transferto-noMember'
                                                    )
                                                }}</small>
                                        </div>
                                        <div class="d-flex justify-content-between align-items-center gap-3">
                                            <ArgonInput v-model="sendEmail" :placeholder="t(
                                                'myAccount--wonE-enterEmail'
                                            )
                                                " @input="onInput" variant="gradient" :class="{
                                                    'is-invalid': errorMessage,
                                                }" :error="errorMessage !== ''" :success="success"
                                                class="w-100 mb-0" />
                                            <button class="btn btn-secondary mb-0 text-nowrap" @click="emailConfirm"
                                                size="sm" variant="outline" :disabled="sendEmail === ''">
                                                {{
                                                    $t(
                                                        'myAccount--wonE-confirmEmail'
                                                    )
                                                }}
                                            </button>
                                        </div>

                                        <div v-if="errorMessage !== ''" class="invalid-feedback text-xs">
                                            {{ errorMessage }}
                                        </div>
                                    </div>
                                    <div class="mb-3">
                                        <div class="input-label-text">
                                            {{
                                                t(
                                                    'myAccount--wonE-confirmEmail'
                                                )
                                            }}
                                        </div>
                                        <ArgonInput v-model="sendEmailConfirm" :placeholder="t(
                                            'myAccount-transferto-checkEmailAgain'
                                        )
                                            " :class="{
                                                'is-invalid': errorMessageCheck,
                                            }" :error="errorMessageCheck !== ''" :success="checkSucess"
                                            @input="onInputCheck" />
                                        <div v-if="errorMessageCheck" class="invalid-feedback text-xs mb-1">
                                            {{ errorMessageCheck }}
                                        </div>
                                    </div>
                                </div>

                                <div>
                                    <label class="d-flex align-items-center">
                                        <div class="icon-container me-2">
                                            <img :src="flagIcon(wonCoutryCode)" alt="icon" class="flag-icon-img" />
                                        </div>
                                        <i class="fa-solid fa-arrow-right-arrow-left me-2 fs-4"></i>
                                        <div class="icon-container me-2">
                                            <img :src="flagIcon(wonCoutryCode)" alt="icon" class="flag-icon-img" />
                                        </div>
                                    </label>
                                    <ArgonAmountInput v-model="transferAmount"
                                        :placeholder="`${$t('transaction_types_transfer')} ${$t('myAccount--input-placeholder')}`"
                                        unit="KRW" :selectedAsset="selectedAsset" :wonEMoneyBalance="wonEMoneyBalance"
                                        :activeTab="activeTab" />
                                </div>

                                <div>
                                    <label class="d-flex align-items-center">
                                        <div class="icon-container me-2">
                                            <img :src="flagIcon(wonCoutryCode)" alt="icon" class="flag-icon-img" />
                                        </div>
                                        <div class="input-label-text">
                                            {{ $t('myAccount--wonE-title') }}
                                        </div>
                                    </label>
                                    <div>
                                        {{
                                            t('myAccount-input-expectedAmount')
                                        }}:
                                        <span class="balance-text" :class="{
                                            'text-blue':
                                                transferAmount !== '',
                                        }">{{ processAfterWonBalance }}</span>{{ wonUnit }}
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary w-100 fs-4" @click="openModal" :disabled="!isValidAmount(transferAmount) ||
                                    errorMessage !== '' ||
                                    errorMessageCheck !== ''
                                    " variant="gradient">
                                    {{ $t('myAccount--wonE-button-transfer') }}
                                </button>
                            </div>

                            <div v-if="
                                activeTab === TRANSACTION_TYPES.RE_EXCHANGE
                            " class="d-grid gap-4">
                                <div>
                                    <label class="d-flex align-items-center">
                                        <div class="icon-container me-2">
                                            <img v-if="songCountryCode" :src="flagIcon(songCountryCode)" alt="icon"
                                                class="flag-icon-img" />
                                        </div>
                                        <div class="input-label-text">
                                            {{ $t('myAccount--songE-title') }}
                                        </div>
                                    </label>

                                    <ExchangeAmountInput v-model="receiveAmount"
                                        :placeholder="`${$t('transaction_types_exchange')} ${$t('myAccount--input-placeholder')}`"
                                        :unit="customerunit" :selectedAsset="selectedAsset" :wonEMoneyBalance="wonEMoneyBalance_FromKRW
                                            " :activeTab="activeTab" :errorAmountMessage="errorAmountMessage"
                                        @update:errorAmountMessage="
                                            errorAmountMessage = $event
                                            " @focus="onfocus('receive')" @blur="onblur" />
                                </div>
                                <div>
                                    <label class="d-flex align-items-center">
                                        <div class="icon-container me-2">
                                            <img :src="flagIcon(wonCoutryCode)" alt="icon" class="flag-icon-img" />
                                        </div>
                                        <div class="input-label-text">
                                            WON-E
                                        </div>
                                    </label>
                                    <div style="height: 80px">
                                        <ExchangeAmountInput v-model="reExchangeAmount" :unit="wonUnit"
                                            :selectedAsset="selectedAsset" :wonEMoneyBalance="wonEMoneyBalance"
                                            :activeTab="activeTab"
                                            :placeholder="`${$t('transaction_types_exchange')} ${$t('myAccount--input-placeholder')}`"
                                            :errorMessage="errorAmountMessage" @update:errorAmountMessage="
                                                errorAmountMessage = $event
                                                " @focus="onfocus('reExchange')" @blur="onblur('reExchange')" />
                                    </div>

                                    <small>{{
                                        t(
                                            'myAccount--wonE-currentExchangeRate'
                                        )
                                    }}: 1 {{ customerunit }} =
                                        {{ currentToKrw }}
                                        {{ wonUnit }}
                                    </small>
                                </div>
                                <div>
                                    {{ t('myAccount-input-expectedAmount') }}:
                                    <span class="balance-text" :class="{
                                        'text-blue':
                                            reExchangeAmount !== '',
                                    }">{{ processAfterWonBalance }}</span>{{ wonUnit }}
                                </div>
                                <button type="submit" class="btn btn-primary w-100 fs-4" @click="openModal"
                                    :disabled="!isValidAmount(exchangeAmount)" variant="gradient">
                                    {{ t('myAccount--songE-button-exchange') }}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="custom-spacer"></div>

        <!-- Graph and Conversion Section -->
        <div id="exchange-rate" class="d-grid gap-5">
            <h3 clas="mb-0">{{ $t('myAccount--header-exchangeRate') }}</h3>
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
                                <h6>
                                    {{ customerunit }}
                                    <i class="fa-solid fa-arrow-right"></i>
                                    {{ wonUnit }}
                                </h6>
                                <div class="d-flex align-items-center">
                                    <div class="position-relative flex-grow-1">
                                        <input type="number" class="form-control" v-model.number="usdAmount"
                                            @input="convertToKrw" aria-label="Amount in USD" />
                                        <img src="@/assets/img/icons/flags/US.png" alt="USA Flag"
                                            class="input-flag-icon" />
                                    </div>
                                    <span class="mx-3">=</span>
                                    <div class="position-relative flex-grow-1">
                                        <input type="text" class="form-control" :value="krwAmount" readonly
                                            aria-label="Amount in KRW" />
                                        <img src="@/assets/img/icons/flags/KR.png" alt="KRW Flag"
                                            class="input-flag-icon" />
                                    </div>
                                </div>
                            </div>
                            <div>
                                <h6>
                                    {{ wonUnit }}
                                    <i class="fa-solid fa-arrow-right"></i>
                                    {{ customerunit }}
                                </h6>
                                <div class="d-flex align-items-center">
                                    <div class="position-relative flex-grow-1">
                                        <input type="number" class="form-control" v-model.number="krwAmountReverse"
                                            @input="convertToUsd" />
                                        <img src="@/assets/img/icons/flags/KR.png" alt="KRW Flag"
                                            class="input-flag-icon" />
                                    </div>
                                    <span class="mx-3">=</span>
                                    <div class="position-relative flex-grow-1">
                                        <input type="text" class="form-control" :value="usdAmountReverse" readonly />
                                        <img src="@/assets/img/icons/flags/US.png" alt="USA Flag"
                                            class="input-flag-icon" />
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
                    {{ $t('myAccount--header-autoExchange') }}
                    <i class="fa-solid fa-angle-right alert-icon"></i>
                </span>
            </div>
            <div class="card">
                <div class="card-body">
                    <div class="d-grid gap-3">
                        <div>
                            <label for="autoCondition" class="form-label">{{
                                $t('myAccount--autoExchange-title')
                                }}</label>
                            <ul v-if="autoConditions.length > 0" class="list-group">
                                <li class="list-group-item">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="d-flex gap-3 flex-column-min">
                                            <div>
                                                {{
                                                    t(
                                                        'myAccount--exchangeRate-baseCurrency'
                                                    )
                                                }}:
                                                {{
                                                    autoConditions[0]?.baseCode
                                                }}
                                            </div>
                                            <div>
                                                {{
                                                    t(
                                                        'myAccount--exchangeRate-targetCurrency'
                                                    )
                                                }}:
                                                {{
                                                    autoConditions[0]
                                                        ?.targetCode
                                                }}
                                            </div>
                                            <div>
                                                {{
                                                    t(
                                                        'myAccount--exchangeRate-targetExchangeRate'
                                                    )
                                                }}:
                                                {{
                                                    autoConditions[0]
                                                        ?.targetExchange
                                                }}
                                            </div>
                                            <div>
                                                {{
                                                    t(
                                                        'myAccount--exchangeRate-targetKrw'
                                                    )
                                                }}:
                                                {{
                                                    autoConditions[0]?.targetKrw
                                                }}
                                            </div>
                                        </div>
                                        <div class="cursor-pointer" @click="
                                            confirmDelete(
                                                autoConditions[0]?.resNo
                                            )
                                            ">
                                            <i class="ni ni-fat-remove text-danger"></i>
                                        </div>
                                        <!-- <button class="btn btn-sm btn-danger mb-0"
                    @click="confirmDelete(autoConditions[0]?.resNo)">삭제</button> -->
                                    </div>
                                </li>
                            </ul>
                            <p v-else style="margin-left: 0.5rem">
                                {{
                                    t(
                                        'myAccount--autoExchange-noReservationAuto'
                                    )
                                }}
                            </p>
                        </div>

                        <div>
                            <label for="alertConditions" class="form-label">{{
                                t('myAccount--exchangeRate-settingHistory')
                                }}</label>
                            <ul v-if="alertConditions.length > 0" class="list-group">
                                <li v-for="condition in alertConditions" :key="condition.resNo" class="list-group-item">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="d-flex gap-3 flex-column-min">
                                            <div>
                                                {{
                                                    t(
                                                        'myAccount--exchangeRate-baseCurrency'
                                                    )
                                                }}: {{ condition.baseCode }}
                                            </div>
                                            <div>
                                                {{
                                                    t(
                                                        'myAccount--exchangeRate-targetCurrency'
                                                    )
                                                }}: {{ condition.targetCode }}
                                            </div>
                                            <div>
                                                {{
                                                    t(
                                                        'myAccount--exchangeRate-targetExchangeRate'
                                                    )
                                                }}:
                                                {{ condition.targetExchange }}
                                            </div>
                                        </div>
                                        <div class="cursor-pointer" @click="
                                            confirmDelete(condition.resNo)
                                            ">
                                            <i class="ni ni-fat-remove text-danger"></i>
                                        </div>
                                        <!-- <button class="btn btn-outline-danger mb-0" @click="confirmDelete(condition.resNo)">
                    <i class="ni ni-fat-remove text-danger"></i>
                  </button> -->
                                    </div>
                                </li>
                            </ul>
                            <p v-if="alertConditions.length === 0" style="margin-left: 0.5rem">
                                {{
                                    t(
                                        'myAccount--exchangeRate-noReservationAlert'
                                    )
                                }}
                            </p>
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
