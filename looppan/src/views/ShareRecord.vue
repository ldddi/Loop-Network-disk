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
        <div class="col-2 container-title">分享时间</div>
        <div class="col-2 container-title">失效时间</div>
        <div class="col-auto container-title">浏览次数</div>
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
        <div class="col-2">{{ file.shareTime }}</div>
        <div class="col-2">{{ file.failTime }}</div>
        <div class="col-auto">{{ file.views }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";

const apiStore = useApiStore();

const files = ref([]);

onMounted(() => {
  getSharedFilesList();
});

const getSharedFilesList = () => {
  axios.post(apiStore.file.getSharedFilesList, {}).then((resp) => {
    console.log(resp);
    files.value = resp.data;
  });
};
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
