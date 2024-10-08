<!--
=========================================================
* Vue Argon Dashboard 2 - v4.0.0
=========================================================

* Product Page: https://creative-tim.com/product/vue-argon-dashboard
* Copyright 2024 Creative Tim (https://www.creative-tim.com)

Coded by www.creative-tim.com

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
-->
<script setup>
import { computed } from 'vue';
import { useStore } from 'vuex';
import Sidenav from './components/sidenav';
// import Configurator from "./views/Configurator.vue";
import Navbar from './components/navbars/Navbar.vue';
import AppFooter from './components/footer/Footer.vue';

const store = useStore();
const isNavFixed = computed(() => store.state.isNavFixed);
const darkMode = computed(() => store.state.darkMode);
const isAbsolute = computed(() => store.state.isAbsolute);
const showSidenav = computed(() => store.state.showSidenav);
const layout = computed(() => store.state.layout);
const showNavbar = computed(() => store.state.showNavbar);
const showFooter = computed(() => store.state.showFooter);
// const showConfig = computed(() => store.state.showConfig);
// const hideConfigButton = computed(() => store.state.hideConfigButton);
// const toggleConfigurator = () => store.commit("toggleConfigurator");

const navClasses = computed(() => {
  return {
    'position-sticky bg-white left-auto top-2 z-index-sticky':
      isNavFixed.value && !darkMode.value,
    'position-sticky bg-default left-auto top-2 z-index-sticky':
      isNavFixed.value && darkMode.value,
    'position-absolute px-4 mx-0 w-100 z-index-2': isAbsolute.value,
    'px-0 mx-4': !isAbsolute.value,
  };
});
</script>
<template>
  <div
    v-show="layout === 'landing'"
    class="landing-bg h-100 bg-gradient-primary position-fixed w-100"
  ></div>

  <sidenav v-if="showSidenav" />

  <main
    class="main-content position-relative max-height-vh-100 h-100 border-radius-lg"
  >
    <!-- nav -->

    <navbar :class="[navClasses]" v-if="showNavbar" />

    <router-view />

    <app-footer v-show="showFooter" />

    <!-- <configurator
      :toggle="toggleConfigurator"
      :class="[showConfig ? 'show' : '', hideConfigButton ? 'd-none' : '']"
    /> -->
    <!-- 모달을 여기로 이동 -->
    <teleport to="body">
      <div id="modal-container"></div>
    </teleport>
  </main>
</template>

<style>
@font-face {
  font-family: 'Pretendard-Regular';
  src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff')
    format('woff');
  font-weight: 400;
  font-style: normal;
}

h1 {
  font-family: 'TTLaundryGothicB', sans-serif;
}
</style>
