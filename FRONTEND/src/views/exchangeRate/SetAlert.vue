<template>
<div class="container">
    <!-- 환율 알림 받기 섹션 -->
    <div class="box">
    <h2>환율 알림 받기</h2>
    <div class="exchange-input">
        <img
        src="https://upload.wikimedia.org/wikipedia/commons/a/a4/Flag_of_the_United_States.svg"
        alt="미국 국기"
        />
        <span>1 USD =</span>
        <input type="text" v-model="alertRate" placeholder="목표 환율을 입력하세요."/>
        <span>KRW</span>
        <img
        src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg"
        alt="한국 국기"
        />
    </div>
    <button @click="confirmAlert">확인</button>
    </div>

    <!-- 자동 환전 설정 섹션 -->
    <div class="box chart-box">
    <h2>자동 환전 설정</h2>
    <h3>1 USD = {{ currentRate }} KRW</h3>
    <!-- 그래프 공간 -->
    <div class="chart">그래프 자리 (Placeholder)</div>

    <!-- 목표 환율 입력 -->
    <div class="exchange-input" style="margin-top: 20px;">
        <img
        src="https://upload.wikimedia.org/wikipedia/commons/a/a4/Flag_of_the_United_States.svg"
        alt="미국 국기"
        />
        <span>1 USD =</span>
        <input type="text" v-model="targetRate" placeholder="목표 환율을 입력하세요." />
        <span>KRW</span>
        <img
        src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg"
        alt="한국 국기"
        />
    </div>

    <!-- 자동 환전 금액 입력 -->
    <div class="exchange-input">
        목표 환율: <br />
        {{ targetAmount }} KRW 이하 
        <input type="text" v-model="targetAmount" placeholder="자동 전환할 금액을 입력하세요." />
        <span>KRW</span>
        <img
        src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg"
        alt="한국 국기"
        />
    </div>
    <button @click="confirmAutoExchange">확인</button>
    </div>
</div>
</template>

<script>
export default {
data() {
    return {
    alertRate: '', // 환율 알림 목표 값
    currentRate: 1339.50, // 현재 환율 (임시 값)
    targetRate: '', // 자동 환전 목표 환율
    targetAmount: '', // 목표 금액
    };
},
methods: {
    confirmAlert() {
    alert(`설정된 알림 목표 환율: 1 USD = ${this.alertRate} KRW`);
    },
    confirmAutoExchange() {
    alert(
        `목표 환율: 1 USD = ${this.targetRate} KRW \n목표 환율: ${this.targetAmount} KRW 이하`
    );
    },
},
};
</script>

<style scoped>
body {
font-family: Arial, sans-serif;
background-color: #f7f7f7;
}
.container {
max-width: 900px;
margin: 0 auto;
padding: 20px;
}
.box {
background-color: white;
border: 1px solid #ddd;
border-radius: 10px;
padding: 20px;
margin-bottom: 20px;
}
h2 {
font-size: 18px;
margin-bottom: 15px;
}
.exchange-input {
display: flex;
align-items: center;
justify-content: space-between;
margin-bottom: 10px;
}
.exchange-input span {
margin-right: 10px; /* span 태그 오른쪽에 여유 공간 추가 */
}
.exchange-input img {
width: 30px;
margin-left: 5px;
margin-right: 10px;
}
.exchange-input input {
width: 70%;
padding: 5px 10px;
font-size: 16px;
border: 1px solid #ddd;
border-radius: 5px;
}
button {
background-color: #ffeb3b;
border: none;
padding: 8px 0;
font-size: 14px;
width: 100%;
cursor: pointer;
border-radius: 5px;
}
button:hover {
background-color: #fdd835;
}
.chart-box {
margin-top: 20px;
}
.chart-box h3 {
font-size: 16px;
margin-bottom: 10px;
}
.chart {
width: 100%;
height: 200px;
background-color: #efefef;
border-radius: 10px;
display: flex;
justify-content: center;
align-items: center;
color: #aaa;
}
</style>
