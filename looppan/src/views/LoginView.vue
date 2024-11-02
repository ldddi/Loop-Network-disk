<template>
  <div class="w-screen h-screen flex items-center justify-center bg-gray-100 bg-[url('/images/bg2.avif')] bg-cover bg-center">
    <div class="w-full max-w-md bg-white shadow-md rounded-lg p-6">
      <form @submit.prevent>
        <h2 class="text-xl font-semibold text-center mb-6">Loop网盘</h2>

        <div class="mb-4">
          <div class="flex items-center border-[1px] border-gray-300 rounded-md p-2 transition duration-300 focus-within:border-blue-400 focus-within:shadow-sm focus-within:border-2">
            <span><i class="bi bi-envelope"></i></span>
            <input v-model="email" type="text" id="email" placeholder="邮箱地址" class="flex-1 outline-none p-1" />
          </div>
        </div>

        <div class="mb-4">
          <div class="flex items-center border-[1px] border-gray-300 rounded-md p-2 transition duration-300 focus-within:border-blue-400 focus-within:shadow-sm focus-within:border-2">
            <span><i class="bi bi-lock"></i></span>
            <input :type="passwordVisible ? 'text' : 'password'" v-model="password" id="password" placeholder="密码" class="flex-1 outline-none p-1" />
            <span @click="togglePasswordVisibility" class="cursor-pointer">
              <i :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </span>
          </div>
        </div>

        <div class="mb-4">
          <div class="flex items-center border-[1px] border-gray-300 rounded-md p-2 transition duration-300 focus-within:border-blue-400 focus-within:shadow-sm focus-within:border-2">
            <input v-model="picCheckCode" type="text" id="captcha" placeholder="输入验证码" class="flex-1 outline-none p-1" />
            <img :src="checkCodeUrl" alt="验证码" @click="changeCheckCode(0)" class="ml-2 cursor-pointer h-9" />
          </div>
        </div>

        <div class="mb-4 flex justify-between items-center">
          <RouterLink to="/forgetPassword" href="#" class="text-blue-500 hover:underline">忘记密码?</RouterLink>
          <RouterLink to="/register" class="text-blue-500 hover:underline">没有账号?</RouterLink>
        </div>

        <div>
          <button @click="SubmitLoginForm" type="submit" class="w-full bg-blue-500 text-white rounded-md py-2 hover:bg-blue-600 transition duration-300">登陆</button>
        </div>
      </form>
    </div>
  </div>
  <InfoAlterBox />
  <ErrorAlertBox></ErrorAlertBox>
  <SuccessAlertBox></SuccessAlertBox>
</template>
<script setup>
import { onMounted, ref } from "vue";
import router from "@/router";
import axios from "@/utils/axiosInstance";
import { useUserStore } from "@/store/useUserStore";
import { useApiStore } from "@/store/useApiStore";
import statickey from "@/utils/statickey";
import ErrorAlertBox from "@/components/ErrorAlertBox.vue";
import SuccessAlertBox from "@/components/SuccessAlertBox.vue";
import InfoAlterBox from "@/components/InfoAlterBox.vue";
import { useRoute } from "vue-router";
import axios2 from "axios";

const userStore = useUserStore();
const apiStore = useApiStore();
const route = useRoute();

let email = ref("");
let password = ref("");
let picCheckCode = ref("");
let checkCodeUrl = ref();

onMounted(() => {
  // axios2({
  //   url: "http://localhost:7090/api/getPicCheckCode",
  //   method: "GET",
  //   responseType: "blob",
  // })
  //   .then((resp) => {
  //     console.log(resp);
  //   })
  //   .catch((error) => {
  //     console.log(error);
  //   });
  changeCheckCode();
});

let passwordVisible = ref(false);
const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value;
};

const changeCheckCode = () => {
  axios
    .get(apiStore.user.getPicCheckCode, {}, "blob", true)
    .then((resp) => {
      checkCodeUrl.value = URL.createObjectURL(resp);
    })
    .catch((error) => {});
  // checkCodeUrl.value = apiStore.user.getPicCheckCode + "?time=" + new Date().getTime();
};

const SubmitLoginForm = async () => {
  try {
    const resp = await axios.post(
      apiStore.user.login,
      {
        email: email.value,
        password: password.value,
        picCheckCode: picCheckCode.value,
      },
      "json",
      "none",
      true
    );

    userStore.updateUser({
      ...resp,
      is_login: true,
    });
    router.push({ name: "HomeAll" });
    localStorage.setItem(statickey.jwtToken, resp.token);
  } catch (error) {
    console.log(error);
    picCheckCode.value = "";
    changeCheckCode();
  }
};
</script>

<style lang="scss" scoped></style>
