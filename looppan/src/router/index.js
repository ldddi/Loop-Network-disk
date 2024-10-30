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
import HomeNav from "@/components/HomeNav.vue";
import ShareNav from "@/components/ShareNav.vue";
import RecycleNav from "@/components/RecycleNav.vue";
import ShareCheckCode from "@/views/ShareCheckCode.vue";
import ShareFilesInfo from "@/views/ShareFilesInfo.vue";
import ForgetPassword from "@/views/ForgetPassword.vue";

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
      path: "/forgetPassword",
      name: "ForgetPassword",
      component: ForgetPassword,
      meta: {
        requestAuth: false,
      },
    },
    // ------- home --------------------------------
    {
      path: "/home",
      name: "HomeNav",
      component: HomeNav,
      meta: {
        requestAuth: true,
      },
      children: [
        {
          path: "",
          redirect: "/home/all", // 默认重定向到 /home/all
        },
        {
          path: "all",
          name: "HomeAll",
          component: HomeAll, // 处理访问 /all 的组件
          meta: {
            requestAuth: true,
          },
        },
        {
          path: "video",
          name: "HomeVideo",
          component: HomeVideo,
          meta: {
            requestAuth: true,
          },
        },
        {
          path: "audio",
          name: "HomeAudio",
          component: HomeAudio,
          meta: {
            requestAuth: true,
          },
        },
        {
          path: "image",
          name: "HomeImage",
          component: HomeImage,
          meta: {
            requestAuth: true,
          },
        },
        {
          path: "document",
          name: "HomeDocument",
          component: HomeDocument,
          meta: {
            requestAuth: true,
          },
        },
        {
          path: "more",
          name: "HomeMore",
          component: HomeMore,
          meta: {
            requestAuth: true,
          },
        },
      ],
    },
    // ------- share --------------------------------
    {
      path: "/share",
      name: "ShareNav",
      component: ShareNav,
      meta: {
        requestAuth: true,
      },
      children: [
        {
          path: "",
          redirect: "/share/myrecord",
        },
        {
          path: "myrecord",
          name: "ShareRecord",
          component: ShareRecord,
          meta: {
            requestAuth: true,
          },
        },
      ],
    },
    // ------- recycle --------------------------------
    {
      path: "/recycle",
      name: "RecycleNav",
      component: RecycleNav,
      meta: {
        requestAuth: true,
      },
      children: [
        {
          path: "",
          redirect: "/recycle/myrecycle",
        },
        {
          path: "myrecycle",
          name: "MyRecycle",
          component: MyRecycle,
          meta: {
            requestAuth: true,
          },
        },
      ],
    },
    {
      path: "/shareCheckCode/:fileId/:userId",
      name: "ShareCheckCode",
      component: ShareCheckCode,
    },
    {
      path: "/shareFilesInfo/:fileId/:userId",
      name: "ShareFilesInfo",
      component: ShareFilesInfo,
    },
  ],
});

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  if (to.meta.requestAuth && !userStore.user.is_login) {
    next({ name: "LoginView" });
  } else if (to.path === "/login" && !userStore.user.is_login) {
    const jwtToken = localStorage.getItem("jwtToken");
    if (jwtToken != null) {
      userStore.getUserInfoByLocalJwt(jwtToken).then((resp) => {
        if (resp == true) {
          router.push({ name: "HomeAll" });
        }
      });
    } else {
      next();
    }
  } else if (to.path === "/login" && userStore.user.is_login) {
    next({ name: "HomeAll" });
  } else if (to.name === "ShareFilesInfo" && !userStore.user.is_code_ok) {
    next({ path: `/shareCheckCode/${to.params.fileId}/${to.params.userId}` });
  } else {
    next();
  }
});

export default router;
