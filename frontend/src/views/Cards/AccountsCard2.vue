<template>
    <div class="card krw-wallet-card p-3">
        <div class="card-body">
            <h6>Song-E Money</h6>
            <div class="wallet-balance">
                <!-- 카드 안에 표시할 이미지 -->
                <div v-if="img" class="position-relative p-3 img-container">
                    <img :src="img" alt="Card Image" class="img-fluid rounded" />
                </div>
                <h3>0 USD</h3>
            </div>
        </div>
    </div>

    <!-- ... 기존 코드 ... -->


</template>

<style scoped>
/* ... 기존 스타일 ... */

.krw-wallet-card {
    background-image: url('/images/song-e-money.png');
    background-size: cover;
    background-position: center;
    color: white;
    /* 텍스트 색상을 흰색으로 변경 */
}

/* 
.krw-wallet-card .btn {
    background-color: rgba(255, 255, 255, 0.2);
    border: 1px solid white;
} */
</style>