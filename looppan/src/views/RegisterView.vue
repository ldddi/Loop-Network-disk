<template>
  <Components>
    <form>
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
            <input v-model="emailCheckCode" type="text" class="form-control" id="emailCode" placeholder="输入邮箱验证码" required />
            <button class="btn btn-outline-primary" @click="sendEmailCheckCode" :disabled="isSending">{{ isSending ? "发送中..." : "发送验证码" }}</button>
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
            <input :type="passwordVisible ? 'text' : 'password'" v-model="comfirmPassword" class="form-control" id="comfirmPassword" placeholder="密码" required />
            <span class="input-group-text cursor-pointer" @click="togglePasswordVisibility">
              <i :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </span>
          </div>
        </div>

        <div class="mb-4">
          <!-- <label for="captcha" class="form-label">请输入验证码</label> -->
          <div class="send-email-panel">
            <input v-model="picCheckCode" type="text" style="margin-right: 5px" class="form-control check-code" id="captcha" placeholder="输入验证码" />
            <img :src="checkCodeUrl" alt="验证码" class="me-2 check-code" @click="changeCheckCode(0)" />
          </div>
        </div>

        <div class="error-message" style="color: red">
          <span>{{ error_message }}</span>
        </div>

        <!-- 提交按钮 -->
        <div class="mb-3 text-center">
          <button type="submit" class="btn btn-primary w-100" @click="clickRegisterButton">注册</button>
        </div>
      </div>
    </form>
  </Components>
</template>

<script setup>
import { onMounted, ref } from "vue";
import Components from "@/components/Components.vue";
import router from "@/router";
import axios from "axios";

const api = {
  picCheckCode: "/api/getPicCheckCode",
  emailCheckCode: "/api/sendEmailCode",
  register: "/api/register",
};
// 表单字段
let email = ref("");
let emailCheckCode = ref("");
let password = ref("");
let checkCodeUrl = ref("");
let comfirmPassword = ref("");
let nickName = ref("");
let picCheckCode = ref("");
let error_message = ref("");

onMounted(() => {
  changeCheckCode(0);
});

const changeCheckCode = (type) => {
  checkCodeUrl.value = api.picCheckCode + "?type=" + type + "&time=" + new Date().getTime();
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
const sendEmailCheckCode = () => {
  axios({
    method: "POST",
    url: api.emailCheckCode,
    data: {
      email: email.value,
    },
    params: {
      type: "1",
    },
  }).then((resp) => {
    console.log(resp.data);
  });

  isSending.value = true;
  // 模拟发送验证码的API请求
  setTimeout(() => {
    isSending.value = false;
  }, 2000);
};

// 提交注册表单
const submitRegistration = () => {
  nickName.value = email.value;
  axios({
    method: "POST",
    url: api.register,
    headers: {
      "Content-Type": "application/json",
    },
    data: {
      email: email.value,
      nickName: nickName.value,
      password: password.value,
      picCheckCode: picCheckCode.value,
      emailCheckCode: emailCheckCode.value,
    },
  }).then((resp) => {
    router.push({ name: "LoginView" });
  });
};
</script>

<style lang="scss" scoped>
.register-panel {
  max-width: 400px;
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
