import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

export const constantRouterMap = [
  //配置默认的路径，默认显示登录页
  {path: '/login', component: () => import('@/views/login')},
  {path: '/', component: () => import('@/views/home')},
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
