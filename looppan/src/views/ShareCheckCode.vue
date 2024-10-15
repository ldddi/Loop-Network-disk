<template>
  <div class="my-container">
    <div class="header">
      <i class="bi bi-cloud-check my-title-icon"></i>
      <span class="logo-text">Loop网盘</span>
    </div>
    <div class="box">
      <div class="box-header">
        <div class="my-img">
          <img src="https://cdn.acwing.com/media/user/profile/photo/136759_lg_199ac0224c.jpg" alt="" />
        </div>
        <div class="header-right">
          <div class="user-info">
            <span>phhh</span>
            <span>2024-10-2</span>
          </div>
          <div class="file-info">
            <span>文件名</span>
          </div>
        </div>
      </div>
      <div class="box-body"></div>
    </div>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped>
.my-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  position: relative;
  top: 20vh;
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
    .box-header {
      height: 70px;
      padding: 10px 20px;
      background-color: #09a6ff;
      display: flex;
      align-items: center;
      justify-content: flex-start;
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
        background-color: rebeccapurple;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        .user-info {
          width: 100%;
          background-color: aqua;
        }
        .file-info {
          width: 100%;
          background-color: bisque;
        }
      }
    }
    .box-body {
      width: 100%;
      height: 100%;
      background-color: blueviolet;
    }
  }
}
</style>
