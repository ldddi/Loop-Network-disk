import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import MainHome from "@/views/MainHome.vue";
import HomeNav from "@/components/HomeNav.vue";
import ShareNav from "@/components/ShareNav.vue";
import RecycleNav from "@/components/RecycleNav.vue";
import HomeAll from "@/views/HomeAll.vue";
import HomeImage from "@/views/HomeImage.vue";
import HomeAudio from "@/views/HomeAudio.vue";
import HomeDocument from "@/views/HomeDocument.vue";
import HomeMore from "@/views/HomeMore.vue";
import HomeVideo from "@/views/HomeVideo.vue";
import ShareRecord from "@/views/ShareRecord.vue";
import MyRecycle from "@/views/MyRecycle.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/login", // 将根路径重定向到 /login
    },
    {
      path: "/login",
      name: "LoginView",
      component: LoginView,
    },
    {
      path: "/register",
      name: "RegisterView",
      component: RegisterView,
    },
    {
      path: "/mainHome",
      name: "MainHome",
      component: MainHome,
    },
    {
      path: "/home",
      name: "Home",
      component: HomeNav,
    },
    {
      path: "/share/myrecord",
      name: "ShareRecord",
      component: ShareRecord,
    },
    {
      path: "/recycle",
      name: "MyRecycle",
      component: MyRecycle,
    },
    {
      path: "/home/all",
      name: "HomeAll",
      component: HomeAll,
    },
    {
      path: "/home/video",
      name: "HomeVideo",
      component: HomeVideo,
    },
    {
      path: "/home/audio",
      name: "HomeAudio",
      component: HomeAudio,
    },
    {
      path: "/home/image",
      name: "HomeImage",
      component: HomeImage,
    },
    {
      path: "/home/document",
      name: "HomeDocument",
      component: HomeDocument,
    },
    {
      path: "/home/more",
      name: "HomeMore",
      component: HomeMore,
    },
  ],
});

export default router;
