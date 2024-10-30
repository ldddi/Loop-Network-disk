<template>
  <div class="w-screen h-screen flex items-center justify-center bg-gray-100 bg-[url('/images/bg1.jpg')] bg-cover bg-center">
    <div class="w-full max-w-md bg-white shadow-md rounded-lg p-6">
      <form @submit.prevent>
        <h2 class="text-xl font-semibold text-center mb-6">注册</h2>

        <!-- 邮箱 -->
        <div class="mb-4">
          <div class="flex items-center border-[1px] border-gray-300 transition duration-300 rounded-md p-2 focus-within:border-blue-400 focus-within:shadow-sm focus-within:border-2">
            <span><i class="bi bi-envelope"></i></span>
            <input v-model="email" type="email" class="flex-1 outline-none p-1" placeholder="邮箱地址" required />
          </div>
        </div>

        <!-- 邮箱验证码 -->
        <div class="mb-4">
          <div class="flex items-center border-[1px] border-gray-300 transition duration-300 rounded-md p-2 focus-within:border-blue-400 focus-within:shadow-sm focus-within:border-2">
            <input v-model="emailCheckCode" type="text" placeholder="输入邮箱验证码" class="flex-1 outline-none p-1" />
            <button
              type="button"
              @click="sendEmailCheckCode"
              :disabled="isSending || !email"
              :class="{
                'bg-blue-500': !isSending,
                'bg-gray-400 cursor-not-allowed': isSending || !email,
                'hover:bg-gray-400 cursor-not-allowed': isSending || !email,
              }"
              class="ml-2 bg-blue-500 text-white rounded-md py-1 px-4 w-30 hover:bg-blue-600 transition duration-300"
            >
              {{ isSending ? "发送中..." : "发送验证码" }}
            </button>
          </div>
        </div>

        <!-- 密码 -->
        <div class="mb-4">
          <div class="flex items-center border-[1px] border-gray-300 transition duration-300 rounded-md p-2 focus-within:border-blue-400 focus-within:shadow-sm focus-within:border-2">
            <span><i class="bi bi-lock"></i></span>
            <input :type="passwordVisible ? 'text' : 'password'" v-model="password" class="flex-1 outline-none p-1" placeholder="密码" />
            <span @click="togglePasswordVisibility" class="cursor-pointer">
              <i :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </span>
          </div>
        </div>

        <!-- 确认密码 -->
        <div class="mb-4">
          <div class="flex items-center border-[1px] border-gray-300 transition duration-300 rounded-md p-2 focus-within:border-blue-400 focus-within:shadow-sm focus-within:border-2">
            <span><i class="bi bi-lock"></i></span>
            <input :type="passwordVisible ? 'text' : 'password'" v-model="confirmPassword" class="flex-1 outline-none p-1" placeholder="请再次输入密码" required />
            <span @click="togglePasswordVisibility" class="cursor-pointer">
              <i :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </span>
          </div>
        </div>

        <!-- 图片验证码 -->
        <div class="mb-4">
          <div class="flex items-center border-[1px] border-gray-300 transition duration-300 rounded-md p-2 focus-within:border-blue-400 focus-within:shadow-sm focus-within:border-2">
            <input v-model="picCheckCode" type="text" id="captcha" placeholder="输入验证码" class="flex-1 outline-none p-1" />
            <img :src="picCheckCodeUrl" alt="验证码" @click="changeCheckCode(0)" class="ml-2 cursor-pointer h-8" />
          </div>
        </div>

        <!-- 提交按钮 -->
        <div class="flex justify-between items-center px-3">
          <RouterLink to="/login" class="w-5/12 text-center bg-gray-200 text-blue-500 rounded-md py-2 hover:bg-gray-300 transition duration-300">取消</RouterLink>
          <button @click="clickRegisterButton" class="w-5/12 bg-blue-500 text-white rounded-md py-2 hover:bg-blue-600 transition duration-300">注册</button>
        </div>
      </form>
    </div>
  </div>

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
import { useAlertStore } from "@/store/useAlertStore";

const apiStore = useApiStore();
const alterStore = useAlertStore();

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
  console.log("sendEmailCheckCode");
  isSending.value = true;
  if (email.value == null) {
    alterStore.error.message = "邮箱不能为空";
    alterStore.error.isVisible = true;
    setTimeout(() => {
      isSending.value = false;
    }, 2000);
    return;
  }

  try {
    const resp = await axios.post(apiStore.user.sendEmailCheckCode, {
      email: email.value,
    });
  } finally {
    setTimeout(() => {
      isSending.value = false;
    }, 2000);
  }
};
</script>

<style lang="scss" scoped></style>
