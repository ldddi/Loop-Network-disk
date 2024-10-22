import { defineStore } from "pinia";
import { reactive, ref } from "vue";

export const useUploadFileStore = defineStore("uploadFile", () => {
  const files = reactive([]);

  let fileId = ref(1);
  let isDropdownVisible = ref(false);
  return {
    files,
    fileId,
    isDropdownVisible,
  };
});
