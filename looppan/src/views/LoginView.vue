<template>
  <div class="body">
    <div class="my-container">
      <form @submit.prevent>
        <div>
          <div>Loop网盘</div>
          <div>
            <!-- email -->
            <div>
              <span><i class="bi bi-envelope"></i></span>
              <input v-model="email" type="email" id="email" aria-describedby="emailHelp" placeholder="邮箱地址" />
            </div>
          </div>
          <!-- password -->
          <div>
            <div>
              <span><i class="bi bi-lock"></i></span>
              <input :type="passwordVisible ? 'text' : 'password'" v-model="password" class="form-control" id="password" placeholder="密码" />
              <span @click="togglePasswordVisibility">
                <i :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
              </span>
            </div>
          </div>

          <!-- check code -->
          <div>
            <div>
              <input v-model="picCheckCode" type="text" id="captcha" placeholder="输入验证码" />
              <img :src="checkCodeUrl" alt="验证码" @click="changeCheckCode(0)" />
            </div>
          </div>

          <!-- remember password -->
          <div>
            <a href="#" class="a-link">忘记密码?</a>
            <RouterLink href="#" :to="{ name: 'RegisterView' }">没有账号</RouterLink>
          </div>
          <div>
            <button @click="SubmitLoginForm" type="submit">登陆</button>
          </div>
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
import { useUserStore } from "@/store/useUserStore";
import { useApiStore } from "@/store/useApiStore";
import statickey from "@/utils/statickey";
import ErrorAlertBox from "@/components/ErrorAlertBox.vue";
import SuccessAlertBox from "@/components/SuccessAlertBox.vue";

const userStore = useUserStore();
const apiStore = useApiStore();

let email = ref("");
let password = ref("");
let picCheckCode = ref("");
let checkCodeUrl = ref();

onMounted(() => {
  changeCheckCode();
});

let passwordVisible = ref(false);
const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value;
};

const changeCheckCode = () => {
  checkCodeUrl.value = apiStore.user.getPicCheckCode + "?time=" + new Date().getTime();
};

const SubmitLoginForm = async () => {
  try {
    const resp = await axios.post(apiStore.user.login, {
      email: email.value,
      password: password.value,
      picCheckCode: picCheckCode.value,
    });

    userStore.updateUser({
      ...resp,
      is_login: true,
    });
    router.push({ name: "HomeAll" });
    localStorage.setItem(statickey.jwtToken, resp.token);
  } catch (error) {
    picCheckCode.value = "";
    changeCheckCode();
  }
};
</script>

<style lang="scss" scoped>
.body {
  height: 100vh;
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
  .my-container {
    width: 350px;
    height: 500px;
  }
}
</style>
