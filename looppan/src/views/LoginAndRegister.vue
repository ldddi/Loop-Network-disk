<template>
  <Components>
    <div v-if="type == 0" class="login-panel">
      <Login :changeType="changeType" />
    </div>
    <div v-else class="login-panel">
      <Register :changeType="changeType" />
    </div>
  </Components>
</template>

<script setup>
import { ref } from "vue";
import Components from "@/components/LoginPanel.vue";
import Login from "@/views/LoginView.vue";
import Register from "@/views/RegisterView.vue";

let type = ref(0); // 0登陆 1注册

const changeType = (propsType) => {
  type.value = propsType;
};
</script>

<style lang="scss" scoped>
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
