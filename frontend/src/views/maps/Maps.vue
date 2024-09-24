<script setup>
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';

// Kakao 지도 API 키
const MAP_API_KEY = '33cf94244b357b4aa60393fea33c07ba';

// 선택된 은행, 지역 설정
const selectedBank = ref('국민은행'); // 디폴트 값을 '국민은행'으로 설정
// const banks = ref([
//   '전체보기',
//   '우리은행',
//   '한국스탠다드차타드은행',
//   '대구은행',
//   '부산은행',
//   '광주은행',
//   '제주은행',
//   '전북은행',
//   '경남은행',
//   '중소기업은행',
//   '한국산업은행',
//   '국민은행',
//   '신한은행',
//   '농협은행',
//   '하나은행',
//   '수협은행',
// ]);

const selectedCity = ref();
const cities = ref([
  '서울특별시',
  '경기도',
  '부산광역시',
  '인천시',
  '대구광역시',
  '대전광역시',
  '울산광역시',
  '경상남도',
  '경상북도',
  '충청북도',
  '충청남도',
  '전라북도',
  '전라남도',
  '강원도',
  '광주광역시',
  '제주특별자치도',
]);
const selectedCityDetail = ref();
const citiesDetail = ref();
const selectedTown = ref(''); // selectedTown 초기화

const keyword = ref('국민은행');

// 이전에 열렸던 정보창을 추적하는 변수
let currentInfowindow = null;

const seoul = [
  '강남구',
  '강동구',
  '강북구',
  '강서구',
  '관악구',
  '광진구',
  '구로구',
  '금천구',
  '노원구',
  '도봉구',
  '동대문구',
  '동작구',
  '마포구',
  '서대문구',
  '서초구',
  '성동구',
  '성북구',
  '송파구',
  '양천구',
  '영등포구',
  '용산구',
  '은평구',
  '종로구',
  '중구',
  '중랑구',
];
const gyeonggi = [
  '고양시',
  '과천시',
  '광명시',
  '광주시',
  '구리시',
  '군포시',
  '김포시',
  '남양주시',
  '동두천시',
  '부천시',
  '성남시',
  '수원시',
  '시흥시',
  '안산시',
  '안성시',
  '안양시',
  '양주시',
  '오산시',
  '용인시',
  '의왕시',
  '의정부시',
  '이천시',
  '파주시',
  '평택시',
  '포천시',
  '하남시',
  '화성시',
  '가평군',
  '양평군',
  '여주군',
  '연천군',
];
const busan = [
  '강서구',
  '금정구',
  '남구',
  '동구',
  '동래구',
  '부산진구',
  '북구',
  '사상구',
  '사하구',
  '서구',
  '수영구',
  '연제구',
  '영도구',
  '중구',
  '해운대구',
  '기장군',
];
const incheon = [
  '계양구',
  '남구',
  '남동구',
  '동구',
  '부평구',
  '서구',
  '연수구',
  '중구',
  '강화군',
  '옹진군',
];
const daegu = [
  '남구',
  '달서구',
  '동구',
  '북구',
  '서구',
  '수성구',
  '중구',
  '달성군',
];
const daejeon = ['대덕구', '동구', '서구', '유성구', '중구'];
const ulsan = ['남구', '동구', '북구', '중구', '울주군'];
const gyeongsangnam = [
  '거제시',
  '김해시',
  '마산시',
  '밀양시',
  '사천시',
  '양산시',
  '진주시',
  '진해시',
  '창원시',
  '통영시',
  '거창군',
  '고성군',
  '남해군',
  '산청군',
  '의령군',
  '창녕군',
  '하동군',
  '함안군',
  '함양군',
  '합천군',
];
const gyeongsangbuk = [
  '경산시',
  '경주시',
  '구미시',
  '김천시',
  '문경시',
  '상주시',
  '안동시',
  '영주시',
  '영천시',
  '포항시',
  '고령군',
  '군위군',
  '봉화군',
  '성주군',
  '영덕군',
  '영양군',
  '예천군',
  '울릉군',
  '울진군',
  '의성군',
  '청도군',
  '청송군',
  '칠곡군',
];
const chungbuk = [
  '제천시',
  '청주시',
  '충주시',
  '괴산군',
  '단양군',
  '보은군',
  '영동군',
  '옥천군',
  '음성군',
  '증평군',
  '진천군',
  '청원군',
];
const chungnam = [
  '계룡시',
  '공주시',
  '논산시',
  '보령시',
  '서산시',
  '아산시',
  '천안시',
  '금산군',
  '당진군',
  '부여군',
  '서천군',
  '연기군',
  '예산군',
  '태안군',
  '홍성군',
];
const jeonbuk = [
  '군산시',
  '김제시',
  '남원시',
  '익산시',
  '전주시',
  '정읍시',
  '고창군',
  '무주군',
  '부안군',
  '순창군',
  '완주군',
  '임실군',
  '장수군',
  '진안군',
];
const jeonnam = [
  '광양시',
  '나주시',
  '목포시',
  '순천시',
  '여수시',
  '강진군',
  '고흥군',
  '곡성군',
  '구례군',
  '담양군',
  '무안군',
  '보성군',
  '신안군',
  '영광군',
  '영암군',
  '완도군',
  '장성군',
  '장흥군',
  '진도군',
  '함평군',
  '해남군',
  '화순군',
];
const gangwon = [
  '강릉시',
  '동해시',
  '삼척시',
  '속초시',
  '원주시',
  '춘천시',
  '태백시',
  '고성군',
  '양구군',
  '양양군',
  '영월군',
  '인제군',
  '정선군',
  '철원군',
  '평창군',
  '홍천군',
  '화천군',
  '횡성군',
];
const jeju = ['서귀포시', '제주시', '남제주군', '북제주군'];

