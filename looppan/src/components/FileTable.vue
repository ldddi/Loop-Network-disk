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
        <div class="col-3 my-col">
          <i v-if="file.fileCategory == statickey.category.folder" class="bi bi-folder2 my-floder my-floder-folder"></i>
          <i v-else-if="file.fileCategory == statickey.category.video" class="bi bi-file-earmark-play my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.audio" class="bi bi-file-music my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.image" class="bi bi-images my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.document" class="bi bi-file-word my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.other" class="bi bi-file-earmark-medical my-floder"></i>
          <RouterLink @click="clickFileName(file)" :to="getLink(file)" class="file-name">{{ file.fileName }}</RouterLink>
          <!--  -->
          <div v-if="renameFileInput == file" class="my-rename-input">
            <input v-model="newName" type="text" class="form-control" aria-label="输入内容" />
            <div class="input-group-append">
              <button @click="renameItem(file)" class="btn btn-info btn-square" type="button" title="确认">
                <i class="bi bi-check"></i>
              </button>
              <button @click="cancleRename" class="btn btn-danger btn-square" type="button" title="取消">
                <i class="bi bi-x"></i>
              </button>
            </div>
          </div>
          <!--  -->
        </div>
        <div class="col-4 my-button">
          <div class="share-button item-button">
            <i class="bi bi-share item-icon"></i>
            <span>分享</span>
          </div>
          <div @click="deleteItem(file)" class="del-button item-button">
            <i class="bi bi-trash item-icon"></i>
            <span>删除</span>
          </div>
          <div @click="clickRename(file)" class="reset-button item-button">
            <i class="bi bi-pencil-square item-icon"></i>
            <span>重命名</span>
          </div>
          <div @click="clickOpenModal(file)" class="move-button item-button">
            <i class="bi bi-arrows-move item-icon"></i>
            <span>移动</span>
          </div>
        </div>
        <div class="col-3">{{ file.createTime }}</div>
        <div v-if="file.folderType != statickey.folderType.folder" class="col-1">{{ file.fileSize }}</div>
      </div>
    </div>
    <div v-if="props.files == null || props.files.length == 0" class="test">
      <div>
        <span>当前文件夹为空</span>
      </div>
      <div class="upload-file" @click="handleClick">
        <i class="bi bi-file-earmark-diff-fill"></i>
        <span>上传文件</span>
      </div>
    </div>
  </div>

  <div v-if="isPreviewVisibleImage" class="preview-modal">
    <div @click="closePreviewImage" class="close-icon">
      <i class="bi bi-x"></i>
    </div>
    <div class="preview-content">
      <img class="isImageScaled" :src="imageUrl" alt="Image Preview" />
    </div>
  </div>

  <div v-if="isPreviewVisibleVideo" class="preview-modal">
    <div @click="closePreviewVideo" class="close-icon">
      <i class="bi bi-x"></i>
    </div>
    <div class="preview-content">
      <video class="isVideoScaled" :src="videoUrl" controls></video>
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

const isPreviewVisibleVideo = ref(false);
const isPreviewVisibleImage = ref(false);
let imageUrl = ref("https://cdn.pixabay.com/photo/2022/06/11/09/20/snake-7256057_1280.jpg");
let videoUrl = ref("https://www.w3schools.com/html/mov_bbb.mp4");
const closePreviewImage = () => {
  isPreviewVisibleImage.value = false;
};

const closePreviewVideo = () => {
  isPreviewVisibleVideo.value = false;
};

const clickFileName = (file) => {
  if (file.fileCategory == statickey.category.image) {
    axios
      .post(apiStore.file.returnImageUrl, {
        fileId: file.fileId,
      })
      .then((resp) => {
        imageUrl.value = resp;
        console.log(resp);
        isPreviewVisibleImage.value = true;
      });
  }
};

let fileIsVisible = ref(false);
let createFileName = ref("");

const props = defineProps(["files", "myInput"]);
const emit = defineEmits(["update-files", "pop-files-cache", "remove-file-from-files", "rename-file", "open-modal"]);
const apiStore = useApiStore();
const route = useRoute();

