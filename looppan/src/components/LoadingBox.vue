<template>
  <transition name="fade">
    <div v-if="alertStore.load.isLoading" class="my-container">
      <div class="loading-overlay my-loading">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { useAlertStore } from "@/store/useAlertStore";

const alertStore = useAlertStore();
</script>

<style scoped>
.my-container {
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.3); /* 半透明的灰色 */
  z-index: 1500;
  display: flex;
  align-items: center;
  justify-content: center;
}

.my-loading {
  position: absolute;
}

/* 添加过渡效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

.fade-enter,
.fade-leave-to {
  opacity: 0; /* 进入时透明，离开时透明 */
}
</style>
