import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/home/Home.vue';
import MyAccounts from '../views/myAccounts/MyAccounts.vue';
import SetAlert from '../views/exchangeRate/SetAlert.vue';
import Payment from '../views/payment/Payment.vue';
import PassWordInputPage from '../views/payment/PasswordInputPage.vue';
import QrScanPage from '../views/payment/QrScanPage.vue';
import Histories from '../views/Histories/Histories.vue';
import Maps from '../views/maps/Maps.vue';
// import Dashboard from "../views/Dashboard.vue";
// import Tables from "../views/Tables.vue";
// import Billing from "../views/Billing.vue";
// import VirtualReality from "../views/VirtualReality.vue";
// import RTL from "../views/Rtl.vue";
import Profile from '../views/profile/Profile.vue';
import ChangePwd from '../views/profile/ChangePassword.vue';
import Register from '../views/signUp/RegisterLegal.vue';
import Registeremail from '../views/signUp/Registeremail.vue';
import Login from '../views/signIn/Login.vue';
import ExchangeRate from '../views/exchangeRate/ExchangeRate.vue';
import ExchangeRateChart from '../views/Chart/ExchangeRateChart.vue';
import LoginPhone from '../views/signIn/LoginPhone.vue';

// map 컴포넌트 추가
import MapComponent from '../views/maps/MapComponent.vue';
import ErrorPage from '../views/error/ErrorPage.vue';

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
        path: '/set-alert',
        name: 'SetAlert',
        component: SetAlert,
    },
    {
        path: '/payment',
        name: 'Payment',
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
        component: Histories,
    },
    {
        path: '/maps',
        name: 'Maps',
        component: Maps,
    },
    {
        path: '/profile',
        name: 'Profile',
        component: Profile,
    },
    // map컴포넌트 추가
    {
        path: '/map',
        name: 'MapComponent',
        component: MapComponent,
    },
    {
        path: '/map',
        name: 'MapComponent',
        component: MapComponent,
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
        path: '/login/phone',
        name: 'LoginPhone',
        component: LoginPhone,
    },
    {
        path: '/register/legal',
        name: 'Register',
        component: Register,
    },
    {
        path: '/register/email',
        name: 'Registeremail',
        component: Registeremail,
    },
    {
        path: '/error',
        name: 'ErrorPage',
        component: ErrorPage,
    },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
    linkActiveClass: 'active',
    linkActiveClass: 'active',
});

export default router;
