<template>
  <Components>
    <form @submit.prevent>
      <div class="login-register">
        <div class="mb-5 login-title">Loop网盘</div>
        <div class="mb-4">
          <!-- email -->
          <div class="input-group">
            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
            <input v-model="email" type="email" class="form-control" id="email" aria-describedby="emailHelp" placeholder="邮箱地址" />
          </div>
        </div>
        <!-- password -->
        <div class="mb-4">
          <div class="input-group">
            <span class="input-group-text"><i class="bi bi-lock"></i></span>
            <input :type="passwordVisible ? 'text' : 'password'" v-model="password" class="form-control" id="password" placeholder="密码" />
            <span class="input-group-text cursor-pointer" @click="togglePasswordVisibility">
              <i :class="passwordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
            </span>
          </div>
        </div>

        <!-- check code -->
        <div class="mb-4">
          <!-- <label for="captcha" class="form-label">请输入验证码</label> -->
          <div class="send-email-panel">
            <input v-model="picCheckCode" type="text" class="form-control check-code" id="captcha" placeholder="输入验证码" />
            <img :src="checkCodeUrl" alt="验证码" class="me-2 check-code" @click="changeCheckCode(0)" />
          </div>
        </div>

        <!-- remember password -->
        <div class="mb-3 form-check">
          <input type="checkbox" class="form-check-input" id="remember" />
          <label class="form-check-label" for="remember">记住密码</label>
        </div>
        <div class="mb-3 no-account d-flex">
          <a href="#" class="a-link">忘记密码?</a>
          <a href="#" class="a-link ms-auto" @click="GoToRegisterView">没有账号</a>
        </div>
        <div class="mb-3 text-center">
          <button @click="SubmitLoginForm" type="submit" class="btn btn-primary w-100">登陆</button>
        </div>
      </div>
    </form>
  </Components>
</template>

<script setup>
import { onMounted, ref } from "vue";
import Components from "@/components/LoginPanel.vue";
import router from "@/router";
import axios from "axios";
import { useUserStore } from "@/store/useUserStore";
const api = {
  checkCode: "/api/getPicCheckCode",
  login: "/api/login/",
};

const userStore = useUserStore();

let email = ref("");
let password = ref("");
let picCheckCode = ref("");
let checkCodeUrl = ref(api.checkCode);

onMounted(() => {
  changeCheckCode(0);
});

let passwordVisible = ref(false);
const togglePasswordVisibility = () => {
  passwordVisible.value = !passwordVisible.value;
};

const changeCheckCode = (type) => {
  checkCodeUrl.value = api.checkCode + "?type=" + type + "&time=" + new Date().getTime();
  console.log(checkCodeUrl.value);
};

const GoToRegisterView = () => {
  router.push({ name: "RegisterView" });
};

const SubmitLoginForm = async () => {
  try {
    const response = await axios({
      method: "POST",
      url: api.login,
      data: {
        email: email.value,
        password: password.value,
        picCheckCode: picCheckCode.value,
      },
    });
    console.log(response.data);
    userStore.user.is_login = true;
    router.push({ name: "HomeAll" });
  } catch (error) {
    console.log("错误信息 ： ", error.response.data);
  }
};
</script>

<style lang="scss" scoped>
.check-code {
  margin-right: 5px;
}

.login-body {
  height: 100vh;
  width: 100vw;
  background: url("/src/assets/login-bg.jpg");
  background-size: cover;
  display: flex;
  .bg {
    flex: 1;
    background-size: cover;
    background-position: center;
    background-size: 800px;
    background-repeat: no-repeat;
    background-image: url("../assets/cloud.svg");
  }
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

  .check-code-panel {
    width: 100%;
    display: flex;
    .check-code {
      margin-left: 10px;
      cursor: pointer;
    }
  }

  .login-btn-qq {
    margin-top: 20px;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: center;
    img {
      cursor: pointer;
      margin-left: 10px;
      width: 20px;
    }
  }
}
</style>
