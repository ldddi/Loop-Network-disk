import { defineStore } from "pinia";

export const useApiStore = defineStore("api", () => {
  const user = {
    updateUserInfo: "/api/updateUserInfo",
    updatePassword: "/api/updatePassword",
    getUserInfo: "/api/getUserInfo",
    sendEmailCheckCode: "/api/sendEmailCode",
    getPicCheckCode: "/api/getPicCheckCode",
    login: "/api/login",
    emailCheckCode: "/api/sendEmailCode",
    register: "/api/register",
  };

  const file = {
    createFile: "/api/createFile",
  };

  return {
    user,
    file,
  };
});
