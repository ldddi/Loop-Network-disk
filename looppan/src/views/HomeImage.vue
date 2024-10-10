<template>
  <div class="header">
    <!-- <div style="width: 500px; height: 1px; min-width: 100px">div</div> -->
    <input ref="myInput" type="file" id="fileInput" style="display: none" multiple accept="image/*" @change="uploadFile($event.target.files)" />
    <button type="button" class="btn btn-pull" @click="resetAndUpload">上传</button>

    <button @click="deleteSelectedFiles" type="button" :class="['btn', 'btn-delete', fileTable == null || fileTable.selectedFiles.length == 0 ? 'disable' : '']">批量删除</button>
    <button type="button" :class="['btn', 'btn-move', fileTable == null || fileTable.selectedFiles.length == 0 ? 'disable' : '']" data-bs-toggle="modal" data-bs-target="#staticBackdrop">批量移动</button>
    <div class="search-container mysearch">
      <input type="text" placeholder="输入文件名搜索..." class="search-input" />
      <i class="bi bi-search-heart search-icon"></i>
    </div>
    <!-- Modal -->
    <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
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

  <FileTable ref="fileTable" :myInput="myInput" :files="files" @rename-file="renameFile" @remove-file-from-files="removeFileFromFiles" @update-files="updateFiles" @get-file-list="getImageFileList" @pop-files-cache="popFilesCache" />
</template>

<script setup>
import FileTable from "@/components/FileTable.vue";
import { onMounted, ref } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";
import statickey from "@/utils/statickey";
import { useRoute } from "vue-router";
import ModalFileTable from "@/components/ModalFileTable.vue";

const fileTable = ref(null);
const files = ref([]);

const apiStore = useApiStore();
const route = useRoute();

const myInput = ref(null);

const modalFileTable = ref(null);
onMounted(() => {
  getImageFileList();
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

const getImageFileList = async () => {
  const resp = await axios.post(apiStore.file.getAllCategoryFile, {
    category: statickey.category.image,
  });
  files.value = resp.data;
};

const uploadFile = (fileList) => {
  const formData = new FormData();
  let filePid = "0";
  if (route.query.path != null) {
    filePid = route.query.path;
  }

  for (let i = 0; i < fileList.length; i++) {
    formData.append("file[]", fileList[i]);
  }
  formData.append("filePId", filePid);

  axios.post(apiStore.file.uploadFile, formData).then((resp) => {
    for (let i = 0; i < resp.data.length; i++) {
      updateFiles(resp.data[i]);
    }
  });
};

const removeFileFromFiles = (file) => {
  files.value = files.value.filter((f) => f.fileId !== file.fileId);
};

const renameFile = ({ file, newName }) => {
  console.log("rename file", file);
  for (let i = 0; i < files.value.length; i++) {
    console.log(files.value[i].fileId, file.fileId);
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
