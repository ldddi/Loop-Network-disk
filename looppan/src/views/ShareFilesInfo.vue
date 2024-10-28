<template>
  <div class="my-container">
    <LoadingBox />
    <div class="header">
      <div class="header-left">
        <i class="bi bi-cloud-check my-title-icon"></i>
        <span class="logo-text">Loop 网盘</span>
      </div>
      <div v-if="userStore.user.is_login" class="header-right">
        <span @click="goToHomeAll" class="go-login">前往首页</span>
        <div class="user-avatar">
          <img :src="userStore.user.avatarUrl" alt="" />
          <div class="dropdown">
            <ul>
              <li @click="userStore.logOut">退出</li>
            </ul>
          </div>
        </div>
        <div class="user-nickname">{{ userStore.user.nickName }}</div>
      </div>
      <div v-else class="header-right">
        <span @click="goToHomeAll" class="go-login">前往登陆</span>
        <span class="no-login">未登录</span>
      </div>
    </div>
    <div class="box">
      <div class="line-1">
        <span>全部文件</span>
        <div class="button">
          <button @click="downloadFile" :disabled="!selectedFiles.length" type="button" class="btn btn-success">下载</button>
          <button @click="saveMyPan" :disabled="!selectedFiles.length" type="button" class="btn btn-primary">保存到我的网盘</button>
        </div>
      </div>

      <div class="container">
        <div class="row title-row" tabindex="0">
          <div class="col-auto">
            <input @change="selectAllFile($event.target.checked)" class="form-check-input" type="checkbox" />
          </div>
          <div class="col-5">文件名</div>
          <div class="col-3">分享时间</div>
          <div class="col-3">大小</div>
        </div>

        <div v-for="file in files" :key="file.fileId" class="row myrow" tabindex="0">
          <div class="col-auto">
            <input :checked="isFileSelected(file.shareId)" @change="selectFile(file.shareId)" class="form-check-input" type="checkbox" />
          </div>
          <div class="col-5 mycol fileName-col">
            <i v-if="file.fileCategory == statickey.category.folder" class="bi bi-folder2 my-floder my-floder-folder"></i>
            <i v-else-if="file.fileCategory == statickey.category.video" class="bi bi-file-earmark-play my-floder"></i>
            <i v-else-if="file.fileCategory == statickey.category.audio" class="bi bi-file-music my-floder"></i>
            <img class="my-img" v-else-if="file.fileCategory == statickey.category.image" :src="file.imageUrl" alt="" />
            <i v-else-if="file.fileCategory == statickey.category.document" class="bi bi-file-word my-floder"></i>
            <i v-else-if="file.fileCategory == statickey.category.other" class="bi bi-file-earmark-medical my-floder"></i>
            <span>{{ file.fileName }}</span>
          </div>
          <div class="col-3 mycol">
            <span>{{ getTime(file.shareTime) }}</span>
          </div>
          <div class="col-3 mycol">
            <span v-if="file.fileCategory != statickey.category.folder">{{ getFileSize(file.fileSize) }}</span>
          </div>
        </div>
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
import { useUserStore } from "@/store/useUserStore";
import statickey from "@/utils/statickey";
import { useAlertStore } from "@/store/useAlertStore";
import ErrorAlertBox from "@/components/ErrorAlertBox.vue";
import SuccessAlertBox from "@/components/SuccessAlertBox.vue";
import router from "@/router";
import LoadingBox from "@/components/LoadingBox.vue";

const apiStore = useApiStore();
const userStore = useUserStore();
const alertStore = useAlertStore();
const route = useRoute();

let selectedFiles = ref([]);

onMounted(async () => {
  getSharedFileInfo();

  const jwtToken = localStorage.getItem("jwtToken");
  if (jwtToken != null) {
    await userStore.getUserInfoByLocalJwt(jwtToken);
  }

  if (userStore.user.avatarUrl.length == "") {
    getAvatarUrl();
  }
});

