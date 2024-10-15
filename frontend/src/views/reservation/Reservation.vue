<script setup>
import { useRouter } from 'vue-router';
import { onMounted, ref, computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
import Accommodation from '@/views/reservation/AccommodationList.vue';
import accommodationApi from '../../api/accommodationApi';

const router = useRouter();

const goToAccommodationDetail = (no) => {
  router.push({ name: 'Accommodation', params: { no } });
};

const accommodations = ref([]);

const destinations = [
  { no: 1, name: '서울', ename: 'Seoul' },
  { no: 2, name: '부산', ename: 'Busan' },
  { no: 3, name: '제주', ename: 'Jeju' },
  { no: 4, name: '강원도', ename: 'Gangwon' },
  { no: 5, name: '경주', ename: 'Gyeongju' },
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
  selectedDestination.value = destination.ename;
  showDestinations.value = false;
};

const searchAccommodations = () => {
  console.log('검색 조건:', { destination: selectedDestination.value, checkIn: checkIn.value, checkOut: checkOut.value, guests: guests.value });
};

const fetchAccommodations = async () => {
  accommodations.value = await accommodationApi.getAll(); // getAll 메서드 호출
};

const filteredAccommodations = computed(() => {
  if (!selectedDestination.value) {
    return accommodations.value;
  }
  return accommodations.value.filter(accommodation => {
    const match = accommodation.address.match(/,\s*([^,]+)$/);
    const destinationName = match ? match[1].trim() : '';
    return destinationName === selectedDestination.value;
  });
});

onMounted(() => {
  fetchAccommodations(); // 컴포넌트가 마운트될 때 호출
});

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
                        {{ destination.ename }}
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
              <div v-for="accommodation in filteredAccommodations" :key="accommodation.accommodationNo" class="col-md-4 mb-3">
                <Accommodation
                  :no="accommodation.accommodationNo"
                  :name="accommodation.name"
                  :description="accommodation.summary"
                  :thumbnail="accommodation.thumbnail"
                  @click="goToAccommodationDetail(accommodation.accommodationNo)" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
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

