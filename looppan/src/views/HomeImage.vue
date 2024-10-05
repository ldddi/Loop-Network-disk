<template>
  <div class="header">
    <button type="button" class="btn btn-pull">上传</button>

    <button type="button" class="btn btn-delete">批量删除</button>
    <button type="button" class="btn btn-move">批量移动</button>
    <div class="search-container">
      <input type="text" placeholder="输入文件名搜索..." class="search-input" />
      <i class="bi bi-search-heart search-icon"></i>
    </div>
  </div>
  <div class="title">图片文件</div>
  <FileTable></FileTable>
</template>

<script setup>
import "@/assets/css/HomeButton.css";
import FileTable from "@/components/FileTable.vue";
</script>

<style lang="scss" scoped>
.header {
  height: 45px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  white-space: nowrap;
  overflow: hidden;
  button {
    margin-right: 8px;
  }
}

.title {
  height: 40px;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 700;
}

.content {
  height: calc(100% - 76px);
  width: 100%;
  position: relative;
  overflow-y: auto;
  .container {
    position: absolute;
    left: 0px;
    .myrow {
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      border-bottom: solid 1px rgba(0, 0, 0, 0.08);
    }
    .container-title {
      font-size: 14px;
      font-weight: 700;
    }
  }
}
</style>
