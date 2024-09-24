<template>
  <div class="map-container">
    <div id="map" ref="mapContainerRef" style="height: 100%"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';

// Kakao 지도 API 키
const MAP_API_KEY = '33cf94244b357b4aa60393fea33c07ba';

const mapContainerRef = ref<HTMLElement | null>(null);
let mapRef = ref<any>(null);
let currentInfowindow: any = null;

// 전역 kakao 객체를 선언하여 TypeScript 오류 방지
declare global {
  interface Window {
    kakao: any;
  }
}

onMounted(() => {
  // Kakao Maps API 비동기 로드
  const script = document.createElement('script');
  script.onload = () => {
    if (window.kakao && window.kakao.maps) {
      window.kakao.maps.load(() => initializeMap());
    }
  };
  script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${MAP_API_KEY}&libraries=services`;
  document.head.appendChild(script);
});

function initializeMap() {
  const mapContainer = mapContainerRef.value as HTMLElement;
  const mapOption = {
    // 서울시 강남구 중심 좌표 (37.497942, 127.027621)
    center: new window.kakao.maps.LatLng(37.497942, 127.027621),
    level: 5, // 확대 수준
  };

  // 지도 객체 생성
  mapRef.value = new window.kakao.maps.Map(mapContainer, mapOption);

  // 장소 검색 객체 생성
  const ps = new window.kakao.maps.services.Places(mapRef.value);

  // '서울시 강남구 국민은행' 키워드로 검색
  ps.keywordSearch('서울시 강남구 국민은행', placesSearchCB);
}

function placesSearchCB(data: any, status: any) {
  if (status === window.kakao.maps.services.Status.OK) {
    // 검색 결과 첫 번째 장소로 지도 중심 이동
    if (data.length > 0) {
      const firstPlaceCoords = new window.kakao.maps.LatLng(
        data[0].y,
        data[0].x
      );
      mapRef.value.setCenter(firstPlaceCoords);
    }

    // 검색된 모든 장소에 마커와 인포윈도우 표시
    data.forEach((place: any) => {
      if (
        place.category_name.includes('KB국민은행') &&
        !place.category_name.includes('ATM')
      ) {
        const coords = new window.kakao.maps.LatLng(place.y, place.x);
        displayMarker(coords, place);
      }
    });
  }
}

function displayMarker(coords: any, place: any) {
  const marker = new window.kakao.maps.Marker({
    map: mapRef.value,
    position: coords,
  });

  const infowindow = new window.kakao.maps.InfoWindow({
    content: `
          <div style="padding:10px; font-size:14px; width:200px;">
            <strong>${place.place_name}</strong><br>
            <em>주소: ${place.address_name}</em>
          </div>
        `,
    removable: true,
  });

  window.kakao.maps.event.addListener(marker, 'click', () => {
    if (currentInfowindow) {
      currentInfowindow.close();
    }
    infowindow.open(mapRef.value, marker);
    currentInfowindow = infowindow;
  });
}
</script>

<style scoped>
.map-container {
  width: 100%;
  height: 200px; /* 미니맵 크기 설정 */
  border: 1px solid #ddd;
  border-radius: 8px;
}
</style>
