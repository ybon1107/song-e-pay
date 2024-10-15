<script setup>
import { computed } from 'vue';
import { useStore } from 'vuex';
import SidenavList from './SidenavList.vue';
import songelogo from '@/assets/img/logos/SongE_pay.png';

const store = useStore();
const isRTL = computed(() => store.state.isRTL);
const layout = computed(() => store.state.layout);
const sidebarType = computed(() => store.state.sidebarType);
const darkMode = computed(() => store.state.darkMode);
</script>
<template>
    <!-- 기존 초록색 배경 부분 -->
    <!-- <div
    v-show="layout === 'default'"
    class="min-height-300 position-absolute w-100"
    :class="`${darkMode ? 'bg-transparent' : 'bg-success'}`"
  /> -->

    <aside
        class="my-3 bg-custom overflow-auto border-end sidenav navbar navbar-vertical navbar-expand-xs border-radius-xl"
        :class="`${isRTL ? 'me-3 rotate-caret fixed-end' : 'fixed-start ms-3'}    
      ${
          layout === 'landing' ? 'bg-transparent shadow-none' : ' '
      } ${sidebarType}`"
        id="sidenav-main"
    >
        <!-- 로고 있는 버전 메뉴바 -->
        <div class="sidenav-header">
            <i
                class="top-0 p-3 cursor-pointer fas fa-times text-secondary opacity-5 position-absolute end-0 d-none d-xl-none"
                aria-hidden="true"
                id="iconSidenav"
            ></i>

            <router-link class="m-0 navbar-brand" to="/my-page">
                <!-- <img :src="darkMode || sidebarType === 'bg-default' ? logoWhite : logo" class="navbar-brand-img h-100"
          alt="main_logo" /> -->
                <!-- <span class="ms-2 font-weight-bold me-2">song-E-pay</span> -->
                <!-- <h3>SongE</h3> -->
                <!-- <img
              class="ld-none d-md-block"
              src="../../../images/3D_SongE.png"
              alt="Illustration"
              style="height: 40px; width: auto"
            /> -->
                <img
                    class="ld-none d-md-block"
                    :src="songelogo"
                    alt="Illustration"
                    style="height: 70px; width: auto"
                />
            </router-link>
        </div>

        <hr class="mt-0 horizontal dark" />
        <sidenav-list />
        <!-- <div class="py-5">
      <sidenav-list />
    </div> -->
    </aside>
</template>

<style>
/* 사이드바 색상 변경 */
/* .aside-total {
  background-color: #f8f9fa !important;
} */

/* @font-face {
  font-family: 'Paperlogy-8ExtraBold';
  src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/2408-3@1.0/Paperlogy-8ExtraBold.woff2') format('woff2');
  font-weight: 800;
  font-style: normal;
} */

/* custom-font 클래스를 통해 폰트 적용 */
/* .custom-font h3 {
  color: black;
  font-style: italic;
  font-family: 'Paperlogy-8ExtraBold', sans-serif;
  font-size: 25px;
} */
</style>