watch(selectedCity, () => {
  selectedCityDetail.value = null;
  if (selectedCity.value == '서울특별시') {
    citiesDetail.value = seoul;
  } else if (selectedCity.value == '경기도') {
    citiesDetail.value = gyeonggi;
  } else if (selectedCity.value == '부산광역시') {
    citiesDetail.value = busan;
  } else if (selectedCity.value == '인천시') {
    citiesDetail.value = incheon;
  } else if (selectedCity.value == '대구광역시') {
    citiesDetail.value = daegu;
  } else if (selectedCity.value == '대전광역시') {
    citiesDetail.value = daejeon;
  } else if (selectedCity.value == '울산광역시') {
    citiesDetail.value = ulsan;
  } else if (selectedCity.value == '경상남도') {
    citiesDetail.value = gyeongsangnam;
  } else if (selectedCity.value == '경상북도') {
    citiesDetail.value = gyeongsangbuk;
  } else if (selectedCity.value == '충청남도') {
    citiesDetail.value = chungnam;
  } else if (selectedCity.value == '충청북도') {
    citiesDetail.value = chungbuk;
  } else if (selectedCity.value == '전라북도') {
    citiesDetail.value = jeonbuk;
  } else if (selectedCity.value == '전라남도') {
    citiesDetail.value = jeonnam;
  } else if (selectedCity.value == '강원도') {
    citiesDetail.value = gangwon;
  } else if (selectedCity.value == '광주광역시') {
    citiesDetail.value = gwangju;
  } else if (selectedCity.value == '제주특별자치도') {
    citiesDetail.value = jeju;
  }
});

const mapRef = ref();

watch([selectedCity, selectedCityDetail, selectedBank], () => {
  keyword.value = '';
  if (selectedCity.value) {
    keyword.value += `${selectedCity.value}`;
  }
  if (selectedCityDetail.value) {
    keyword.value += `${selectedCityDetail.value}`;
  }
  if (selectedBank.value) {
    if (selectedBank.value === '전체보기') {
      keyword.value += '은행';
    } else {
      keyword.value += `${selectedBank.value}`;
    }
  }
});

