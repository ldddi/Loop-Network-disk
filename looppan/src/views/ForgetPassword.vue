<template>
  <div class="w-screen h-screen flex items-center justify-center bg-[url(/images/bg1.jpg)] bg-cover bg-no-repeat">
    <div class="flex w-full max-w-md min-w-min shadow-md flex-col justify-around bg-white p-6 rounded-md">
      <div class="flex items-center justify-center mb-7 font-semibold text-lg">
        <span>找回密码</span>
      </div>
      <div class="mb-4 flex items-center border-[1px] border-gray-300 transition duration-300 focus-within:border-blue-400 focus-within:border-2 p-2 rounded-md">
        <i class="bi bi-envelope"></i>
        <input v-model="email" type="text" class="p-1" placeholder="找回邮箱" />
      </div>
      <div class="mb-4 flex items-center border-[1px] border-gray-300 p-2 rounded-md transition duration-300 focus-within:border-blue-400 focus-within:border-2">
        <input v-model="checkCode" type="text" class="p-1" placeholder="输入邮箱验证码" />
        <button
          @click="sendCheckCode"
          :disabled="isSending || !email"
          :class="{
            'bg-gray-500 cursor-not-allowed hover:bg-gray-500': isSending || !email,
            'bg-blue-500 hover:bg-blue-600': !isSending && email,
          }"
          class="w-32 rounded-md text-white"
        >
          {{ isSending ? "发送中" : "发送验证码" }}
        </button>
      </div>
      <div class="mb-4 flex items-center border-[1px] border-gray-300 p-2 rounded-md transition duration-300 focus-within:border-blue-400 focus-within:border-2">
        <span><i class="bi bi-lock"></i></span>
        <input v-model="password" :type="passwordVisible ? 'text' : 'password'" class="p-1" placeholder="新密码" />
        <i @click="changePasswordVisible" class="cursor-pointer" :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
      </div>
      <div class="mb-4 flex items-center border-[1px] border-gray-300 p-2 rounded-md transition duration-300 focus-within:border-blue-400 focus-within:border-2">
        <span><i class="bi bi-lock"></i></span>
        <input v-model="confirmPassword" :type="confirmPasswordVisible ? 'text' : 'password'" class="p-1" placeholder="确认新密码" />
        <i @click="changeConfirmPasswordVisible" class="cursor-pointer" :class="confirmPasswordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
      </div>
      <div class="flex items-center justify-around p-2">
        <button @click="cancelButton" class="bg-gray-200 w-24 h-8 text-blue-500 rounded-md hover:bg-gray-400 transition duration-300">取消</button>
        <button @click="confirmButton" class="bg-blue-500 w-24 h-8 text-white rounded-md hover:bg-blue-600 transition duration-300">修改密码</button>
      </div>
    </div>
  </div>

  <SuccessAlertBox />
  <ErrorAlertBox />
</template>

<script setup>
import router from "@/router";
import { ref } from "vue";
import axios from "@/utils/axiosInstance";
import { useApiStore } from "@/store/useApiStore";
import SuccessAlertBox from "@/components/SuccessAlertBox.vue";
import ErrorAlertBox from "@/components/ErrorAlertBox.vue";
import { useAlertStore } from "@/store/useAlertStore";

const apiStore = useApiStore();
const alterStore = useAlertStore();

const isSending = ref(false);
const passwordVisible = ref(false);
const confirmPasswordVisible = ref(false);
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const checkCode = ref("");

const changePasswordVisible = () => {
  passwordVisible.value = !passwordVisible.value;
};

const changeConfirmPasswordVisible = () => {
  confirmPasswordVisible.value = !confirmPasswordVisible.value;
};

const sendCheckCode = async () => {
  isSending.value = true;
  if (!email.value.includes("@")) {
    alterStore.error.message = "邮箱格式不正确";
    alterStore.error.isVisible = true;
    setTimeout(() => {
      alterStore.error.isVisible = false;
    }, 3000);
    isSending.value = false;
    return;
  }
  try {
    const resp = await axios.post(apiStore.user.sendEmailCheckCode, {
      email: email.value,
    });
  } finally {
    isSending.value = false;
  }
};

const confirmButton = async () => {
  try {
    const resp = await axios.get(apiStore.user.forgetPassword, {
      email: email.value,
      password: password.value,
      confirmPassword: confirmPassword.value,
      emailCheckCode: checkCode.value,
    });
    router.push({ name: "LoginView" });
  } finally {
  }
};

const cancelButton = () => {
  console.log("document.classList.add('class');");
  router.push({ name: "LoginView" });
};
</script>

<style scoped></style>
