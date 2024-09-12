import { createRouter, createWebHistory } from "vue-router";
import Home from "@/views/Home.vue";
import MyAccounts from "@/views/MyAccounts.vue";
import ExchangeRate from "@/views/ExchangeRate.vue";
import Payments from "@/views/Payments.vue";
import Histories from "@/views/Histories.vue";
import Maps from "@/views/Maps.vue";
// import Dashboard from "../views/Dashboard.vue";
// import Tables from "../views/Tables.vue";
// import Billing from "../views/Billing.vue";
// import VirtualReality from "../views/VirtualReality.vue";
// import RTL from "../views/Rtl.vue";
// import Profile from "../views/Profile.vue";
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
  // {
  //   path: "/billing",
  //   name: "Billing",
  //   component: Billing,
  // },
  // {
  //   path: "/virtual-reality",
  //   name: "Virtual Reality",
  //   component: VirtualReality,
  // },
  // {
  //   path: "/rtl-page",
  //   name: "RTL",
  //   component: RTL,
  // },
  // {
  //   path: "/profile",
  //   name: "Profile",
  //   component: Profile,
  // },
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
