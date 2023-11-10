import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './style.css'
import App from './App.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'Home',
      component: () => import('@/pages/Home.vue')
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/pages/Login.vue')
    },
    {
      path: '/personManage',
      name: 'PersonManage',
      component: () => import('@/pages/PersonManage.vue')
    }
  ]
})

createApp(App).use(router).use(createPinia()).use(ElementPlus).mount('#app')
