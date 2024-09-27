<template>
  <div class="container-fluid">
    <h3>금융 지도</h3>

    <div class="row mb-3">
      <!-- 광역시/도 선택 -->
      <div class="col-md-3">
        <label for="city-select">광역시 / 도</label>
        <select v-model="selectedProvince" @change="fetchCities" id="city-select" class="form-select">
          <option value="" disabled selected>광역시 선택</option>
          <option v-for="province in provinces" :key="province" :value="province">
            {{ province }}
          </option>
        </select>
      </div>
      <!-- 시/군/구 선택 -->
      <div class="col-md-3">
        <label for="district-select">시/군/구 선택</label>
        <select v-model="selectedCity" id="district-select" class="form-control">
        <option value="" disabled selected>시/군/구 선택</option>
        <option v-for="city in cities" :key="city" :value="city">
          {{ city }}
        </option>
      </select>

      </div>

      <!-- 찾기 버튼 -->
      <div class="col-md-3 d-flex align-items-end">
        <button class="search-button btn btn-primary m-0" @click="searchBank">
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

<script>
export default {
  data() {
    return {
      map: null,
      service: null,
      geocoder: null,
      selectedProvince: '',
      selectedCity: '',
      provinces: [
        '서울특별시',
        '부산광역시',
        '대구광역시',
        '인천광역시',
        '광주광역시',
        '대전광역시',
        '울산광역시',
        '세종특별자치시',
        '경기도',
        '강원도',
        '충청북도',
        '충청남도',
        '전라북도',
        '전라남도',
        '경상북도',
        '경상남도',
        '제주특별자치도',
      ],
      cities: [],
      markers: [], // To hold marker instances
    };
  },
  mounted() {
    this.initMap();
  },
  methods: {
    // 오늘의 운영시간에서 시간만 추출하는 함수
    getTodayOpeningHours(openingHours) {
      const todayIndex = new Date().getDay(); // 오늘 요일 인덱스 가져오기
      const todayHours = openingHours[todayIndex]; // 오늘에 해당하는 운영시간 가져오기

      // 운영시간 문자열에서 콜론(:) 이후의 시간 부분만 추출
      return todayHours ? todayHours.split(': ')[1] : '운영시간 정보 없음';
    },

    initMap() {
      this.map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: 37.5665, lng: 126.978 }, // Initial map center at Seoul
        zoom: 12,
      });
      this.service = new google.maps.places.PlacesService(this.map);
      this.geocoder = new google.maps.Geocoder(); // Initialize Geocoder
    },

    // 기존 마커 제거 함수
    clearMarkers() {
      this.markers.forEach((marker) => {
        marker.setMap(null); // 지도에서 마커를 제거
      });
      this.markers = []; // 마커 배열 초기화
    },

    fetchCities() {
      // 광역시 및 도에 따른 시/군/구 설정
      if (this.selectedProvince === '서울특별시') {
        this.cities = [
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
      } else if (this.selectedProvince === '부산광역시') {
        this.cities = [
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
        ];
      } else if (this.selectedProvince === '대구광역시') {
        this.cities = [
          '남구',
          '달서구',
          '달성군',
          '동구',
          '북구',
          '서구',
          '수성구',
          '중구',
        ];
      } else if (this.selectedProvince === '인천광역시') {
        this.cities = [
          '강화군',
          '계양구',
          '남동구',
          '동구',
          '미추홀구',
          '부평구',
          '서구',
          '연수구',
          '옹진군',
          '중구',
        ];
      } else if (this.selectedProvince === '광주광역시') {
        this.cities = ['광산구', '남구', '동구', '북구', '서구'];
      } else if (this.selectedProvince === '대전광역시') {
        this.cities = ['대덕구', '동구', '서구', '유성구', '중구'];
      } else if (this.selectedProvince === '울산광역시') {
        this.cities = ['남구', '동구', '북구', '울주군', '중구'];
      } else if (this.selectedProvince === '세종특별자치시') {
        this.cities = ['세종시'];
      } else if (this.selectedProvince === '경기도') {
        this.cities = [
          '가평군',
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
          '양평군',
          '여주시',
          '연천군',
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
        ];
      } else if (this.selectedProvince === '강원도') {
        this.cities = [
          '강릉시',
          '고성군',
          '동해시',
          '삼척시',
          '속초시',
          '양구군',
          '양양군',
          '영월군',
          '원주시',
          '인제군',
          '정선군',
          '철원군',
          '춘천시',
          '태백시',
          '평창군',
          '홍천군',
          '화천군',
          '횡성군',
        ];
      } else if (this.selectedProvince === '충청북도') {
        this.cities = [
          '괴산군',
          '단양군',
          '보은군',
          '영동군',
          '옥천군',
          '음성군',
          '제천시',
          '진천군',
          '청주시',
          '충주시',
        ];
      } else if (this.selectedProvince === '충청남도') {
        this.cities = [
          '계룡시',
          '공주시',
          '금산군',
          '논산시',
          '당진시',
          '보령시',
          '부여군',
          '서산시',
          '서천군',
          '아산시',
          '예산군',
          '천안시',
          '청양군',
          '태안군',
          '홍성군',
        ];
      } else if (this.selectedProvince === '전라북도') {
        this.cities = [
          '고창군',
          '군산시',
          '김제시',
          '남원시',
          '무주군',
          '부안군',
          '순창군',
          '완주군',
          '익산시',
          '임실군',
          '장수군',
          '전주시',
          '정읍시',
          '진안군',
        ];
      } else if (this.selectedProvince === '전라남도') {
        this.cities = [
          '강진군',
          '고흥군',
          '곡성군',
          '광양시',
          '구례군',
          '나주시',
          '담양군',
          '목포시',
          '무안군',
          '보성군',
          '순천시',
          '신안군',
          '여수시',
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
      } else if (this.selectedProvince === '경상북도') {
        this.cities = [
          '경산시',
          '경주시',
          '고령군',
          '구미시',
          '군위군',
          '김천시',
          '문경시',
          '봉화군',
          '상주시',
          '성주군',
          '안동시',
          '영덕군',
          '영양군',
          '영주시',
          '영천시',
          '예천군',
          '울릉군',
          '울진군',
          '의성군',
          '청도군',
          '청송군',
          '칠곡군',
          '포항시',
        ];
      } else if (this.selectedProvince === '경상남도') {
        this.cities = [
          '거제시',
          '거창군',
          '고성군',
          '김해시',
          '남해군',
          '밀양시',
          '사천시',
          '산청군',
          '양산시',
          '의령군',
          '진주시',
          '창녕군',
          '창원시',
          '통영시',
          '하동군',
          '함안군',
          '함양군',
          '합천군',
        ];
      } else if (this.selectedProvince === '제주특별자치도') {
        this.cities = ['서귀포시', '제주시'];
      }
    },

    // Use geocoding to get the latitude and longitude of the selected location
    searchBank() {
      const location = `${this.selectedProvince} ${this.selectedCity}`;

      // Geocoding을 사용하여 선택된 위치의 위도와 경도를 가져옴
      this.geocoder.geocode({ address: location }, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK && results.length > 0) {
          const result = results[0];
          const resultLocation = result.geometry.location;
          this.map.setCenter(resultLocation);
          this.map.setZoom(15); // 해당 위치로 확대

          // Clear existing markers
          this.clearMarkers();

          // Bounding Box 설정 (해당 구역의 경계를 제한)
          const bounds = new google.maps.LatLngBounds(
            new google.maps.LatLng(
              result.geometry.viewport.getSouthWest().lat(),
              result.geometry.viewport.getSouthWest().lng()
            ),
            new google.maps.LatLng(
              result.geometry.viewport.getNorthEast().lat(),
              result.geometry.viewport.getNorthEast().lng()
            )
          );

          const request = {
            bounds: bounds, // 경계 설정을 추가
            query: `KB국민은행 ${this.selectedCity}`, // 쿼리에 선택한 구를 추가
          };

          // 장소 검색 수행
          this.service.textSearch(request, (places, status) => {
            if (status === google.maps.places.PlacesServiceStatus.OK) {
              this.clearMarkers(); // 기존 마커 제거

              places.forEach((place) => {
                // 검색 결과에서 ATM과 국민카드가 포함된 결과는 제외
                if (
                  !place.name.includes('ATM') &&
                  !place.name.includes('국민카드')
                ) {
                  const marker = new google.maps.Marker({
                    map: this.map,
                    position: place.geometry.location,
                    title: place.name,
                  });

                  // Places Details 요청을 통해 세부 정보를 가져옴
                  const detailsRequest = {
                    placeId: place.place_id,
                    fields: ['name', 'formatted_address', 'opening_hours'], // 필요한 정보 필드 설정
                  };

                  this.service.getDetails(
                    detailsRequest,
                    (placeDetails, status) => {
                      if (
                        status === google.maps.places.PlacesServiceStatus.OK
                      ) {
                        // 오늘의 운영시간 가져오기
                        let todayOpeningHours = '운영시간 정보 없음';
                        if (
                          placeDetails.opening_hours &&
                          placeDetails.opening_hours.weekday_text
                        ) {
                          // 현재 날짜 가져오기
                          const today = new Date();
                          const todayIndex = today.getDay();

                          // weekday_text 배열에서 요일이 일요일부터 시작하는지 확인
                          let weekdayText =
                            placeDetails.opening_hours.weekday_text;
                          const firstDayText = weekdayText[0].toLowerCase();
                          const isSundayFirst =
                            firstDayText.includes('sunday') ||
                            firstDayText.includes('일요일');

                          // 요일 인덱스 조정
                          let correctedIndex = isSundayFirst
                            ? todayIndex
                            : todayIndex === 0
                              ? 6
                              : todayIndex - 1;

                          // 오늘의 운영 시간만 출력
                          let openingHoursText =
                            weekdayText[correctedIndex] || '운영시간 정보 없음';

                          // "요일: 시간" 형식에서 시간 부분만 추출
                          if (openingHoursText.includes(': ')) {
                            todayOpeningHours = openingHoursText.split(': ')[1]; // 시간만 추출
                          } else {
                            todayOpeningHours = '운영시간 정보 없음';
                          }
                        }

                        // InfoWindow에 오늘의 운영시간 추가
                        const infoWindow = new google.maps.InfoWindow({
                          content: `<div><strong>${placeDetails.name}</strong><br>
    <strong>주소:</strong> ${placeDetails.formatted_address}<br>
    <strong>오늘의 운영시간:</strong> ${todayOpeningHours}</div>`,
                        });

                        marker.addListener('click', () => {
                          infoWindow.open(this.map, marker);
                        });

                        this.markers.push(marker); // 마커 배열에 추가
                      } else {
                        console.error('Place details request failed:', status);
                      }
                    }
                  );
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
    },

    // Use `.bind(this)` to ensure `this` refers to Vue instance
  },
};
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