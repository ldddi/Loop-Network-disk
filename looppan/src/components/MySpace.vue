<template>
  <div class="home-nav-bottom">
    <span>空间使用</span>
    <div class="progress-bar">
      <div class="bar"></div>
    </div>
    <div class="space-info">
      <span>0</span>
      <span>/</span>
      <span>5GB</span>
    </div>
  </div>
</template>

<script setup></script>

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
      width: 50%;
      background-color: #04a1f5;
      height: 100%;
    }
  }
}
</style>