onMounted(() => {
  const script = document.createElement('script');
  script.onload = () => kakao.maps.load(() => initMap('init'));
  script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${MAP_API_KEY}&libraries=services`;
  document.head.appendChild(script);
});

function initMap(state = 'current') {
  const mapContainer = document.getElementById('map');
  const mapOption = {
    center: new kakao.maps.LatLng(37.566826, 126.9786567),
    level: 7,
  };

  // 맵이 초기화되지 않았을 때만 초기화
  if (!mapRef.value) {
    mapRef.value = new kakao.maps.Map(mapContainer, mapOption);
  }

  const map = mapRef.value;
  const ps = new kakao.maps.services.Places(map);

  if (state === 'search') {
    console.log('키워드로 검색 중:', keyword.value);
    ps.keywordSearch(keyword.value, placesSearchCB);
  } else {
    console.log('카테고리로 검색 중: 국민은행');
    ps.categorySearch('국민은행', placesSearchCB, { useMapBounds: true });
  }
}

function placesSearchCB(data, status) {
  if (status === kakao.maps.services.Status.OK) {
    console.log('검색 결과:', data);

    if (data.length > 0) {
      const firstPlaceCoords = new kakao.maps.LatLng(data[0].y, data[0].x);
      mapRef.value.setCenter(firstPlaceCoords); // 첫 번째 검색 결과로 지도 중심 이동
    }

    data.forEach((place) => {
      if (
        place.category_name.includes('KB국민은행') &&
        !place.category_name.includes('ATM')
      ) {
        const geocoder = new kakao.maps.services.Geocoder();
        geocoder.addressSearch(place.address_name, (result, status) => {
          if (status === kakao.maps.services.Status.OK) {
            const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            displayMarker(coords, place);
          } else {
            console.error('Geocoder 오류 상태:', status);
          }
        });
      }
    });
  } else {
    console.log('검색 결과 없음 또는 오류 발생:', status);
  }
}

function displayMarker(coords, place) {
  if (!mapRef.value) return;

  const marker = new kakao.maps.Marker({
    map: mapRef.value,
    position: coords,
  });

  const infowindow = new kakao.maps.InfoWindow({
    content: `
      <div style="padding:10px; font-size:14px; width:200px;">
        <strong>${place.place_name}</strong><br>
        <em>주소: ${place.address_name}</em>
      </div>
    `,
    removable: true,
  });

  kakao.maps.event.addListener(marker, 'click', () => {
    if (currentInfowindow) {
      currentInfowindow.close();
    }
    infowindow.open(mapRef.value, marker);
    currentInfowindow = infowindow;
    mapRef.value.setCenter(coords); // 지도 중심을 마커 위치로 이동
  });
}

function clickSearch() {
  const townValue = selectedTown.value ? selectedTown.value : '';
  keyword.value =
    `${selectedCity.value || ''} ${selectedCityDetail.value || ''} ${townValue || ''} 국민은행`.trim();
  console.log('검색 키워드:', keyword.value);
  initMap('search');
}
</script>

<template>
  <div class="container-fluid px-4">
    <h1>금융 지도</h1>

    <div class="row mb-3">
      <!-- 광역시/도 선택 -->
      <div class="col-md-3">
        <label for="city-select">광역시 / 도</label>
        <select id="city-select" v-model="selectedCity" class="form-control">
          <option disabled value="">광역시 / 도 선택</option>
          <option v-for="city in cities" :key="city" :value="city">
            {{ city }}
          </option>
        </select>
      </div>
      <!-- 시/군/구 선택 -->
      <div class="col-md-3">
        <label for="district-select">시/군/구 선택</label>
        <select id="district-select" v-model="selectedCityDetail" class="form-control"
          :disabled="!citiesDetail || !citiesDetail.length">
          <option disabled value="">시/군/구 선택</option>
          <option v-for="district in citiesDetail || []" :key="district" :value="district">
            {{ district }}
          </option>
        </select>
      </div>

      <!-- 찾기 버튼 -->
      <div class="col-md-3 d-flex align-items-end">
        <button class="search-button btn btn-primary m-0" @click="clickSearch">
          <i class="mdi mdi-map-search-outline"></i> 찾기
        </button>
      </div>
    </div>

    <!-- 지도 영역 -->
    <div class="card">
      <div id="map"></div>
    </div>

  </div>
</template>

<style scoped>
@font-face {
  font-family: 'TTLaundryGothicB';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/2403-2@1.0/TTLaundryGothicB.woff2') format('woff2');
  font-weight: 700;
  font-style: normal;
}

h1 {
  font-family: 'TTLaundryGothicB', sans-serif;
}

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
  .search-button {
    width: 100%;
    margin-top: 1rem !important;
  }
}

@media (max-width: 480px) {
  /* 모바일 기기에서는 지도 높이를 더 줄이기 */
  #map {
    height: 40vh;
  }
}
</style>