<template>
  <div class="home-nav-bottom">
    <span>空间使用</span>
    <div class="progress-bar">
      <div class="bar" :style="{ width: getWidth }"></div>
    </div>
    <div class="space-info">
      <span>{{ getFileSize(userStore.user.useSpace) }}</span>
      <span>/</span>
      <span>{{ getFileSize(userStore.user.totalSpace) }}</span>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from "@/store/useUserStore";
import { computed } from "vue";

const userStore = useUserStore();

const getWidth = computed(() => {
  return `${(userStore.user.useSpace / userStore.user.totalSpace).toFixed(2) * 100}%`;
});

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
</script>

<style lang="scss" scoped>
.home-nav-bottom {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  margin: 0 0 20px 10px;
  .progress-bar {
    width: 80%;
    height: 8px;
    border-radius: 8px;
    background-color: #eceef5;
    margin: 3px 0;
    .bar {
      background-color: #04a1f5;
      height: 100%;
    }
  }
}
</style>
