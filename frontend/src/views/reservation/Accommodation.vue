<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const accommodation = ref(null);
const checkInDate = ref('');
const checkOutDate = ref('');
const guestCount = ref(1);

const formatDate = (daysToAdd = 0) => {
  const date = new Date();
  date.setDate(date.getDate() + daysToAdd);
  return date.toISOString().split('T')[0];
};

onMounted(async () => {
    const id = parseInt(route.params.id);
    // API 호출 로직...
    accommodation.value = {
        id: 1,
        name: '해변 별장',
        description: '아름다운 해변가의 별장입니다.',
        amenities: ['와이파이', '에어컨', '주방'],
        price: 150000,
        imageUrl: 'https://img.freepik.com/premium-photo/modern-villa-sun-beach_674594-29706.jpg',
    };

    // 체크인 날짜를 오늘 날짜로 설정
    checkInDate.value = formatDate();
    // 체크아웃 날짜를 다음 날로 설정
    checkOutDate.value = formatDate(1);
});

const totalNights = computed(() => {
    if (checkInDate.value && checkOutDate.value) {
        const start = new Date(checkInDate.value);
        const end = new Date(checkOutDate.value);
        const diffTime = Math.abs(end - start);
        return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    }
    return 0;
});

const totalPrice = computed(() => {
    return totalNights.value * accommodation.value?.price || 0;
});

const handleReservation = () => {
    const reservationData = {
        accommodationId: accommodation.value.id,
        checkInDate: checkInDate.value,
        checkOutDate: checkOutDate.value,
        guestCount: guestCount.value,
        totalPrice: totalPrice.value
    };
    console.log(reservationData);

    // params를 통해 예약 데이터를 직접 전달합니다.
    router.push({
        name: 'ReservationPayment',
        query: reservationData
    });
};
</script>

<template>
    <div class="container-fluid" v-if="accommodation">
        <div class="accommodation-image">
            <img :src="accommodation.imageUrl" :alt="accommodation.name" class="img-fluid">
        </div>
        <div class="row mt-4">
            <div class="col-md-8">
                <h2>{{ accommodation.name }}</h2>
                <p>{{ accommodation.description }}</p>
                <h3>편의 시설</h3>
                <ul>
                    <li v-for="amenity in accommodation.amenities" :key="amenity">{{ amenity }}</li>
                </ul>
            </div>
            <div class="col-md-4">
                <div class="reservation-form">
                    <h3>예약하기</h3>
                    <p>₩{{ accommodation.price.toLocaleString() }} /박</p>
                    <div class="form-group">
                        <label for="checkIn">체크인 날짜</label>
                        <div class="date-input-wrapper" @click="$refs.checkIn.showPicker()">
                            <input type="date" id="checkIn" v-model="checkInDate" class="form-control"
                                :min="formatDate()" ref="checkIn">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="checkOut">체크아웃 날짜</label>
                        <div class="date-input-wrapper" @click="$refs.checkOut.showPicker()">
                            <input type="date" id="checkOut" v-model="checkOutDate" class="form-control" 
                                :min="checkInDate" ref="checkOut">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="guestCount">인원 수</label>
                        <input type="number" id="guestCount" v-model="guestCount" min="1" class="form-control">
                    </div>
                    <button @click="handleReservation" class="btn btn-primary mt-3">예약하기</button>
                    <hr />
                    <p v-if="totalNights > 0" class="mt-3">
                        총 {{ totalNights }}박: {{ totalPrice.toLocaleString() }}원
                    </p>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.btn {
    width: 100%;
}

.accommodation-image {
    width: 100%;
    height: 400px;
    overflow: hidden;
}

.accommodation-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.reservation-form {
    border: 1px solid #ddd;
    padding: 20px;
    border-radius: 5px;
}

.date-input-wrapper {
    position: relative;
    cursor: pointer;
}

.date-input-wrapper input[type="date"] {
    width: 100%;
}

.date-input-wrapper input[type="date"]::-webkit-calendar-picker-indicator {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    width: auto;
    height: auto;
    color: transparent;
    background: transparent;
}
</style>