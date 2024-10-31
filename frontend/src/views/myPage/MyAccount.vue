<script setup>
import ArgonInput from "@/components/templates/ArgonInput.vue";
import AccountsCard from "@/views/Cards/AccountsCard.vue";
import ArgonAmountInput from "@/components/yb_templates/ArgonAmountInput.vue";
// import ExchangeAmountInput from '@/components/yb_templates/ExchangeAmountInput.vue';
import { ref, onMounted, computed, watchEffect } from "vue";
import myaccountApi from "../../api/myaccountApi";
import { useExchangeStore } from "@/stores/exchangeStore";
import SecondPasswordModal from "@/components/modal/SecondPasswordModal.vue";
import Swal from "sweetalert2";
import {
  TRANSACTION_TYPES,
  TRANSACTION_TYPES_KEY,
} from "@/constants/transactionType";
import {
  COUNTRY_CODE,
  COUNTRY_KEY,
  CURRENCY_NAME,
} from "@/constants/countryCode";

//i18n
import { useI18n } from "vue-i18n";
const { t } = useI18n();

//user
import { useAuthStore } from "@/stores/auth";
const auth = useAuthStore();
const user = computed(() => auth.user);
// const userId = computed(() => auth.user.userId);

//숫자 포맷팅
import currencyFormatter from "../../js/currencyFormatter";
const { formatNumber, exchangeCurrency } = currencyFormatter;

// Import Swiper Vue.js components
import { Swiper, SwiperSlide } from "swiper/vue";

// Import Swiper styles
import "swiper/css";
import "swiper/css/effect-coverflow";
import "swiper/css/pagination";
// import required modules
import { EffectCoverflow, Pagination } from "swiper/modules";
// Define the modules to be used
const modules = [EffectCoverflow, Pagination];

const emit = defineEmits(["password-verified", "close"]);
const store = useExchangeStore();

const SONGE = "song-e";
const WONE = "won-e";

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

const depositAmount = ref("");
const refundAmount = ref("");
const transferAmount = ref("");

const sendEmail = ref("");
const sendEmailConfirm = ref("");
const isMember = ref(null);
const showModal = ref(false);
let currentAction = ref("");

const currentToKrw = computed(() => store.currentToKrw);
const currentFromKrw = computed(() => store.currentFromKrw);
// const currentToKrw = computed(() => 0.0865);
// const currentFromKrw = computed(() => 11.5594);

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
    balance = songEMoneyBalance.value + parseFloat(depositAmount.value || 0);
  }
  // 환전일 경우 잔액 감소
  if (activeTab.value === TRANSACTION_TYPES.EXCHANGE) {
    balance = songEMoneyBalance.value - parseFloat(songAmount.value || 0);
  }
  // 환불일 경우 잔액 감소
  else if (activeTab.value === TRANSACTION_TYPES.REFUND) {
    balance = songEMoneyBalance.value - parseFloat(refundAmount.value || 0);
  }
  // 계산된 숫자를 포맷하여 반환
  return formatNumber(balance); // 소수점 두 자릿수까지 표시
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
    wonBalance = wonEMoneyBalance.value - parseFloat(reWonAmount.value || 0);
  }
  // 계산된 숫자를 포맷하여 반환
  return formatNumber(wonBalance); // 소수점 두 자릿수까지 표시
});

