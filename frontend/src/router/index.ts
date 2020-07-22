import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

  const routes: Array<RouteConfig> = [
  {
    path: '/Home',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/Info',
    name: 'Information',
    component: () => import('../views/Information.vue')
  },
  {
    path: '/Note',
    name: 'Note',
    component: () => import('../views/Note.vue')
  },
  {
    path: '/Room',
    name: 'Room',
    component: () => import('../views/Room.vue')
  },
  {
    path: '/Settings',
    name: 'Settings',
    component: () => import('../views/Settings.vue')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
