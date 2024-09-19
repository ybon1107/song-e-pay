import { createRouter, createWebHistory } from "vue-router";
import Home from "@/views/Home/Home.vue";
import MyAccounts from "@/views/MyAccounts/MyAccounts.vue";
import ExchangeRate from "@/views/ExchangeRate/ExchangeRate.vue";
import Payments from "@/views/Payments/Payments.vue";
import Histories from "@/views/Histories/Histories.vue";
import Maps from "@/views/Maps/Maps.vue";
// import Dashboard from "../views/Dashboard.vue";
// import Tables from "../views/Tables.vue";
// import Billing from "../views/Billing.vue";
// import VirtualReality from "../views/VirtualReality.vue";
// import RTL from "../views/Rtl.vue";
import Profile from "../views/profile/Profile.vue";
import ChangePwd from "../views/profile/ChangePassword.vue";
// import Signup from "../views/Signup.vue";
// import Signin from "../views/Signin.vue";

const routes = [
  {
    path: "/",
    name: "/",
    redirect: "/home",
  },
  {
    path: "/home",
    name: "Home",
    component: Home,
  },
  {
    path: "/my-accounts",
    name: "MyAccounts",
    component: MyAccounts,
  },
  {
    path: "/exchange-rate",
    name: "ExchangeRate",
    component: ExchangeRate,
  },
  {
    path: "/payments",
    name: "Payments",
    component: Payments,
  },
  {
    path: "/histories",
    name: "Histories",
    component: Histories,
  },
  {
    path: "/maps",
    name: "Maps",
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
  // {
  //   path: "/signin",
  //   name: "Signin",
  //   component: Signin,
  // },
  // {
  //   path: "/signup",
  //   name: "Signup",
  //   component: Signup,
  // },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  linkActiveClass: "active",
});

export default router;
