import router from "@/router";
import axios from "@/utils/axiosInstance";
import { defineStore } from "pinia";
import { reactive } from "vue";
import { useApiStore } from "./useApiStore";
import statickey from "@/utils/statickey";

export const useUserStore = defineStore("User", () => {
  let user = reactive({
    nickName: "",
    avatar: "",
    email: "",
    totalSpace: "",
    useSpace: "",
    token: "",
    is_login: false,
    is_code_ok: false,
    extraction_code: "",
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
    localStorage.removeItem(statickey.jwtToken);
    router.push({ name: "LoginView" });
  };

  const getUserInfoByLocalJwt = async (jwtToken) => {
    const resp = await axios.post(apiStore.user.getUserInfo, {});
    updateUser({
      ...resp,
      token: jwtToken,
      is_login: true,
    });
    router.push({ name: "HomeAll" });
  };

  return {
    user,
    updateUser,
    logOut,
    getUserInfoByLocalJwt,
  };
});