// 자산 탭 선택
const selectAsset = (asset) => {
  selectedAsset.value = asset;
  // Song-E Money 선택 시 기본 탭을 deposit로 설정, Won-E Money 선택 시 기본 탭을 transfer로 설정
  activeTab.value =
    asset === SONGE ? TRANSACTION_TYPES.DEPOSIT : TRANSACTION_TYPES.RE_EXCHANGE;
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
        if (!response.data)
          throw new Error("My account has an insufficient balance.");
        break;
      case TRANSACTION_TYPES.EXCHANGE:
        response = await exchange();
        kwd = i18n_EXCHANGE;
        break;
      case TRANSACTION_TYPES.REFUND:
        response = await refund();
        if (!response.data)
          throw new Error("My Song-E has an insufficient balance.");
        kwd = i18n_REFUND;
        break;
      case TRANSACTION_TYPES.TRANSFER:
        response = await transfer();
        console.log(response);
        kwd = i18n_TRANSFER;
        break;
      case TRANSACTION_TYPES.RE_EXCHANGE:
        response = await reExchange();
        kwd = i18n_RE_EXCHANGE;
        break;
    }

    kwd = t(kwd);

    // ResponseEntity의 상태 코드 확인
    console.log(response);
    if (response.data) {
      Swal.fire({
        title: t("myAccount--swal-title"),
        text: t("myAccount--swal-content", { kwd: kwd }),
        icon: "success",
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
      throw new Error(
        response.data?.message || t("swal--default-error-message")
      );
    }
    resetValue();
  } catch (error) {
    console.log(error);
    await Swal.fire({
      title: t("swal--title-fail"),
      html: error.response.data || t("swal--default-error-message"),
      icon: "error",
    });
    resetValue();
  }
};

// API 호출 함수
const callAccountApi = async (apiFunction, params) => {
  try {
    const response = await apiFunction(params);
    console.log("rerer" + response);
    return response;
  } catch (error) {
    throw error;
  }
};

const ACCOUNT = computed(() => {
  const { userId, accountNo, songNo, krwNo, countryCode } = user.value; // 구조 분해 할당 사용
  return {
    accountDTO: { accountNo },
    songAccountDTO: { songNo, countryCode },
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
  const amount = wonAmount.value; //외화기준 금액 넣어주는 걸로 변경
  // 환율 API 연결 했을 때
  // const exchangeRate = currentFromKrw.value;
  const exchangeRate = 1;
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
  const amount = reSongAmount.value; //외화기준 금액 넣어주는 걸로 변경
  // 환율 API 연결 했을 때
  // const exchangeRate = currentToKrw.value;
  const exchangeRate = 1;
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
  const { userId, songNo, krwNo } = user.value; // 구조 분해 할당 사용
  let target_krwNo;
  const amount = transferAmount.value; // 송금하려는 금액
  const stateCode = isMember.value === "no-member" ? 4 : 1;
  const targetHistoryContent = `${userId} → WonE`;
  if (isMember.value === "no-member") {
    target_krwNo = sendEmail.value;
  } else {
    target_krwNo = await myaccountApi.getKrwNo(sendEmail.value);
  }
  // console.log("tar" + target_krwNo);
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
      historyContent: `WonE → ${sendEmail.value}`,
      amount,
    },
  });

  if (response != "") {
    console.log("?????" + response);
    return response;
  }
};

const resetValue = () => {
  console.log("reset????");
  refundAmount.value = "";
  transferAmount.value = "";
  depositAmount.value = "";

  reSongAmount.value = "";
  wonAmount.value = "";

  sendEmail.value = "";
  sendEmailConfirm.value = "";
  success = false;
  isMember.value = "";
  isconfirmed = false;
  checkSucess = false;
  // receiveInput.value = '';
};

const fetchBalances = async () => {
  myaccountApi.fetchkrwAccountBalance(user.value.krwNo).then((balance) => {
    if (balance === "") {
      Swal.fire({
        title: t("myAccount--Alert-swal-fail"),
        text: t("Invalid won-e account."),
        icon: "error",
      });
    } else {
      wonEMoneyBalance.value = balance;
    }
  });

  myaccountApi.fetchsongeAccountBalance(user.value.songNo).then((balance) => {
    if (balance === "") {
      Swal.fire({
        title: t("myAccount--Alert-swal-fail"),
        text: t("Invalid song-e account."),
        icon: "error",
      });
    } else {
      songEMoneyBalance.value = balance;
    }
  });
};

