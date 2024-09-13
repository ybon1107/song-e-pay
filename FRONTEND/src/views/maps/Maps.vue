<template>
  <div class="map-container">
    <h1>금융 지도</h1>
    <h3>지역 선택:</h3>

    <!-- 시/도 선택 -->
    <label for="city">시/도:</label>
    <select v-model="selectedCity" @change="onCityChange">
      <option value="" disabled>시/도 선택</option>
      <option
        v-for="city in Object.keys(cityDistricts)"
        :key="city"
        :value="city"
      >
        {{ city }}
      </option>
    </select>

    <!-- 군/구 선택 -->
    <label for="district">군/구:</label>
    <select v-model="selectedDistrict" :disabled="!districts.length">
      <option value="" disabled>군/구 선택</option>
      <option v-for="district in districts" :key="district" :value="district">
        {{ district }}
      </option>
    </select>

    <!-- 검색 버튼 -->
    <button @click="onDistrictChange" :disabled="!selectedDistrict">
      검색
    </button>

    <!-- 지도 -->
    <div id="map" style="width: 600px; height: 400px"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const selectedCity = ref('');
const selectedDistrict = ref('');
const districts = ref([]);

const cityDistricts = {
  서울특별시: {
    강남구: { lat: 37.5172, lng: 127.0473 },
    서초구: { lat: 37.4832, lng: 127.0324 },
    송파구: { lat: 37.5146, lng: 127.1065 },
    강북구: { lat: 37.6396, lng: 127.0258 },
    종로구: { lat: 37.5731, lng: 126.9793 },
  },
  부산광역시: {
    해운대구: { lat: 35.1632, lng: 129.1636 },
    수영구: { lat: 35.1424, lng: 129.1146 },
    동래구: { lat: 35.2054, lng: 129.0836 },
    사하구: { lat: 35.1045, lng: 128.9712 },
    중구: { lat: 35.1067, lng: 129.0327 },
  },
  대구광역시: {
    중구: { lat: 35.8663, lng: 128.5941 },
    동구: { lat: 35.8867, lng: 128.6353 },
    서구: { lat: 35.8716, lng: 128.5593 },
    수성구: { lat: 35.8584, lng: 128.6306 },
    달서구: { lat: 35.8292, lng: 128.5324 },
  },
  인천광역시: {
    남동구: { lat: 37.4472, lng: 126.7314 },
    부평구: { lat: 37.508, lng: 126.7217 },
    서구: { lat: 37.5454, lng: 126.6754 },
    중구: { lat: 37.4736, lng: 126.6214 },
    연수구: { lat: 37.4104, lng: 126.6784 },
  },
  광주광역시: {
    동구: { lat: 35.1439, lng: 126.9221 },
    서구: { lat: 35.1526, lng: 126.8904 },
    남구: { lat: 35.1337, lng: 126.902 },
    북구: { lat: 35.1742, lng: 126.9113 },
    광산구: { lat: 35.1391, lng: 126.7936 },
  },
  대전광역시: {
    동구: { lat: 36.3483, lng: 127.4345 },
    중구: { lat: 36.3216, lng: 127.4191 },
    서구: { lat: 36.3546, lng: 127.3848 },
    유성구: { lat: 36.3622, lng: 127.3568 },
    대덕구: { lat: 36.346, lng: 127.4145 },
  },
  울산광역시: {
    중구: { lat: 35.5684, lng: 129.3329 },
    남구: { lat: 35.5436, lng: 129.3177 },
    동구: { lat: 35.5047, lng: 129.4161 },
    북구: { lat: 35.5828, lng: 129.3612 },
    울주군: { lat: 35.5207, lng: 129.2429 },
  },
  경기도: {
    수원시: { lat: 37.2636, lng: 127.0286 },
    고양시: { lat: 37.6584, lng: 126.832 },
    용인시: { lat: 37.2411, lng: 127.1775 },
    성남시: { lat: 37.42, lng: 127.126 },
    화성시: { lat: 37.1996, lng: 126.831 },
  },
  강원도: {
    춘천시: { lat: 37.8813, lng: 127.7298 },
    원주시: { lat: 37.3422, lng: 127.9191 },
    강릉시: { lat: 37.7519, lng: 128.8761 },
    동해시: { lat: 37.5244, lng: 129.1143 },
    속초시: { lat: 38.207, lng: 128.5912 },
  },
  충청북도: {
    청주시: { lat: 36.6424, lng: 127.489 },
    충주시: { lat: 36.971, lng: 127.925 },
    제천시: { lat: 37.1326, lng: 128.1914 },
    단양군: { lat: 36.9785, lng: 128.365 },
    옥천군: { lat: 36.3064, lng: 127.571 },
  },
  충청남도: {
    천안시: { lat: 36.815, lng: 127.1139 },
    아산시: { lat: 36.7898, lng: 127.0019 },
    서산시: { lat: 36.7849, lng: 126.4505 },
    당진시: { lat: 36.8932, lng: 126.6297 },
    홍성군: { lat: 36.6014, lng: 126.6606 },
  },
  전라북도: {
    전주시: { lat: 35.8242, lng: 127.147 },
    익산시: { lat: 35.9483, lng: 126.9576 },
    군산시: { lat: 35.967, lng: 126.7363 },
    남원시: { lat: 35.416, lng: 127.3905 },
    정읍시: { lat: 35.5699, lng: 126.8567 },
  },
  전라남도: {
    목포시: { lat: 34.8118, lng: 126.3922 },
    순천시: { lat: 34.9507, lng: 127.4873 },
    여수시: { lat: 34.7604, lng: 127.6622 },
    광양시: { lat: 34.9407, lng: 127.6958 },
    나주시: { lat: 35.0153, lng: 126.7126 },
  },
  경상북도: {
    포항시: { lat: 36.019, lng: 129.3435 },
    경주시: { lat: 35.8562, lng: 129.2247 },
    구미시: { lat: 36.1194, lng: 128.3443 },
    안동시: { lat: 36.5684, lng: 128.7294 },
    영주시: { lat: 36.8057, lng: 128.624 },
  },
  경상남도: {
    창원시: { lat: 35.2271, lng: 128.6811 },
    김해시: { lat: 35.2286, lng: 128.8898 },
    진주시: { lat: 35.1802, lng: 128.1076 },
    양산시: { lat: 35.335, lng: 129.0371 },
    거제시: { lat: 34.8804, lng: 128.6216 },
  },
  제주특별자치도: {
    제주시: { lat: 33.4996, lng: 126.5312 },
    서귀포시: { lat: 33.253, lng: 126.5585 },
  },
};

