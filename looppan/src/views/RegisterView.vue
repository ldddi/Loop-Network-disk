<template>
  <form @submit.prevent>
    <div>
      <h2>注册</h2>

      <!-- 邮箱 -->
      <div>
        <div>
          <span><i class="bi bi-envelope"></i></span>
          <input v-model="email" type="email" class="form-control" id="email" placeholder="邮箱地址" required />
        </div>
      </div>

      <!-- 邮箱验证码 -->
      <div>
        <div>
          <input v-model="emailCheckCode" type="text" id="emailCode" placeholder="输入邮箱验证码" />
          <button type="button" @click="sendEmailCheckCode" :disabled="isSending">{{ isSending ? "发送中..." : "发送验证码" }}</button>
        </div>
      </div>

      <!-- 密码 -->
      <div>
        <div>
          <span><i class="bi bi-lock"></i></span>
          <input :type="passwordVisible ? 'text' : 'password'" v-model="password" class="form-control" id="password" placeholder="密码" />
          <span @click="togglePasswordVisibility">
            <i :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
          </span>
        </div>
      </div>

      <!-- 密码 -->
      <div>
        <div>
          <span><i class="bi bi-lock"></i></span>
          <input :type="passwordVisible ? 'text' : 'password'" v-model="confirmPassword" class="form-control" id="confirmPassword" placeholder="请再次输入密码" required />
          <span @click="togglePasswordVisibility">
            <i :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
          </span>
        </div>
      </div>

      <div>
        <!-- <label for="captcha" class="form-label">请输入验证码</label> -->
        <div>
          <input v-model="picCheckCode" type="text" id="captcha" placeholder="输入验证码" />
          <img :src="picCheckCodeUrl" alt="验证码" @click="changeCheckCode(0)" />
        </div>
      </div>

      <div>
        <span>{{ error_message }}</span>
      </div>

      <!-- 提交按钮 -->
      <div>
        <RouterLink :to="{ name: 'LoginView' }">取消</RouterLink>
        <button @click="clickRegisterButton">注册</button>
      </div>
    </div>
  </form>
  <ErrorAlertBox></ErrorAlertBox>
  <SuccessAlertBox></SuccessAlertBox>
</template>

<script setup>
import { onMounted, ref } from "vue";
import router from "@/router";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";
import ErrorAlertBox from "@/components/ErrorAlertBox.vue";
import SuccessAlertBox from "@/components/SuccessAlertBox.vue";

const apiStore = useApiStore();

// 表单字段
let email = ref("");
let emailCheckCode = ref("");
let password = ref("");
let confirmPassword = ref("");
let picCheckCode = ref("");

let picCheckCodeUrl = ref("");

onMounted(() => {
  changeCheckCode(0);
});

const changeCheckCode = () => {
  picCheckCodeUrl.value = apiStore.user.getPicCheckCode + "?time=" + new Date().getTime();
};

// 密码可见性切换
let passwordVisible = ref(false);
const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value;
};

const clickRegisterButton = async () => {
  try {
    const resp = await axios.post(apiStore.user.register, {
      email: email.value,
      password: password.value,
      confirmPassword: confirmPassword.value,
      picCheckCode: picCheckCode.value,
      emailCheckCode: emailCheckCode.value,
    });
    router.push({ name: "LoginView" });
  } catch (error) {
    if (error.message == "图片验证码错误") {
      changeCheckCode();
    }
  }
};

// 发送邮箱验证码
let isSending = ref(false);
const sendEmailCheckCode = async () => {
  isSending.value = true;
  const resp = await axios.post(apiStore.user.sendEmailCheckCode, {
    email: email.value,
  });
  setTimeout(() => {
    isSending.value = false;
  }, 2000);
};
</script>

<style lang="scss" scoped></style>
