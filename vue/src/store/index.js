import { createStore } from 'vuex'
import Vue from 'vue'
import Vuex from 'vuex'

export default createStore({
  state: {
    currentPathName: ''
  },
  mutations: {
    setPath(state) {
      state.currentPathName = localStorage.getItem("currentPathName")
    }
  },
  actions: {
  },
  modules: {
  }
})
