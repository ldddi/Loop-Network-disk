<template>
  <HomeNav>
    <div class="header">
      <button type="button" class="btn btn-pull">上传</button>
      <button type="button" class="btn btn-new">新建文件</button>
      <button type="button" class="btn btn-delete">批量删除</button>
      <button type="button" class="btn btn-move">批量移动</button>
      <div class="search-container">
        <input type="text" placeholder="输入文件名搜索..." class="search-input" />
        <i class="bi bi-search-heart search-icon"></i>
      </div>
    </div>
    <div class="title">全部文件</div>
    <div class="file-table-container">
      <!-- 新增的滚动容器 -->
      <FileTable />
    </div>
  </HomeNav>
</template>

<script setup>
import HomeNav from "@/components/HomeNav.vue";
import FileTable from "@/components/FileTable.vue";
</script>

<style lang="scss" scoped>
.header {
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
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

.file-table-container {
  width: calc(100vw - 320px);
  height: calc(100vh - 180px); /* 调整为适合的高度 */

  overflow-y: auto; /* 纵向滚动 */

  white-space: nowrap; /* 确保子元素不换行 */
  position: relative; /* 使其成为滚动区域 */
}
</style>
