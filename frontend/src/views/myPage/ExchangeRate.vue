<script setup>
import { ref, computed, watchEffect } from 'vue';
import { COUNTRY_CODE, COUNTRY_KEY, CURRENCY_NAME } from '@/constants/countryCode';
import ExchangeRateChart from '@/views/Chart/ExchangeRateChart.vue';
import { useExchangeStore } from '@/stores/exchangeStore';
const store = useExchangeStore();
//user
import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const user = computed(() => auth.user);

const songCountryCode = ref(null);
const customerunit = ref(null);
const customerCountry = ref(null);

const wonCoutryCode = COUNTRY_CODE.KR;
const wonUnit = CURRENCY_NAME[wonCoutryCode];

const usdAmount = ref(1);
const krwAmount = ref(0);
const krwAmountReverse = ref(1000);
const usdAmountReverse = ref(0);

const currentToKrw = computed(() => store.currentToKrw);
const currentFromKrw = computed(() => store.currentFromKrw);

const convertToKrw = () => {
    krwAmount.value = (usdAmount.value * currentToKrw.value).toFixed(5);
};

const convertToUsd = () => {
    usdAmountReverse.value = (krwAmountReverse.value * currentFromKrw.value).toFixed(5);
};


const fetchExchangeRates = async () => {
    try {
        convertToKrw();
        convertToUsd();
    } catch (error) {
        console.error('환율 데이터를 가져오는 중 오류 발생:', error);
    }
};

watchEffect(() => {
    if (user.value) {
        songCountryCode.value = user.value.countryCode;
        customerunit.value = CURRENCY_NAME[songCountryCode.value];
        customerCountry.value = COUNTRY_KEY[songCountryCode.value];
        fetchExchangeRates();
    }
});

</script>

<template>
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
                            {{ customerunit }} <i class="fa-solid fa-arrow-right"></i>
                            {{ wonUnit }}
                        </h6>
                        <div class="d-flex align-items-center">
                            <div class="position-relative flex-grow-1">
                                <input type="number" class="form-control" v-model.number="usdAmount"
                                    @input="convertToKrw" aria-label="Amount in USD" />
                                <img src="@/assets/img/icons/flags/US.png" alt="USA Flag" class="input-flag-icon" />
                            </div>
                            <span class="mx-3">=</span>
                            <div class="position-relative flex-grow-1">
                                <input type="text" class="form-control" :value="krwAmount" readonly
                                    aria-label="Amount in KRW" />
                                <img src="@/assets/img/icons/flags/KR.png" alt="KRW Flag" class="input-flag-icon" />
                            </div>
                        </div>
                    </div>
                    <div>
                        <h6>
                            {{ wonUnit }} <i class="fa-solid fa-arrow-right"></i>
                            {{ customerunit }}
                        </h6>
                        <div class="d-flex align-items-center">
                            <div class="position-relative flex-grow-1">
                                <input type="number" class="form-control" v-model.number="krwAmountReverse"
                                    @input="convertToUsd" />
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
</template>ㄴ