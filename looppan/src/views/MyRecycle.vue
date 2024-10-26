<template>
  <div class="title">
    <button @click="cancelDelete" :disabled="!selectedFiles.length" type="button" class="btn btn-new">
      <i class="bi bi-arrow-clockwise"></i>
      还原
    </button>
    <button @click="deleteRecycleFiles" :disabled="!selectedFiles.length" type="button" class="btn btn-delete">
      <i class="bi bi-trash"></i>
      批量删除
    </button>
  </div>
  <div class="content">
    <div class="container">
      <div class="row myrow my-title">
        <div class="col-auto">
          <input v-model="isSelected" @change="selectAllFiles($event.target.checked)" class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-6 container-title">文件名</div>
        <div class="col-3 container-title">删除时间</div>
        <div class="col-2 container-title">大小</div>
      </div>

      <div v-for="file in files" :key="file.fileId" tabindex="0" class="row myrow">
        <div class="col-auto">
          <input @change="toggleSelection(file.fileId)" :checked="isFileSelected(file.fileId)" class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-4 my-fileName">
          <i v-if="file.fileCategory == statickey.category.folder" class="bi bi-folder2 my-floder my-floder-folder"></i>
          <i v-else-if="file.fileCategory == statickey.category.video" class="bi bi-file-earmark-play my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.audio" class="bi bi-file-music my-floder"></i>
          <!-- <i v-else-if="file.fileCategory == statickey.category.image" class="bi bi-images my-floder"></i> -->
          <img v-else-if="file.fileCategory == statickey.category.image" class="my-floder my-image" :src="file.fileCover" alt="" />

          <i v-else-if="file.fileCategory == statickey.category.document" class="bi bi-file-word my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.other" class="bi bi-file-earmark-medical my-floder"></i>
          <span>{{ file.fileName }}</span>
        </div>
        <div class="col-2 my-icon">
          <div @click="deleteRecycleFiles2(file)" class="copy-link">
            <i class="bi bi-trash"></i>
            <span>删除</span>
          </div>
          <div @click="cancelDelete2(file)" class="cancle-link">
            <i class="bi bi-arrow-clockwise"></i>
            <span>还原</span>
          </div>
        </div>
        <div class="col-3">{{ getTime(file.recoveryTime) }}</div>
        <div v-if="file.folderType != statickey.folderType.folder" class="col-2">{{ getSize(file.fileSize) }}</div>
      </div>
    </div>

    <div v-if="alertStore.load.isLoading != true && files.length == 0" class="noll">
      <span>暂无数据</span>
    </div>
  </div>
  <LoadingBox />
</template>

<script setup>
import { onMounted, ref } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";
import LoadingBox from "@/components/LoadingBox.vue";
import { useAlertStore } from "@/store/useAlertStore";
import statickey from "@/utils/statickey";
import { useUserStore } from "@/store/useUserStore";
const apiStore = useApiStore();
const alertStore = useAlertStore();
const userStore = useUserStore();
const files = ref([]);
const selectedFiles = ref([]);
onMounted(() => {
  getRecycleFiles();
});

const cancelDelete = () => {
  axios
    .post(apiStore.file.cancelDelete, {
      filesId: selectedFiles.value,
    })
    .then((resp) => {
      files.value = files.value.filter((file) => !selectedFiles.value.includes(file.fileId));
      selectedFiles.value = [];
      isSelected.value = false;
    });
};

const cancelDelete2 = (file) => {
  axios
    .post(apiStore.file.cancelDelete, {
      filesId: [file.fileId],
    })
    .then((resp) => {
      files.value = files.value.filter((f) => f.fileId != file.fileId);
      selectedFiles.value = [];
    });
};

const deleteRecycleFiles = async () => {
  alertStore.load.isLoading = true;
  await axios
    .post(apiStore.file.deleteRecycleFiles, {
      filesId: selectedFiles.value,
    })
    .then((resp) => {
      files.value = files.value.filter((file) => !selectedFiles.value.includes(file.fileId));
      selectedFiles.value = [];
      isSelected.value = false;
      getUseSpace();
    });
  alertStore.load.isLoading = false;
};

const deleteRecycleFiles2 = async (file) => {
  alertStore.load.isLoading = true;
  await axios
    .post(apiStore.file.deleteRecycleFiles, {
      filesId: [file.fileId],
    })
    .then((resp) => {
      files.value = files.value.filter((f) => f.fileId != file.fileId);
      selectedFiles.value = [];
      getUseSpace();
    });
  alertStore.load.isLoading = false;
};

