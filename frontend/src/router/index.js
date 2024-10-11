import { createRouter, createWebHistory } from 'vue-router';
<<<<<<< HEAD
=======
// import Home from '../views/home/Home.vue';
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
import MyAccounts from '../views/myAccounts/MyAccounts.vue';
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
<<<<<<< HEAD
import SchedulePage from '../views/Schedule/Schedule.vue';

const routes = [
  {
    path: '/',
    name: 'Main',
    component: MainPage,
  },
  {
    path: '/my-accounts',
    name: 'MyAccounts',
    meta: {
      requiresAuth: true,
    },
    component: MyAccounts,
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
=======
const routes = [
    {
        path: '/',
        name: '/',
        redirect: '/my-accounts',
    },
    // {
    //   path: '/home',
    //   name: 'Home',
    //   component: Home,
    // },
    {
        path: '/my-accounts',
        name: 'MyAccounts',
        component: MyAccounts,
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
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

  if ((to.name === 'Password' || to.name === 'Qr') && from.name !== 'Payment') {
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
      name: 'MyAccounts',
    });
  }

  next();
});

export default router;
