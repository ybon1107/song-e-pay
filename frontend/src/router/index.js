import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/home/Home.vue';
import MyAccounts from '../views/myAccounts/MyAccounts.vue';
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
import Login from '../views/signIn/Login.vue';
import LoginPhone from '../views/signIn/LoginPhone.vue';
import LoginIssueInfo from '../views/signIn/LoginIssueInfo.vue';
import RegisterLegal from '../views/signUp/RegisterLegal.vue';
import RegisterEmail from '../views/signUp/RegisterEmail.vue';
import RegisterEmailCheck from '../views/signUp/RegisterEmailCheck.vue';
import RegisterEmailSuccess from '../views/signUp/RegisterEmailSuccess.vue';
import RegisterPhone from '../views/signUp/RegisterPhone.vue';
import RegisterPhoneSubmit from '../views/signUp/RegisterPhoneSubmit.vue';
import RegisterDetails from '../views/signUp/RegisterDetails.vue';
import CodeSendAnother from '../views/signIn/CodeSendAnother.vue';
import ExchangeRate from '../views/exchangeRate/ExchangeRate.vue';
import MainPage from '../views/main/Main.vue';
import SchedulePage from '../views/Schedule/Schedule.vue';
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
        path: '/login/phone',
        name: 'LoginPhone',
        component: LoginPhone,
    },
    {
        path: '/login/phone/another',
        name: 'LoginPhoneAnother',
        component: CodeSendAnother,
    },
    {
        path: '/register/legal',
        name: 'RegisterLegal',
        component: RegisterLegal,
    },
    {
        path: '/register/email',
        name: 'RegisterEmail',
        component: RegisterEmail,
    },
    {
        path: '/register/email/check',
        name: 'RegisterEmailCheck',
        component: RegisterEmailCheck,
    },
    {
        path: '/register/email/success',
        name: 'RegisterEmailSuccess',
        component: RegisterEmailSuccess,
    },
    {
        path: '/register/phone',
        name: 'RegisterPhone',
        component: RegisterPhone,
    },
    {
        path: '/register/phone/another',
        name: 'RegisterPhoneAnother',
        component: CodeSendAnother,
    },
    {
        path: '/register/phone/submit',
        name: 'RegisterPhoneSubmit',
        component: RegisterPhoneSubmit,
    },
    {
        path: '/register/detail',
        name: 'RegisterDetails',
        component: RegisterDetails,
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
        component: SchedulePage,
    },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
    linkActiveClass: 'active',
});

export default router;
