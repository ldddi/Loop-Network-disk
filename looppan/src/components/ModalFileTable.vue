<template>
  <div v-if="filesCache.length == 0" class="file-header">全部文件</div>
  <div v-else class="file-header-2">
    <span @click="returnLast" class="myreturn">返回上一级</span>
    <span class="allFolder">全部文件夹 ></span>
    <div v-for="file in filesCache" :key="file.fileId" class="active">{{ file.fileName }}&nbsp; >&nbsp;</div>
  </div>
  <div class="my-container">
    <div @click="updateFiles(file)" v-for="file in files" :key="file.fileId" class="file-item" tabindex="0">
      <i class="bi bi-folder2 my-floder my-floder-folder"></i>
      <span>{{ file.fileName }}</span>
    </div>
    <div v-if="files.length == 0" class="files-empty">
      <div>
        <span>移动到此文件夹</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";

const apiStore = useApiStore();

onMounted(() => {
  filePId.value = "0";
  filesCache.value = [];
  getFileList();
});

const files = ref([]);
let filePId = ref("0");

let filesCache = ref([]);

const returnLast = () => {
  if (filesCache.value.length > 0) {
    filesCache.value.pop();
    updateFiles(filesCache.value[filesCache.value.length - 1]);
  } else {
    updateFiles();
  }
};

const updateFiles = (file) => {
  if (file != null) {
    axios
      .get(apiStore.file.getFolderList, {
        filePId: file.fileId,
      })
      .then((resp) => {
        if (!filesCache.value.includes(file)) {
          filesCache.value.push(file);
        }
        files.value = resp.data;
      });
  } else {
    axios
      .get(apiStore.file.getFolderList, {
        filePId: 0,
      })
      .then((resp) => {
        files.value = resp.data;
      });
  }
};

const getFileList = () => {
  axios
    .get(apiStore.file.getFolderList, {
      filePId: filePId.value,
    })
    .then((resp) => {
      files.value = resp.data;
    });
};

defineExpose({ filesCache });
</script>

<style lang="scss" scoped>
.active {
  color: #5faeff;
}

.file-header-2 {
  display: flex;
  span {
    color: #5faeff;
  }
  .myreturn {
    padding-right: 5px;
    border-right: 1px solid rgba(0, 0, 0, 0.1);
    cursor: pointer;
  }
  .allFolder {
    margin: 0 5px;
  }
}

.files-empty {
  div {
    height: 100px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.my-container {
  .file-header {
    height: 50px;
    font-weight: 800;
    background-color: #f1f1f1;
    padding-left: 5px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }
  .file-item {
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    border-bottom: 1px solid rgba(0, 0, 0, 0.08);
    span {
      font-size: 14px;
      user-select: none;
      cursor: pointer;
    }
    span:hover {
      color: #5faeff;
    }
    i {
      font-size: 25px;
      margin-right: 10px;
      color: #e6a23c;
    }
  }
  .file-item:hover {
    background-color: #f8f8f8;
  }
  .file-item:focus {
    background-color: #ecf5ff;
  }
}
</style>
