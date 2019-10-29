import {login} from '@/api/login'//引入登录 api 接口

const user = {
  state: {
    username: window.sessionStorage.getItem("username"),
    token: window.sessionStorage.getItem("token")
  },
  mutations: {
    SET_TOKEN: (state, data) => {
      state.token = data;
      window.sessionStorage.setItem("token", data);
    },
    SET_USERNAME: (state, data) => {
      state.username = data;
      window.sessionStorage.setItem("username", data);
    },
    LOGOUT: (state) => {
      state.username = null;
      state.token = null;
      window.sessionStorage.removeItem("token");
      window.sessionStorage.removeItem("username");

    }
  },
  actions: {
    // 登录
    Login({commit}, userInfo) { //定义 Login 方法，在组件中使用 this.$store.dispatch("Login") 调用
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => { //封装一个 Promise
        login(username, userInfo.password).then(response => { //使用 login 接口进行网络请求
          resolve(response) //将结果封装进 Promise
        }).catch(error => {
          reject(error)
        })
      })
    },
  }
}
export default user
