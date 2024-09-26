<template>
    <div class="card wallet-card" :style="{ backgroundImage: `url(${backgroundImage})` }" @click="navigateTo"
        @mouseover="isFocused = true" @mouseleave="isFocused = false" :class="{ 'focused': isFocused, 'selected': isSelected }">
        <div class="card-header bg-transparent">
            <h5>{{ title }}</h5>
        </div>
        <div class="card-body">
            <div class="d-flex align-items-center justify-content-end">
                <!-- 카드 안에 표시할 이미지 -->
                <div class="icon-container">
                    <img :src="icon" alt="icon" class="icon-image">
                </div>
                <h3 class="m-0">{{ formattedBalance }}</h3>
            </div>
        </div>
    </div>

</template>

<script setup>
import { defineProps, ref, computed, onMounted } from 'vue';
import myaccountApi from '../../api/myaccountApi';

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
    currency: {
        type: String,
        default: ''
    },
    // backgroundImage: {
    //     type: String,
    //     default: ''
    // },
    // icon: {
    //     type: String,
    //     default: ''
    // },
    isSelected: {
        type: Boolean,
        default: false
    }
});

//추후에 i18n키값 구분 로직으로 변경 
const title = computed(() => props.assetType === 'song-e' ? 'Song-E Money' : 'Won-E Money');
const backgroundImage = computed(() => `/images/${props.assetType}-money.png`);
const icon = computed(() => `/images/${props.currency}.png`);



const songEMoneyBalance = ref(0); // Song-E Money의 잔액
const wonEMoneyBalance = ref(0); // Won-E Money의 잔액


// const fetchBalances = () => {
//   myaccountApi.fetchkrwAccountBalance('1234').then((balance) => {
//     wonEMoneyBalance.value = balance;
//   });

//   myaccountApi.fetchsongeAccountBalance('1234').then((balance) => {
//     songEMoneyBalance.value = balance;
//   });
// };

const fetchBalance = async () => {
  try {
    if (props.assetType === 'song-e') {
      const result = await myaccountApi.fetchsongeAccountBalance('1234');
      balance.value = result;
    } else {
      const result = await myaccountApi.fetchkrwAccountBalance('1234');
      balance.value = result;
    }
  } catch (error) {
    console.error('잔액을 가져오는 중 오류 발생:', error);
    // 오류 처리 로직 추가 (예: 사용자에게 오류 메시지 표시)
  }
};


onMounted(() => {
    fetchBalance();
});

const formatNumber = (num) => {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const formattedBalance = computed(() => {
    const formattedNumber = formatNumber(balance.value.toFixed(2));
    return `${props.currency} ${formattedNumber}`;
});

</script>

<style scoped>
.wallet-card {
    background-size: cover;
    background-position: center;
    /* 텍스트 색상을 흰색으로 변경 */
}

.wallet-card:hover {
    cursor: pointer;
}

.wallet-card.focused {
    transform: scale(1.05);
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}

.wallet-card.selected {
    border: 3px solid #2cce89;
    /* 선택된 카드에 녹색 테두리 추가 */
    box-shadow: 0 0 15px rgba(44, 206, 137, 0.5);
    /* 선택된 카드에 그림자 효과 추가 */
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