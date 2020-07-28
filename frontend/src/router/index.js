import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import Welcome from "../views/Welcome.vue";
import SignUp from "../components/user/SignUp.vue";
import Findpw from "../components/user/Findpw.vue";
import Login from "../components/user/Login.vue";
import Profile from "../components/user/Profile.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/welcome",
    name: "Welcome",
    component: Welcome,
  },
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue"),
  },
  {
    path: "/signup",
    name: "SignUp",
    component: SignUp,
  },
  {
    path: "/findpw",
    name: "Findpw",
    component: Findpw,
  },
  {
    path: "/login",
    name: Login,
    component: Login,
  },
  {
    path: "/profile",
    name: Profile,
    component: Profile,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
