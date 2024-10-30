/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/**/*.{vue,js,ts,jsx,tsx}", // 添加你要扫描的路径
  ],
  theme: {
    extend: {
      boxShadow: {
        "light-shadow": "1px 1px 5px rgba(0, 0, 0, 0.1)",
        "medium-shadow": "2px 2px 10px rgba(0, 0, 0, 0.15)",
        "strong-shadow": "4px 4px 15px rgba(0, 0, 0, 0.2)",
        "floating-shadow": "0 8px 30px rgba(0, 0, 0, 0.1)",
        "rounded-shadow": "0 4px 20px rgba(0, 0, 0, 0.25)",
        "inset-shadow": "inset 0 2px 5px rgba(0, 0, 0, 0.1)",
      },
    },
  },
  plugins: [],
};
