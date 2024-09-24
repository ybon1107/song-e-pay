<template>
  <div>
    <h1>금융 지도</h1>
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
  data() {
    return {
      map: null,
      address: '',
      infoWindow: null,
      marker: null,
    };
  },
  mounted() {
    this.loadNaverMap();
  },
  methods: {
    loadNaverMap() {
      if (window.naver && window.naver.maps) {
        this.initMap();
      } else {
        const script = document.createElement('script');
        script.src =
          'https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=YOUR_CLIENT_ID';
        script.async = true;
        script.onload = () => {
          this.initMap();
        };
        document.head.appendChild(script);
      }
    },
    initMap() {
      const mapOptions = {
        center: new naver.maps.LatLng(37.3595316, 127.1052133), // 기본 좌표
        zoom: 15,
        mapTypeControl: true,
      };

      this.map = new naver.maps.Map('map', mapOptions);

      this.marker = new naver.maps.Marker({
        map: this.map,
        position: this.map.getCenter(),
      });

      this.infoWindow = new naver.maps.InfoWindow({
        anchorSkew: true,
      });

      // 지도 클릭 시 좌표 검색
      naver.maps.Event.addListener(this.map, 'click', (e) => {
        this.searchCoordinateToAddress(e.coord);
      });
    },
    searchCoordinateToAddress(latlng) {
      naver.maps.Service.reverseGeocode(
        {
          coords: latlng,
          orders: [
            naver.maps.Service.OrderType.ADDR,
            naver.maps.Service.OrderType.ROAD_ADDR,
          ].join(','),
        },
        (status, response) => {
          if (status !== naver.maps.Service.Status.OK) {
            alert('주소를 찾을 수 없습니다.');
            return;
          }

          const items = response.v2.results;
          const address = this.makeAddress(items[0]);

          this.marker.setPosition(latlng);
          this.map.setCenter(latlng);

          this.infoWindow.setContent(
            `<div style="padding:10px;">${address}</div>`
          );
          this.infoWindow.open(this.map, this.marker);
        }
      );
    },
    searchAddress() {
      if (!this.address) {
        alert('주소를 입력하세요');
        return;
      }

      naver.maps.Service.geocode(
        {
          query: this.address,
        },
        (status, response) => {
          if (status !== naver.maps.Service.Status.OK) {
            alert('주소를 찾을 수 없습니다.');
            return;
          }

          const result = response.v2.addresses[0];
          const point = new naver.maps.LatLng(result.y, result.x);

          this.map.setCenter(point);
          this.marker.setPosition(point);
          this.infoWindow.setContent(
            `<div style="padding:10px;">${result.roadAddress || result.jibunAddress}</div>`
          );
          this.infoWindow.open(this.map, this.marker);
        }
      );
    },
    makeAddress(item) {
      if (!item) return '';
      const region = item.region;
      const land = item.land;
      let address = `${region.area1.name} ${region.area2.name} ${region.area3.name}`;

      if (land) {
        if (land.type === '2') address += ' 산';
        address += ` ${land.number1}`;
        if (land.number2) address += `-${land.number2}`;
      }

      return address;
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
