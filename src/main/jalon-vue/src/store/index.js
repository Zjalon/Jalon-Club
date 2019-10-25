import Vue from 'vue'
import Vuex from 'vuex'

import user from './modules/user'


Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user //使用 user.js 中的 action
  }
})

export default store
