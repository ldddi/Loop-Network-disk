import router from "@/router";
import axios from "axios";
import { defineStore } from "pinia";
import { reactive } from "vue";

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

  const api = {
    GetUserInfo: "/api/getUserInfo",
  };

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
      const response = await axios({
        method: "POST",
        url: api.GetUserInfo,
        headers: {
          Authorization: "Bearer " + jwtToken,
        },
      });
      console.log(response.data);
      updateUser({
        ...response.data,
        is_login: true,
      });
      router.push({ name: "HomeAll" });
    } catch (error) {
      console.log("faild getuesrinfo");
      localStorage.removeItem("jwtToken");
      console.log(error);
    }
  };

  return {
    user,
    updateUser,
    logOut,
    getUserInfoByLocalJwt,
  };
});
