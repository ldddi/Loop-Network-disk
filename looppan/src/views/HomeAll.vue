<template>
  <HomeNav>
    <div class="header">
      <!-- <div style="width: 500px; height: 1px; min-width: 100px">div</div> -->
      <button type="button" class="btn btn-pull">上传</button>
      <button type="button" class="btn btn-new">新建文件</button>
      <button type="button" class="btn btn-delete">批量删除</button>
      <button type="button" class="btn btn-move">批量移动</button>
      <div class="search-container mysearch">
        <input type="text" placeholder="输入文件名搜索..." class="search-input" />
        <i class="bi bi-search-heart search-icon"></i>
      </div>
    </div>
    <div class="title">全部文件</div>

    <FileTable />
  </HomeNav>
</template>

<script setup>
import HomeNav from "@/components/HomeNav.vue";
import FileTable from "@/components/FileTable.vue";
</script>

<style lang="scss" scoped>
.header {
  height: 45px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  overflow-x: hidden;
  white-space: nowrap; /* 不换行 */
  button {
    margin-right: 8px;
  }
  .mysearch {
    flex-shrink: 1; /* 允许按钮收缩 */
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
