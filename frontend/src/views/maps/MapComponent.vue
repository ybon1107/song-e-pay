<template>
  <div>
    <div>
      <label for="address">주소 검색: </label>
      <input
        type="text"
        id="address"
        v-model="address"
        placeholder="주소를 입력하세요"
      />
      <button @click="searchAddress">검색</button>
    </div>

    <div id="map" style="width: 100%; height: 400px"></div>
  </div>
</template>

<script>
export default {
  name: 'MapComponent',
  data() {
    return {
      address: '', // 입력한 주소
      map: null, // 네이버 지도 객체
      marker: null, // 마커 객체
      infoWindow: null, // 인포윈도우 객체
    };
  },
  mounted() {
    this.loadNaverMap();
  },
  methods: {
    loadNaverMap() {
      // 스크립트가 이미 로드된 경우 바로 지도 초기화
      if (window.naver && window.naver.maps) {
        this.initMap();
      } else {
        // 네이버 지도 스크립트 로드
        const script = document.createElement('script');
        script.src =
          'https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=your-client-id&submodules=geocoder';
        script.async = true;
        script.onload = () => {
          this.initMap();
        };
        script.onerror = () => {
          console.error('Failed to load Naver Maps API.');
        };
        document.head.appendChild(script);
      }
    },
    initMap() {
      // 지도 생성
      const mapOptions = {
        center: new naver.maps.LatLng(37.3595704, 127.105399),
        zoom: 10,
      };
      this.map = new naver.maps.Map('map', mapOptions);

      // 마커 및 인포윈도우 초기화
      this.marker = new naver.maps.Marker({
        position: mapOptions.center,
        map: this.map,
      });

      this.infoWindow = new naver.maps.InfoWindow({
        anchorSkew: true,
      });
    },
    searchAddress() {
      if (!this.address) {
        alert('주소를 입력하세요');
        return;
      }

      // 주소를 좌표로 변환
      naver.maps.Service.geocode(
        {
          query: this.address,
        },
        (status, response) => {
          if (status === naver.maps.Service.Status.ERROR) {
            return alert('Something went wrong!');
          }

          if (response.v2.meta.totalCount === 0) {
            return alert('검색된 주소가 없습니다.');
          }

          const result = response.v2.addresses[0];
          const point = new naver.maps.LatLng(result.y, result.x);

          // 지도 중심 이동
          this.map.setCenter(point);

          // 마커 이동
          this.marker.setPosition(point);

          // 인포윈도우에 검색된 주소 표시
          const htmlContent = `
          <div style="padding:10px;min-width:200px;line-height:150%;">
            <h4>검색 주소</h4>
            [지번 주소] ${result.jibunAddress}<br/>
            [도로명 주소] ${result.roadAddress || '없음'}
          </div>
        `;
          this.infoWindow.setContent(htmlContent);
          this.infoWindow.open(this.map, this.marker);
        }
      );
    },
  },
};
</script>

<style scoped>
#map {
  width: 100%;
  height: 400px;
}
</style>
