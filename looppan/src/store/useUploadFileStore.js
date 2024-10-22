import { defineStore } from "pinia";
import { reactive } from "vue";

export const useUploadFileStore = defineStore("uploadFile", () => {
  const files = reactive([]);

  return {
    files,
  };
});
