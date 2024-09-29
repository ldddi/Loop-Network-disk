import router from "@/router";
import axios from "@/utils/axiosInstance";
import { defineStore } from "pinia";
import { reactive } from "vue";
import { useApiStore } from "./useApiStore";

export const useUserStore = defineStore("User", () => {
  let user = reactive({
    nickName: "",
    avatar: "",
    email: "",
    totalSpace: "",
    useSpace: "",
    token: "",
    is_login: false,
  });

  const apiStore = useApiStore();

  const updateUser = (data) => {
    user.nickName = data.nickName;
    user.email = data.email;
    user.totalSpace = data.totalSpace;
    user.useSpace = data.useSpace;
    user.avatar = data.avatar;
    user.token = data.token;
    user.is_login = data.is_login;
  };

  const logOut = () => {
    user.is_login = false;
    localStorage.removeItem("jwtToken");
    router.push({ name: "LoginView" });
  };

  const getUserInfoByLocalJwt = async (jwtToken) => {
    try {
      const resp = await axios.post(apiStore.user.getUserInfo, {});
      console.log(resp);

      updateUser({
        ...resp,
        token: jwtToken,
        is_login: true,
      });
      router.push({ name: "HomeAll" });
    } catch (error) {
      localStorage.removeItem("jwtToken");
      console.log(error.message);
    }
  };

  return {
    user,
    updateUser,
    logOut,
    getUserInfoByLocalJwt,
  };
});
