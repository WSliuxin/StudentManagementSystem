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
    path: '/user',
    name: 'Layout',
    component: Layout,
    children: [
      {
        path: '',
        name: 'User',
        component: () =>import("@/views/User")
      },
      {
        path: '/floor',
        name: 'Floor',
        component: () =>import("@/views/Floor")
      },
      {
        path: '/person',
        name: 'Person',
        component: () =>import("@/views/Person")
      },
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
