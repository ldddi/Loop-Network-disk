<template>
  <div class="my-container">
    <ChangeInfoBox ref="ChildChangeInfoBox"></ChangeInfoBox>
    <ChangePasswordBox ref="ChildChangePasswordBox" />
    <!-- nav 导航栏 -->
    <div class="header">
      <div class="left-logo">
        <i class="bi bi-cloud-arrow-up logo-icon"></i>
        <span class="logo-text">Loop网盘</span>
      </div>
      <div class="right-info">
        <div @click="onChangeDropdownVisible" ref="arrow" class="right-arrow">
          <i class="bi bi-arrow-down-up arrow-icon"></i>
          <div v-if="isDropdownVisible" ref="dropdown" class="upload-box" @click.stop>
            <div class="triangle-1"></div>
            <div class="triangle-2"></div>
            <div class="upload-title">上传任务 (仅展示本次上传任务)</div>
            <div class="upload-content">暂无上传任务</div>
          </div>
        </div>
        <!-- 弹出框 -->

        <div class="right-user-info">
          <div class="user-avatar">
            <img :src="userStore.user.avatar" alt="" />
            <div class="dropdown">
              <ul>
                <li @click="changePersonInfo">修改个人信息</li>
                <li @click="changePassword">修改密码</li>
                <li>退出</li>
              </ul>
            </div>
          </div>
          <div class="user-nickname">{{ userStore.user.nickName }}</div>
        </div>
      </div>
    </div>
    <div class="body">
      <div class="left-nav">
        <div @click="onChangeToHome" :class="['left-nav-item', 'left-nav-item1', route.name === 'HomeAll' || route.name === 'HomeVideo' || route.name === 'HomeAudio' || route.name === 'HomeImage' || route.name === 'HomeDocument' || route.name === 'HomeMore' ? 'active' : '']">
          <i class="bi bi-house left-nav-icon"></i>
          <span>首页</span>
        </div>
        <div @click="onChangeToShare" :class="['left-nav-item', route.name === 'ShareRecord' ? 'active' : '']">
          <i class="bi bi-share left-nav-icon"></i>
          <span>分享</span>
        </div>
        <div @click="onChangeToRecycle" :class="['left-nav-item', route.name === 'MyRecycle' ? 'active' : '']">
          <i class="bi bi-recycle left-nav-icon"></i>
          <span>回收站</span>
        </div>
      </div>
      <div class="right-panel">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script setup>