let selectedFiles = ref([]);
let newName = ref();
let renameFileInput = ref();

const clickOpenModal = (file) => {
  selectedFiles.value.push(file.fileId);
  emit("open-modal");
};

const clickRename = (file) => {
  renameFileInput.value = file;
  if (file.folderType == statickey.folderType.file) {
    let str = file.fileName;
    console.log(str.substring(0, str.lastIndexOf(".")));
    newName.value = str.substring(0, str.lastIndexOf("."));
  } else {
    newName.value = file.fileName;
  }
};

const cancleRename = () => {
  renameFileInput.value = null;
  newName.value = null;
};

const isFileSelected = (fileId) => {
  return selectedFiles.value.includes(fileId);
};

const selectAllFiles = (isChecked) => {
  if (isChecked) {
    // 如果选中，添加所有文件 ID 到数组
    selectedFiles.value = props.files.map((file) => file.fileId);
  } else {
    // 如果取消选中，清空数组
    selectedFiles.value = [];
  }
};

const toggleSelection = (fileId) => {
  if (selectedFiles.value.includes(fileId)) {
    // 取消选中
    selectedFiles.value = selectedFiles.value.filter((id) => id !== fileId);
  } else {
    // 选中
    selectedFiles.value.push(fileId);
  }
};

const handleClick = () => {
  props.myInput.click();
};

const getLink = (file) => {
  const prepath = route.query.path;
  if (file.folderType == statickey.folderType.folder) {
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
  createFileName.value = "";
  fileIsVisible.value = true;
};

const deleteItem = (file) => {
  const filesId = [];
  filesId.push(file.fileId);
  axios
    .post(apiStore.file.deleteSelectedFiles, {
      filesId: filesId,
    })
    .then((resp) => {
      console.log(resp);
      emit("remove-file-from-files", file);
    });
};

const renameItem = (file) => {
  axios
    .post(apiStore.file.renameFile, {
      fileId: file.fileId,
      newName: newName.value,
    })
    .then((resp) => {
      console.log(resp);
      cancleRename();
      let newName = resp.data;
      emit("rename-file", { file, newName });
    });
};

const createFileConfirm = async () => {
  let filePid = "0";
  if (route.query.path != null) {
    filePid = route.query.path;
  }
  const resp = await axios.post(apiStore.file.createFile, {
    filePId: filePid,
    fileName: createFileName.value,
  });
  emit("update-files", resp.data);
  fileIsVisible.value = false;
};

const createFileCancel = () => {
  createFileName.value = "";
  fileIsVisible.value = false;
};

defineExpose({ createFile, selectedFiles });
</script>

<style lang="scss" scoped>
.isImageScaled {
  transform: scale(0.6); /* 进行缩放 */
  transition: transform 0.1s; /* 添加过渡效果 */
}

.isVideoScaled {
  transform: scale(1.2); /* 进行缩放 */
  transition: transform 0.1s;
}

.preview-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  .close-icon {
    position: absolute;
    top: 10px;
    right: 20px;
    font-size: 30px;
    cursor: pointer;
    color: #fff;
  }
  .preview-content {
    max-width: 90%;
    max-height: 90%;
    margin-bottom: 1%;
    display: flex; /* 使内容为flex布局 */
    align-items: center; /* 垂直居中 */
    justify-content: center; /* 水平居中 */
    img {
      max-width: 100%;
      max-height: 100%;
      border-radius: 10px;
      object-fit: cover; /* 保持图像的宽高比 */
    }
    video {
      max-width: 100%;
      max-height: 100%;
      border-radius: 10px;
      object-fit: cover; /* 保持图像的宽高比 */
    }
  }
}

//

.my-col {
  position: relative;
}

.my-rename-input {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  position: absolute;
  top: 0;
  width: 120%;
  button {
    width: 35px;
    height: 35px;
    line-height: 17.5px;

    margin-left: 5px;
  }
}

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
