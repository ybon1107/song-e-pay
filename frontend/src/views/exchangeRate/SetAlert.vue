<template>
  <div class="container">
    <!-- 자동 환전 설정 섹션 -->
    <div class="box chart-box">
      <h2>자동 환전 설정</h2>
      <h3>1 USD = {{ currentRate }} KRW</h3>

      <!-- 그래프 공간 -->
      <div class="chart-container">
        <ExchangeRateChart
          chartId="toexchangeChart"
          chartType="to"
          period="1m"
        />
      </div>

      <!-- 목표 환율 입력 -->
      <div class="exchange-input" style="margin-top: 20px">
        <img
          src="https://upload.wikimedia.org/wikipedia/commons/a/a4/Flag_of_the_United_States.svg"
          alt="미국 국기"
        />
        <span>1 USD</span>
        <span>=</span>
        <input
          type="number"
          v-model="targetExchange"
          placeholder="목표 환율을 입력하세요."
        />
        <span>KRW</span>
        <img
          src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg"
          alt="한국 국기"
        />
      </div>

      <!-- 자동 환전 금액 입력 -->
      <div class="exchange-input">
        <div class="targetbox">
          목표 환율: <br />
          {{ targetRate }} KRW 이하
        </div>
        <input
          type="number"
          v-model="targetAmount"
          placeholder="자동 전환할 금액을 입력하세요."
        />
        <span>KRW</span>
        <img
          src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg"
          alt="한국 국기"
        />
      </div>
      <button @click="confirmAutoExchange(1, 0, targetExchange, targetAmount)">
        확인
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useExchangeStore } from "../../stores/exchangeStore";
import ExchangeRateChart from "@/views/Chart/ExchangeRateChart.vue";
import axios from "axios";

const store = useExchangeStore();

// Pinia에서 currentToKrw 값을 가져와 currentRate로 사용
const currentRate = ref(store.currentToKrw);

// 상태 변수들을 ref로 선언
const targetRate = ref(null); // 자동 환전 목표 환율
const targetAmount = ref(null); // 목표 금액

// 자동 환전 저장 함수
const confirmAutoExchange = async (
  baseCode,
  targetCode,
  targetExchange,
  targetAmount
) => {
  try {
    // const token = localStorage.getItem("jwt_token"); // JWT 토큰 가져오기
    // userNo를 추가(임시)
    const userNo = 1;
    console.log(userNo, baseCode, targetCode, targetExchange, targetAmount);
    // 서버에 POST 요청 보내기
    const response = await axios.post(
      "/api/exchange-reservation/setalert",
      {
        userNo: userNo,
        baseCode: baseCode,
        targetCode: targetCode,
        targetExchange: targetExchange,
        targetAmount: targetAmount,
      }
      // {
      //   headers: {
      //     Authorization: `Bearer ${token}`, // 인증 헤더에 토큰 추가
      //   },
      // }
    );

    if (response.status === 200) {
      alert("자동 환전 예약이 성공적으로 저장되었습니다.");
    }
  } catch (error) {
    console.error("자동 환전 예약 중 오류 발생:", error);
    alert("자동 환전 예약에 실패했습니다. 오류: " + error.response.data);
  }
};
</script>

<style scoped>
body {
  font-family: Arial, sans-serif;
  background-color: #f7f7f7;
}
.container {
  max-width: 80%;
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
h3 {
  font-size: 16px;
  margin-bottom: 10px;
  color: #333;
}
.chart-container {
  width: 100%;
  width: 100%; /* 그래프 높이 늘리기 */
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2%;
  margin-bottom: 2%; /* 그래프와 상단 요소 간격 */
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
.targetbox {
  font-size: 14px;
  color: #666;
}
</style>
