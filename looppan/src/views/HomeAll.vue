<template>
  <div class="header">
    <!-- <div style="width: 500px; height: 1px; min-width: 100px">div</div> -->
    <input ref="myInput" type="file" id="fileInput" style="display: none" multiple @change="uploadFile($event.target.files)" />
    <button type="button" class="btn btn-pull" onclick="document.getElementById('fileInput').click();">上传</button>

    <button @click="fileTable.createFile" type="button" class="btn btn-new">新建文件夹</button>
    <button @click="deleteSelectedFiles" type="button" :class="['btn', 'btn-delete', fileTable == null || fileTable.selectedFiles.length == 0 ? 'disable' : '']">批量删除</button>
    <button type="button" :class="['btn', 'btn-move', fileTable == null || fileTable.selectedFiles.length == 0 ? 'disable' : '']" data-bs-toggle="modal" data-bs-target="#staticBackdrop">批量移动</button>
    <div class="search-container mysearch">
      <input type="text" placeholder="输入文件名搜索..." class="search-input" />
      <i class="bi bi-search-heart search-icon"></i>
    </div>
    <!-- Modal -->
    <div v-if="fileTable != null && fileTable.selectedFiles.length != 0" class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
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
  <div v-if="computedIsShowAllFile" class="title">全部文件</div>
  <div v-else class="title">
    <span class="allFolder">当前文件夹：</span>
    <div v-for="file in filesCache" :key="file.fileId" class="active">{{ file.fileName }}</div>
  </div>

  <FileTable ref="fileTable" :myInput="myInput" :files="files" @update-files="updateFiles" @get-file-list="getFileList" @pop-files-cache="popFilesCache" />
</template>

<script setup>
import FileTable from "@/components/FileTable.vue";
import { computed, onMounted, ref } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";
import statickey from "@/utils/statickey";
import { useRoute } from "vue-router";
import ModalFileTable from "@/components/ModalFileTable.vue";

const fileTable = ref(null);
const files = ref([]);

let filesCache = ref([]);

const apiStore = useApiStore();
const route = useRoute();

const myInput = ref(null);

const modalFileTable = ref(null);
onMounted(() => {
  getFileList();
});

const comfirmMoveFiles = () => {
  console.log("Move files", fileTable.value.selectedFiles);
  console.log("Move files2", modalFileTable.value.filesCache[modalFileTable.value.filesCache.length - 1]);
  axios
    .post(apiStore.file.moveFiles, {
      filesId: fileTable.value.selectedFiles,
      pId: modalFileTable.value.filesCache[modalFileTable.value.filesCache.length - 1].fileId,
    })
    .then((resp) => {
      console.log(resp);
      getFileList();
    })
    .catch((err) => {
      console.log(err.message);
    });
};

const computedIsShowAllFile = computed(() => {
  return route.query.path === undefined;
});

const popFilesCache = () => {
  filesCache.value.pop();
};

const deleteSelectedFiles = () => {
  if (fileTable.value.selectedFiles.length > 0) {
    console.log(fileTable.value.selectedFiles);
    axios
      .post(apiStore.file.deleteSelectedFiles, {
        filesId: fileTable.value.selectedFiles,
      })
      .then((resp) => {
        console.log(resp);
        const selectedFiles = fileTable.value.selectedFiles;

        files.value = files.value.filter((file) => !selectedFiles.includes(file.fileId));
        fileTable.value.selectedFiles = [];
        console.log(files.value);
      })
      .catch((error) => {
        console.log(error.message);
        fileTable.value.selectedFiles = [];
      });
  }
};

const getFileList = async () => {
  try {
    const resp = await axios.get(apiStore.file.getFileList, {
      category: statickey.category.folder,
      path: route.query.path,
    });
    files.value = resp.data;
    if (resp.clickedFile != null && !filesCache.value.includes(resp.clickedFile)) {
      filesCache.value.push(resp.clickedFile);
    }
  } catch (error) {
    console.log(error.message);
  }
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
  console.log(formData, fileList.length);
  axios
    .post(apiStore.file.uploadFile, formData)
    .then((resp) => {
      console.log(resp);
      for (let i = 0; i < resp.data.length; i++) {
        updateFiles(resp.data[i]);
      }
    })
    .catch((error) => {
      console.log(error);
    });
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
