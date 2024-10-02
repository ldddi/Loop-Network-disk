<template>
  <div class="content">
    <div class="container">
      <div class="row myrow my-title">
        <div class="col-auto">
          <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-3 container-title">文件名</div>
        <div class="col-4 container-title"></div>
        <div class="col-3 container-title">修改时间</div>
        <div class="col-1 container-title">大小</div>
      </div>

      <div v-for="n in 20" class="row myrow" :key="n" tabindex="0">
        <div class="col-auto">
          <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-3">
          <span class="file-name">hhh</span>
        </div>
        <div class="col-4 my-button">
          <div class="share-button item-button">
            <i class="bi bi-share item-icon"></i>
            <span>分享</span>
          </div>
          <div class="del-button item-button">
            <i class="bi bi-trash item-icon"></i>
            <span>删除</span>
          </div>
          <div class="reset-button item-button">
            <i class="bi bi-pencil-square item-icon"></i>
            <span>重命名</span>
          </div>
          <div class="move-button item-button">
            <i class="bi bi-arrows-move item-icon"></i>
            <span>移动</span>
          </div>
        </div>
        <div class="col-3">修改时间</div>
        <div class="col-1">大小</div>
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped>
.file-name:hover {
  cursor: pointer;
  color: #5faeff;
}

.content {
  height: 100%;
  width: 100%;

  .container {
    position: absolute;
    left: 0px;
    .myrow {
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      border-bottom: solid 1px rgba(0, 0, 0, 0.08);
      .my-button {
        display: flex;
        align-items: center;
        justify-content: flex-end;
        padding-right: 30px;
      }
      .item-button {
        margin-right: 8px;
        color: #5faeff;
        display: none;
        cursor: pointer;
        // background-color: aqua;
        transition: all 0.3s ease; // 添加过渡效果
        .item-icon {
          font-size: 13px;
          margin-right: 3px;
        }
      }
    }
    .container-title {
      font-size: 14px;
      font-weight: 700;
    }
  }
}

.myrow:hover .item-button {
  display: block !important; // 悬停时显示
}

.myrow:hover {
  background-color: #f4f7fa;
}

.myrow:focus {
  background-color: #ecf5ff;
}

.my-title {
  position: sticky;
  top: 0;
  background-color: white;
  z-index: 10;
}
</style>
