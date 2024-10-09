<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const reservationData = ref(null);

onMounted(() => {
  reservationData.value = {
    accommodationId: Number(route.query.accommodationId),
    checkInDate: route.query.checkInDate,
    checkOutDate: route.query.checkOutDate,
    guestCount: Number(route.query.guestCount),
    totalPrice: Number(route.query.totalPrice)
  };
});

const formatDate = (dateString) => {
  const options = { year: 'numeric', month: 'long', day: 'numeric' };
  return new Date(dateString).toLocaleDateString('ko-KR', options);
};

const formatPrice = (price) => {
  return new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW' }).format(price);
};
</script>

<template>
  <div class="container mt-5">
    <h1 class="mb-4">결제 정보</h1>
    <div v-if="reservationData" class="card">
      <div class="card-body">
        <h5 class="card-title">예약 상세 정보</h5>
        <ul class="list-group list-group-flush">
          <li class="list-group-item">
            <strong>숙소 ID:</strong> {{ reservationData.accommodationId }}
          </li>
          <li class="list-group-item">
            <strong>체크인 날짜:</strong> {{ formatDate(reservationData.checkInDate) }}
          </li>
          <li class="list-group-item">
            <strong>체크아웃 날짜:</strong> {{ formatDate(reservationData.checkOutDate) }}
          </li>
          <li class="list-group-item">
            <strong>인원 수:</strong> {{ reservationData.guestCount }}명
          </li>
          <li class="list-group-item">
            <strong>총 가격:</strong> {{ formatPrice(reservationData.totalPrice) }}
          </li>
        </ul>
      </div>
    </div>
    <div v-else class="alert alert-danger mt-3">
      예약 정보를 불러오는 데 실패했습니다.
    </div>
    <div class="card-footer bg-transparent border-0">
      <button class="btn btn-primary mt-3" @click="requestPay">결제하기</button>
    </div>
  </div>
</template>

<style scoped>
.card, .card-footer {
  max-width: 500px;
  margin: 0 auto;
}

.card-footer {
  padding: 0;
}

.btn {
  width: 100%;
}
</style>