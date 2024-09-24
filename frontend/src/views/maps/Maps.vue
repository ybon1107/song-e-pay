<template>
  <div>
    <div>
      <select v-model="selectedProvince" @change="fetchCities">
        <option value="" disabled selected>광역시 선택</option>
        <option v-for="province in provinces" :key="province" :value="province">
          {{ province }}
        </option>
      </select>

      <select v-model="selectedCity" @change="fetchDistricts">
        <option value="" disabled selected>시/군/구 선택</option>
        <option v-for="city in cities" :key="city" :value="city">
          {{ city }}
        </option>
      </select>

      <select v-model="selectedDistrict" @change="searchBank">
        <option value="" disabled selected>구 선택</option>
        <option v-for="district in districts" :key="district" :value="district">
          {{ district }}
        </option>
      </select>
    </div>

    <div id="map" style="width: 100%; height: 500px"></div>
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
      selectedDistrict: '',
      provinces: [
        '서울특별시',
        '부산광역시',
        '대구광역시',
        '인천광역시',
        '광주광역시',
        '대전광역시',
        '울산광역시',
      ],
      cities: [],
      districts: [],
      markers: [],
    };
  },
  mounted() {
    this.initMap();
  },
  methods: {
    initMap() {
      this.map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: 37.5665, lng: 126.978 }, // 서울 좌표
        zoom: 12,
      });
      this.service = new google.maps.places.PlacesService(this.map);
      this.geocoder = new google.maps.Geocoder(); // Initialize Geocoder
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
    fetchDistricts() {
      // 시/군/구에 따른 동 설정
      if (this.selectedCity === '강남구') {
        this.districts = [
          '압구정동',
          '역삼동',
          '삼성동',
          '청담동',
          '신사동',
          '논현동',
          '대치동',
          '개포동',
        ];
      } else if (this.selectedCity === '종로구') {
        this.districts = [
          '청운동',
          '신교동',
          '궁정동',
          '효자동',
          '창성동',
          '통의동',
          '통인동',
          '누상동',
          '누하동',
          '옥인동',
        ];
      } else if (this.selectedCity === '해운대구') {
        this.districts = ['중동', '좌동', '우동', '송정동', '반송동', '재송동'];
      } else if (this.selectedCity === '부산진구') {
        this.districts = [
          '부암동',
          '당감동',
          '가야동',
          '개금동',
          '범천동',
          '범전동',
          '연지동',
          '전포동',
        ];
      } else if (this.selectedCity === '수원시') {
        this.districts = ['장안구', '권선구', '팔달구', '영통구'];
      } else if (this.selectedCity === '성남시') {
        this.districts = ['분당구', '수정구', '중원구'];
      } else if (this.selectedCity === '용인시') {
        this.districts = ['수지구', '기흥구', '처인구'];
      } else if (this.selectedCity === '인천광역시 중구') {
        this.districts = [
          '운서동',
          '용유동',
          '을왕동',
          '운남동',
          '중산동',
          '영종동',
        ];
      } else if (this.selectedCity === '인천광역시 남동구') {
        this.districts = [
          '간석동',
          '만수동',
          '구월동',
          '남촌동',
          '수산동',
          '장수동',
        ];
      } else if (this.selectedCity === '대전광역시 서구') {
        this.districts = [
          '둔산동',
          '탄방동',
          '월평동',
          '관저동',
          '가수원동',
          '내동',
        ];
      } else if (this.selectedCity === '대구광역시 달서구') {
        this.districts = [
          '성서동',
          '상인동',
          '월성동',
          '도원동',
          '송현동',
          '진천동',
        ];
      } else if (this.selectedCity === '대구광역시 수성구') {
        this.districts = [
          '범어동',
          '수성동',
          '지산동',
          '황금동',
          '만촌동',
          '중동',
        ];
      } else if (this.selectedCity === '광주광역시 서구') {
        this.districts = [
          '화정동',
          '쌍촌동',
          '상무동',
          '유덕동',
          '치평동',
          '광천동',
        ];
      } else if (this.selectedCity === '울산광역시 남구') {
        this.districts = [
          '삼산동',
          '달동',
          '무거동',
          '신정동',
          '옥동',
          '야음동',
        ];
      } else if (this.selectedCity === '세종특별자치시') {
        this.districts = [
          '도담동',
          '종촌동',
          '고운동',
          '아름동',
          '새롬동',
          '다정동',
        ];
      } else if (this.selectedCity === '경기도 고양시') {
        this.districts = ['덕양구', '일산동구', '일산서구'];
      } else if (this.selectedCity === '경기도 성남시') {
        this.districts = ['분당구', '수정구', '중원구'];
      } else if (this.selectedCity === '경기도 평택시') {
        this.districts = [
          '비전동',
          '동삭동',
          '세교동',
          '칠괴동',
          '청북읍',
          '포승읍',
        ];
      } else if (this.selectedCity === '경기도 안양시') {
        this.districts = ['동안구', '만안구'];
      } else if (this.selectedCity === '경상북도 포항시') {
        this.districts = ['남구', '북구'];
      } else if (this.selectedCity === '경상남도 창원시') {
        this.districts = [
          '의창구',
          '성산구',
          '마산합포구',
          '마산회원구',
          '진해구',
        ];
      } else if (this.selectedCity === '제주시') {
        this.districts = [
          '아라동',
          '노형동',
          '연동',
          '이도동',
          '삼양동',
          '외도동',
        ];
      } else if (this.selectedCity === '서귀포시') {
        this.districts = ['대정읍', '남원읍', '성산읍', '안덕면', '표선면'];
      }
      // 더 많은 시/군/구 및 동 추가 가능
    },
    // Use geocoding to get the latitude and longitude of the selected location
    searchBank() {
      const location = `${this.selectedProvince} ${this.selectedCity} ${this.selectedDistrict}`;

      // Use geocoding to get the latitude and longitude of the selected location
      this.geocoder.geocode({ address: location }, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK) {
          const resultLocation = results[0].geometry.location;
          this.map.setCenter(resultLocation); // Center map to the location
          this.map.setZoom(15); // Zoom in to the location

          // Now search for KB국민은행 near the location
          const request = {
            location: resultLocation,
            radius: '5000', // 5km 반경
            query: `KB국민은행`,
          };

          this.service.textSearch(request, (results, status) => {
            if (status === google.maps.places.PlacesServiceStatus.OK) {
              this.clearMarkers(); // 이전 마커 제거

              results.forEach((place) => {
                const marker = new google.maps.Marker({
                  map: this.map,
                  position: place.geometry.location,
                  title: place.name,
                });

                // Add InfoWindow to show details when the marker is clicked
                const infoWindow = new google.maps.InfoWindow({
                  content: `<div><strong>${place.name}</strong><br>
                        주소: ${place.formatted_address}<br>
                        전화번호: ${place.formatted_phone_number || '정보 없음'}</div>`,
                });

                // Add click event to open InfoWindow when marker is clicked
                marker.addListener('click', () => {
                  infoWindow.open(this.map, marker);
                });

                this.markers.push(marker);
              });
            }
          });
        } else {
          console.error(
            `Geocode was not successful for the following reason: ${status}`
          );
        }
      });
    },

    clearMarkers() {
      this.markers.forEach((marker) => marker.setMap(null));
      this.markers = [];
    },
  },
};
</script>

<style scoped>
/* 스타일 추가 필요 시 작성 */
</style>
