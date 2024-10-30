<template>
  <div class="my-container">
    <div class="all">
      <div class="header">
        <i class="bi bi-cloud-check my-title-icon"></i>
        <span class="logo-text">Loop网盘</span>
      </div>
      <div class="box">
        <div class="box-header">
          <div class="my-img">
            <img :src="userAvatarUrl" alt="" />
          </div>
          <div class="header-right">
            <div class="user-info">
              <span class="user-info-username">{{ nickName }}</span>
              <span class="user-info-file-sharedtime">分享于:&nbsp; {{ getCreateTime(shareTime) }}</span>
            </div>
            <div class="file-info">
              <span class="file-info-filename">分享文件:</span>
              <span>{{ fileName }}</span>
            </div>
          </div>
        </div>
        <div class="box-body">
          <div class="label">
            <label for="">请输入提取码:</label>
          </div>
          <div class="input">
            <input v-model="code" class="my-input" type="text" />
            <button @click="getFile" type="button" class="btn btn-primary my-button">提取文件</button>
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
import router from "@/router";
import { useUserStore } from "@/store/useUserStore";
import ErrorAlertBox from "@/components/ErrorAlertBox.vue";
import SuccessAlertBox from "@/components/SuccessAlertBox.vue";

let fileName = ref("");
let nickName = ref("");
let shareTime = ref("");
let userAvatar = ref("");
let userAvatarUrl = ref("");

const apiStore = useApiStore();
const userStore = useUserStore();
const route = useRoute();

onMounted(() => {
  getSharedUserInfo();
});

let code = ref("");
const getFile = () => {
  axios
    .get(apiStore.file.shareCheckCode + "/" + route.params.fileId + "/" + route.params.userId, {
      code: code.value,
    })
    .then((resp) => {
      userStore.user.is_code_ok = true;
      userStore.user.extraction_code = code.value;
      router.push({ path: `/shareFilesInfo/${route.params.fileId}/${route.params.userId}` });
    });
};

const getSharedUserInfo = () => {
  axios
    .get(apiStore.file.getSharedUserInfo, {
      fileId: route.params.fileId,
      userId: route.params.userId,
    })
    .then((resp) => {
      userAvatar.value = resp.data.userAvatar;
      fileName.value = resp.data.fileName;
      nickName.value = resp.data.nickName;
      shareTime.value = resp.data.shareTime;
      getAvatarUrl();
    });
};

const getAvatarUrl = () => {
  axios
    .get(
      apiStore.file.getAvatarByte,
      {
        filePath: userAvatar.value,
      },
      "blob"
    )
    .then((resp) => {
      const url = URL.createObjectURL(resp);
      userAvatarUrl.value = url;
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
</script>

<style lang="scss" scoped>
.box-body {
  width: 100%;
  height: 170px;
  padding: 40px 20px 60px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: #ffffff;
  .label {
    label {
      font-weight: 800;
    }
  }
  .input {
    white-space: nowrap;
    input {
      height: 30px;
      border: 1px solid rgba(0, 0, 0, 0.3);
      border-radius: 6px;
      width: calc(100% - 100px);
      margin-right: 10px;
    }
    .my-input:focus {
      border: 1px solid #3f9eff;
    }
  }
}

.my-button {
  background-color: #3f9eff;
  border: none;
  border-radius: 8px !important;
}

.my-button:hover {
  background-color: #79bbff;
}

.my-button:active {
  background-color: #347ecc;
}

.all {
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  position: relative;
  top: 20vh;
}

.my-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  position: relative;
  background-image: url(/images/back.jpg);
  background-size: cover;

  background-repeat: no-repeat;
  .header {
    .my-title-icon {
      color: #09a6ff;
      font-size: 40px;
      font-weight: 900;
      margin-right: 10px;
    }
    .logo-text {
      font-size: 25px;
      color: #09a6ff;
      margin-left: 5px;
      font-weight: 700;
      user-select: none;
    }
  }
  .box {
    width: 500px;
    height: 240px;
    border-radius: 12px;
    box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.2);
    overflow: hidden;
    position: relative;
    .box-header {
      height: 70px;
      padding: 10px 20px;
      background-color: #3f9eff;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      position: relative;
      .my-img {
        width: 50px;
        height: 50px;
        margin-right: 20px;
        img {
          width: 50px;
          height: 50px;
          border-radius: 50%;
        }
      }
      .header-right {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        .user-info {
          width: 100%;
          color: #ffffff;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          .user-info-username {
            font-size: 18px;
            margin-right: 16px;
          }
          .user-info-file-sharedtime {
            font-size: 12px;
          }
        }
        .file-info {
          width: 100%;
          color: #ffffff;
          font-size: 12px;
          .file-info-filename {
            margin-right: 15px;
          }
        }
      }
    }
  }
}
</style>
