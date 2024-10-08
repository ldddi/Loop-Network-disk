<template>
  <div class="header">
    <input type="file" id="fileInput" style="display: none" multiple accept="audio/*" />
    <button type="button" class="btn btn-pull" onclick="document.getElementById('fileInput').click();">上传</button>
    <button type="button" class="btn btn-delete">批量删除</button>
    <button type="button" class="btn btn-move">批量移动</button>
    <div class="search-container">
      <input type="text" placeholder="输入文件名搜索..." class="search-input" />
      <i class="bi bi-search-heart search-icon"></i>
    </div>
  </div>
  <div class="title">音频文件</div>

  <FileTable />
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
</style>
