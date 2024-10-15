import { createRouter, createWebHistory } from 'vue-router';
import MyPage from '../views/myPage/MyPage.vue';
import Payment from '../views/payment/Payment.vue';
import PassWordInputPage from '../views/payment/PasswordInputPage.vue';
import QrScanPage from '../views/payment/QrScanPage.vue';
import Histories from '../views/Histories/Histories.vue';
import Maps from '../views/maps/Maps.vue';
import Profile from '../views/profile/Profile.vue';
import ChangePwd from '../views/profile/ChangePassword.vue';
import Login from '../views/signIn/Login.vue';
import LoginIssueInfo from '../views/signIn/LoginIssueInfo.vue';
import RegisterLegal from '../views/signUp/RegisterLegal.vue';
import RegisterDetails from '../views/signUp/RegisterDetails.vue';
import RegisterSuccess from '../views/signUp/RegisterSuccess.vue';
import ExchangeRate from '../views/exchangeRate/ExchangeRate.vue';
import MainPage from '../views/main/Main.vue';
import ErrorPage from '../views/error/ErrorPage.vue';
import SchedulePage from '../views/Schedule/Schedule.vue';
import Reservation from '../views/reservation/Reservation.vue';
import Accommodation from '../views/reservation/Accommodation.vue';
import ReservationPayment from '../views/reservation/ReservationPayment.vue';
import AdminPage from '../views/admin/Admin.vue';

const routes = [
  {
    path: '/',
    name: 'Main',
    component: MainPage,
  },
  {
    path: '/my-page',
    name: 'MyPage',
    meta: {
      requiresAuth: true,
    },
    component: MyPage,
  },
  {
    path: '/exchange-rate',
    name: 'ExchangeRate',
    meta: {
      requiresAuth: true,
    },
    component: ExchangeRate,
  },
  {
    path: '/payment',
    name: 'Payment',
    meta: {
      requiresAuth: true,
    },
    component: Payment,
  },
  {
    path: '/payment/password',
    name: 'Password',
    component: PassWordInputPage,
  },
  {
    path: '/payment/qr',
    name: 'Qr',
    component: QrScanPage,
  },
  {
    path: '/histories',
    name: 'Histories',
    meta: {
      requiresAuth: true,
    },
    component: Histories,
  },
  {
    path: '/maps',
    name: 'Maps',
    meta: {
      requiresAuth: true,
    },
    component: Maps,
  },
  {
    path: '/profile',
    name: 'Profile',
    meta: {
      requiresAuth: true,
    },
    component: Profile,
  },
  {
    path: '/change-password',
    name: 'ChangePwd',
    component: ChangePwd,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/login/issue-info',
    name: 'LoginIssueInfo',
    component: LoginIssueInfo,
  },
  {
    path: '/register/legal',
    name: 'RegisterLegal',
    component: RegisterLegal,
  },
  {
    path: '/register/detail',
    name: 'RegisterDetails',
    component: RegisterDetails,
  },
  {
    path: '/register/success',
    name: 'RegisterSuccess',
    component: RegisterSuccess,
  },
  {
    path: '/error',
    name: 'ErrorPage',
    component: ErrorPage,
  },
  {
    path: '/main',
    name: 'MainPage',
    component: MainPage,
  },
  {
    path: '/schedule',
    name: 'SchedulePage',
    meta: {
      requiresAuth: true,
    },
    component: SchedulePage,
  },
  {
    path: '/reservation',
    name: 'Reservation',
    meta: {
      requiresAuth: true,
    },
    component: Reservation,
  },
  {
    path: '/accommodation/:no',
    name: 'Accommodation',
    meta: {
      requiresAuth: true,
    },
    component: Accommodation,
  },
  {
    path: '/reservationpayment',
    name: 'ReservationPayment',
    meta: {
      requiresAuth: true,
    },
    component: ReservationPayment,
  },
  {
    path: '/admin',
    name: 'AdminPage',
    meta: {
      requiresAuth: true,
    },
    component: AdminPage,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  linkActiveClass: 'active',
});

router.beforeEach((to, from, next) => {
  const auth = JSON.parse(localStorage.getItem('auth'));
  const token = auth ? auth.token : null;

  // 로그인이 필요한 페이지에 접근하려고 할 때
  if (to.meta.requiresAuth && !token) {
    return next({
      name: 'Login',
      state: { redirect: to.fullPath },
    });
  }

  if (to.name === 'Password' && from.name !== 'Payment') {
    return next({
      name: 'Payment',
    });
  }

  if ((to.name === 'Qr') && from.name !== 'Password') {
    return next({
      name: 'Payment',
    });
  }

  if ((to.name === 'change-password') && from.name !== 'Profile') {
    return next({
      name: 'Profile',
    });
  }

  if (to.name === 'Login' && token) {
    return next({
      name: 'MyPage',
    });
  }

  next();
});

export default router;
