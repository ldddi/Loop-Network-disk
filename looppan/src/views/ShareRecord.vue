<template>
  <div class="title">
    <button type="button" class="btn btn-share">
      <i class="bi bi-ban"></i>
      取消分享
    </button>
  </div>
  <div class="content">
    <div class="container">
      <div class="row myrow my-title">
        <div class="col-auto">
          <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-6 container-title">文件名</div>
        <div class="col-3 container-title">修改时间</div>
        <div class="col-2 container-title">大小</div>
      </div>

      <div v-for="file in files" :key="file.fileId" tabindex="0" class="row myrow">
        <div class="col-auto">
          <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
        </div>
        <div class="col-4 my-fileName">{{ file.fileName }}</div>
        <div class="col-2 my-icon">
          <div class="copy-link">
            <i class="bi bi-link-45deg"></i>
            <span>赋值链接</span>
          </div>
          <div class="cancle-link">
            <i class="bi bi-ban"></i>
            <span>取消分享</span>
          </div>
        </div>
        <div class="col-3">{{ file.createTime }}</div>
        <div class="col-2">大小</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const files = ref([
  {
    fileName: "hhh",
    fileId: "1",
    createTime: "2015-12-6",
  },
  {
    fileName: "xxx",
    fileId: "2",
    createTime: "2016-12-6",
  },
  {
    fileName: "ccc",
    fileId: "3",
    createTime: "2017-12-6",
  },
]);
</script>

<style lang="scss" scoped>
.my-fileName {
  cursor: pointer;
}

.my-fileName:hover {
  color: #3f9eff;
}

.my-icon {
  opacity: 0;
  display: flex;
  color: #3f9eff;
  .copy-link {
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    margin-right: 5px;
    span {
      margin: 0 3px;
    }
  }
  .cancle-link {
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    span {
      margin: 0 3px;
    }
  }
}

.myrow:hover {
  background-color: #f4f7fa;
  .my-icon {
    opacity: 1;
  }
}

.myrow:focus {
  background-color: #ecf5ff;
}

.title {
  margin-top: 20px;
  width: 100%;
}

.content {
  height: calc(100% - 60px);
  width: 100%;
  overflow-y: auto;
  position: relative;
  white-space: nowrap;
  .container {
    position: absolute;
    left: 0;
    .myrow {
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      min-width: 600px;
      border-bottom: solid 1px rgba(0, 0, 0, 0.08);
    }
    .container-title {
      font-size: 14px;
      font-weight: 700;
    }
  }
}

.my-title {
  position: sticky;
  top: 0;
  background-color: white;
  z-index: 10;
}

// button
.title {
  .btn-share {
    background-color: #3f9eff;
  }
  .btn-share:hover {
    background-color: #79bbff;
  }
  .btn-share:active {
    background-color: #347ecc;
  }
}
</style>
