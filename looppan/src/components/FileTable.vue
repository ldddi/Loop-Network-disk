<template>
  <div class="content">
    <div class="container">
      <div class="row myrow my-title">
        <div class="col-auto">
          <input class="form-check-input" @change="selectAllFiles($event.target.checked)" type="checkbox" value="" id="defaultCheck1" />
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
      <div v-for="file in props.files" class="row myrow" :key="file.fileId" tabindex="0">
        <div class="col-auto">
          <input class="form-check-input" type="checkbox" :checked="isFileSelected(file.fileId)" @change="toggleSelection(file.fileId)" value="" id="defaultCheck1" />
        </div>
        <div class="col-3">
          <i v-if="file.fileCategory == statickey.category.folder" class="bi bi-folder2 my-floder my-floder-folder"></i>
          <i v-else-if="file.fileCategory == statickey.category.video" class="bi bi-file-earmark-play my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.audio" class="bi bi-file-music my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.image" class="bi bi-images my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.document" class="bi bi-file-word my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.other" class="bi bi-file-earmark-medical my-floder"></i>
          <RouterLink :to="getLink(file)" class="file-name">{{ file.fileName }}</RouterLink>
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
        <div v-if="file.folderType != statickey.folderType.folder" class="col-1">{{ file.fileSize }}</div>
      </div>
    </div>
    <div v-if="props.files.length == 0" class="test">
      <div>
        <span>当前文件夹为空</span>
      </div>
      <div class="upload-file" @click="handleClick">
        <i class="bi bi-file-earmark-diff-fill"></i>
        <span>上传文件</span>
      </div>
    </div>
  </div>
  <ErrorAlertBox />
  <SuccessAlertBox />
</template>

<script setup>
import { computed, nextTick, onMounted, ref, watch } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";
import { useRoute } from "vue-router";
import ErrorAlertBox from "./ErrorAlertBox.vue";
import SuccessAlertBox from "./SuccessAlertBox.vue";
import statickey from "@/utils/statickey";

let fileIsVisible = ref(false);
let createFileName = ref("");

const props = defineProps(["files", "myInput"]);
const emit = defineEmits(["update-files", "pop-files-cache"]);
const apiStore = useApiStore();
const route = useRoute();

let selectedFiles = ref([]);

const isFileSelected = (fileId) => {
  return selectedFiles.value.includes(fileId);
};

const selectAllFiles = (isChecked) => {
  if (isChecked) {
    // 如果选中，添加所有文件 ID 到数组
    selectedFiles.value = props.files.map((file) => file.fileId);
    console.log("全选：" + selectedFiles.value);
  } else {
    // 如果取消选中，清空数组
    selectedFiles.value = [];
    console.log("全不选：" + selectedFiles.value);
  }
};

const toggleSelection = (fileId) => {
  if (selectedFiles.value.includes(fileId)) {
    // 取消选中
    selectedFiles.value = selectedFiles.value.filter((id) => id !== fileId);
    console.log("取消：" + selectedFiles.value);
  } else {
    // 选中
    selectedFiles.value.push(fileId);
    console.log("选中：" + selectedFiles.value);
  }
};

const handleClick = () => {
  props.myInput.click();
};

const getLink = (file) => {
  const prepath = route.query.path;
  if (file.folderType == statickey.folderType.folder) {
    console.log("hhh");
    if (prepath == null) {
      return { name: "HomeAll", query: { path: file.fileId } };
    } else {
      return { name: "HomeAll", query: { path: prepath + "/" + file.fileId } };
    }
  }

  return { name: "HomeAll" };
};

watch(
  () => route.query,
  async (newPath, oldPath) => {
    await nextTick();
    emit("get-file-list", newPath);
    emit("pop-files-cache");
  }
);

const createFile = () => {
  fileIsVisible.value = true;
};

const createFileConfirm = async () => {
  let filePid = "0";
  if (route.query.path != null) {
    filePid = route.query.path;
  }

  try {
    const resp = await axios.post(apiStore.file.createFile, {
      filePId: filePid,
      fileName: createFileName.value,
    });
    console.log(resp);
    emit("update-files", resp.data);
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

defineExpose({ createFile, selectedFiles });
</script>

<style lang="scss" scoped>
.test {
  width: 25%;
  height: 35%;
  position: relative;
  top: 35%;
  left: 30%;

  padding: 3% 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  .upload-file {
    cursor: pointer;
    background-color: #f1f1f1;
    border-radius: 8px;
    width: 25%;
    height: 55%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    i {
      font-size: 45px;
      color: #5faeff;
    }
  }
}

.container {
  :deep(a) {
    color: #636d7d;
    text-decoration: none;
    &:hover {
      color: #5faeff !important; // 可选：改变文字颜色
    }
  }
}

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
  user-select: none;
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
      user-select: none;
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
