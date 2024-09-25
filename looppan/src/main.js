import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

// 引入elementplus
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";

import "bootstrap/dist/css/bootstrap.css";
import "bootstrap/dist/js/bootstrap";
import "bootstrap-icons/font/bootstrap-icons.css";

// 引入图标
import "@/assets/icon/iconfont.css";
import "@/assets/base.scss";

import "@/assets/css/HomeButton.css";

import { createPinia } from "pinia";

const app = createApp(App);
const pinia = createPinia();
app.use(ElementPlus);
app.use(pinia);
app.use(router);

app.mount("#app");
