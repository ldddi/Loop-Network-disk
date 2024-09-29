<template>
  <div class="login-body">
    <div class="bg"></div>
    <div class="login-panel">
      <slot></slot>
    </div>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped>
.check-code {
  margin-right: 5px;
}

.login-body {
  height: 100vh;
  width: 100vw;
  background: url("/src/assets/images/cloud4.jpg") !important;
  background-size: cover !important;
  background-repeat: no-repeat;
  display: flex;
  .login-panel {
    width: 430px;
    margin-right: 15%;
    margin-top: calc((100vh - 500px) / 2);
  }
  .bg {
    flex: 1;
    background-size: cover;
    background-position: center;
    background-size: 800px;
    background-repeat: no-repeat;
    // background-image: url("../assets/cloud.svg");
  }
}
</style>
