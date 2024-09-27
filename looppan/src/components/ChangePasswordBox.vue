<template>
  <div>
    <!-- 模态框 -->
    <div class="modal fade" :class="{ show: isVisible }" :style="{ display: isVisible ? 'block' : 'none' }" tabindex="-1" aria-hidden="!isVisible">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">填写信息</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="handleSubmit">
              <div class="my-username">
                <label for="password" class="form-label">新密码</label>
                <input type="password" class="form-control" v-model="password" required />
                <label for="password" class="form-label">确认密码</label>
                <input type="password" class="form-control" v-model="confirmPassword" required />
                <label for="checkCode" class="form-label">验证码</label>
                <div class="checkcode-css">
                  <input class="form-control" v-model="checkCode" required />
                  <button class="btn btn-primary" @click="sendEmailCheckCode" :disabled="isSending">{{ isSending ? "发送中..." : "发送验证码" }}</button>
                </div>
              </div>
            </form>
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
import { useUserStore } from "@/store/useUserStore";
import { ref } from "vue";

const userStore = useUserStore();
const isVisible = ref(false);

const password = ref(userStore.user.password);

const confirmPassword = ref("");

const showModal = () => {
  isVisible.value = true;
};

const closeModal = () => {
  isVisible.value = false;
};

const handleSubmit = () => {
  isVisible.value = false;
};

defineExpose({
  showModal,
});
</script>

<style lang="scss" scoped>
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
