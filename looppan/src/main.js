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

const app = createApp(App);
app.use(ElementPlus);
app.use(router);

app.mount("#app");
