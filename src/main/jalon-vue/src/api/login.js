import request from '@/utils/request' //引入封装好的 axios 请求

export function login(username, password) { //登录接口
  let formData = new FormData();
  formData.append("username", username);
  formData.append("password", password);
  return request({ //使用封装好的 axios 进行网络请求
    url: '/login',
    headers: {
      "Accept":"application/json; charset=utf-8",
      "Content-Type": "multipart/form-data"
    },
    method: 'post',
    data: formData
  })
}
