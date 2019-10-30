import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

export const constantRouterMap = [
  //配置默认的路径，默认显示登录页
  {path: '/', component: () => import('@/views/login')},
  {path: '/login', component: () => import('@/views/login')},

  //配置登录成功页面，使用时需要使用 path 路径来实现跳转
  {path: '/success', component: () => import('@/views/success')},

  //配置登录失败页面，使用时需要使用 path 路径来实现跳转
  {path: '/error', component: () => import('@/views/error'), hidden: true}
];

const router = new VueRouter({
  routes: constantRouterMap
});

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    next();
  } else {
    const token = store.state.user.token;
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
});

export default router
