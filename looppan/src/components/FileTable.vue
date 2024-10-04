<template>
  <div class="content">
    <div class="container">
      <div class="row myrow my-title">
        <div class="col-auto">
          <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-3 container-title">文件名</div>
        <div class="col-4 container-title"></div>
        <div class="col-3 container-title">修改时间</div>
        <div class="col-1 container-title">大小</div>
      </div>
      <div v-if="fileIsVisible" class="create-file row create-file-row">
        <div class="col-auto">
          <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-5">
          <div class="input-group mb-3">
            <input v-model="createFileName" type="text" class="form-control" aria-label="Text input with checkbox" />
          </div>
        </div>
        <div @click="createFileConfirm" class="col-auto create-file-button-col"><button type="button" class="btn btn-info create-file-button">√</button></div>
        <div @click="createFileCancel" class="col-auto create-file-button-col"><button type="button" class="btn btn-info create-file-button">×</button></div>
      </div>
      <div v-for="file in FatherFiles.files" class="row myrow" :key="file.fileId" tabindex="0">
        <div class="col-auto">
          <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-3">
          <i v-if="file.category == 0" class="bi bi-folder2 my-floder my-floder-folder"></i>
          <i v-else-if="file.category == 1" class="bi bi-file-earmark-play my-floder"></i>
          <i v-else-if="file.category == 2" class="bi bi-file-music my-floder"></i>
          <i v-else-if="file.category == 3" class="bi bi-images my-floder"></i>
          <i v-else-if="file.category == 4" class="bi bi-file-word my-floder"></i>
          <i v-else-if="file.category == 5" class="bi bi-file-earmark-medical my-floder"></i>
          <span class="file-name">{{ file.fileName }}</span>
        </div>
        <div class="col-4 my-button">
          <div class="share-button item-button">
            <i class="bi bi-share item-icon"></i>
            <span>分享</span>
          </div>
          <div class="del-button item-button">
            <i class="bi bi-trash item-icon"></i>
            <span>删除</span>
          </div>
          <div class="reset-button item-button">
            <i class="bi bi-pencil-square item-icon"></i>
            <span>重命名</span>
          </div>
          <div class="move-button item-button">
            <i class="bi bi-arrows-move item-icon"></i>
            <span>移动</span>
          </div>
        </div>
        <div class="col-3">{{ file.createTime }}</div>
        <div class="col-1">{{ file.fileSize }}</div>
      </div>
    </div>
  </div>
  <ErrorAlertBox />
  <SuccessAlertBox />
</template>

<script setup>
import { onMounted, ref } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";
import { useRoute } from "vue-router";
import ErrorAlertBox from "./ErrorAlertBox.vue";
import SuccessAlertBox from "./SuccessAlertBox.vue";

let fileIsVisible = ref(false);
let createFileName = ref("");

const FatherFiles = defineProps(["files"]);
const apiStore = useApiStore();
const route = useRoute();

const createFile = () => {
  fileIsVisible.value = true;
};

const createFileConfirm = async () => {
  let filePid = "0";
  if (route.params.path != null) {
    filePid = route.params.path;
  }

  try {
    const resp = await axios.post(apiStore.file.createFile, {
      filePId: filePid,
      fileName: createFileName.value,
    });
    console.log(resp);
  } catch (error) {
    console.log(error.message);
  } finally {
    fileIsVisible.value = false;
  }
};

const createFileCancel = () => {
  createFileName.value = "";
  fileIsVisible.value = false;
};

defineExpose({ createFile });
</script>

<style lang="scss" scoped>
.my-floder-folder {
  color: #ffcf40;
}

.my-floder {
  font-size: 24px;
  margin-right: 8px;
}

.create-file-row {
  height: 50px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  border-bottom: solid 1px rgba(0, 0, 0, 0.08);
  .create-file-button {
    padding: 0 !important;
    height: 30px;
    width: 30px;
    border-radius: 8px !important;
  }
  .create-file-button-col {
    padding: 0 5px;
  }
}
.create-file-row:hover {
  background-color: #f4f7fa;
}

.input-group {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 !important;
}

.file-name:hover {
  cursor: pointer;
  color: #5faeff;
}

.content {
  width: calc(100vw - 320px);
  height: calc(100vh - 180px); /* 调整为适合的高度 */
  overflow-y: auto; /* 纵向滚动 */
  white-space: nowrap; /* 确保子元素不换行 */
  position: relative; /* 使其成为滚动区域 */

  .container {
    position: absolute;
    left: 0px;
    .myrow {
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      min-width: 600px;
      border-bottom: solid 1px rgba(0, 0, 0, 0.08);
      .my-button {
        display: flex;
        align-items: center;
        justify-content: flex-end;
        padding-right: 30px;
      }
      .item-button {
        margin-right: 8px;
        color: #5faeff;
        display: none;
        cursor: pointer;
        // background-color: aqua;
        transition: all 0.3s ease; // 添加过渡效果
        .item-icon {
          font-size: 13px;
          margin-right: 3px;
        }
      }
    }
    .container-title {
      font-size: 14px;
      font-weight: 700;
    }
  }
}

.myrow:hover .item-button {
  display: block !important; // 悬停时显示
}

.myrow:hover {
  background-color: #f4f7fa;
}

.myrow:focus {
  background-color: #ecf5ff;
}

.my-title {
  position: sticky;
  top: 0;
  background-color: white;
  z-index: 10;
}
</style>
