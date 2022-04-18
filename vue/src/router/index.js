import { createRouter, createWebHistory } from 'vue-router'
import Login from "../views/Login";

const routes = [
  {
    path: '/login',
    name: 'login',
    component: Login,
  },
  {
    path: '/register',
    name: 'Register',
    component: () =>import("@/views/Register.vue")
  },
  {
    path: '/404',
    name: '404',
    component: () =>import("@/views/404.vue")
  },
]

let router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

//路由重置方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
  })
}


// 刷新页面会导致路由重置
export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus) {

    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Layout')) {
      // 拼接动态路由
      const manageRoute = { path: '/', name: 'Layout', component: () =>import("../layout/Layout"),redirect: "/home",children: [
          { path: '/person', name: '个人信息', component: () => import('@/views/Person')},
        ] }
      const menus = JSON.parse(storeMenus)
      menus.forEach(items => {
        if (items.path) { //当且仅当path不为空的时候才去设置路由
          let itemMenu = { path: items.path.replace("/",""), name: items.name,component: () =>import("../views/" + items.pagePath + "")}
          manageRoute.children.push(itemMenu)
        } else if (items.children.length) {
          items.children.forEach(item => {
            if (item.path) {
              let itemMenu = { path: item.path.replace("/",""), name: item.name,component: () =>import('../views/' + item.pagePath + '.vue')}
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      // 动态添加到现在的路由对象中去
      router.addRoute(manageRoute)
    }

  }
}

//重置在调用set路由
setRoutes()

router.beforeEach((to,form,next) => {
  const storeMenus = localStorage.getItem("menus")
  if (!to.matched.length) {
    if (storeMenus) {
      next('/404')
    } else {
      next('/login')
    }
  }
  next()

})
export default router
