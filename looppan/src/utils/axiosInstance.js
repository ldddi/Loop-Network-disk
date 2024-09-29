import router from "@/router";
import { useUserStore } from "@/store/useUserStore";
import axios from "axios";

// 创建 Axios 实例
const axiosInstance = axios.create({
  // baseURL: "http://localhost:7090", // 替换为你的基础 URL
  timeout: 5000, // 超时时间
});

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    // 可以在这里添加 Authorization header 或其他请求配置
    const token = localStorage.getItem("jwtToken"); // 从 localStorage 获取 token
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
    return response.data; // 直接返回数据
  },
  (error) => {
    // 处理响应错误
    if (error.response) {
      if (error.response.data.message == "token失效") {
        localStorage.removeItem("jwtToken");
        const userStore = useUserStore();
        userStore.user.is_login = false;
        userStore.user.token = "";
        console.log("token失效", error.response);
      }
      console.log(error);
      console.error("{error.response.data -> Error:", error.response.data);
      return Promise.reject(error.response.data);
    } else {
      console.error("{error.message -> } Error:", error.message);
      return Promise.reject(error.message);
    }
  }
);

// 封装 GET 方法
const get = (url, params) => {
  return axiosInstance.get(url, { params });
};

// 封装 POST 方法
const post = (url, data) => {
  return axiosInstance.post(url, data);
};

// 导出封装的 axios 实例
export default {
  get,
  post,
};
