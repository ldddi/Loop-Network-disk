import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import HomeAll from "@/views/HomeAll.vue";
import HomeImage from "@/views/HomeImage.vue";
import HomeAudio from "@/views/HomeAudio.vue";
import HomeDocument from "@/views/HomeDocument.vue";
import HomeMore from "@/views/HomeMore.vue";
import HomeVideo from "@/views/HomeVideo.vue";
import ShareRecord from "@/views/ShareRecord.vue";
import MyRecycle from "@/views/MyRecycle.vue";
import { useUserStore } from "@/store/useUserStore";

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
      meta: {
        requestAuth: false,
      },
    },
    {
      path: "/register",
      name: "RegisterView",
      component: RegisterView,
      meta: {
        requestAuth: false,
      },
    },
    {
      path: "/share/myrecord",
      name: "ShareRecord",
      component: ShareRecord,
      meta: {
        requestAuth: true,
      },
    },
    {
      path: "/recycle",
      name: "MyRecycle",
      component: MyRecycle,
      meta: {
        requestAuth: true,
      },
    },
    {
      path: "/home/all",
      name: "HomeAll",
      component: HomeAll,
      meta: {
        requestAuth: true,
      },
    },
    {
      path: "/home/video",
      name: "HomeVideo",
      component: HomeVideo,
      meta: {
        requestAuth: true,
      },
    },
    {
      path: "/home/audio",
      name: "HomeAudio",
      component: HomeAudio,
      meta: {
        requestAuth: true,
      },
    },
    {
      path: "/home/image",
      name: "HomeImage",
      component: HomeImage,
      meta: {
        requestAuth: true,
      },
    },
    {
      path: "/home/document",
      name: "HomeDocument",
      component: HomeDocument,
      meta: {
        requestAuth: true,
      },
    },
    {
      path: "/home/more",
      name: "HomeMore",
      component: HomeMore,
      meta: {
        requestAuth: true,
      },
    },
  ],
});

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  if (to.meta.requestAuth && !userStore.user.is_login) {
    next({ name: "LoginView" });
  } else {
    next(); // 继续导航
  }
});

export default router;
