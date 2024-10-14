<template>
    <div v-if="user">
        <!-- <div class="card">
            <div class="card-background"></div>
            <div class="card-content">
                <h3>Card Title</h3>
                <p>This is some content on the card.</p>
            </div>
        </div> -->
        <div class="card card-img-bg" :id="props.assetType === 'song-e' ? 'card-songe' : 'card-wone'"
            :style="{ backgroundImage: `url(${backgroundImage})` }">
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
import { defineProps, ref, computed, watchEffect } from 'vue';
import myaccountApi from '../../api/myaccountApi';
import { CURRENCY_NAME, COUNTRY_CODE } from '@/constants/countryCode';

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
            if (fetchedBalance === '') {
                throw new Error('유효하지 않은 song-e계좌');
            }
            balance.value = formatNumber(fetchedBalance);
        } catch (error) {
            console.error('Error fetching song-e balance:', error);
        }
    } else {
        try {
            const fetchedBalance = await myaccountApi.fetchkrwAccountBalance(
                user.value.krwNo
            );
            if (fetchedBalance === '') {
                throw new Error('유효하지 않은 won-e계좌');
            }
            balance.value = formatNumber(fetchedBalance);
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

// fetchBalance 함수를 외부에서 접근 가능
defineExpose({ fetchBalance });
</script>

<style scoped>
.card {
    width: 100%;
    /* 카드의 너비 */
    max-width: 500px;
    /* 원하는 최대 너비 */
    height: auto;
    /* 카드의 높이를 자동으로 조절 */
    aspect-ratio: 407 / 271;
    /* 카드 비율을 이미지의 원래 비율에 맞게 유지 */
    position: relative;
    overflow: hidden;
    border: 1px solid #ccc;
    border-radius: 10px;
}

.card-background {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: url('/images/won-e-money.png');
    background-size: contain;
    /* 이미지 비율을 유지하면서 카드에 맞춤 */
    background-position: center;
    /* 이미지가 카드의 중앙에 배치되도록 설정 */
    background-repeat: no-repeat;
    /* 빈 공간을 반복하지 않도록 설정 */
    z-index: 1;
}

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

.card {
    position: relative;
    overflow: hidden;
    text-align: center;
    /* 이미지를 가운데로 정렬하기 위해 필요할 수 있음 */
}

.card-img {
    max-width: 100%;
    max-height: 100%;
    width: auto;
    /* 이미지가 비율을 유지하면서 조정되도록 설정 */
    height: auto;
}
</style>
