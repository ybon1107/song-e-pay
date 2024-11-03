<template>
  <div class="container-fluid">
    <br />
    <h3 class="mapsFont">{{ $t('map--title') }}</h3>
    <div class="row mb-3 mapSelect">
      <!-- 광역시/도 선택 -->
      <div class="col-md-3 col-sm-3 d-flex align-items-end mb-2">
        <select v-model="selectedProvince" @change="fetchCities" id="city-select" class="form-select form-field-radius">
          <option value="" disabled selected>
            {{ $t('map--select-cityLabel') }}
          </option>
          <option v-for="province in provinces" :key="province" :value="province">
            {{ $t(province) }}
          </option>
        </select>
      </div>
      <!-- 시/군/구 선택 -->
      <div class="col-md-3 col-sm-3 d-flex align-items-end mb-2">
        <select v-model="selectedCity" id="district-select" class="form-select form-field-radius">
          <option value="" disabled selected>
            {{ $t('map--select-districtLabel') }}
          </option>
          <option v-for="city in cities" :key="city" :value="city">
            {{ $t(city) }}
          </option>
        </select>
      </div>
      <!-- 검색 버튼 -->
      <div class="col-md-2 col-sm-3 d-flex align-items-end mb-2">
        <button class="search-button btn btn-primary w-100 form-field-radius mb-0" @click="searchBank">
          <i class="mdi mdi-map-search-outline"></i>
          {{ $t('map--button-search') }}
        </button>
      </div>
      <!-- 내 위치에서 찾기 버튼 -->
      <div class="col-md-2 col-sm-3 d-flex align-items-end mb-2">
        <button class="btn btn-secondary w-100 form-field-radius mb-0" @click="findUserLocation">
          {{ $t('map--mylocation') }}
        </button>
      </div>
    </div>
    <!-- 지도 영역 -->
    <div class="card">
      <div id="map"></div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import {
  PROVINCES,
  SEOUL_CITIES,
  BUSAN_CITIES,
  DAEGU_CITIES,
  INCHEON_CITIES,
  GWANGJU_CITIES,
  DAEJEON_CITIES,
  ULSAN_CITIES,
  SEJONG_CITIES,
  GYEONGGI_CITIES,
  GANGWON_CITIES,
  CHUNGCHEONGBUK_CITIES,
  CHUNGCHEONGNAM_CITIES,
  JEOLLABUK_CITIES,
  JEOLLANAM_CITIES,
  GYEONGSANGBUK_CITIES,
  GYEONGSANGNAM_CITIES,
  JEJU_CITIES,
} from '../../constants/localUnits';
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
const map = ref(null);
const service = ref(null);
const geocoder = ref(null);
const selectedProvince = ref('');
const selectedCity = ref('');
const provinces = ref(PROVINCES);
const cities = ref([]);
const markers = ref([]); // To hold marker instances
let userMarker = null;
onMounted(() => {
  initMap();
});
function initMap() {
  map.value = new google.maps.Map(document.getElementById('map'), {
    center: { lat: 37.5665, lng: 126.978 }, // Initial map center at Seoul
    zoom: 12,
  });
  service.value = new google.maps.places.PlacesService(map.value);
  geocoder.value = new google.maps.Geocoder(); // Initialize Geocoder
}
// 사용자의 현재 위치를 가져오고 주변 KB국민은행을 검색하는 함수
function findUserLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const userLocation = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        };
        map.value.setCenter(userLocation);
        map.value.setZoom(15); // 줌 설정
        setUserMarker(userLocation);
        searchNearbyBanks(userLocation);
      },
      () => {
        alert('위치 정보를 사용할 수 없습니다.');
      }
    );
  } else {
    alert('사용자의 브라우저에서 위치 정보를 지원하지 않습니다.');
  }
}
// 사용자의 위치에 파란색 마커를 설정하는 함수
function setUserMarker(location) {
  if (userMarker) {
    userMarker.setMap(null); // 기존 마커 제거
  }
  userMarker = new google.maps.Marker({
    position: location,
    map: map.value,
    icon: {
      url: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png', // 파란색 마커
    },
    title: '나의 위치',
  });
}
// 주변 KB국민은행을 검색하는 함수
function searchNearbyBanks(location) {
  const request = {
    location: location,
    radius: 2000, // 3km 반경 내에서 검색
    query: 'KB국민은행',
  };
  service.value.textSearch(request, (places, status) => {
    if (status === google.maps.places.PlacesServiceStatus.OK) {
      clearMarkers(); // 기존 마커 제거
      places.forEach((place) => {
        // 검색 결과에서 ATM과 국민카드가 포함된 결과는 제외
        if (!place.name.includes('ATM') && !place.name.includes('국민카드')) {
          const marker = new google.maps.Marker({
            map: map.value,
            position: place.geometry.location,
            title: place.name,
          });
          const infoWindow = new google.maps.InfoWindow({
            content: `<div><strong>${place.name}</strong><br>
            <strong>주소:</strong> ${place.formatted_address}</div>`,
          });
          marker.addListener('click', () => {
            infoWindow.open(map.value, marker);
          });
          markers.value.push(marker); // 마커 배열에 추가
        }
      });
    } else {
      console.error('Places search was not successful:', status);
    }
  });
}
// 오늘의 운영시간에서 시간만 추출하는 함수
function getTodayOpeningHours(openingHours) {
  const todayIndex = new Date().getDay(); // 오늘 요일 인덱스 가져오기
  const todayHours = openingHours[todayIndex]; // 오늘에 해당하는 운영시간 가져오기
  // 운영시간 문자열에서 콜론(:) 이후의 시간 부분만 추출
  return todayHours ? todayHours.split(': ')[1] : '운영시간 정보 없음';
}
// 기존 마커 제거 함수
function clearMarkers() {
  markers.value.forEach((marker) => {
    marker.setMap(null); // 지도에서 마커를 제거
  });
  markers.value = []; // 마커 배열 초기화
}
function fetchCities() {
  // 광역시 및 도에 따른 시/군/구 설정
  console.log(selectedProvince.value);
  console.log(PROVINCES[0]);
  if (selectedProvince.value === PROVINCES[0]) {
    cities.value = SEOUL_CITIES;
  } else if (selectedProvince.value === PROVINCES[1]) {
    cities.value = BUSAN_CITIES;
  } else if (selectedProvince.value === PROVINCES[2]) {
    cities.value = DAEGU_CITIES;
  } else if (selectedProvince.value === PROVINCES[3]) {
    cities.value = INCHEON_CITIES;
  } else if (selectedProvince.value === PROVINCES[4]) {
    cities.value = DAEJEON_CITIES;
  } else if (selectedProvince.value === PROVINCES[5]) {
    cities.value = GWANGJU_CITIES;
  } else if (selectedProvince.value === PROVINCES[6]) {
    cities.value = ULSAN_CITIES;
  } else if (selectedProvince.value === PROVINCES[7]) {
    cities.value = SEJONG_CITIES;
  } else if (selectedProvince.value === PROVINCES[8]) {
    cities.value = GYEONGGI_CITIES;
  } else if (selectedProvince.value === PROVINCES[9]) {
    cities.value = GANGWON_CITIES;
  } else if (selectedProvince.value === PROVINCES[10]) {
    cities.value = CHUNGCHEONGBUK_CITIES;
  } else if (selectedProvince.value === PROVINCES[11]) {
    cities.value = CHUNGCHEONGNAM_CITIES;
  } else if (selectedProvince.value === PROVINCES[12]) {
    cities.value = JEOLLABUK_CITIES;
  } else if (selectedProvince.value === PROVINCES[13]) {
    cities.value = JEOLLANAM_CITIES;
  } else if (selectedProvince.value === PROVINCES[14]) {
    cities.value = GYEONGSANGBUK_CITIES;
  } else if (selectedProvince.value === PROVINCES[15]) {
    cities.value = GYEONGSANGNAM_CITIES;
  } else if (selectedProvince.value === PROVINCES[16]) {
    cities.value = JEJU_CITIES;
  }
}
function searchBank() {
  const provinceKey = selectedProvince.value;
  const cityKey = selectedCity.value;
  // 영어로 된 위치 정보 생성
  const provinceEnglish = getEnglishName(provinceKey);
  const cityEnglish = getEnglishName(cityKey);
  const locationEnglish = `${provinceEnglish} ${cityEnglish}`;
  // 한국어로 된 위치 정보 (표시용)
  const provinceKorean = t(provinceKey);
  const cityKorean = t(cityKey);
  const locationKorean = `${provinceKorean} ${cityKorean}`;
  console.log('location (Korean)', locationKorean);
  console.log('location (English)', locationEnglish);
  console.log('selectedCity', cityKey);
  // Geocoding을 사용하여 선택된 위치의 위도와 경도를 가져옴
  geocoder.value.geocode({ address: locationEnglish }, (results, status) => {
    if (status === google.maps.GeocoderStatus.OK && results.length > 0) {
      const result = results[0];
      const resultLocation = result.geometry.location;
      map.value.setCenter(resultLocation);
      map.value.setZoom(15); // 해당 위치로 확대
      // Clear existing markers
      clearMarkers();
      // Bounding Box 설정 (해당 구역의 경계를 제한)
      const bounds = new google.maps.LatLngBounds(
        new google.maps.LatLng(result.geometry.viewport.getSouthWest().lat(), result.geometry.viewport.getSouthWest().lng()),
        new google.maps.LatLng(result.geometry.viewport.getNorthEast().lat(), result.geometry.viewport.getNorthEast().lng())
      );
      const request = {
        bounds: bounds,
        query: `KB Kookmin Bank ${cityEnglish}`, // 영어로 된 도시 이름 사용
      };
      // 장소 검색 수행
      service.value.textSearch(request, (places, status) => {
        if (status === google.maps.places.PlacesServiceStatus.OK) {
          clearMarkers(); // 기존 마커 제거
          places.forEach((place) => {
            // 검색 결과에서 ATM과 국민카드가 포함된 결과는 제외
            if (!place.name.includes('ATM') && !place.name.includes('국민카드')) {
              const marker = new google.maps.Marker({
                map: map.value,
                position: place.geometry.location,
                title: place.name,
              });
              // Places Details 요청을 통해 세부 정보를 가져옴
              const detailsRequest = {
                placeId: place.place_id,
                fields: ['name', 'formatted_address', 'opening_hours'], // 필요한 정보 필드 설정
              };
              service.value.getDetails(detailsRequest, (placeDetails, status) => {
                if (status === google.maps.places.PlacesServiceStatus.OK) {
                  // 오늘의 운영시간 가져오기
                  let todayOpeningHours = t('map--marker-box-openingHours');
                  if (placeDetails.opening_hours && placeDetails.opening_hours.weekday_text) {
                    // 현재 날짜 가져오기
                    const today = new Date();
                    const todayIndex = today.getDay();
                    // weekday_text 배열에서 요일이 일요일부터 시작하는지 확인
                    let weekdayText = placeDetails.opening_hours.weekday_text;
                    const firstDayText = weekdayText[0].toLowerCase();
                    const isSundayFirst = firstDayText.includes('sunday') || firstDayText.includes('일요일');
                    // 요일 인덱스 조정
                    let correctedIndex = isSundayFirst ? todayIndex : todayIndex === 0 ? 6 : todayIndex - 1;
                    // 오늘의 운영 시간만 출력
                    let openingHoursText = weekdayText[correctedIndex] || '운영시간 정보 없음';
                    // "요일: 시간" 형식에서 시간 부분만 추출
                    if (openingHoursText.includes(': ')) {
                      todayOpeningHours = openingHoursText.split(': ')[1]; // 시간만 추출
                    } else {
                      todayOpeningHours = '운영시간 정보 없음';
                    }
                  }
                  // InfoWindow 내용 수정
                  const infoWindow = new google.maps.InfoWindow({
                    content: `
                        <div>
                          <strong>${placeDetails.name}</strong><br>
                          <strong>Address:</strong> ${placeDetails.formatted_address}<br>
                          <strong>Today's Opening Hours:</strong> ${todayOpeningHours}
                        </div>
                      `,
                  });
                  marker.addListener('click', () => {
                    infoWindow.open(map.value, marker);
                  });
                  markers.value.push(marker); // 마커 배열에 추가
                } else {
                  console.error('Place details request failed:', status);
                }
              });
            }
          });
        } else {
          console.error('Places search was not successful:', status);
        }
      });
    } else {
      console.error('Geocode was not successful:', status);
    }
  });
}
// 키를 영어 이름으로 변환하는 함수
function getEnglishName(key) {
  const englishNames = {
    'province--seoul': 'Seoul',
    'city--seoul-1': 'Gangnam-gu',
    'city--seoul-2': 'Gangdong-gu',
    // ... 다른 도시들에 대한 영어 이름 매핑
  };
  return englishNames[key] || key;
}
</script>
<style scoped>
/* 반응형 지도 크기 설정 */
#map {
  width: 100%;
  height: 60vh;
  /* 화면 높이의 60% */
  min-height: 400px;
  /* 최소 높이 설정 */
}
/* 작은 화면일 때 */
@media (max-width: 768px) {
  /*  지도 높이 줄이기 */
  #map {
    height: 50vh;
  }
  /* 버튼 늘리기 */
}
@media (max-width: 480px) {
  /* 모바일 기기에서는 지도 높이를 더 줄이기 */
  #map {
    height: 40vh;
  }
}
.form-field-radius {
  border-radius: 50px;
  height: 40px;
  padding-left: 20px;
}
</style>
