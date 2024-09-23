<template>
  <HomeNav>
    <div class="header">
      <button type="button" class="btn btn-pull">上传</button>

      <button type="button" class="btn btn-delete">批量删除</button>
      <button type="button" class="btn btn-move">批量移动</button>
      <div class="search-container">
        <input type="text" placeholder="输入文件名搜索..." class="search-input" />
        <i class="bi bi-search-heart search-icon"></i>
      </div>
    </div>
    <div class="title">文档文件</div>
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

        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">hhh</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>

        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
        <div class="row myrow">
          <div class="col-auto">
            <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" />
          </div>
          <div class="col-6">www</div>
          <div class="col-3">修改时间</div>
          <div class="col-2">大小</div>
        </div>
      </div>
    </div>
  </HomeNav>
</template>

<script setup>
import HomeNav from "@/components/HomeNav.vue";
import "@/assets/css/HomeButton.css";
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
.my-title {
  position: sticky;
  top: 0;
  background-color: white;
  z-index: 10;
}
</style>
