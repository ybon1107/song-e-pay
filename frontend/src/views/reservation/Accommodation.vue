<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import accommodationApi from '../../api/accommodationApi';

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

const maxCapacity = computed(() => accommodation.value?.capacity || 1);

watch(guestCount, (newCount) => {
    if (newCount > maxCapacity.value) {
        guestCount.value = maxCapacity.value;
    }
});

onMounted(async () => {
    const no = parseInt(route.params.no);
    // API 호출 로직...
    accommodation.value = await accommodationApi.getAccommodation(no);

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
    return totalNights.value * accommodation.value.pricePerNight || 0;
});

const handleReservation = () => {
    const reservationData = {
        accommodationName: accommodation.value.name,
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
            <img :src="accommodation.img" :alt="accommodation.name" class="img-fluid">
        </div>
        <div class="row mt-4">
            <div class="col-md-8">
                <h2>{{ accommodation.name }}</h2>
                <p>{{ accommodation.description }}</p>
                <h3>Amenities</h3>
                <ul>
                    <li v-for="amenity in accommodation.amenities" :key="amenity.amenitiesNo">{{ amenity.amenitiesName
                        }}</li>
                </ul>
            </div>
            <div class="col-md-4">
                <div class="reservation-form">
                    <h3>Reservation</h3>
                    <p>₩{{ accommodation.pricePerNight.toLocaleString() }} /night</p>
                    <div class="form-group">
                        <label for="checkIn">Check-in Date</label>
                        <div class="date-input-wrapper" @click="$refs.checkIn.showPicker()">
                            <input type="date" id="checkIn" v-model="checkInDate" class="form-control"
                                :min="formatDate()" ref="checkIn">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="checkOut">Check-out Date</label>
                        <div class="date-input-wrapper" @click="$refs.checkOut.showPicker()">
                            <input type="date" id="checkOut" v-model="checkOutDate" class="form-control"
                                :min="checkInDate" ref="checkOut">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="guestCount">{{ accommodation.capacity }} guests</label>
                            <input type="number" id="guestCount" v-model="guestCount" min="1" :max="maxCapacity"
                            class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="address">Address</label>
                        <p>{{ accommodation.address }}</p>
                    </div>
                    <button @click="handleReservation" class="btn btn-primary mt-3">Reserve</button>
                    <hr />
                    <p v-if="totalNights > 0" class="mt-3">
                        Total {{ totalNights }} nights: ₩{{ totalPrice.toLocaleString() }}
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
    border-radius: 10px;
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
