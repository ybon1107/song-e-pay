<template>
  <div class="error-container">
    <div class="image-section">
      <img src="/images/error_songe3.png" alt="Error Illustration" />
    </div>
    <div class="text-section">
      <h2>
        죄송합니다.<br />
        요청하신 페이지를 찾을 수 없습니다.<br />
      </h2>
      <p>
        방문하려는 페이지의 주소가 잘못 입력되었거나, 삭제되어 찾을 수
        없습니다.<br />
        입력하신 주소가 정확한지 다시 한 번 확인해 주시기 바랍니다.
      </p>
      <div class="button-container">
        <button @click="goBack">이전 페이지</button>
        <button @click="goHome">Songe 홈</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    goBack() {
      this.$router.go(-1); // 이전 페이지로 돌아가기
    },
    goHome() {
      this.$router.push('/'); // 홈페이지로 이동
    },
  },
};
</script>

<style scoped>
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
  /* background-color: #f5f5f5; */
  padding: 20px;
}

.text-section {
  margin-bottom: 20px;
}

h1 {
  color: #333;
}

p {
  color: #666;
}

.button-container {
  margin-top: 20px;
}

button {
  padding: 12px 24px;
  margin: 10px;
  background-color: #ffc107;
  color: white;
  border: none;
  border-radius: 30px; /* 둥근 버튼 */
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #ffd65a;
}

.image-section img {
  max-width: 50%;
  height: auto;
  border-radius: 10px;
}

@media (min-width: 768px) {
  .error-container {
    flex-direction: row;
    justify-content: space-between;
  }

  .text-section {
    width: 50%;
    text-align: left;
  }

  .image-section {
    width: 50%;
  }
}
</style>
