<template>
  <div class="title">
    <button type="button" class="btn btn-share">
      <i class="bi bi-ban"></i>
      取消分享
    </button>
  </div>
  <div v-if="files.length != 0" class="content">
    <div class="container">
      <div class="row myrow my-title">
        <div class="col-auto">
          <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-6 container-title">文件名</div>
        <div class="col-2 container-title">分享时间</div>
        <div class="col-2 container-title">失效时间</div>
        <div class="col-auto container-title">浏览次数</div>
      </div>

      <div v-for="file in files" :key="file.fileId" tabindex="0" class="row myrow">
        <div class="col-auto">
          <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-4 my-fileName">
          <i v-if="file.fileCategory == statickey.category.folder" class="bi bi-folder2 my-floder my-floder-folder"></i>
          <i v-else-if="file.fileCategory == statickey.category.video" class="bi bi-file-earmark-play my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.audio" class="bi bi-file-music my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.image" class="bi bi-images my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.document" class="bi bi-file-word my-floder"></i>
          <i v-else-if="file.fileCategory == statickey.category.other" class="bi bi-file-earmark-medical my-floder"></i>

          <span>{{ file.fileName }}</span>
        </div>
        <div class="col-2 my-icon">
          <div @click="getSharedFileUrl(file)" class="copy-link">
            <i class="bi bi-link-45deg"></i>
            <span>复制链接</span>
          </div>
          <div @click="cancelSharedFile(file)" class="cancle-link">
            <i class="bi bi-ban"></i>
            <span>取消分享</span>
          </div>
        </div>
        <div class="col-2">
          <span>{{ file.shareTime }}</span>
        </div>
        <div class="col-2">
          <span>{{ file.failTime }}</span>
        </div>
        <div class="col-auto">
          <span>{{ file.views }}</span>
        </div>
      </div>
    </div>
  </div>
  <div v-else class="noll">
    <span>暂无数据</span>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";
import statickey from "@/utils/statickey";

const apiStore = useApiStore();

const files = ref([]);

onMounted(() => {
  getSharedFilesList();
});

const getSharedFilesList = () => {
  axios.post(apiStore.file.getSharedFilesList, {}).then((resp) => {
    console.log(resp);

    files.value = resp.data;
    console.log(files.value);
  });
};

const getSharedFileUrl = (file) => {
  axios
    .post(apiStore.file.getSharedFileUrl, {
      shareId: file.shareId,
    })
    .then((resp) => {
      // 将 URL 复制到剪贴板
      navigator.clipboard.writeText(resp.data);
    });
};

const cancelSharedFile = (file) => {
  axios
    .post(apiStore.file.cancelSharedFile, {
      shareId: file.shareId,
    })
    .then((resp) => {
      console.log(resp);
      deleteFileByShareId(file.shareId);
    });
};

const deleteFileByShareId = (shareId) => {
  files.value = files.value.filter((file) => file.shareId !== shareId);
};
</script>

<style lang="scss" scoped>
span {
  user-select: none;
}

.noll {
  height: calc(100% - 60px);
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  user-select: none;
}

.my-floder-folder {
  color: #ffcf40;
}

.my-floder {
  font-size: 24px;
  margin-right: 8px;
}

.my-fileName i {
  font-size: 24px;
  margin-right: 8px;
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
    cursor: pointer;
    margin-right: 5px;
    span {
      margin: 0 3px;
    }
    i {
      font-size: 20px;
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
}

.content {
  height: calc(100% - 60px);
  width: 100%;
  overflow-y: auto;
  position: relative;
  white-space: nowrap;
  .container {
    position: absolute;
    left: 0;
    .myrow {
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      min-width: 900px;
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
