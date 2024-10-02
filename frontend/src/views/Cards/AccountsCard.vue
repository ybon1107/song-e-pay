<template>
    <div v-if="user">
        <div class="card custom-card" :style="{ backgroundImage: `url(${backgroundImage})` }">
            <div class="card-body d-flex align-items-end justify-content-end">
                <div class="d-flex align-items-center">
                    <div class="icon-container me-2">
                        <img :src=flagIcon alt="icon" class="icon-image">
                    </div>
                    <p class="fs-4 mb-0 me-2">{{ balance }}</p>
                    <p class="fs-6 mb-0">{{ displayCountry }}</p>
                </div>
            </div>
        </div>
    </div>
    <div v-else>
        <div class="card custom-card" :style="{ bacgroundColor: gray }">
        </div>
    </div>
</template>

<script setup>
import { defineProps, ref, computed, onMounted } from 'vue';
import myaccountApi from '../../api/myaccountApi';
import { INTL_LOCALE, CURRENCY_NAMES } from "@/constants/countryCode";

import currencyFormatter from '../../js/currencyFormatter';
const { formatNumber, formatCurrency } = currencyFormatter;

import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const user = computed(() => auth.user);

const isFocused = ref(false);
const balance = ref(0);

const props = defineProps({
    // title: {
    //     type: String,
    //     default: ''
    // },
    // balance: {
    //     type: Number,
    //     default: 0
    // },
    assetType: {
        type: String,
        required: true
    },
    // value: {
    //     type: String,
    //     default: "",
    // },
    // backgroundImage: {
    //     type: String,
    //     default: ''
    // },
    // icon: {
    //     type: String,
    //     default: ''
    // },
    // isSelected: {
    //     type: Boolean,
    //     default: false
    // }
});

//추후에 i18n키값 구분 로직으로 변경
const backgroundImage = computed(() => `/images/${props.assetType}-money.png`);
// const icon = computed(() => `/images/${props.currency}.png`);
const flagIcon = computed(() => {
    const iconName = props.assetType === 'song-e' ? user.value.countryCode : '0';
    return `/images/flag_${iconName}.png`;
});
const displayCountry = computed(() => {
    return props.assetType === 'song-e' ? CURRENCY_NAMES[user.value.countryCode] : CURRENCY_NAMES[0];
});



const fetchBalance = async () => {
    if (props.assetType === 'song-e') {
        myaccountApi.fetchsongeAccountBalance(user.value.songNo).then((fetchedBalance) => {
            balance.value = formatNumber(fetchedBalance.toFixed(2));
            // balance.value = formatCurrency(fetchedBalance,INTL_LOCALE[user.value.countryCode],CURRENCY_NAMES[user.value.countryCode]);

        });
    } else {
        myaccountApi.fetchkrwAccountBalance(user.value.krwNo).then((fetchedBalance) => {
            balance.value = formatNumber(fetchedBalance.toFixed(2));
            // formatCurrency(fetchedBalance,INTL_LOCALE[0],CURRENCY_NAMES[0]);
        });
    }
};

onMounted(() => {
    fetchBalance();
});

// fetchBalance 함수를 외부에서 접근 가능하게 만듭니다.
defineExpose({ fetchBalance });

</script>

<style scoped>
.custom-card {
    width: 8.56cm;
    aspect-ratio: 1.585;
    /* 비율 8.56 / 5.398 */
    background-size: cover;
}

@media (max-width: 500px) {
    .card {
        width: 90%;
    }
}

.icon-container {
    /* 뷰포트 너비의 3% */
    width: 3vw;
    height: 3vw;
    /* 최소 크기 설정 */
    min-width: 20px;
    min-height: 20px;
    /* 최대 크기 설정 */
    max-width: 40px;
    max-height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.icon-image {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
}

@media (max-width: 768px) {
    .icon-container {
        /* 작은 화면에서는 더 큰 비율 적용 */
        width: 5vw;
        height: 5vw;
    }
}
</style>