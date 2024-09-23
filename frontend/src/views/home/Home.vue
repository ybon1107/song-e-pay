<template>
  <div class="main-page-container">
    <div class="content-area">
      <h2>홈</h2>
      <p>메인 콘텐츠 영역입니다.</p>
    </div>

    <!-- 지도 영역 -->
    <div class="map-area">
      <!-- 클릭 시 /maps 페이지로 이동 -->
      <button @click="goToMap" class="go-to-map-btn">Go to Map</button>

      <!-- Kakao 지도 표시 -->
      <div id="mini-map"></div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';

const router = useRouter();

// 지도 페이지로 이동하는 함수
const goToMap = () => {
  router.push('/maps');
};

// Kakao 지도 API 키
const MAP_API_KEY = '33cf94244b357b4aa60393fea33c07ba';

onMounted(() => {
  // Kakao 지도 API 비동기 로드
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
  const mapContainer = document.getElementById('mini-map');
  const mapOption = {
    center: new window.kakao.maps.LatLng(37.497942, 127.027621), // 서울시 강남구 좌표
    level: 5, // 지도의 확대 레벨
  };

  // 지도 객체 생성
  const map = new window.kakao.maps.Map(mapContainer, mapOption);

  // 지도에 마커 추가 (서울 강남구 국민은행 예시)
  const markerPosition = new window.kakao.maps.LatLng(37.497942, 127.027621);
  const marker = new window.kakao.maps.Marker({
    position: markerPosition,
    map: map,
  });
}
</script>

<style scoped>
.main-page-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-start;
}

.content-area {
  flex: 2;
  padding: 20px;
  background-color: #f8f9fa;
}

.map-area {
  flex: 1;
  padding: 10px;
  background-color: #e9ecef;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
}

#mini-map {
  width: 100%;
  height: 200px; /* 미니맵의 높이 설정 */
  border-radius: 8px;
}

/* Go to Map 버튼 스타일 */
.go-to-map-btn {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%); /* 중앙 정렬을 위한 transform */
  padding: 10px 20px; /* 버튼의 크기를 키우기 위해 padding을 조절 */
  font-size: 16px; /* 글자 크기를 조금 더 키움 */
  color: black;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  z-index: 10; /* 지도 위에 버튼을 보이도록 설정 */
}

.go-to-map-btn:hover {
  background-color: #0056b3;
}
</style>
