<template>
  <div>
    <el-card class="login-form-layout">
      <el-form
        autocomplete="on"
        :model="loginForm"
        ref="loginForm"
        label-position="left"
      >
        <h2 class="login-title color-main">mall-admin-web</h2>
        <el-form-item prop="username">
          <el-input
            name="username"
            type="text"
            v-model="loginForm.username"
            autocomplete="on"
            placeholder="请输入用户名"
          >
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            name="password"
            :type="pwdType"
            @keyup.enter.native="handleLogin"
            v-model="loginForm.password"
            autocomplete="on"
            placeholder="请输入密码"
          >
          </el-input>
        </el-form-item>
        <el-form-item :hidden="data.flag">
          <el-alert
            :title="data.msg"
            type="error"
            :closable="false"
          ></el-alert>
        </el-form-item>
        <el-form-item style="margin-bottom: 60px">
          <el-button
            style="width: 100%"
            type="primary"
            :loading="loading"
            @click.native.prevent="handleLogin"
          >登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: "login",
    data() {
      return {
        loginForm: {
          username: "admin",
          password: "123456"
        },
        loading: false,
        pwdType: "password",
        data: {
          msg: "",
          flag: true
        }
      };
    },
    methods: {
      handleLogin() {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true;
            this.$store
              .dispatch("Login", this.loginForm)
              .then(response => {
                this.loading = false;
                let data = response.data;
                if (data.backCode == 200) {
                  this.$store.commit("SET_USERNAME", data.username);
                  this.$store.commit("SET_TOKEN", data.token);
                  this.$router.push({
                    path: "/",
                    query: {data: data.backMsg}
                  });
                } else {
                  this.data.msg = data.backMsg;
                  this.data.flag = false;
                }
              })
              .catch(() => {
                this.loading = false;
              });
          } else {
            // eslint-disable-next-line no-console
            console.log("参数验证不合法！");
            return false;
          }
        });
      }
    }
  };
</script>

<style scoped>
  .login-form-layout {
    position: absolute;
    left: 0;
    right: 0;
    width: 360px;
    margin: 140px auto;
    border-top: 10px solid #409eff;
  }

  .login-title {
    text-align: center;
  }
</style>