// 이메일 입력 유효성 검증
let isconfirmed = false;
let success = false;
const emailConfirm = async () => {
  isconfirmed = true;
  const userId = sendEmail.value;
  const response = await myaccountApi.confirmEmail(userId);
  if (response) {
    // console.log("???fsdd");
    if (userId === user.value.userId) {
      console.log("???dfeeeee");
      //userId 세션에서 가져오기(로그인할때 사용된 이메일)
      isMember.value = "self";
      success = false;
      errorMessage.value = t("myAccount-transferto-notificationInput");
    } else {
      console.log("???");
      isMember.value = "member"; // 회원 이메일로 표시
      success = true;
      errorMessage.value = "";
    }
  } else if (sendEmail.value !== "" && emailPattern.test(sendEmail.value)) {
    isMember.value = "no-member"; // 비회원 이메일로 표시
    success = true;
    errorMessage.value = "";
  } else {
    isMember.value = "error";
    success = false;
  }
};
// 이메일 형식 확인을 위한 정규식
const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

const errorMessageCheck = ref("");
let checkSucess = false;
const onInputCheck = (event) => {
  if (sendEmailConfirm.value === "" && checkSucess) {
    errorMessageCheck.value = t("myAccount-transferto-checkEmailAgain");
  } else {
    if (sendEmail.value !== event.target.value) {
      errorMessageCheck.value = t("myAccount-transferto-emailNotCorrect");
    } else {
      errorMessageCheck.value = "";
      checkSucess = true;
    }
  }
};
const errorMessage = ref("");
const onInput = (event) => {
  sendEmail.value = event.target.value;
  isconfirmed = false;

  if (sendEmail.value == "" && isconfirmed) {
    errorMessage.value = t("myAccount-transferto-insertEmail");
    isconfirmed = false;
  } else if (!emailPattern.test(sendEmail.value)) {
    errorMessage.value = t("myAccount-transferto-notificationForm");
  } else if (isconfirmed == false) {
    errorMessage.value = t("myAccount-transferto-checkEmail");
  } else if (isMember.value === "self") {
    errorMessage.value = t("myAccount-transferto-notificationInput");
  } else {
    errorMessage.value = "";
  }
  if (sendEmailConfirm.value !== "") {
    onInputCheck({ target: { value: sendEmailConfirm.value } });
  }
};

const flagIcon = (code) => {
  return `/images/flag_${code}.png`;
};

// 환전탭
const wonAmount = ref("");
const isInvalid = ref(false); // 불리언 값을 ref로 변경
const songAmount = computed(() => {
  const won = parseInt(wonAmount.value, 10);
  if (isNaN(won)) return "";
  return Math.round(won * currentFromKrw.value); // 소수점 이하 버림
});
function handleWonInput(event) {
  // 숫자만 허용 (소수점 제외)
  let newValue = event.target.value.replace(/[^0-9]/g, "");
  // 송금액이 100 미만이 되는 최대 원화 금액 계산
  const maxWon = Math.floor(
    (songEMoneyBalance.value - 0.01) / currentFromKrw.value
  );
  // 입력값이 최대 원화 금액을 초과하면 잘라냄
  if (parseInt(newValue, 10) > maxWon) {
    newValue = maxWon.toString();
    isInvalid.value = true;
  } else {
    isInvalid.value = false;
  }
  wonAmount.value = newValue;
}

// 환급탭
const reSongAmount = ref("");
const isInvalidRe = ref(false);
const reWonAmount = computed(() => {
  const won = parseInt(reSongAmount.value, 10);
  if (isNaN(won)) return "";
  return Math.floor(won * currentToKrw.value); // 소수점 이하 버림
});
function handleSongInput(event) {
  let newValue = event.target.value.replace(/[^0-9]/g, ""); // 숫자만 허용 (소수점 제외)
  const maxWon = Math.floor(
    (wonEMoneyBalance.value - 0.01) / currentToKrw.value
  );
  // 입력값이 최대 원화 금액을 초과하면 잘라냄
  if (parseInt(newValue, 10) > maxWon) {
    newValue = maxWon.toString();
    isInvalidRe.value = true;
  } else {
    isInvalidRe.value = false;
  }
  reSongAmount.value = newValue;
}

const onSlideChange = (swiper) => {
  const asset = swiper.activeIndex === 0 ? SONGE : WONE;
  selectAsset(asset);
};

