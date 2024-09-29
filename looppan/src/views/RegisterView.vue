<template>
  <Components>
    <form @submit.prevent>
      <div class="register-panel login-register">
        <h2 class="mb-5" style="text-align: center">注册</h2>

        <!-- 邮箱 -->
        <div class="mb-4">
          <div class="input-group">
            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
            <input v-model="email" type="email" class="form-control" id="email" placeholder="邮箱地址" required />
          </div>
        </div>

        <!-- 邮箱验证码 -->
        <div class="mb-4">
          <div class="input-group">
            <input v-model="emailCheckCode" type="text" class="form-control" id="emailCode" placeholder="输入邮箱验证码" />
            <button class="btn btn-primary" @click="sendEmailCheckCode" :disabled="isSending">{{ isSending ? "发送中..." : "发送验证码" }}</button>
          </div>
        </div>

        <!-- 密码 -->
        <div class="mb-4">
          <div class="input-group">
            <span class="input-group-text"><i class="bi bi-lock"></i></span>
            <input :type="passwordVisible ? 'text' : 'password'" v-model="password" class="form-control" id="password" placeholder="密码" required />
            <span class="input-group-text cursor-pointer" @click="togglePasswordVisibility">
              <i :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </span>
          </div>
        </div>

        <!-- 密码 -->
        <div class="mb-4">
          <div class="input-group">
            <span class="input-group-text"><i class="bi bi-lock"></i></span>
            <input :type="passwordVisible ? 'text' : 'password'" v-model="confirmPassword" class="form-control" id="confirmPassword" placeholder="请再次输入密码" required />
            <span class="input-group-text cursor-pointer" @click="togglePasswordVisibility">
              <i :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </span>
          </div>
        </div>

        <div class="mb-4">
          <!-- <label for="captcha" class="form-label">请输入验证码</label> -->
          <div class="send-email-panel">
            <input v-model="picCheckCode" type="text" style="margin-right: 5px" class="form-control check-code" id="captcha" placeholder="输入验证码" />
            <img :src="picCheckCodeUrl" alt="验证码" class="me-2 check-code" @click="changeCheckCode(0)" />
          </div>
        </div>

        <div class="error-message" style="color: red">
          <span>{{ error_message }}</span>
        </div>

        <!-- 提交按钮 -->
        <div class="mb-3 text-center d-flex justify-content-center gap-3">
          <button @click="goToLogin" class="btn btn-secondary w-50">取消</button>
          <button class="btn btn-primary w-50" @click="clickRegisterButton">注册</button>
        </div>
      </div>
    </form>
    <div v-if="isVisible" class="my-alert alert alert-danger alert-dismissible fade show" role="alert">
      <div class="message">{{ message }}</div>
      <div @click="closeAlert" class="close-icon">
        <i class="bi bi-x"></i>
      </div>
    </div>
  </Components>
</template>

<script setup>
import { onMounted, ref } from "vue";
import Components from "@/components/LoginPanel.vue";
import router from "@/router";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";

const apiStore = useApiStore();

// 表单字段
let email = ref("");
let emailCheckCode = ref("");
let password = ref("");
let confirmPassword = ref("");
let picCheckCode = ref("");

let picCheckCodeUrl = ref("");

const isVisible = ref(false);
const message = ref("这是一个提示信息！");

onMounted(() => {
  changeCheckCode(0);
});

const showAlert = () => {
  isVisible.value = true;

  setTimeout(() => {
    closeAlert();
  }, 5000);
};

const closeAlert = () => {
  isVisible.value = false;
};

const changeCheckCode = () => {
  picCheckCodeUrl.value = apiStore.user.getPicCheckCode + "?time=" + new Date().getTime();
};

const goToLogin = () => {
  router.push({ name: "LoginView" });
};

// 密码可见性切换
let passwordVisible = ref(false);
const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value;
};

const clickRegisterButton = () => {
  submitRegistration();
};

// 发送邮箱验证码
let isSending = ref(false);
const sendEmailCheckCode = async () => {
  try {
    const resp = await axios.post(apiStore.user.sendEmailCheckCode, {
      email: email.value,
    });
    console.log(resp);
    message.value = resp.message;
    showAlert();
  } catch (error) {
    console.log(error.message);
    message.value = error.message;
    showAlert();
  }

  isSending.value = true;
  setTimeout(() => {
    isSending.value = false;
  }, 2000);
};

// 提交注册表单
const submitRegistration = async () => {
  try {
    const resp = await axios.post(apiStore.user.register, {
      email: email.value,
      password: password.value,
      confirmPassword: confirmPassword.value,
      picCheckCode: picCheckCode.value,
      emailCheckCode: emailCheckCode.value,
    });

    console.log(resp);
    message.value = resp.message;
    showAlert();
    router.push({ name: "LoginView" });
  } catch (error) {
    console.log(error.message);
    message.value = error.message;
    showAlert();
  }
};
</script>

<style lang="scss" scoped>
.my-alert {
  position: fixed;
  display: flex;
  align-items: center;
  position: absolute;
  top: 10vh;
  left: 40vw;
  .close-icon {
    position: absolute;
    right: 5px;
    width: 30px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    i {
      font-size: 20px;
    }
  }
}

.input-group {
  input {
    margin-right: 8px;
  }
  button {
    max-width: 108px;
  }
}

.register-panel {
  max-width: 600px;
  margin: 0 auto;
}

.input-group-text {
  width: 45px;
}

.mb-5 {
  margin-bottom: 2rem;
}

.mb-4 {
  margin-bottom: 1.5rem;
}

.login-body {
  .login-panel {
    width: 430px;
    margin-right: 15%;
    margin-top: calc((100vh - 500px) / 2);
    .login-register {
      padding: 25px;
      background: #fff;
      border-radius: 5px;
      .login-title {
        text-align: center;
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 20px;
      }
      .send-email-panel {
        display: flex;
        width: 100%;
        justify-content: space-between;
        .send-mail-btn {
          margin-left: 5px;
        }
      }

      .rememberme-panel {
        width: 100%;
      }

      .no-account {
        width: 100%;
        height: flex;
        justify-content: space-between;
      }
      .op-btn {
        width: 100%;
      }
    }
  }
}
</style>
