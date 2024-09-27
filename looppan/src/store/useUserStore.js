import { defineStore } from "pinia";
import { reactive } from "vue";

export const useUserStore = defineStore("User", () => {
  let user = reactive({
    nickName: "",
    avatar: "",
    token: "",
    is_login: true,
  });

  const updateUser = (data) => {
    user.nickName = data.nickName;
    user.avatar = data.avatar;
    user.token = data.token;
    user.is_login = data.is_login;
  };

  return {
    user,
    updateUser,
  };
});
