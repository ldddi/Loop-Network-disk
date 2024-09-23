<template>
  <FrameWork>
    <div class="content">
      <div class="home-nav">
        <div class="home-nav-top">
          <div @click="onChangeToAll" :class="['home-nav-top-item', route.name === 'HomeAll' ? 'active' : '']">
            <i class="bi bi-grid home-icon"></i>
            <span>全部</span>
          </div>
          <div @click="onChangeToVideo" :class="['home-nav-top-item', route.name === 'HomeVideo' ? 'active' : '']">
            <i class="bi bi-play-fill home-icon"></i>
            <span>视频</span>
          </div>
          <div @click="onChangeToAudio" :class="['home-nav-top-item', route.name === 'HomeAudio' ? 'active' : '']">
            <i class="bi bi-soundwave home-icon"></i>
            <span>音频</span>
          </div>
          <div @click="onChangeToImage" :class="['home-nav-top-item', route.name === 'HomeImage' ? 'active' : '']">
            <i class="bi bi-file-earmark-image home-icon"></i>
            <span>图片</span>
          </div>
          <div @click="onChangeToDocument" :class="['home-nav-top-item', route.name === 'HomeDocument' ? 'active' : '']">
            <i class="bi bi-file-earmark-word home-icon"></i>
            <span>文档</span>
          </div>
          <div @click="onChangeToMore" :class="['home-nav-top-item', route.name === 'HomeMore' ? 'active' : '']">
            <i class="bi bi-three-dots home-icon"></i>
            <span>更多</span>
          </div>
        </div>
        <div class="home-nav-bottom">
          <MySpace></MySpace>
        </div>
      </div>
      <div class="right-content">
        <slot></slot>
      </div>
    </div>
  </FrameWork>
</template>

<script setup>
import router from "@/router";
import FrameWork from "./FrameWork.vue";
import MySpace from "./MySpace.vue";
import { useRoute } from "vue-router";

const route = useRoute();
const onChangeToAll = () => {
  router.push({ name: "HomeAll" });
};
const onChangeToVideo = () => {
  router.push({ name: "HomeVideo" });
};
const onChangeToAudio = () => {
  router.push({ name: "HomeAudio" });
};
const onChangeToImage = () => {
  router.push({ name: "HomeImage" });
};
const onChangeToDocument = () => {
  router.push({ name: "HomeDocument" });
};
const onChangeToMore = () => {
  router.push({ name: "HomeMore" });
};
</script>

<style lang="scss" scoped>
.active {
  color: #04a1f5;
  background-color: #eef9fe !important;
}
.content {
  width: 100%;
  height: 100%;
  display: flex;
  .right-content {
    height: 100%;
    width: calc(100% - 200px);
    padding: 20px 0 0 20px;
  }
}

.home-nav {
  width: 200px;
  height: 100%;
  padding: 20px 10px 0 10px;
  border-right: solid 1px #f1f2f4;
  font-weight: 700;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  .home-nav-top {
    width: 100%;

    .home-nav-top-item {
      width: 180px;
      height: 40px;
      font-size: 14px;
      font-weight: 600;
      border-radius: 8px;
      box-sizing: content-box;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      .home-icon {
        font-size: 16px;
        margin-right: 16px;
      }
    }
    .home-nav-top-item:hover {
      background-color: #f3f3f3;
    }
  }
}
.home-nav-bottom {
  width: 100%;
}
</style>