let map;
let places; // 장소 검색 객체
let markers = [];

// 시/도를 선택하면 군/구 목록을 업데이트
const onCityChange = () => {
  if (selectedCity.value) {
    districts.value = Object.keys(cityDistricts[selectedCity.value]);
    selectedDistrict.value = ''; // 군/구 초기화
  } else {
    districts.value = [];
  }
};

// 군/구를 선택하면 지도의 중심 좌표를 변경하고 은행 검색
const onDistrictChange = () => {
  if (selectedCity.value && selectedDistrict.value) {
    const { lat, lng } =
      cityDistricts[selectedCity.value][selectedDistrict.value];
    const newPosition = new kakao.maps.LatLng(lat, lng);
    map.setCenter(newPosition); // 지도의 중심 좌표를 변경
    searchBanks(newPosition); // 은행(BK9) 검색
  }
};

// 은행(BK9) 카테고리 검색 및 마커 표시
const searchBanks = (location) => {
  removeMarkers(); // 기존 마커를 제거

  const callback = (status, result, pagination) => {
    if (status === kakao.maps.services.Status.OK) {
      console.log('은행 검색 성공:', result);
      result.forEach((place) => {
        const marker = new kakao.maps.Marker({
          map: map,
          position: new kakao.maps.LatLng(place.y, place.x),
        });
        markers.push(marker); // 마커 배열에 추가
      });
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
      console.log('은행 검색 결과 없음');
    } else {
      console.error('은행 검색 실패:', status);
    }
  };

  // 'BK9' 카테고리로 은행을 검색
  places.categorySearch('BK9', callback, { location, radius: 1000 });
};

// 기존 마커 제거 함수
const removeMarkers = () => {
  markers.forEach((marker) => marker.setMap(null));
  markers = [];
};

// Kakao Maps API를 이용해 지도를 초기화하고, 선택된 지역에 따라 업데이트
onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    const mapContainer = document.getElementById('map');
    const mapOption = {
      center: new kakao.maps.LatLng(37.5665, 126.978), // 기본 좌표 (서울 중심)
      level: 5,
    };

    // 지도 생성
    map = new kakao.maps.Map(mapContainer, mapOption);

    // Kakao Places 객체 생성
    places = new kakao.maps.services.Places();

    // 초기 은행 검색
    searchBanks(mapOption.center);
  } else {
    console.error('Kakao Maps API가 로드되지 않았습니다.');
  }
});
</script>

<style scoped>
.map-container {
  margin: 20px;
}
#map {
  width: 100%;
  height: 400px;
}
</style>
