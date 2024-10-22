<template>
  <div class="header">
    <!-- <div style="width: 500px; height: 1px; min-width: 100px">div</div> -->
    <input ref="myInput" type="file" id="fileInput" style="display: none" multiple accept="audio/*" @change="uploadFile2($event.target.files)" />
    <button type="button" class="btn btn-pull" @click="resetAndUpload">上传</button>

    <button @click="deleteSelectedFiles" type="button" :class="['btn', 'btn-delete', fileTable == null || fileTable.selectedFiles.length == 0 ? 'disable' : '']">批量删除</button>
    <button type="button" :class="['btn', 'btn-move', fileTable == null || fileTable.selectedFiles.length == 0 ? 'disable' : '']" data-bs-toggle="modal" data-bs-target="#staticBackdrop">批量移动</button>
    <div class="search-container mysearch">
      <input type="text" placeholder="输入文件名搜索..." class="search-input" />
      <i class="bi bi-search-heart search-icon"></i>
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
  <div class="title">音频文件</div>
  <LoadingBox />

  <FileTable ref="fileTable" :myInput="myInput" :files="files" @open-modal="openModal" @rename-file="renameFile" @remove-file-from-files="removeFileFromFiles" @update-files="updateFiles" @get-file-list="getAudioFileList" @pop-files-cache="popFilesCache" />
</template>

<script setup>
import FileTable from "@/components/FileTable.vue";
import { computed, onMounted, ref } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";
import statickey from "@/utils/statickey";
import { useRoute } from "vue-router";
import ModalFileTable from "@/components/ModalFileTable.vue";
import { Modal } from "bootstrap";
import LoadingBox from "@/components/LoadingBox.vue";
import { useAlertStore } from "@/store/useAlertStore";
import { useUploadFileStore } from "@/store/useUploadFileStore";

const fileTable = ref(null);
const files = ref([]);

const apiStore = useApiStore();
const alertStore = useAlertStore();
const uploadFileStore = useUploadFileStore();
const route = useRoute();

const myInput = ref(null);
const myModal = ref(null);

const openModal = () => {
  if (myModal.value) {
    const modalInstance = new Modal(myModal.value);
    modalInstance.show();
  } else {
    console.error("Modal 元素未找到");
  }
};

const modalFileTable = ref(null);
onMounted(() => {
  getAudioFileList();
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
      getFileList();
    });
};

const deleteSelectedFiles = () => {
  if (fileTable.value.selectedFiles.length > 0) {
    axios
      .post(apiStore.file.deleteSelectedFiles, {
        filesId: fileTable.value.selectedFiles,
      })
      .then((resp) => {
        const selectedFiles = fileTable.value.selectedFiles;
        files.value = files.value.filter((file) => !selectedFiles.includes(file.fileId));
        fileTable.value.selectedFiles = [];
      });
    fileTable.value.selectedFiles = [];
  }
};

const getAudioFileList = async () => {
  alertStore.load.isLoading = true;
  const resp = await axios.post(apiStore.file.getAllCategoryFile, {
    category: statickey.category.audio,
  });
  files.value = resp.data;
  alertStore.load.isLoading = false;
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

  let resp;
  await new Promise((resolve) => {
    setTimeout(async () => {
      resp = await axios.post(apiStore.file.uploadFile2, formData);
      resolve();
    }, statickey.upload.time); // 调整这里的值以控制速度
  });
  return resp;
};

const uploadFile2 = async (files) => {
  if (files == null) {
    return;
  }

  let filePid = "0";
  if (route.query.path != null) {
    filePid = route.query.path;
  }
  let isOk = false;
  let fileNameList = [];
  for (let i = 0; i < files.length; i++) {
    fileNameList.unshift(files[i].name);
  }
  await axios
    .post(apiStore.file.checkFilename, {
      filePId: filePid,
      fileNameList: fileNameList,
    })
    .then((resp) => {
      isOk = true;
    });

  if (isOk == false) {
    return;
  }

  for (let i = 0; i < files.length; i++) {
    uploadFileStore.files.unshift({
      fileName: files[i].name,
      fileSize: files[i].size,
      isFinish: false,
      finishChunks: 0,
      totalChunks: 0,
    });
  }

  for (let i = 0; i < files.length; i++) {
    const file = files[i];
    const totalChunks = Math.ceil(file.size / chunkSize.value);

    uploadFileStore.files[i].totalChunks = totalChunks;
    let resp;
    for (let j = 0; j < totalChunks; j++) {
      const start = j * chunkSize.value;
      const end = Math.min(start + chunkSize.value, file.size);
      const chunk = file.slice(start, end);
      resp = await uploadChunk(chunk, filePid, file.name, file.size, j, totalChunks);
      uploadFileStore.files[i].finishChunks = j + 1;
    }

    uploadFileStore.files[i].isFinish = true;
    updateFiles(resp.data);
  }
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