const getTime = (time) => {
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

const getAvatarUrl = () => {
  axios
    .get(
      apiStore.file.getAvatarByte,
      {
        filePath: userStore.user.avatar,
      },
      "blob"
    )
    .then((resp) => {
      const url = URL.createObjectURL(resp);
      userStore.user.avatarUrl = url;
    });
};

const getImageUrl = async (file) => {
  if (file.filePath == null) {
    file.filePath = "";
    return;
  }
  try {
    const resp = await axios.get(
      apiStore.file.getAvatarByte,
      {
        filePath: file.filePath,
      },
      "blob"
    );
    const url = URL.createObjectURL(resp);
    return url;
  } catch (error) {}
};

const goToHomeAll = () => {
  router.push({ name: "HomeAll" });
};

const saveMyPan = async () => {
  alertStore.load.isLoading = true;
  if (userStore.user.token == null) {
    alertStore.error.message = "请先登陆";
    alertStore.error.isVisible = true;
    setTimeout(() => {
      alertStore.error.isVisible = false;
      alertStore.error.message = "";
    }, 5000);
    return;
  }
  try {
    const resp = await axios.get(apiStore.file.saveMyPan, {
      shareId: selectedFiles.value[0],
      userId: userStore.user.userId,
    });
  } finally {
    selectedFiles.value = [];
    alertStore.load.isLoading = false;
  }
};

const downloadFile = () => {
  axios.get(apiStore.file.downloadSharedFile, { shareId: selectedFiles.value[0] }, "blob").then((resp) => {
    const url = URL.createObjectURL(resp);
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", selectedFiles.value[0].fileName || "downloadFile");
    document.body.append(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
    selectedFiles.value = [];
  });
};

const getSharedFileInfo = () => {
  axios
    .get(apiStore.file.getSharedFileInfo, {
      fileId: route.params.fileId,
      userId: route.params.userId,
      code: userStore.user.extraction_code,
    })
    .then(async (resp) => {
      if (resp.data.fileCategory == statickey.category.image) {
        const imageUrl = await getImageUrl(resp.data);
        files.value.unshift({ ...resp.data, imageUrl });
      } else {
        files.value.unshift(resp.data);
      }
    });
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

const selectAllFile = (selected) => {
  if (selected) {
    selectedFiles.value = [];
    for (let i = 0; i < files.value.length; i++) {
      selectedFiles.value.unshift(files.value[i].shareId); // 使用 files.value
    }
  } else {
    selectedFiles.value = [];
  }
};

const isFileSelected = (shareId) => {
  if (selectedFiles.value.includes(shareId)) {
    return true;
  }
  return false;
};

const selectFile = (shareId) => {
  if (selectedFiles.value.includes(shareId)) {
    selectedFiles.value = selectedFiles.value.filter((id) => id != shareId);
  } else {
    selectedFiles.value.unshift(shareId);
  }
};

let files = ref([]);
</script>

<style lang="scss" scoped>
.my-floder-folder {
  color: #ffcf40;
}

.my-floder {
  font-size: 24px;
  margin-right: 8px;
}

.my-img {
  width: 24px;
  height: 24px;
  margin-right: 8px;
  border-radius: 8px;
}

.user-nickname {
  color: white;
}

.no-login {
  color: white;
}

.go-login {
  margin-right: 18px;
  width: 80px;
  height: 30px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  background-color: #5c90f0;
  cursor: pointer;
}

.dropdown {
  width: 80px;
  display: none; /* 默认隐藏下拉菜单 */
  position: absolute; /* 绝对定位，放置在父元素下方 */
  top: 44px;
  right: -1px;
  background-color: white; /* 背景色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  z-index: 100; /* 确保下拉菜单在最上层 */
  padding-top: 10px;
}

.dropdown ul {
  list-style: none;
  padding: 0; /* 去掉内边距 */
  margin: 0; /* 去掉外边距 */
  width: 100%; /* 设置宽度为100% */

  text-align: center; /* 文本居中 */
}

.dropdown li {
  display: flex; /* 使用 flexbox */
  justify-content: center; /* 居中内容 */
  margin: 0 10px 10px 10px;
  border-radius: 8px;
  cursor: pointer;
}
.dropdown li:hover {
  background-color: #cedff3;
}

.user-avatar:hover .dropdown {
  display: flex !important;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

.myrow:hover {
  background-color: #f4f7fa;
}

.myrow:focus {
  background-color: #ecf5ff;
}

.row {
  user-select: none;
}

.container {
  background-color: #ffffff;
  width: 100%;

  overflow: auto;
  border-radius: 10px;
  .row {
    height: 50px;
    align-items: center;
    border-bottom: 1px solid rgba(0, 0, 0, 0.2);
  }
  .title-row {
    font-weight: 800;
    position: sticky;
  }
  .mycol span {
    &:hover {
      cursor: pointer;
      color: #09a6ff;
    }
  }
  .fileName-col i {
    font-size: 24px;
    margin-right: 8px;
  }
}

.my-container {
  width: 100vw;
  height: 100vh;
  background-image: url(/src/assets/images/back.jpg);
  background-size: cover;
  background-repeat: no-repeat;
  .header {
    width: 100%;
    height: 56px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 15vw;
    background-color: #09a6ff;
    box-shadow: 1px 2px 5px rgba(0, 0, 0, 0.2);
    .my-title-icon {
      color: white;
      font-size: 40px;
      font-weight: 900;
      margin-right: 10px;
    }
    .logo-text {
      font-size: 25px;
      color: white;
      margin-left: 5px;
      font-weight: 700;
      user-select: none;
    }
    .header-right {
      display: flex;
      align-items: center;
      justify-content: space-around;

      .user-avatar {
        width: 45px;
        height: 45px;

        margin-right: 10px;
        position: relative;
        & img {
          width: 100%;
          height: 100%;
          border-radius: 50%;
        }
      }
    }
  }
  .box {
    width: 100%;
    height: calc(100% - 56px);
    padding: 0 15vw;
    .line-1 {
      width: 100%;
      height: 75px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-weight: 800;
      .button button {
        font-size: 14px;
        margin-left: 8px;
      }
    }
  }
}
</style>
