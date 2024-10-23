<template>
  <div>
    <!-- 模态框 -->
    <div class="modal fade" :class="{ show: isVisible }" :style="{ display: isVisible ? 'block' : 'none' }" tabindex="-1" aria-hidden="!isVisible">
      <div class="modal-dialog my-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">填写信息</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <div>
              <div class="mb-3">
                <label for="nickname" class="form-label">昵称</label>
                <input type="text" class="form-control" v-model="nickName" required />
              </div>
              <div class="mb-3">
                <label for="avatar" class="form-label">头像</label>
                <input type="file" class="form-control" @change="handleFileChange" accept="image/*" />
                <div v-if="imageUrl" class="mt-2">
                  <img :src="imageUrl" alt="头像预览" class="img-thumbnail" style="max-width: 200px; max-height: 200px" />
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeModal">取消</button>
            <button type="button" class="btn btn-primary" @click="handleSubmit">确认</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 模态框背景遮罩 -->
    <div v-if="isVisible" class="modal-backdrop fade show"></div>
  </div>
</template>

<script setup>
import { useApiStore } from "@/store/useApiStore";
import { useUserStore } from "@/store/useUserStore";
import axios from "@/utils/axiosInstance";
import { onMounted, ref } from "vue";

const userStore = useUserStore();
const apiStore = useApiStore();

const isVisible = ref(false);

let nickName = ref(userStore.user.nickName);
const formData = new FormData();
const imageUrl = ref(null); // 用于存储头像预览的URL

onMounted(() => {
  imageUrl.value = null;
});

const showModal = () => {
  isVisible.value = true;
};

const closeModal = () => {
  isVisible.value = false;
  imageUrl.value = null; // 关闭模态框时重置预览
  isVisible.value = false;
};

const handleFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    // 使用 URL.createObjectURL 创建一个临时的图片URL用于预览
    imageUrl.value = URL.createObjectURL(file);
    formData.append("avatar", file); // 添加文件到 FormData
  }
};

const handleSubmit = async () => {
  formData.append("nickName", nickName.value);
  const resp = await axios.post(apiStore.user.updateUserInfo, formData);
  if (resp.nickName) {
    userStore.user.nickName = resp.nickName;
  }
  if (resp.avatar) {
    userStore.user.avatar = resp.avatar;
    userStore.getAvatarUrl();
  }
  isVisible.value = false;
};

defineExpose({
  showModal,
});
</script>

<style lang="scss" scoped>
.modal-dialog {
  margin-top: 15vh; /* 将模态框向下移动到垂直高度的三分之一处 */
}

.modal {
  display: none; /* 确保模态框在未显示时不占用空间 */
}

.my-password-css {
  margin-top: 10px;
  border-top: solid 1px;
}

.checkcode-css {
  display: flex;
  align-items: center;
  justify-content: space-between;
  button {
    box-sizing: border-box;
    width: 150px;
    margin-left: 20px;
  }
}
</style>
