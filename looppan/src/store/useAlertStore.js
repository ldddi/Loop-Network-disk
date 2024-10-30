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

  const info = reactive({
    isVisible: false,
    message: "",
  });

  const load = reactive({
    isLoading: false,
  });

  return {
    error,
    success,
    load,
  };
});
