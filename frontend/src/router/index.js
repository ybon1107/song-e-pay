import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/home/Home.vue';
import MyAccounts from '../views/myAccounts/MyAccounts.vue';
import ExchangeRate from '../views/exchangeRate/ExchangeRate.vue';
import SetAlert from "../views/exchangeRate/SetAlert.vue";
import Payments from '../views/payments/Payments.vue';
import PassWordInputPage from "../views/payments/PasswordInputPage.vue"; 
import QrScanPage from "../views/payments/QrScanPage.vue"; 
import Histories from '../views/Histories/Histories.vue';
import Maps from '../views/Maps/Maps.vue';
// import Dashboard from "../views/Dashboard.vue";
// import Tables from "../views/Tables.vue";
// import Billing from "../views/Billing.vue";
// import VirtualReality from "../views/VirtualReality.vue";
// import RTL from "../views/Rtl.vue";
import Profile from "../views/profile/Profile.vue";
import ChangePwd from "../views/profile/ChangePassword.vue";
import Register from "../views/signUp/RegisterLegal.vue";
import Registeremail from "../views/signUp/Registeremail.vue";
import Login from "../views/signIn/Login.vue";

const routes = [
  {
    path: '/',
    name: '/',
    redirect: '/home',
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
  },
  {
    path: '/my-accounts',
    name: 'MyAccounts',
    component: MyAccounts,
  },
  {
    path: '/exchange-rate',
    name: 'ExchangeRate',
    component: ExchangeRate,
  },
  {
    path: "/set-alert",
    name: "SetAlert",
    component: SetAlert,
  },
  {
    path: '/payments',
    name: 'Payments',
    component: Payments,
  },
  {
    path: '/payments/pw',
    name: 'Password',
    component: PassWordInputPage,
  },
  {
    path: '/payments/qr',
    name: 'Qr',
    component: QrScanPage,
  },
  {
    path: '/histories',
    name: 'Histories',
    component: Histories,
  },
  {
    path: '/maps',
    name: 'Maps',
    component: Maps,
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
  },
  {
    path: "/change-password",
    name: "ChangePwd",
    component: ChangePwd,
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: "/register/legal",
    name: "Register",
    component: Register,
  },
  {
    path: "/register/email",
    name: "Registeremail",
    component: Registeremail,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  linkActiveClass: 'active',
});

export default router;
