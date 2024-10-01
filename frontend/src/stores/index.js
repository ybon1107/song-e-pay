import { createStore } from 'vuex';
import 'bootstrap/dist/css/bootstrap.min.css';

export default createStore({
  state: {
    hideConfigButton: false,
    isPinned: false,
    showConfig: false,
    sidebarType: 'bg-white',
    isRTL: false,
    mcolor: '',
    darkMode: false,
    isNavFixed: false,
    isAbsolute: false,
    showNavs: true,
    showSidenav: true,
    showNavbar: true,
    showFooter: true,
    showMain: true,
    layout: 'default',
  },
  mutations: {
    toggleConfigurator(state) {
      state.showConfig = !state.showConfig;
    },
    sidebarMinimize(state) {
      let sidenav_show = document.querySelector('#app');
      if (state.isPinned) {
        sidenav_show.classList.add('g-sidenav-hidden');
        sidenav_show.classList.remove('g-sidenav-pinned');
        state.isPinned = false;
      } else {
        sidenav_show.classList.add('g-sidenav-pinned');
        sidenav_show.classList.remove('g-sidenav-hidden');
        state.isPinned = true;
      }
    },
    sidebarType(state, payload) {
      state.sidebarType = payload;
    },
    navbarFixed(state) {
      if (state.isNavFixed === false) {
        state.isNavFixed = true;
      } else {
        state.isNavFixed = false;
      }
    },
    updateSidenavVisibility(state, isVisible) {
      state.showSidenav = isVisible;
    },
  },
  actions: {
    toggleSidebarColor({ commit }, payload) {
      commit('sidebarType', payload);
    },
    checkSidenavVisibility({ commit }) {
      const currentPath = window.location.pathname; // 현재 경로를 가져옵니다.
      // 경로가 '/main'이면 showSidenav를 false로 설정
      if (currentPath === '/main') {
        commit('updateSidenavVisibility', false);
      } else {
        commit('updateSidenavVisibility', true);
      }
    },
  },
  getters: {},
});
