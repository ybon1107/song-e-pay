<template>
    <div v-if="user">
        <div
            class="card card-img-bg"
            :id="props.assetType === 'song-e' ? 'card-songe' : 'card-wone'"
            :style="{ backgroundImage: `url(${backgroundImage})` }"
            :class="props.assetType === 'song-e' ? 'bg-primary' : 'bg-info'"
        >
            <div class="card-body d-flex align-items-end justify-content-end">
                <div class="d-flex align-items-center">
                    <div class="icon-container me-2">
                        <img :src="flagIcon" alt="icon" class="flag-icon-img" />
                    </div>
                    <p class="fs-4 mb-0 me-2">{{ balance }}</p>
                    <p class="fs-6 mb-0">{{ displayCountry }}</p>
                </div>
            </div>
        </div>
    </div>
    <div v-else>
        <div class="card card-img-bg" :style="{ backgroundColor: gray }"></div>
    </div>
</template>

<script setup>
import { defineProps, ref, computed,watchEffect } from 'vue';
import myaccountApi from '../../api/myaccountApi';
import { CURRENCY_NAME } from '@/constants/countryCode';

import currencyFormatter from '../../js/currencyFormatter';
const { formatNumber } = currencyFormatter;

import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const user = computed(() => auth.user);
const balance = ref(0);

const props = defineProps({
  assetType: {
    type: String,
    required: true,
  },
});

//추후에 i18n키값 구분 로직으로 변경
const backgroundImage = computed(() => `/images/${props.assetType}-money.png`);

const flagIcon = computed(() => {
    const iconName =
        props.assetType === 'song-e' ? user.value?.countryCode : '0';
    return `/images/flag_c${iconName}.png`;
});
const displayCountry = computed(() => {
    return props.assetType === 'song-e'
        ? CURRENCY_NAME[user.value?.countryCode]
        : CURRENCY_NAME[0];
});

const fetchBalance = async () => {
    if (!user.value) return; // user 데이터가 없으면 함수 실행을 중단

    if (props.assetType === 'song-e') {
        try {
            const fetchedBalance = await myaccountApi.fetchsongeAccountBalance(
                user.value.songNo
            );
            balance.value = formatNumber(fetchedBalance.toFixed(2));
            // balance.value = formatCurrency(fetchedBalance, INTL_LOCALE[user.value.countryCode], CURRENCY_NAME[user.value.countryCode]);
        } catch (error) {
            console.error('Error fetching song-e balance:', error);
        }
    } else {
        try {
            const fetchedBalance = await myaccountApi.fetchkrwAccountBalance(
                user.value.krwNo
            );
            balance.value = formatNumber(fetchedBalance.toFixed(2));
            // formatCurrency(fetchedBalance, INTL_LOCALE[0], CURRENCY_NAME[0]);
        } catch (error) {
            console.error('Error fetching KRW balance:', error);
        }
    }
};

// watchEffect를 사용하여 user 데이터가 준비되면 fetchBalance를 호출
watchEffect(() => {
    if (user.value) {
        fetchBalance();
    }
});

// fetchBalance 함수를 외부에서 접근 가능하게 만듭니다.
defineExpose({ fetchBalance });
</script>

<style scoped>
/* acountscard가 선택됐을 때 */
.selected #card-songe {
    border: 3px solid #ffd166;
    box-shadow: 0 0 30px #ffd166;
    transform: scale(1.08);
    transition: all 0.3s ease;
}
.selected #card-wone {
    border: 3px solid #5c9ea6;
    box-shadow: 0 0 30px #5c9ea6;
    transform: scale(1.08);
    transition: all 0.3s ease;
}

@media (max-width: 768px) {
    .icon-container {
        /* 작은 화면에서는 더 큰 비율 적용 */
        width: 5vw;
        height: 5vw;
    }
}
</style>
