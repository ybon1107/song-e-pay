<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import { useStore } from "vuex";

import SidenavItem from "./SidenavItem.vue";
import SidenavCard from "./SidenavCard.vue";

const store = useStore();
const isRTL = computed(() => store.state.isRTL);

const getRoute = () => {
  const route = useRoute();
  const routeArr = route.path.split("/");
  return routeArr[1];
};
</script>
<template>
  <div
    class="collapse navbar-collapse w-auto h-auto h-100"
    id="sidenav-collapse-main"
  >
    <ul class="navbar-nav">
      <li class="nav-item">
        <sidenav-item
          to="/my-page"
          :class="getRoute() === 'my-page' ? 'active' : ''"
          :navText="isRTL ? 'الجداول' : 'sidenavList--myPage'"
        >
          <template v-slot:icon>
            <i class="ni ni-money-coins text-dark text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>

      <li class="nav-item">
        <sidenav-item
          to="/exchange-rate"
          :class="getRoute() === 'exchange-rate' ? 'active' : ''"
          :navText="isRTL ? 'الفواتیر' : 'sidenavList--exchangeRate'"
        >
          <template v-slot:icon>
            <i class="ni ni-chart-bar-32 text-dark text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>

      <!-- <li class="nav-item">
        <sidenav-item to="/payment" :class="getRoute() === 'payment' ? 'active' : ''"
          :navText="isRTL ? 'الواقع الافتراضي' : 'sidenavList--payment'">
          <template v-slot:icon>
            <i class="ni ni-credit-card text-dark text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li> -->

      <li class="nav-item">
        <sidenav-item
          to="/histories"
          :class="getRoute() === 'histories' ? 'active' : ''"
          :navText="isRTL ? 'الواقع الافتراضي' : 'sidenavList--history'"
        >
          <template v-slot:icon>
            <i class="ni ni-collection text-dark text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>

      <li class="nav-item">
        <sidenav-item
          to="/maps"
          :class="getRoute() === 'maps' ? 'active' : ''"
          :navText="isRTL ? 'الواقع الافتراضي' : 'sidenavList--map'"
        >
          <template v-slot:icon>
            <i class="ni ni-pin-3 text-dark text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>

      <li class="nav-item">
        <sidenav-item
          to="/reservation"
          :class="getRoute() === 'reservation' ? 'active' : ''"
          :navText="isRTL ? 'الواقع الافتراضي' : 'sidenavList--reservation'"
        >
          <template v-slot:icon>
            <i class="ni ni-cart text-dark text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>

      <li class="nav-item">
        <sidenav-item
          to="/schedule"
          :class="getRoute() === 'schedule' ? 'active' : ''"
          :navText="isRTL ? 'الواقع الافتراضي' : 'sidenavList--schedule'"
        >
          <template v-slot:icon>
            <i class="ni ni-calendar-grid-58 text-dark text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>
      <!-- <li class="mt-3 nav-item">
        <h6
          v-if="isRTL"
          class="text-xs ps-4 text-uppercase font-weight-bolder opacity-6"
          :class="isRTL ? 'me-4' : 'ms-2'"
        >
          صفحات المرافق
        </h6>

        <h6
          v-else
          class="text-xs ps-4 text-uppercase font-weight-bolder opacity-6"
          :class="isRTL ? 'me-4' : 'ms-2'"
        >
          ACCOUNT PAGES
        </h6>
      </li>

      <li class="nav-item">
        <sidenav-item
          to="/profile"
          :class="getRoute() === 'profile' ? 'active' : ''"
          :navText="isRTL ? 'حساب تعريفي' : 'Profile'"
        >
          <template v-slot:icon>
            <i class="ni ni-single-02 text-dark text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>

      <li class="nav-item">
        <sidenav-item
          to="/signin"
          :class="getRoute() === 'signin' ? 'active' : ''"
          :navText="isRTL ? 'تسجيل الدخول' : 'Sign In'"
        >
          <template v-slot:icon>
            <i class="ni ni-single-copy-04 text-danger text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li>

      <li class="nav-item">
        <sidenav-item
          to="/signup"
          :class="getRoute() === 'signup' ? 'active' : ''"
          :navText="isRTL ? 'اشتراك' : 'Sign Up'"
        >
          <template v-slot:icon>
            <i class="ni ni-collection text-info text-sm opacity-10"></i>
          </template>
        </sidenav-item>
      </li> -->
    </ul>
  </div>

  <!-- <div class="pt-3 mx-3 mt-3 sidenav-footer">
    
    <sidenav-card :card="{
      title: 'Need Help?',
      description: 'Please check our docs',
      links: [
        {
          label: 'Documentation',
          route:
            'https://www.creative-tim.com/learning-lab/vue/overview/argon-dashboard/',
          color: 'dark',
        },
        {
          label: 'Buy now',
          route:
            'https://www.creative-tim.com/product/vue-argon-dashboard-pro?ref=vadp',
          color: 'success',
        },
      ],
    }" />
  </div> -->
</template>
