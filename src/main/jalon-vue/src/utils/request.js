import axios from 'axios'
import store from '@/store'

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
  // 每个请求拼接token
  const token = store.state.user.token;
  token && (config.headers.common['Authorization'] = token);
  return config;
});
export default service
