<script setup>
import { useRouter } from 'vue-router';
import { onMounted, ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import iamportApi from '@/api/iamportApi';
import Accommodation from '@/views/reservation/AccommodationList.vue';

const router = useRouter();

const goToAccommodationDetail = (no) => {
  router.push({ name: 'Accommodation', params: { no } });
};

const accommodations = ref([
  { id: 1, name: '서울 호텔', description: '서울 중심부에 위치한 고급 호텔' },
  { id: 2, name: '부산 리조트', description: '해변가에 위치한 편안한 리조트' },
  { id: 3, name: '제주 펜션', description: '제주의 아름다운 자연 속 펜션' },
  { id: 4, name: '강원도 호텔', description: '서울 중심부에 위치한 고급 호텔' },
  { id: 5, name: '경주 펜션', description: '해변가에 위치한 편안한 리조트' },
  { id: 6, name: '제주 펜션', description: '제주의 아름다운 자연 속 펜션' },
]);

onMounted(() => {
  // jQuery 및 iamport 초기화
  const scriptJQuery = document.createElement('script');
  scriptJQuery.src = 'http://code.jquery.com/jquery-latest.min.js';
  document.head.appendChild(scriptJQuery);

  const scriptIamport = document.createElement('script');
  scriptIamport.src = 'https://cdn.iamport.kr/js/iamport.payment-1.2.0.js';
  document.head.appendChild(scriptIamport);

  scriptIamport.onload = () => {
    // Iamport 초기화
    window.IMP.init('imp74103424'); // 'iamport'를 가맹점 식별코드로 변경
  };
});

const destinations = [
  { no: 1, name: '서울' },
  { no: 2, name: '부산' },
  { no: 3, name: '제주' },
  { no: 4, name: '강원도' },
  { no: 5, name: '경주' },
];

const selectedDestination = ref('');
const showDestinations = ref(false);
const checkIn = ref('');
const checkOut = ref('');
const guests = ref(1);

const toggleDestinations = () => {
  showDestinations.value = !showDestinations.value;
};

const selectDestination = (destination) => {
  selectedDestination.value = destination.name;
  showDestinations.value = false;
};

const searchAccommodations = () => {
  console.log('검색 조건:', { destination: selectedDestination.value, checkIn: checkIn.value, checkOut: checkOut.value, guests: guests.value });
};


const requestPay = async () => {
  window.IMP.request_pay({
    pg: "kakaopay", // PG사 설정
    pay_method: "kakaopay", // 결제 방법
    merchant_uid: 'merchant_' + new Date().getTime(), // 주문 번호
    name: '결제테스트', // 상품 이름
    amount: 14000, // 결제 가격
    custom_data: {
      begin_date: '2024-10-04',
      end_date: '2024-10-04',
    },
    buyer_email: 'iamport@siot.do',
    buyer_name: '구매자', // 구매자 이름
    buyer_tel: '010-1234-5678', // 구매자 연락처
    // buyer_addr: '서울특별시 강남구 삼성동', // 구매자 주소
    // buyer_postcode: '123-456' // 구매자 우편번호
  }, function (response) {
    if (response.success) {
      console.log('결제 성공', response);
    } else {
      // 결제 실패 시 로직
      console.log('결제 실패', response);
    }
  });
}
</script>

<template>
  <div class="container-fluid">
    <div id="accommodation-search" class="d-grid gap-5">
      <h3 class="mb-0">숙소 검색</h3>
      <div class="row">
        <div class="col-md-12">
          <div class="card">
            <div class="card-body">
              <form @submit.prevent="searchAccommodations" class="row g-3">
                <div class="col-md-3">
                  <label for="destination" class="form-label">여행지</label>
                  <div class="position-relative">
                    <input type="text" class="res-btn destination-input form-control" id="destination"
                      v-model="selectedDestination" @click="toggleDestinations" readonly placeholder="여행지 선택">
                    <div v-if="showDestinations"
                      class="position-absolute top-100 start-0 w-100 mt-1 bg-white border rounded shadow-sm destination-dropdown">
                      <button v-for="destination in destinations" :key="destination.id"
                        @click="selectDestination(destination)" class="btn btn-link text-start d-block w-100"
                        type="button">
                        {{ destination.name }}
                      </button>
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <label for="checkIn" class="form-label">체크인</label>
                  <input type="date" class="res-btn form-control" id="checkIn" v-model="checkIn">
                </div>
                <div class="col-md-3">
                  <label for="checkOut" class="form-label">체크아웃</label>
                  <input type="date" class="res-btn form-control" id="checkOut" v-model="checkOut">
                </div>
                <div class="col-md-2">
                  <label for="guests" class="form-label">인원수</label>
                  <input type="number" class="res-btn form-control" id="guests" v-model="guests" min="1">
                </div>
                <div class="col-md-1 d-flex align-items-end">
                  <button type="submit" class="btn btn-primary">검색</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row mt-5">
      <div class="col-md-12">
        <div class="card">
          <div class="card-body">
            <div class="row">
              <div v-for="accommodation in accommodations" :key="accommodation.id" class="col-md-4 mb-3">
                <Accommodation
                  :no="accommodation.no"
                  :name="accommodation.name"
                  :description="accommodation.description"
                  @click="goToAccommodationDetail(accommodation.id)" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- <button @click="requestPay">결제하기</button> -->
</template>

<style scoped>
.res-btn {
  background-color: #ffffff !important;
  cursor: pointer;
}

.btn-primary {
  margin-bottom: 0px !important;
  padding: 0.625rem 0.75rem;
}

.destination-input:hover {
  background-color: #f8f9fa !important;
}

.btn-link {
  text-decoration: none;
  color: #333;
}

.btn-link:hover {
  background-color: #f8f9fa;
}

.destination-dropdown {
  z-index: 1000;
}
</style>

