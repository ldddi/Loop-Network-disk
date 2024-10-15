<template>
  <div v-if="alertStore.error.isVisible" class="position-fixed my-alert alert alert-danger alert-dismissible fade show" role="alert">
    <div class="message">{{ alertStore.error.message }}</div>
    <div @click="closeAlert" class="close-icon">
      <i class="bi bi-x"></i>
    </div>
  </div>
</template>

<script setup>
import { useAlertStore } from "@/store/useAlertStore";

const alertStore = useAlertStore();

const closeAlert = () => {
  alertStore.error.isVisible = false;
};
</script>

<style lang="scss" scoped>
.my-alert {
  display: flex;
  align-items: center;
  top: 10px !important;
  left: 45%;
  position: relative;
  z-index: 1100;

  .close-icon {
    position: absolute;
    right: 5px;
    width: 30px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    i {
      font-size: 20px;
    }
  }
}
</style>
