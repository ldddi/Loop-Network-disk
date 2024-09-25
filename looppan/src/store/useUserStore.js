import { defineStore } from "pinia";
import { reactive } from "vue";

export const useUserStore = defineStore("User", () => {
  let user = reactive({
    is_login: true,
  });

  return {
    user,
  };
});