import router from "@/router";
import { useUserStore } from "@/store/useUserStore";
import { onBeforeUnmount, onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import ChangeInfoBox from "./ChangeInfoBox.vue";
import ChangePasswordBox from "./ChangePasswordBox.vue";

let route = useRoute();
let isDropdownVisible = ref(false);
const dropdown = ref(null);
const arrow = ref(null);
const userStore = useUserStore();
const ChildChangeInfoBox = ref(null);
const ChildChangePasswordBox = ref(null);

const onChangeToHome = () => {
  router.push({ name: "HomeAll" });
};

const onChangeToShare = () => {
  router.push({ name: "ShareRecord" });
};

const onChangeToRecycle = () => {
  router.push({ name: "MyRecycle" });
};

const onChangeDropdownVisible = () => {
  isDropdownVisible.value = !isDropdownVisible.value;
};

const onCloseDropdown = (event) => {
  if (arrow.value && !arrow.value.contains(event.target) && dropdown.value && !dropdown.value.contains(event.target)) {
    console.log("在外边");
    isDropdownVisible.value = false;
  }
};

const changePersonInfo = () => {
  ChildChangeInfoBox.value.showModal();
};

const changePassword = () => {
  ChildChangePasswordBox.value.showModal();
};

onMounted(() => {
  document.addEventListener("click", onCloseDropdown);
});
onBeforeUnmount(() => {
  document.removeEventListener("click", onCloseDropdown);
});
</script>

<style lang="scss" scoped>
.dropdown {
  width: 150px;
  display: none; /* 默认隐藏下拉菜单 */
  position: absolute; /* 绝对定位，放置在父元素下方 */
  top: 40px;
  background-color: white; /* 背景色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  z-index: 100; /* 确保下拉菜单在最上层 */
  padding-top: 10px;
  ul {
    text-align: center;
  }
}

.user-avatar:hover .dropdown {
  display: flex !important;
  flex-direction: row !important;
  align-items: center;
  justify-content: center;
  padding: 10px 0;
  border-radius: 8px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

.dropdown ul {
  list-style: none; /* 去掉列表样式 */
  padding: 0; /* 去掉内边距 */
  margin: 0; /* 去掉外边距 */
}

.dropdown li {
  padding: 10px 15px; /* 内边距 */
  cursor: pointer; /* 鼠标悬浮时显示为手指 */
  border-radius: 8px;
}

.dropdown li:hover {
  background-color: #ecf5ff; /* 悬浮时背景色 */
  color: #3f9eff;
}

.active {
  color: #09a6ff;
  background-color: #f1faff !important;
}

.my-container {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.header {
  width: 100vw;
  height: 56px;
  box-sizing: border-box;
  padding: 0 24px;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  .left-logo {
    .logo-icon {
      color: #09a6ff;
      font-size: 40px;
    }
    .logo-text {
      font-size: 25px;
      color: #09a6ff;
      margin-left: 5px;
      font-weight: 700;
    }
  }
  .right-info {
    display: flex;
    align-items: center;
    justify-content: center;
    .right-arrow {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      cursor: pointer;
      .arrow-icon {
        font-size: 18px;
        font-weight: bold !important;
      }
    }
    .right-arrow:hover {
      background-color: #f0f3f7;
      transition: all 0.2s ease-in-out;
    }
    .right-user-info {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-left: 16px;
      cursor: pointer;
      .user-avatar {
        position: relative;
        width: 46px;
        height: 46px;
        border-radius: 50%;
        // background-color: lightblue;
        img {
          width: 100%;
          height: 100%;
        }
      }
      .user-nickname {
        margin-left: 5px;
      }
    }
  }
}

.body {
  position: relative;
  .left-nav {
    width: 80px;
    height: calc(100vh - 56px);
    // background-color: lightblue;
    position: fixed;
    box-shadow: 3px 0 10px rgba(0, 0, 0, 0.08);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    .left-nav-item {
      box-sizing: content-box;
      width: 66px;
      height: 54px;
      padding-bottom: 12px;
      margin-bottom: 12px;
      border-radius: 8px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-items: center;
      font-size: 14px;
      font-weight: 700;
      cursor: pointer;
      .left-nav-icon {
        font-size: 28px;
      }
    }
    .left-nav-item:hover {
      background-color: #f4f5f9;
    }
    .left-nav-item1 {
      margin-top: 24px;
    }
  }

  .right-panel {
    width: calc(100vw - 80px);
    height: calc(100vh - 56px);
    // background-color: #09a6ff;
    position: absolute;
    left: 80px;
    display: flex;
    min-width: 800px;
  }
}

.upload-box {
  position: absolute;
  top: 50px; /* 调整显示框的位置 */
  right: -105px;
  width: 800px;
  height: 500px;
  background-color: white;
  border: 1px solid #ddd;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  padding: 10px;
  border-radius: 4px;
  z-index: 11;
  cursor: auto;
}

.upload-title {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 10px;
  height: 30px;
  border-bottom: solid 1px rgba(0, 0, 0, 0.08);
}

.upload-content {
  font-size: 14px;
  color: #999;
  text-align: center;
}

/* 带有三条边的三角形样式 */
.triangle-1 {
  position: absolute;
  top: -10px;
  right: 115px;
  width: 0;
  height: 0;
  border-left: 10px solid transparent; /* 左边框，斜边 */
  border-right: 10px solid transparent; /* 右边框，斜边 */
  border-bottom: 10px solid #000; /* 底部边框 */
}
.triangle-2 {
  position: absolute;
  top: -10px;
  right: 115px;
  width: 0;
  height: 0;
  border-left: 10px solid transparent; /* 左边框，斜边 */
  border-right: 10px solid transparent; /* 右边框，斜边 */
  border-bottom: 10px solid white; /* 底部边框 */
}
</style>
