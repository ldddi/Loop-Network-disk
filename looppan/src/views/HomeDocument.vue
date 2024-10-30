<template>
  <div class="header">
    <!-- <div style="width: 500px; height: 1px; min-width: 100px">div</div> -->
    <input ref="myInput" type="file" id="fileInput" style="display: none" multiple accept=".pdf,.doc,.docx,.txt,.xls,.xlsx" @change="uploadFile2($event.target.files)" />
    <button type="button" class="btn btn-pull" @click="resetAndUpload">上传文档</button>

    <button @click="deleteSelectedFiles" type="button" :class="['btn', 'btn-delete', fileTable == null || fileTable.selectedFiles.length == 0 ? 'disable' : '']">批量删除</button>
    <button type="button" :class="['btn', 'btn-move', fileTable == null || fileTable.selectedFiles.length == 0 ? 'disable' : '']" data-bs-toggle="modal" data-bs-target="#staticBackdrop">批量移动</button>
    <div class="search-container mysearch">
      <input v-model="searchFilename" type="text" placeholder="输入文件名搜索..." class="search-input" />
      <img @click="fileSeach" class="svg-search" src="/svg/Search.svg" alt="" />
    </div>
    <!-- Modal -->
    <div ref="myModal" class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-scrollable my-modal">
        <div class="modal-content">
          <div class="modal-header">
            <h1 class="modal-title fs-5" id="staticBackdropLabel">移动到</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body my-modal-body">
            <!--  -->
            <ModalFileTable ref="modalFileTable"></ModalFileTable>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button @click="comfirmMoveFiles" type="button" data-bs-dismiss="modal" class="btn btn-primary">移动到此处</button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="title">图片文件</div>

  <LoadingBox />

  <FileTable ref="fileTable" @handle-scroll="handleScroll" :myInput="myInput" :files="files" @open-modal="openModal" @rename-file="renameFile" @remove-file-from-files="removeFileFromFiles" @update-files="updateFiles" @load-page="loadPage" @pop-files-cache="popFilesCache" />
</template>

<script setup>
import FileTable from "@/components/FileTable.vue";
import { onMounted, ref } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";
import statickey from "@/utils/statickey";
import { useRoute } from "vue-router";
import ModalFileTable from "@/components/ModalFileTable.vue";
import { Modal } from "bootstrap";
import LoadingBox from "@/components/LoadingBox.vue";
import { useAlertStore } from "@/store/useAlertStore";
import { useUploadFileStore } from "@/store/useUploadFileStore";
import { useUserStore } from "@/store/useUserStore";

let searchFilename = ref("");
const fileSeach = () => {
  let filePId = "0";
  if (route.query.path != null) {
    filePId = route.query.path;
  }
  if (searchFilename.value == "") {
    getDocumentFileList();
    return;
  }
  let type = statickey.folderType.file;
  if (route.path.includes("all")) {
    type = statickey.folderType.folder;
  }

  axios
    .get(apiStore.file.fileSearch, {
      filePId: filePId,
      fileName: searchFilename.value,
      type: type,
    })
    .then(async (resp) => {
      files.value = resp.data;
      for (let i = 0; i < files.value.length; i++) {
        if (files.value[i].fileCategory == statickey.category.image) {
          files.value[i].fileCover = await getImageUrl(files.value[i].fileId);
        }
      }
    });
};

const fileTable = ref(null);
const files = ref([]);

const apiStore = useApiStore();
const alertStore = useAlertStore();
const userStore = useUserStore();
const uploadFileStore = useUploadFileStore();
const route = useRoute();

const myInput = ref(null);
const myModal = ref(null);

const openModal = () => {
  if (myModal.value) {
    const modalInstance = new Modal(myModal.value);
    modalFileTable.value.getFileList();
    modalInstance.show();
  } else {
    console.error("Modal 元素未找到");
  }
};

