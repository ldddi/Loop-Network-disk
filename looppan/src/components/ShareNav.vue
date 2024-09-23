<template>
  <FrameWork>
    <div class="share-nav">
      <div class="share-nav-top">
        <div class="nav-top-item">分享记录</div>
      </div>
      <div class="share-nav-bottom">
        <MySpace></MySpace>
      </div>
    </div>
    <div class="content">
      <slot></slot>
    </div>
  </FrameWork>
</template>

<script setup>
import FrameWork from "./FrameWork.vue";
import MySpace from "./MySpace.vue";
</script>

<style lang="scss" scoped>
.share-nav {
  width: 200px;
  height: 100%;
  padding: 20px 10px 0 10px;
  border-right: 1px solid rgba(0, 0, 0, 0.08);
  font-weight: 700;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  .share-nav-top {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    .nav-top-item {
      width: 180px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 8px;
      color: #04a1f5;
      background-color: #eef9fe;
    }
  }
  .share-nav-bottom {
    width: 100%;
  }
}

.content {
  width: calc(100% - 200px);
  height: 100%;
  padding-left: 20px;
}
</style>
