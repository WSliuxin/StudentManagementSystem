import { createRouter, createWebHistory } from 'vue-router'
import Layout from "@/layout/Layout";
import Login from "@/views/Login";

const routes = [
  {
    path: '/',
    name: 'login',
    component: Login,
    redirect: "/login",
    children: [
      {
        path: 'login',
        name: 'login',
        component: () =>import("@/views/Login"),
      }
    ]
  },
  {
    path: '/home',
    name: 'Layout',
    component: Layout,
    children: [
      {path: '', name: '主页', component: () =>import("@/views/Home")},
      {path: '/role', name: '角色管理', component: () =>import("@/views/Role")},
      {path: '/menu', name: '菜单管理', component: () =>import("@/views/Menu")},
      {path: '/user', name: '用户管理', component: () =>import("@/views/User")},
      {path: '/floor', name: '宿舍楼管理', component: () =>import("@/views/Floor")},
      {path: '/person', name: '宿舍公告管理', component: () =>import("@/views/Person")},
      {path: '/announcement', name: 'Announcement', component: () =>import("@/views/Announcement")},
      {path: '/file', name: '文件管理', component: () =>import("@/views/File")}
    ]
  },
  {
    path: '/register',
    name: 'Register',
    component: () =>import("@/views/Register")
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
