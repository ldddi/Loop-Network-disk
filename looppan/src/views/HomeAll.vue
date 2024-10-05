<template>
  <div class="header">
    <!-- <div style="width: 500px; height: 1px; min-width: 100px">div</div> -->
    <button type="button" class="btn btn-pull">上传</button>
    <button @click="fileTable.createFile" type="button" class="btn btn-new">新建文件</button>
    <button type="button" class="btn btn-delete">批量删除</button>
    <button type="button" class="btn btn-move">批量移动</button>
    <div class="search-container mysearch">
      <input type="text" placeholder="输入文件名搜索..." class="search-input" />
      <i class="bi bi-search-heart search-icon"></i>
    </div>
  </div>
  <div class="title">全部文件</div>

  <FileTable ref="fileTable" :files="files" />
</template>

<script setup>
import FileTable from "@/components/FileTable.vue";
import { ref } from "vue";

const fileTable = ref(null);
const files = ref([]);

files.value = [
  {
    fileName: "hhh",
    fileId: "1",
    category: "0",
    createTime: "123.2354.123.2121",
    fileSize: "20kb",
  },
  {
    fileName: "hhh",
    fileId: "2",
    category: "1",
    createTime: "123.2354.123.2121",
    fileSize: "20kb",
  },
  {
    fileName: "hhh",
    fileId: "3",
    category: "2",
    createTime: "123.2354.123.2121",
    fileSize: "20kb",
  },
  {
    fileName: "hhh",
    fileId: "4",
    category: "3",
    createTime: "123.2354.123.2121",
    fileSize: "20kb",
  },
  {
    fileName: "hhh",
    fileId: "4",
    category: "4",
    createTime: "123.2354.123.2121",
    fileSize: "20kb",
  },
  {
    fileName: "hhh",
    fileId: "4",
    category: "5",
    createTime: "123.2354.123.2121",
    fileSize: "20kb",
  },
];
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
