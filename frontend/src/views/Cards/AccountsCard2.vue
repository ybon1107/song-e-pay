<template>
    <div class="card wallet-card" :style="{ backgroundImage: `url(${backgroundImage})` }">
        <div class="card-body py-6">
            <h5>{{ title }}</h5>
            <div class="wallet-balance">
                <!-- 카드 안에 표시할 이미지 -->
                <div class="icon-container">
                    <img :src="icon" alt="icon" class="icon-image">
                </div>
                <h3 class="m-0">{{ balance }} {{ currency }}</h3>
            </div>
        </div>
    </div>

</template>

<script setup>
import { defineProps } from 'vue';

const props = defineProps({
    title: {
        type: String,
        default: ''
    },
    balance: {
        type: Number,
        default: 0
    },
    currency: {
        type: String,
        default: ''
    },
    backgroundImage: {
        type: String,
        default: ''
    },
    icon: {
        type: String,
        default: ''
    }
})
</script>

<style scoped>
/* ... 기존 스타일 ... */

.wallet-card {
    background-size: cover;
    background-position: center;
    /* 텍스트 색상을 흰색으로 변경 */
}

.wallet-balance {
  display: flex;
  align-items: center;
}

.icon-container {
    width: 3vw;
    /* 뷰포트 너비의 3% */
    height: 3vw;
    /* 뷰포트 너비의 3% */
    min-width: 20px;
    /* 최소 크기 설정 */
    min-height: 20px;
    /* 최소 크기 설정 */
    max-width: 40px;
    /* 최대 크기 설정 */
    max-height: 40px;
    /* 최대 크기 설정 */
    display: flex;
    align-items: center;
    justify-content: center;

    margin-right: 1rem;
}

.icon-image {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
}

@media (max-width: 768px) {
    .icon-container {
        width: 5vw;
        /* 작은 화면에서는 더 큰 비율 적용 */
        height: 5vw;
    }
}
</style>