watchEffect(() => {
  if (user.value) {
    songCountryCode.value = user.value.countryCode;
    customerunit.value = CURRENCY_NAME[songCountryCode.value];
    customerCountry.value = COUNTRY_KEY[songCountryCode.value];
    fetchBalances();
  }
});
</script>
<template>
  <SecondPasswordModal
    v-if="showModal"
    @close="closeModal"
    @password-verified="handlePasswordVerified"
  />

  <h3 class="mb-0">My account</h3>
  <swiper
    :effect="'coverflow'"
    :grabCursor="true"
    :centeredSlides="true"
    :slidesPerView="'auto'"
    :coverflowEffect="{
      rotate: 50,
      stretch: 0,
      depth: 100,
      modifier: 1,
      slideShadows: true,
    }"
    :pagination="true"
    :modules="modules"
    class="mySwiper"
    @slideChange="onSlideChange"
  >
    <swiper-slide>
      <accounts-card
        ref="songEMoneyCardRef"
        :assetType="SONGE"
        :class="{ selected: selectedAsset === SONGE }"
      />
    </swiper-slide>
    <swiper-slide>
      <accounts-card
        ref="wonEMoneyCardRef"
        :assetType="WONE"
        :class="{ selected: selectedAsset === WONE }"
      />
    </swiper-slide>
  </swiper>

  <!-- <div class="custom-spacer"></div> -->
  <!-- <div class="row justify-content-center gap-3">
        <div class="col-lg-4 col-md-6 max-margin-bottom d-flex justify-content-center">
            <accounts-card ref="songEMoneyCardRef" :assetType="SONGE" @click="selectAsset(SONGE)"
                :class="{ selected: selectedAsset === SONGE }" />
        </div>

        <div class="col-lg-4 col-md-6 d-flex justify-content-center">
            <accounts-card ref="wonEMoneyCardRef" :assetType="WONE" @click="selectAsset(WONE)"
                :class="{ selected: selectedAsset === WONE }" />
        </div>
    </div> -->
  <div class="card">
    <!-- Song-E Money 선택 시 -->
    <template v-if="selectedAsset === SONGE">
      <nav class="nav custom-nav nav-underline nav-justified">
        <a
          class="flex-sm-fill text-sm-center nav-link"
          :class="{ active: activeTab === TRANSACTION_TYPES.DEPOSIT }"
          @click="activeTab = TRANSACTION_TYPES.DEPOSIT"
          aria-current="page"
        >
          {{ $t("myAccount--nav-songE-fill") }}
        </a>
        <a
          class="flex-sm-fill text-sm-center nav-link"
          :class="{ active: activeTab === TRANSACTION_TYPES.EXCHANGE }"
          @click="activeTab = TRANSACTION_TYPES.EXCHANGE"
        >
          {{ $t(i18n_EXCHANGE) }}
        </a>
        <a
          class="flex-sm-fill text-sm-center nav-link"
          :class="{ active: activeTab === TRANSACTION_TYPES.REFUND }"
          @click="activeTab = TRANSACTION_TYPES.REFUND"
        >
          {{ $t("myAccount--nav-songE-vacate") }}
        </a>
      </nav>
    </template>

    <!-- Won-E Money 선택 시 -->
    <template v-if="selectedAsset === WONE">
      <nav class="nav custom-nav nav-underline nav-justified">
        <a
          class="flex-sm-fill text-sm-center nav-link"
          :class="{ active: activeTab === TRANSACTION_TYPES.RE_EXCHANGE }"
          @click="activeTab = TRANSACTION_TYPES.RE_EXCHANGE"
        >
          {{ $t(i18n_EXCHANGE) }}
        </a>
        <a
          class="flex-sm-fill text-sm-center nav-link"
          :class="{ active: activeTab === TRANSACTION_TYPES.TRANSFER }"
          @click="activeTab = TRANSACTION_TYPES.TRANSFER"
          aria-current="page"
        >
          {{ $t(i18n_TRANSFER) }}
        </a>
      </nav>
    </template>

    <!-- Song-E Money의 탭 내용 -->
    <div class="card-body" v-if="selectedAsset === SONGE">
      <div class="d-flex justify-content-center">
        <div class="d-flex flex-column tab-content-width">
          <div
            v-if="activeTab === TRANSACTION_TYPES.DEPOSIT"
            class="tab-pane fade show active d-grid gap-4"
          >
            <div>
              <label class="d-flex align-items-center">
                <div class="icon-container me-2">
                  <img
                    v-if="songCountryCode"
                    :src="flagIcon(songCountryCode)"
                    alt="icon"
                    class="flag-icon-img"
                  />
                </div>
                <div class="input-label-text">
                  {{ $t("myAccount--songE-title") }}
                </div>
              </label>
              <ArgonAmountInput
                v-model="depositAmount"
                :placeholder="$t('myAccount--input-placeholder')"
                :unit="customerunit"
              />
            </div>
            <div>
              {{ t("myAccount-input-expectedAmount") }}:
              <span
                class="balance-text"
                :class="{ 'text-blue': depositAmount !== '' }"
                >{{ processAfterBalance }}</span
              >{{ customerunit }}
            </div>
            <button
              type="submit"
              class="btn btn-primary w-100 fs-4"
              @click="openModal"
              :disabled="!isValidAmount(depositAmount)"
              variant="gradient"
            >
              {{ t("myAccount--songE-button-fill") }}
            </button>
          </div>

          <!-- 환전탭 -->
          <div
            v-if="activeTab === TRANSACTION_TYPES.EXCHANGE"
            class="d-grid gap-4"
          >
            <div>
              <label class="d-flex align-items-center">
                <div class="icon-container me-2">
                  <img
                    :src="flagIcon(wonCoutryCode)"
                    alt="icon"
                    class="flag-icon-img"
                  />
                </div>
                <div class="input-label-text">WON-E</div>
              </label>
              <div class="input-group">
                <input
                  id="won"
                  type="text"
                  class="form-control"
                  :class="{ 'is-invalid': isInvalid }"
                  v-model="wonAmount"
                  @input="handleWonInput"
                  @click="wonAmount = ''"
                  :placeholder="$t('myAccount--input-placeholder')"
                />
                <span class="input-group-text bg-secondary text-white">
                  {{ wonUnit }}
                </span>
              </div>
            </div>
            <div>
              <label class="d-flex align-items-center">
                <div class="icon-container me-2">
                  <img
                    v-if="songCountryCode"
                    :src="flagIcon(songCountryCode)"
                    alt="icon"
                    class="flag-icon-img"
                  />
                </div>
                <div class="input-label-text">
                  {{ $t("myAccount--songE-title") }}
                </div>
              </label>
              <div class="input-group">
                <input
                  id="song"
                  type="text"
                  class="form-control"
                  :class="{ 'is-invalid': isInvalid }"
                  :value="formatNumber(songAmount)"
                  readonly
                />
                <span class="input-group-text bg-secondary text-white">
                  {{ customerunit }}
                </span>
                <div v-if="isInvalid" class="invalid-feedback text-xs">
                  {{ $t("myAccount--error-overAmount") }}
                </div>
              </div>
              <small
                >{{ t("myAccount--songE-exchangeRate") }}: 1 {{ wonUnit }} =
                {{ currentFromKrw }}
                {{ customerunit }}
              </small>
            </div>
            <div>
              {{ t("myAccount-input-expectedAmount") }}:
              <span
                class="balance-text"
                :class="{ 'text-red': songAmount !== '' }"
                >{{ processAfterBalance }}</span
              >{{ customerunit }}
            </div>
            <button
              type="submit"
              class="btn btn-primary w-100 fs-4"
              @click="openModal"
              :disabled="!isValidAmount(wonAmount)"
              variant="gradient"
            >
              {{ t("myAccount--wonE-button-exchange") }}
            </button>
          </div>

          <div
            v-if="activeTab === TRANSACTION_TYPES.REFUND"
            class="d-grid gap-4"
          >
            <div>
              <label class="d-flex align-items-center">
                <div class="icon-container me-2">
                  <img
                    v-if="songCountryCode"
                    :src="flagIcon(songCountryCode)"
                    alt="icon"
                    class="flag-icon-img"
                  />
                </div>
                <div class="input-label-text">
                  {{ t("myAccount-deposit-myaccountNo") }}
                </div>
              </label>
              <ArgonAmountInput
                v-model="refundAmount"
                :placeholder="`${$t('transaction_types_refund')} ${$t('myAccount--input-placeholder')}`"
                :unit="customerunit"
                :selectedAsset="selectedAsset"
                :songEMoneyBalance="songEMoneyBalance"
                :activeTab="activeTab"
              />
            </div>
            <div>
              <label class="d-flex align-items-center">
                <div class="icon-container me-2">
                  <img
                    v-if="songCountryCode"
                    :src="flagIcon(songCountryCode)"
                    alt="icon"
                    class="flag-icon-img"
                  />
                </div>
                <div class="input-label-text">
                  {{ $t("myAccount--songE-title") }}
                </div>
              </label>
              <div>
                {{ t("myAccount-input-expectedAmount") }}:
                <span
                  class="balance-text"
                  :class="{ 'text-red': refundAmount !== '' }"
                  >{{ processAfterBalance }}</span
                >{{ customerunit }}
              </div>
            </div>
            <button
              type="submit"
              class="btn btn-primary w-100 fs-4"
              @click="openModal"
              :disabled="!isValidAmount(refundAmount)"
              variant="gradient"
            >
              {{ t("myAccount--songE-button-vacate") }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Won-E Money의 탭 내용 -->
    <div class="card-body" v-if="selectedAsset === WONE">
      <div class="d-flex justify-content-center">
        <div class="d-flex flex-column tab-content-width">
          <div
            v-if="activeTab === TRANSACTION_TYPES.TRANSFER"
            class="d-grid gap-4"
          >
            <div>
              <div class="mb-3">
                <div class="d-flex align-items-center mb-1">
                  <div class="input-label-text me-3">
                    {{ $t("myAccount-transferto-sendtoEmail") }}
                  </div>
                  <!-- 회원/비회원 표시 -->
                  <span
                    v-if="isMember === 'member'"
                    class="badge badge-sm bg-gradient-success"
                    >{{ t("myAccount-transferto-member") }}</span
                  >
                  <span
                    v-else-if="isMember === 'no-member'"
                    class="badge badge-sm bg-gradient-success"
                    >{{ t("myAccount-transferto-noMember") }}</span
                  >
                </div>
                <div
                  class="d-flex justify-content-between align-items-center gap-3"
                >
                  <ArgonInput
                    v-model="sendEmail"
                    :placeholder="$t('myAccount--wonE-enterEmail')"
                    @input="onInput"
                    variant="gradient"
                    :class="{ 'is-invalid': errorMessage }"
                    :error="errorMessage !== ''"
                    :success="success"
                    class="w-100 mb-0"
                  />
                  <button
                    class="btn btn-secondary mb-0 text-nowrap"
                    @click="emailConfirm"
                    size="sm"
                    variant="outline"
                    :disabled="sendEmail === ''"
                  >
                    {{ $t("myAccount--wonE-confirmEmail") }}
                  </button>
                </div>

                <div
                  v-if="errorMessage !== ''"
                  class="invalid-feedback text-xs"
                >
                  {{ errorMessage }}
                </div>
              </div>
              <div class="mb-3">
                <div class="input-label-text">
                  {{ $t("myAccount--wonE-confirmEmail") }}
                </div>
                <ArgonInput
                  v-model="sendEmailConfirm"
                  :placeholder="$t('myAccount-transferto-checkEmailAgain')"
                  :class="{ 'is-invalid': errorMessageCheck }"
                  :error="errorMessageCheck !== ''"
                  :success="checkSucess"
                  @input="onInputCheck"
                />
                <div
                  v-if="errorMessageCheck"
                  class="invalid-feedback text-xs mb-1"
                >
                  {{ errorMessageCheck }}
                </div>
              </div>
            </div>

            <div>
              <label class="d-flex align-items-center">
                <div class="icon-container me-2">
                  <img
                    :src="flagIcon(wonCoutryCode)"
                    alt="icon"
                    class="flag-icon-img"
                  />
                </div>
                <i class="fa fa-arrow-right-arrow-left me-2 fs-4"></i>
                <div class="icon-container me-2">
                  <img
                    :src="flagIcon(wonCoutryCode)"
                    alt="icon"
                    class="flag-icon-img"
                  />
                </div>
              </label>
              <ArgonAmountInput
                v-model="transferAmount"
                :placeholder="`${$t('transaction_types_transfer')} ${$t('myAccount--input-placeholder')}`"
                unit="KRW"
                :selectedAsset="selectedAsset"
                :wonEMoneyBalance="wonEMoneyBalance"
                :activeTab="activeTab"
              />
            </div>

            <div>
              <label class="d-flex align-items-center">
                <div class="icon-container me-2">
                  <img
                    :src="flagIcon(wonCoutryCode)"
                    alt="icon"
                    class="flag-icon-img"
                  />
                </div>
                <div class="input-label-text">
                  {{ $t("myAccount--wonE-title") }}
                </div>
              </label>
              <div>
                {{ t("myAccount-input-expectedAmount") }}:
                <span
                  class="balance-text"
                  :class="{ 'text-red': transferAmount !== '' }"
                  >{{ processAfterWonBalance }}</span
                >{{ wonUnit }}
              </div>
            </div>
            <button
              type="submit"
              class="btn btn-primary w-100 fs-4"
              @click="openModal"
              :disabled="
                !isValidAmount(transferAmount) ||
                errorMessage !== '' ||
                errorMessageCheck !== ''
              "
              variant="gradient"
            >
              {{ $t("myAccount--wonE-button-transfer") }}
            </button>
          </div>

          <!-- 환급탭 -->
          <div
            v-if="activeTab === TRANSACTION_TYPES.RE_EXCHANGE"
            class="d-grid gap-4"
          >
            <div>
              <label class="d-flex align-items-center">
                <div class="icon-container me-2">
                  <img
                    v-if="songCountryCode"
                    :src="flagIcon(songCountryCode)"
                    alt="icon"
                    class="flag-icon-img"
                  />
                </div>
                <div class="input-label-text">
                  {{ $t("myAccount--songE-title") }}
                </div>
              </label>
              <div class="input-group">
                <input
                  id="won"
                  type="text"
                  class="form-control"
                  :class="{ 'is-invalid': isInvalidRe }"
                  v-model="reSongAmount"
                  @input="handleSongInput"
                  @click="reSongAmount = ''"
                  :placeholder="$t('myAccount--input-placeholder')"
                />
                <span class="input-group-text bg-secondary text-white">
                  {{ customerunit }}
                </span>
              </div>
            </div>
            <div>
              <label class="d-flex align-items-center">
                <div class="icon-container me-2">
                  <img
                    :src="flagIcon(wonCoutryCode)"
                    alt="icon"
                    class="flag-icon-img"
                  />
                </div>
                <div class="input-label-text">WON-E</div>
              </label>
              <div class="input-group">
                <input
                  id="song"
                  type="text"
                  class="form-control"
                  :class="{ 'is-invalid': isInvalid }"
                  :value="formatNumber(reWonAmount)"
                  readonly
                />
                <span class="input-group-text bg-secondary text-white">
                  {{ wonUnit }}
                </span>
                <div v-if="isInvalid" class="invalid-feedback">
                  {{ $t("myAccount--error-overAmount") }}
                </div>
              </div>
              <small
                >{{ t("myAccount--wonE-currentExchangeRate") }}: 1
                {{ customerunit }} = {{ currentToKrw }}
                {{ wonUnit }}
              </small>
            </div>
            <div>
              {{ t("myAccount-input-expectedAmount") }}:
              <span
                class="balance-text"
                :class="{ 'text-red': reWonAmount !== '' }"
                >{{ processAfterWonBalance }}</span
              >{{ wonUnit }}
            </div>
            <button
              type="submit"
              class="btn btn-primary w-100 fs-4"
              @click="openModal"
              :disabled="!isValidAmount(reSongAmount)"
              variant="gradient"
            >
              {{ t("myAccount--songE-button-exchange") }}
            </button>
          </div>
        </div>
      </div>
    </div>
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

.swiper {
  width: 100%;
  padding-top: 50px;
  padding-bottom: 50px;
}

.swiper-slide {
  background-position: center;
  background-size: cover;
  width: auto;
  height: auto;
}

.swiper-slide img {
  display: block;
  width: 100%;
}
</style>
