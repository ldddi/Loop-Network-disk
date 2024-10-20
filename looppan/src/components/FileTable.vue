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
          <RouterLink v-if="file.fileCategory == statickey.category.folder" :to="getLink(file)" class="file-name">{{ file.fileName }}</RouterLink>
          <span v-else class="file-name" @click="clickFileName(file)">{{ file.fileName }}</span>
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
          <div class="share-button item-button" data-bs-toggle="modal" data-bs-target="#shareModal">
            <i class="bi bi-share item-icon"></i>
            <span @click="clickShareIcon(file)">分享</span>
          </div>
          <div v-if="file.folderType != statickey.folderType.folder" class="download-button item-button">
            <i class="bi bi-download item-icon"></i>
            <span @click="clickDownload(file)">下载</span>
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
        <div class="col-3">{{ getCreateTime(file.createTime) }}</div>
        <div v-if="file.folderType != statickey.folderType.folder" class="col-1">{{ getFileSize(file.fileSize) }}</div>
      </div>
    </div>
    <div v-if="!alertStore.load.isLoading && (props.files == null || props.files.length == 0)" class="test">
      <div>
        <span>当前文件夹为空</span>
      </div>
      <div class="upload-file" @click="handleClick">
        <i class="bi bi-file-earmark-diff-fill"></i>
        <span>上传文件</span>
      </div>
    </div>
  </div>
  <!-- image preview modal -->
  <div v-if="isPreviewVisibleImage" class="preview-modal">
    <div @click="closePreviewImage" class="close-icon">
      <i class="bi bi-x"></i>
    </div>
    <div class="preview-content">
      <img class="isImageScaled" :src="imageUrl" alt="Image Preview" />
    </div>
  </div>

  <!-- viedo preview modal -->
  <div v-if="isPreviewVisibleVideo" class="preview-modal">
    <div @click="closePreviewVideo" class="close-icon">
      <i class="bi bi-x"></i>
    </div>
    <div class="preview-content">
      <video class="isVideoScaled" :src="videoUrl" controls></video>
    </div>
  </div>

  <!-- 音频预览模态框 -->
  <div v-if="isPreviewVisibleAudio" class="preview-modal">
    <div @click="closePreviewAudio" class="close-icon">
      <i class="bi bi-x"></i>
    </div>
    <div class="preview-content">
      <audio class="isAudioScaled" :src="audioUrl" controls></audio>
    </div>
  </div>

  <!-- Modal -->
  <div class="modal fade" id="shareModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="staticBackdropLabel">分享</h1>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div v-if="!isShowShareUrl" class="modal-body my-body">
          <div class="time">
            <label for="fileName">
              <span>&nbsp;</span>
              文件名:&nbsp;{{ shareFile.fileName }}
            </label>
            <div></div>
            <div></div>
            <div></div>
            <div></div>
          </div>
          <div class="time">
            <label>
              <span style="color: red">*</span>
              有效期:
            </label>
            <div class="input">
              <input type="radio" id="sevenDays" value="7" v-model="selectedDuration" />
              <label for="sevenDays">7天</label>
            </div>
            <div class="input">
              <input type="radio" id="thirtyDays" value="30" v-model="selectedDuration" />
              <label for="thirtyDays">30天</label>
            </div>
            <div class="input">
              <input type="radio" id="perpetual" value="perpetual" v-model="selectedDuration" />
              <label for="perpetual">永久</label>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button type="button" @click="clickShare" class="btn btn-primary">分享</button>
          </div>
        </div>

        <div v-else class="modal-body my-body">
          <div>
            <label class="share" for="fileName">文件名:</label>
            <span>{{ shareFile.fileName }}</span>
          </div>
          <div>
            <label class="share" for="">分享链接:</label>
            <span>{{ shareUrl }}</span>
          </div>
          <div>
            <label class="share" for="">提取码:</label>
            <span>{{ shareCode }}</span>
          </div>
          <div class="modal-footer">
            <button @click="clickCloseIcon" type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
          </div>
        </div>
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
import { useAlertStore } from "@/store/useAlertStore";

const alertStore = useAlertStore();

const isPreviewVisibleVideo = ref(false);
const isPreviewVisibleImage = ref(false);
const isPreviewVisibleAudio = ref(false);

let imageUrl = ref("");
let videoUrl = ref("");
let audioUrl = ref("");

const closePreviewImage = () => {
  isPreviewVisibleImage.value = false;
};

const closePreviewVideo = () => {
  isPreviewVisibleVideo.value = false;
};

const closePreviewAudio = () => {
  isPreviewVisibleAudio.value = false;
  selectedDuration.value = null;
};

const clickShareIcon = (file) => {
  isShowShareUrl.value = false;
  shareFile.value = file;
};

const clickCloseIcon = () => {
  isShowShareUrl.value = false;
};

let shareFile = ref("");
let selectedDuration = ref(7);
let isShowShareUrl = ref(false);
let shareUrl = ref("");
let shareCode = ref("");
const clickShare = () => {
  axios
    .post(apiStore.file.shareFile, {
      fileId: shareFile.value.fileId,
      time: selectedDuration.value,
    })
    .then((resp) => {
      shareUrl.value = resp.data.url;
      shareCode.value = resp.data.code;
      isShowShareUrl.value = true;
    });
};

const clickDownload = (file) => {
  axios
    .post(
      apiStore.file.downloadFile,
      {
        fileId: file.fileId,
      },
      "blob"
    )
    .then((resp) => {
      const url = URL.createObjectURL(resp);

      // 创建一个 <a> 元素，并设置其 href 属性为 Blob URL
      const link = document.createElement("a");
      link.href = url;

      // 设置下载文件名
      link.setAttribute("download", file.fileName || "downloadedFile");

      // 将链接添加到 DOM，并模拟点击下载
      document.body.appendChild(link);
      link.click();

      // 下载完成后移除链接
      document.body.removeChild(link);

      // 释放 Blob URL
      window.URL.revokeObjectURL(url);
    });
};

const clickFileName = (file) => {
  if (file.fileCategory == statickey.category.folder || file.fileCategory == statickey.category.document) return;
  axios
    .post(
      apiStore.file.returnFileByte,
      {
        fileId: file.fileId,
      },
      "blob"
    )
    .then((resp) => {
      const Blob = resp; // 获取 Blob 数据
      if (file.fileCategory == statickey.category.video) {
        isPreviewVisibleVideo.value = true;
        videoUrl.value = URL.createObjectURL(Blob);
      } else if (file.fileCategory == statickey.category.audio) {
        isPreviewVisibleAudio.value = true;
        audioUrl.value = URL.createObjectURL(Blob);
      } else if (file.fileCategory == statickey.category.image) {
        isPreviewVisibleImage.value = true;
        imageUrl.value = URL.createObjectURL(Blob);
      }
    });
};

const getCreateTime = (time) => {
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

const getFileSize = (size) => {
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
    selectedFiles.value = props.files.map((file) => file.fileId);
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

    if (newPath.path == null || (oldPath.path != null && newPath.path.length < oldPath.path.length)) {
      emit("pop-files-cache");
    }
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
.share {
  width: 60px;
  margin-right: 15px;
}

.my-body {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 200px;
  .time {
    display: flex;
    align-items: center;
    justify-content: space-around;
    margin-bottom: 15px;
  }
}

.isImageScaled {
  transform: scale(0.7); /* 进行缩放 */
  transition: transform 0.1s; /* 添加过渡效果 */
}

.isVideoScaled {
  transform: scale(0.9); /* 进行缩放 */
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
