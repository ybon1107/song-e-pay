<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { NaverMap, NaverMarker } from 'vue3-naver-maps';

// 지도 기본 설정
const mapOptions = ref({
  center: { lat: 37.5665, lng: 126.978 }, // 서울 중심 좌표
  zoom: 13,
});

// 금융 환경 목록을 저장할 배열
const envList = ref([]);

// Client ID 및 Client Secret
const clientId = '6f3b9bb2-0843-45a9-a872-12bbe9b1e3bf';
const clientSecret = '04e47ee5-9f50-41b0-88f5-1b7f4b947b9f'; // 실제 Client Secret 값을 사용해야 함

// API 토큰을 발급받는 함수
const getAccessToken = async () => {
  const tokenUrl = 'https://openapi.finmap.or.kr/v1.0/kftc/inquiry/env_lists';
  const data = new URLSearchParams();
  data.append('grant_type', 'client_credentials');
  data.append('client_id', clientId);
  data.append('client_secret', clientSecret);

  try {
    const response = await axios.post(tokenUrl, data.toString(), {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    return response.data.access_token; // 발급받은 Access Token 반환
  } catch (error) {
    console.error('Failed to get access token', error);
    return null;
  }
};

// API 요청을 통해 금융 환경 목록을 가져오는 함수
const fetchEnvList = async (accessToken) => {
  const apiUrl = 'https://openapi.finmap.or.kr/v1.0/inquiry/brch_detail';

  try {
    const response = await axios.post(
      apiUrl,
      {}, // 필요한 경우 요청 본문에 데이터 추가
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      }
    );

    if (response.data.rsp_code === '000') {
      envList.value = response.data.env_list;
    } else {
      console.error('API Error:', response.data.rsp_message);
    }
  } catch (error) {
    console.error('Failed to fetch env data:', error);
  }
};

// 컴포넌트가 마운트될 때 API 요청을 보냅니다.
onMounted(async () => {
  const accessToken = await getAccessToken(); // Access Token을 먼저 발급받습니다.
  if (accessToken) {
    fetchEnvList(accessToken); // Access Token으로 금융 환경 정보를 가져옵니다.
  }
});
</script>

<template>
  <div class="container">
    <h2>금융 환경 지도</h2>

    <!-- 네이버 지도 -->
    <naver-map style="width: 100%; height: 400px" :map-options="mapOptions">
      <!-- 금융 환경 목록에서 마커 표시 -->
      <naver-marker
        v-for="env in envList"
        :key="env.env_code"
        :latitude="env.latitude"
        :longitude="env.longitude"
        :title="env.name"
      />
    </naver-map>

    <!-- 금융 환경 목록 -->
    <ul>
      <li v-for="env in envList" :key="env.env_code">
        {{ env.name }} ({{ env.latitude }}, {{ env.longitude }})
      </li>
    </ul>
  </div>
</template>

<style scoped>
.container {
  margin: 20px;
}
</style>
