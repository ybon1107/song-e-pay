<template>
  <div>
    <!-- 지도 영역 -->
    <div
      class="map-container"
      @click="goToMaps"
      @mouseover="showOverlay"
      @mouseleave="hideOverlay"
    >
      <div id="map" class="map-shadow"></div>
      <div v-if="overlayVisible" class="map-overlay">
        <p>지도로 이동</p>
      </div>
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
      overlayVisible: false, // 오버레이 표시 상태
      apiLoaded: false // API 로드 상태를 추적하기 위한 새로운 데이터 속성
    };
  },
  mounted() {
    this.loadGoogleMapsAPI();
  },
  methods: {
    loadGoogleMapsAPI() {
      if (!this.apiLoaded) {
        const script = document.createElement('script');
        script.src = `https://maps.googleapis.com/maps/api/js?key=${import.meta.env.VITE_GOOGLE_MAP_API_KEY}&libraries=places,advanced-markers`;
        script.async = true;
        script.defer = true;
        script.onload = () => {
          this.apiLoaded = true;
          this.initMap();
        };
        document.head.appendChild(script);
      } else {
        this.initMap();
      }
    },
    initMap() {
      this.map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: 37.5665, lng: 126.978 }, // Initial map center at Seoul
        zoom: 12,
      });
      this.service = new google.maps.places.PlacesService(this.map);
      this.geocoder = new google.maps.Geocoder(); // Initialize Geocoder
    },
    goToMaps() {
      // Maps.vue로 이동
      this.$router.push({ name: 'Maps' });
    },
    showOverlay() {
      // 마우스 오버 시 오버레이 표시
      this.overlayVisible = true;
    },
    hideOverlay() {
      // 마우스가 벗어나면 오버레이 숨기기
      this.overlayVisible = false;
    },
  },
};
</script>

<style scoped>
.map-container {
  position: relative;
  width: 100%;
  height: 300px;
  cursor: pointer;
}

.map-shadow {
  box-shadow: 10px 10px 30px rgba(0, 0, 0, 0.3); /* 그림자 */
  border-radius: 10px; /* 모서리 둥글게 */
  width: 100%;
  height: 100%;
}

.map-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 반투명 회색 배경 */
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 10px;
  font-size: 20px;
}

.map-overlay p {
  margin: 0;
}

/* 반응형 디자인 */
@media only screen and (max-width: 768px) {
  .map-container {
    height: 200px;
  }
}

@media only screen and (max-width: 480px) {
  .map-container {
    height: 150px;
  }
}
</style>
