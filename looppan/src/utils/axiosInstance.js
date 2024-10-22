import router from "@/router";
import { useUserStore } from "@/store/useUserStore";
import axios from "axios";
import statickey from "./statickey";
import { useAlertStore } from "@/store/useAlertStore";

// 创建 Axios 实例
const axiosInstance = axios.create({
  // baseURL: "http://localhost:7090", // 替换为你的基础 URL
  timeout: 100000, // 超时时间
});

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    // 可以在这里添加 Authorization header 或其他请求配置
    const token = localStorage.getItem(statickey.jwtToken); // 从 localStorage 获取 token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response) => {
    const alertStore = useAlertStore();
    if (response.data.message) {
      alertStore.success.message = response.data.message;
      alertStore.success.isVisible = true;
      setTimeout(() => {
        alertStore.success.isVisible = false;
      }, 5000);
    }

    return response.data; // 直接返回数据
  },
  async (error) => {
    // 处理响应错误
    if (error.response) {
      let data = error.response.data;
      if (error.response.data instanceof Blob) {
        data = await error.response.data.text().then((text) => JSON.parse(text));
      }

      if (data.message == "user is unauthorized" || error.response.status == 401) {
        localStorage.removeItem(statickey.jwtToken);
        const userStore = useUserStore();
        userStore.user.is_login = false;
        userStore.user.token = "";
        router.push({ name: "LoginView" });
      }
      const alertStore = useAlertStore();
      alertStore.error.message = data.message;
      alertStore.error.isVisible = true;
      setTimeout(() => {
        alertStore.error.isVisible = false;
      }, 3000);
      return Promise.reject(data);
    } else {
      console.error(error);
      return Promise.reject(error.message);
    }
  }
);

// 封装 GET 方法
const get = (url, params, responseType = "json") => {
  return axiosInstance.get(url, { params, responseType });
};

// 封装 POST 方法
const post = (url, data, responseType = "json", onUploadProgress) => {
  return axiosInstance.post(url, data, { responseType, onUploadProgress });
};

// 导出封装的 axios 实例
export default {
  get,
  post,
};
