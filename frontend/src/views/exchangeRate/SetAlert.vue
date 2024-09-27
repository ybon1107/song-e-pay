<template>
  <div class="container">
    <!-- 환율 알림 받기 섹션 -->
    <div class="box">
      <h2>환율 알림 받기</h2>
      <div class="exchange-input">
        <img src="https://upload.wikimedia.org/wikipedia/commons/a/a4/Flag_of_the_United_States.svg" alt="미국 국기" />
        <span>1 USD</span>
        <span>=</span>
        <input type="number" v-model="alertRate" placeholder="목표 환율을 입력하세요." />
        <span>KRW</span>
        <img src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg" alt="한국 국기" />
      </div>
      <button @click="confirmAlert">확인</button>
      <br />
      <br />
      <hr />
      <br />
      <div class="exchange-input">
        <img src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg" alt="한국 국기" />
        <span>1 KRW</span>
        <span>=</span>
        <input type="number" v-model="alertRate2" placeholder="목표 환율을 입력하세요." />
        <span>USD</span>
        <img src="https://upload.wikimedia.org/wikipedia/commons/a/a4/Flag_of_the_United_States.svg" alt="미국 국기" />
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
      <div class="exchange-input" style="margin-top: 20px">
        <img src="https://upload.wikimedia.org/wikipedia/commons/a/a4/Flag_of_the_United_States.svg" alt="미국 국기" />
        <span>1 USD</span>
        <span>=</span>
        <input
          type="number"
          v-model="targetExchange"
          placeholder="목표 환율을 입력하세요."
        />
        <span>KRW</span>
        <img src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg" alt="한국 국기" />
      </div>

      <!-- 자동 환전 금액 입력 -->
      <div class="exchange-input">
        <div class="targetbox">
          목표 환율: <br />
          {{ targetExchange }} KRW 이하
        </div>
        <input
          type="number"
          v-model="targetKrw"
          placeholder="자동 전환할 금액을 입력하세요."
        />
        <span>KRW</span>
        <img src="https://upload.wikimedia.org/wikipedia/commons/0/09/Flag_of_South_Korea.svg" alt="한국 국기" />
      </div>
      <button @click="confirmAutoExchange(1, 0, targetExchange, targetKrw)">
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
import { useRouter } from "vue-router";

const router = useRouter();

const store = useExchangeStore();

// Pinia에서 currentToKrw 값을 가져와 currentRate로 사용
const currentRate = ref(store.currentToKrw);

// 상태 변수들을 ref로 선언
const targetExchange = ref(null); // 자동 환전 목표 환율
const targetKrw = ref(null); // 목표 금액

// 자동 환전 저장 함수
const confirmAutoExchange = async (
  baseCode,
  targetCode,
  targetExchange,
  targetKrw
) => {
  try {
    // const token = localStorage.getItem("jwt_token"); // JWT 토큰 가져오기
    // userNo를 추가(임시)
    const userNo = 1;
    console.log(userNo, baseCode, targetCode, targetExchange, targetKrw);
    // 서버에 POST 요청 보내기
    const response = await axios.post(
      "/api/exchange-reservation/setalert",
      {
        userNo: userNo,
        baseCode: baseCode,
        targetCode: targetCode,
        targetExchange: targetExchange,
        targetKrw: targetKrw,
      }
      // {
      //   headers: {
      //     Authorization: `Bearer ${token}`, // 인증 헤더에 토큰 추가
      //   },
      // }
    );

    if (response.status === 200) {
      alert("자동 환전 예약이 성공적으로 저장되었습니다.");
      router.push("/exchange-rate");
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