const currentPage = ref(0);
const count = ref(0);
const handleScroll = () => {
  const { scrollTop, clientHeight, scrollHeight } = fileTable.value.scrollContainer;
  if (scrollTop + clientHeight >= scrollHeight - 5) {
    loadPage(currentPage.value + 1); // 滚动到底部时加载下一页
    currentPage.value++;
  }
};

const loadPage = async (page) => {
  if (page * 15 > count.value) {
    currentPage.value--;
    return;
  }

  const data = await getDocumentFileList(page);
  if (data == null) {
    return;
  }
  if (page != 0) {
    files.value.push(...data);
  } else {
    currentPage.value = 0;
    files.value = data;
  }
};

const modalFileTable = ref(null);
onMounted(() => {
  loadPage(0);
});

const resetAndUpload = () => {
  myInput.value.value = null; // 重置文件输入
  document.getElementById("fileInput").click(); // 触发文件选择
};

const comfirmMoveFiles = () => {
  let fileId;
  if (modalFileTable.value.filesCache.length > 0) {
    fileId = modalFileTable.value.filesCache[modalFileTable.value.filesCache.length - 1].fileId;
  } else {
    fileId = "0";
  }

  axios
    .post(apiStore.file.moveFiles, {
      filesId: fileTable.value.selectedFiles,
      pId: fileId,
    })
    .then((resp) => {
      fileTable.value.selectedFiles = [];
      fileTable.value.isSelected = false;
      getFileList();
    });
};

const deleteSelectedFiles = () => {
  if (fileTable.value.selectedFiles.length > 0) {
    const selectedFiles = fileTable.value.selectedFiles;
    axios
      .post(apiStore.file.deleteSelectedFiles, {
        filesId: fileTable.value.selectedFiles,
      })
      .then((resp) => {
        files.value = files.value.filter((file) => !selectedFiles.includes(file.fileId));
        fileTable.value.isSelected = false;
      });
    fileTable.value.selectedFiles = [];
  }
};

const getDocumentFileList = async (page) => {
  alertStore.load.isLoading = true;
  try {
    const resp = await axios.post(apiStore.file.getAllCategoryFile, {
      category: statickey.category.document,
      page: page,
    });
    count.value = resp.count;
    return resp.data;
  } finally {
    alertStore.load.isLoading = false;
  }
};

const chunkSize = ref(1024 * 1024);

const uploadChunk = async (chunk, filePid, fileName, fileSize, chunkIndex, totalChunks) => {
  const formData = new FormData();
  formData.append("filePId", filePid);
  formData.append("chunk", chunk);
  formData.append("fileName", fileName);
  formData.append("fileSize", fileSize);
  formData.append("chunkIndex", chunkIndex);
  formData.append("totalChunks", totalChunks);

  try {
    await new Promise((resolve) => {
      setTimeout(async () => {
        resolve();
      }, statickey.upload.time); // 调整这里的值以控制速度
    });
    const resp = await axios.post(apiStore.file.uploadFile2, formData);
    return resp;
  } catch (error) {
    return error;
  }
};

