import axios from 'axios'

var root = process.env.API_ROOT;

// 创建 axios 实例
const service = axios.create({
  timeout: 15000,
  headers: {'Content-Type': 'application/json'}
})
// 请求之前
service.interceptors.request.use((config) => {
  //请求之前重新拼装url
  config.url = root + config.url;
  return config;
});
export default service