const getUseSpace = async () => {
  const resp = await axios.get(apiStore.user.getUseSpace, {});
  userStore.user.useSpace = resp.data;
};

const getRecycleFiles = async () => {
  alertStore.load.isLoading = true;
  try {
    const resp = await axios.get(apiStore.file.getRecycleFiles, {}).then((resp) => {
      files.value = resp.data;
    });

    const promises = files.value.map(async (file) => {
      if (file.fileCategory == statickey.category.image) {
        file.fileCover = await getImageUrl(file.fileId);
      }
      return file;
    });
    files.value = await Promise.all(promises);
  } finally {
    alertStore.load.isLoading = false;
  }
};

const getImageUrl = async (fileId) => {
  const resp = await axios.post(
    apiStore.file.returnFileByte,
    {
      fileId: fileId,
    },
    "blob"
  );
  const url = URL.createObjectURL(resp);
  return url;
};

const getTime = (time) => {
  const date = new Date(time);

  const options = {
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
    hour12: false, // 使用24小时制
  };
  const formattedTime = date.toLocaleString("zh-CN", options); // 根据需要选择语言

  return formattedTime;
};

const getSize = (size) => {
  if (size / (1024 * 1024 * 1024) >= 1) {
    return `${(size / (1024 * 1024 * 1024)).toFixed(2)} GB`;
  } else if (size / (1024 * 1024) >= 1) {
    return `${(size / (1024 * 1024)).toFixed(2)} MB`;
  } else if (size / 1024 >= 1) {
    return `${(size / 1024).toFixed(2)} KB`;
  } else {
    return `${size} B`;
  }
};
const isSelected = ref(false);
const selectAllFiles = (isChecked) => {
  if (isChecked) {
    selectedFiles.value = files.value.map((file) => file.fileId);
  } else {
    selectedFiles.value = [];
  }
};

const toggleSelection = (fileId) => {
  if (selectedFiles.value.includes(fileId)) {
    selectedFiles.value = selectedFiles.value.filter((id) => id !== fileId);
  } else {
    selectedFiles.value.push(fileId);
  }
};

const isFileSelected = (fileId) => {
  return selectedFiles.value.includes(fileId);
};
</script>

<style lang="scss" scoped>
.my-image {
  width: 24px;
  height: 24px;
}

.my-floder-folder {
  color: #ffcf40;
}

.my-floder {
  font-size: 24px;
  margin-right: 8px;
}

.noll {
  height: calc(100% - 60px);
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  user-select: none;
  span {
    width: 100px;
    height: 50px;
    background-color: #edf3f8;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 8px;
    font-size: 14px;
  }
}

.my-fileName span {
  cursor: pointer;
}

.my-fileName span:hover {
  color: #3f9eff;
}

.my-icon {
  opacity: 0;
  display: flex;
  color: #3f9eff;
  .copy-link {
    display: flex;
    align-items: center;
    justify-content: center;
    color: red;
    cursor: pointer;
    margin-right: 5px;
    span {
      margin: 0 3px;
    }
  }
  .cancle-link {
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    span {
      margin: 0 3px;
    }
  }
}

.myrow:hover {
  background-color: #f4f7fa;
  .my-icon {
    opacity: 1;
  }
}

.myrow:focus {
  background-color: #ecf5ff;
}

.title {
  margin-top: 20px;
  width: 100%;
  button {
    margin-right: 10px;
  }
}

.content {
  height: calc(100% - 60px);
  width: 100%;
  overflow-y: auto;
  position: relative;
  white-space: nowrap;
  user-select: none;
  .container {
    position: absolute;
    left: 0;
    .myrow {
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      min-width: 600px;
      border-bottom: solid 1px rgba(0, 0, 0, 0.08);
    }
    .container-title {
      font-size: 14px;
      font-weight: 700;
    }
  }
}

.my-title {
  position: sticky;
  top: 0;
  background-color: white;
  z-index: 10;
}

// button
.title {
  .btn-share {
    background-color: #3f9eff;
  }
  .btn-share:hover {
    background-color: #79bbff;
  }
  .btn-share:active {
    background-color: #347ecc;
  }
}
</style>
