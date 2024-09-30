import { defineStore } from "pinia";
import { reactive } from "vue";

export const useAlertStore = defineStore("alert", () => {
  const error = reactive({
    isVisible: false,
    message: "",
  });

  const success = reactive({
    isVisible: false,
    message: "",
  });

  return {
    error,
    success,
  };
});