const uploadFile2 = async (files) => {
  if (!(files instanceof FileList) || files.length === 0) {
    return;
  }

  const fileArray = Array.from(files);
  let filePid = route.query.path || "0";
  let isOk = false;
  let fileNameList = fileArray.map((file) => file.name);

  await axios
    .post(apiStore.file.checkFilename, {
      filePId: filePid,
      fileNameList: fileNameList,
    })
    .then(() => {
      isOk = true;
    });

  if (!isOk) {
    return;
  }

  uploadFileStore.isDropdownVisible = true;
  const maxConcurrentUploads = 5;
  let currentUploads = 0; // 记录当前正在上传的文件数量
  const curFileIds = [];
  const fileId = uploadFileStore.fileId;
  for (const file of fileArray) {
    uploadFileStore.files.unshift({
      fileId: uploadFileStore.fileId,
      fileName: file.name,
      fileSize: file.size,
      isFinish: false,
      finishChunks: 0,
      totalChunks: 0,
      isCancel: false,
      isPause: false,
      isError: false,
      errorMessage: "",
    });
    curFileIds.push(uploadFileStore.fileId);
    uploadFileStore.fileId += 1;
  }
  uploadFileStore.fileId = fileId;
  let index = 0;
  const uploadFile = async (file) => {
    const fileId = curFileIds[index++];
    const f = uploadFileStore.files.find((item) => item.fileId === fileId);
    const totalChunks = Math.ceil(file.size / chunkSize.value);
    f.totalChunks = totalChunks;

    let resp;
    for (let j = 0; j < totalChunks; j++) {
      if (f.isCancel) {
        break;
      }

      while (f.isPause) {
        if (f.isCancel) {
          break;
        }
        await new Promise((resolve) => setTimeout(resolve, 100));
      }
      if (f.isCancel) {
        break;
      }

      const start = j * chunkSize.value;
      const end = Math.min(start + chunkSize.value, file.size);
      const chunk = file.slice(start, end);
      resp = await uploadChunk(chunk, filePid, file.name, file.size, j, totalChunks);
      if (resp.timestamp != null) {
        f.isError = true;
        f.errorMessage = resp.message;
        break;
      }
      f.finishChunks += 1;
    }

    if (!f.isCancel && !f.isError) {
      f.isFinish = true;

      updateFiles(resp.data); // 更新状态
      await getUseSpace();
    } else if (!f.isError) {
      uploadFileCancel(filePid, f.fileName);
      uploadFileStore.files = uploadFileStore.files.filter((file) => file.fileId != f.fileId);
    }
  };

  for (const file of fileArray) {
    while (currentUploads >= maxConcurrentUploads) {
      await new Promise((resolve) => setTimeout(resolve, 300)); // 等待直到有可用的上传位置
    }

    currentUploads++; // 增加正在上传的数量
    uploadFile(file).finally(() => {
      uploadFileStore.fileId++;
      currentUploads--; // 上传完成后减少数量
    });
  }

  // 等待所有剩余的上传完成
  await new Promise((resolve) => {
    const checkUploads = setInterval(() => {
      if (currentUploads === 0) {
        clearInterval(checkUploads);
        resolve();
      }
    }, 100);
  });
};

const getUseSpace = async () => {
  const resp = await axios.get(apiStore.user.getUseSpace, {});
  userStore.user.useSpace = resp.data;
};

const uploadFileCancel = (filePId, fileName) => {
  axios.post(apiStore.file.uploadFileCancel, {
    filePId: filePId,
    fileName: fileName,
  });
};

const removeFileFromFiles = (file) => {
  files.value = files.value.filter((f) => f.fileId !== file.fileId);
};

const renameFile = ({ file, newName }) => {
  for (let i = 0; i < files.value.length; i++) {
    if (files.value[i].fileId === file.fileId) {
      files.value[i].fileName = newName;
      break;
    }
  }
};

const updateFiles = (newFile) => {
  files.value.unshift(newFile);
};
</script>

<style lang="scss" scoped>
.my-modal {
  position: relative;
  top: 15vh;
}

.my-modal-body {
  width: 100%;
}

.header {
  position: relative;
  .disable {
    opacity: 0.5;
    cursor: not-allowed;
    pointer-events: none; /* 禁用鼠标事件 */
  }
}

.header {
  height: 45px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  overflow-x: hidden;
  white-space: nowrap; /* 不换行 */
  button {
    margin-right: 8px;
  }
  .mysearch {
    flex-shrink: 1; /* 允许按钮收缩 */
    height: 40px;
    position: relative;
    .svg-search {
      display: inline;
      width: 18px;
      height: 18px;
      position: absolute;
      top: 11px;
      right: 10px;
      cursor: pointer;
    }
  }
}

.title {
  height: 40px;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 700;
}
</style>
